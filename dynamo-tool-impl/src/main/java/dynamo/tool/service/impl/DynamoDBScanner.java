package dynamo.tool.service.impl;

import com.google.gson.Gson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import java.util.HashMap;
import java.util.Map;

import dynamo.tool.enums.MarkFieldsEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zongbo.zhang on 4/3/18.
 */

@Slf4j
public class DynamoDBScanner {

    private static DynamoServiceImpl dynamoService = new DynamoServiceImpl();

    public static void scanPage(String tableName){

        int itemCount = 0;

        int blackCount = 0,blackSuc = 0,xunJiCount = 0,xunJiSuc = 0,zhiChaCount = 0,zhiChaSuc = 0,mingJianCount = 0,mingJianSuc = 0;


        Map<String, AttributeValue> lastKeyEvaluated = null;

        Map<String, Condition> filter = new HashMap<>();
        filter.put("update_time",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("2018-03")));
        filter.put("service_name",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("ICE")));

        log.info("begin scan table: {} ",tableName);
        do{
           ScanResult result = dynamoService.scanPage(tableName,filter,lastKeyEvaluated);


            for (Map<String ,AttributeValue> item : result.getItems()){
                itemCount++;
                int markField = countScan(item);
                if ((markField & MarkFieldsEnum.BLACKLIST.getValue()) == MarkFieldsEnum.BLACKLIST.getValue()){
                    blackCount ++;
                }
                if ((markField & MarkFieldsEnum.BLACKLIST_SUC.getValue()) == MarkFieldsEnum.BLACKLIST_SUC.getValue()){
                    blackSuc ++;
                }
                if ((markField & MarkFieldsEnum.XUNJI.getValue()) == MarkFieldsEnum.XUNJI.getValue()){
                    xunJiCount ++;
                }
                if ((markField & MarkFieldsEnum.XUNJI_SUC.getValue()) == MarkFieldsEnum.XUNJI_SUC.getValue()){
                    xunJiSuc ++;
                }
                if ((markField & MarkFieldsEnum.ZHICHA.getValue()) == MarkFieldsEnum.ZHICHA.getValue()){
                    zhiChaCount ++;
                }
                if ((markField & MarkFieldsEnum.ZHICHA_SUC.getValue()) == MarkFieldsEnum.ZHICHA_SUC.getValue()){
                    zhiChaSuc ++;
                }
                if ((markField & MarkFieldsEnum.MINGJIAN.getValue()) == MarkFieldsEnum.MINGJIAN.getValue()){
                    mingJianCount ++;
                }
                if ((markField & MarkFieldsEnum.MINGJIAN_SUC.getValue()) == MarkFieldsEnum.MINGJIAN_SUC.getValue()){
                    mingJianSuc ++;
                }

                if (itemCount % 100 == 0) {
                    log.info("*-*-**-*-**-*-*" + tableName + " scan: " + itemCount + "th! *-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*blackCount: " + blackCount + " blackSuc: " + blackSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*xunJiCount: " + xunJiCount + " xunJiSuc: " + xunJiSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*zhiChaCount: " + zhiChaCount + " zhiChaSuc: " + zhiChaSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*mingJianCount: " + mingJianCount + " mingJianSuc: " + mingJianSuc + "*-*-**-*-**-*-**-*-*\n");
                }
            }
           lastKeyEvaluated = result.getLastEvaluatedKey();

        }while (lastKeyEvaluated != null);
        log.info(tableName + " scan finished,toatl:" + itemCount);
        log.info("*-*-**-*-**-*-*blackCount: " + blackCount + " blackSuc: " + blackSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*xunJiCount: " + xunJiCount + " xunJiSuc: " + xunJiSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*zhiChaCount: " + zhiChaCount + " zhiChaSuc: " + zhiChaSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*mingJianCount: " + mingJianCount + " mingJianSuc: " + mingJianSuc + "*-*-**-*-**-*-**-*-*");

    }

    private static int countScan(Map<String,AttributeValue> item){
        int markField = MarkFieldsEnum.EMPTY.getValue();
        Gson gson = new Gson();
        //log.info("item:{}",gson.toJson(item));
        JSONObject jsonObject = JSON.parseObject(gson.toJson(item)).getJSONObject("risk_json").getJSONObject("s");
        if (jsonObject != null){
            if (jsonObject.getJSONObject("BLACK") != null){
                markField = markField + MarkFieldsEnum.BLACKLIST.getValue();
            }
            if (jsonObject.getJSONObject("fenli_xunji") != null){
                markField = markField + MarkFieldsEnum.XUNJI.getValue();
                int xunJi_score = Integer.parseInt(jsonObject.getJSONObject("fenli_xunji").getJSONObject("result").getString("score"));
                if (xunJi_score >= 300 && xunJi_score <= 850){
                    markField = markField + MarkFieldsEnum.XUNJI_SUC.getValue();
                }
            }if (jsonObject.getJSONObject("fenli_zhicha") != null){
                markField = markField +  MarkFieldsEnum.ZHICHA.getValue();
                int zhiCha_score = Integer.parseInt(jsonObject.getJSONObject("fenli_zhicha").getJSONObject("result").getString("score"));
                if (zhiCha_score >= 300 && zhiCha_score <= 850){
                    markField = markField + MarkFieldsEnum.ZHICHA_SUC.getValue();
                }
            }if (jsonObject.getJSONObject("MING_JIAN_REPORT") != null){
                markField = markField +  MarkFieldsEnum.MINGJIAN.getValue() + MarkFieldsEnum.MINGJIAN_SUC.getValue();
                String bj_score = jsonObject.getJSONObject("MING_JIAN_REPORT").getString("bj_score");
                String bj_details = jsonObject.getJSONObject("MING_JIAN_REPORT").getString("bj_details");
                if ("300.5".equals(bj_score) || "规则0：运营商数据缺失".equals(bj_details)){
                    markField = markField - MarkFieldsEnum.MINGJIAN_SUC.getValue();
                }
            }
        }

        return markField;
    }

    public static void main(String[] args) {
        scanPage("o2o-risk-result-prod");
    }

}
