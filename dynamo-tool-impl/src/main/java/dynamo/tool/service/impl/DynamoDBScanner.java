package dynamo.tool.service.impl;

import com.google.gson.Gson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.apache.commons.lang.StringUtils;

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

    public static void scanPage(String tableName, String month){

        int itemCount = 0;

        int blackCount = 0, blackSuc = 0, xunJiCount = 0, xunJiSuc = 0, zhiChaCount = 0, zhiChaSuc = 0, mingJianCount = 0, mingJianSuc = 0, huiYanCount =0, huiYanSuc = 0;


        Map<String, AttributeValue> lastKeyEvaluated = null;

        Map<String, Condition> filter = new HashMap<>();
        filter.put("update_time",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS(month))); //"2018-04"
        filter.put("service_name",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("ICE")));

        log.info("begin scan table: {} ",tableName);
        do{
           ScanResult result = dynamoService.scanPage(tableName,filter,lastKeyEvaluated);


            for (Map<String ,AttributeValue> item : result.getItems()){
                itemCount++;
                int markField = countScan(item);
                /**
                 * 黑名单调用次数
                 */
                if ((markField & MarkFieldsEnum.BLACKLIST.getValue()) == MarkFieldsEnum.BLACKLIST.getValue()){
                    blackCount ++;
                }
                /**
                 * 黑名单成功次数
                 */
                if ((markField & MarkFieldsEnum.BLACKLIST_SUC.getValue()) == MarkFieldsEnum.BLACKLIST_SUC.getValue()){
                    blackSuc ++;
                }
                /**
                 * 寻迹调用次数
                 */
                if ((markField & MarkFieldsEnum.XUNJI.getValue()) == MarkFieldsEnum.XUNJI.getValue()){
                    xunJiCount ++;
                }
                /**
                 * 寻迹成功次数
                 */
                if ((markField & MarkFieldsEnum.XUNJI_SUC.getValue()) == MarkFieldsEnum.XUNJI_SUC.getValue()){
                    xunJiSuc ++;
                }
                /**
                 * 智查调用次数
                 */
                if ((markField & MarkFieldsEnum.ZHICHA.getValue()) == MarkFieldsEnum.ZHICHA.getValue()){
                    zhiChaCount ++;
                }
                /**
                 * 智查成功次数
                 */
                if ((markField & MarkFieldsEnum.ZHICHA_SUC.getValue()) == MarkFieldsEnum.ZHICHA_SUC.getValue()){
                    zhiChaSuc ++;
                }
                /**
                 * 慧眼调用次数
                 */
                if ((markField & MarkFieldsEnum.HUIYAN.getValue()) == MarkFieldsEnum.HUIYAN.getValue()){
                    huiYanCount ++;
                }
                /**
                 * 慧眼成功次数
                 */
                if ((markField & MarkFieldsEnum.HUIYAN_SUC.getValue()) == MarkFieldsEnum.HUIYAN_SUC.getValue()){
                    huiYanSuc ++;
                }
                /**
                 * 明鉴调用次数
                 */
                if ((markField & MarkFieldsEnum.MINGJIAN.getValue()) == MarkFieldsEnum.MINGJIAN.getValue()){
                    mingJianCount ++;
                }
                /**
                 * 明鉴成功次数
                 */
                if ((markField & MarkFieldsEnum.MINGJIAN_SUC.getValue()) == MarkFieldsEnum.MINGJIAN_SUC.getValue()){
                    mingJianSuc ++;
                }

                if (itemCount % 100 == 0) {
                    log.info("*-*-**-*-**-*-*" + tableName + " scan: " + itemCount + "th! *-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*blackCount: " + blackCount + " blackSuc: " + blackSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*xunJiCount: " + xunJiCount + " xunJiSuc: " + xunJiSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*zhiChaCount: " + zhiChaCount + " zhiChaSuc: " + zhiChaSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*mingJianCount: " + mingJianCount + " mingJianSuc: " + mingJianSuc + "*-*-**-*-**-*-**-*-*");
                    log.info("*-*-**-*-**-*-*huiYanCount: " + huiYanCount + " huiYanSuc: " + huiYanSuc + "*-*-**-*-**-*-**-*-*\n");
                }
            }
           lastKeyEvaluated = result.getLastEvaluatedKey();

        }while (lastKeyEvaluated != null);
        log.info(tableName + " scan finished,toatl:" + itemCount);
        log.info("*-*-**-*-**-*-*blackCount: " + blackCount + " blackSuc: " + blackSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*xunJiCount: " + xunJiCount + " xunJiSuc: " + xunJiSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*zhiChaCount: " + zhiChaCount + " zhiChaSuc: " + zhiChaSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*mingJianCount: " + mingJianCount + " mingJianSuc: " + mingJianSuc + "*-*-**-*-**-*-**-*-*");
        log.info("*-*-**-*-**-*-*huiYanCount: " + huiYanCount + " huiYanSuc: " + huiYanSuc + "*-*-**-*-**-*-**-*-*");

    }

    private static int countScan(Map<String,AttributeValue> item){
        int markField = MarkFieldsEnum.EMPTY.getValue();
        Gson gson = new Gson();
        //log.info("item:{}",gson.toJson(item));
        JSONObject jsonObject = JSON.parseObject(gson.toJson(item)).getJSONObject("risk_json").getJSONObject("s");
        if (jsonObject != null){
            /**
             * 黑名单默认成功
             */
            if (jsonObject.getJSONObject("BLACK") != null){
                markField = markField + MarkFieldsEnum.BLACKLIST.getValue();
            }
            /**
             * 寻迹分不为-1时成功
             */
            if (jsonObject.getJSONObject("fenli_xunji") != null){
                markField = markField + MarkFieldsEnum.XUNJI.getValue();
                int xunJi_score = Integer.parseInt(jsonObject.getJSONObject("fenli_xunji").getJSONObject("result").getString("score"));
                if (xunJi_score != -1){
                    markField = markField + MarkFieldsEnum.XUNJI_SUC.getValue();
                }
            }
            /**
             * 智查分不为-1时成功
             */
            if (jsonObject.getJSONObject("fenli_zhicha") != null){
                markField = markField +  MarkFieldsEnum.ZHICHA.getValue();
                int zhiCha_score = Integer.parseInt(jsonObject.getJSONObject("fenli_zhicha").getJSONObject("result").getString("score"));
                if (zhiCha_score != -1){
                    markField = markField + MarkFieldsEnum.ZHICHA_SUC.getValue();
                }
            }
            /**
             * 明鉴分不为300.5且没有出现"规则0"时成功
             */
            if (jsonObject.getJSONObject("MING_JIAN_REPORT") != null){
                markField = markField +  MarkFieldsEnum.MINGJIAN.getValue() + MarkFieldsEnum.MINGJIAN_SUC.getValue();
                String bj_score = jsonObject.getJSONObject("MING_JIAN_REPORT").getString("bj_score");
                String bj_details = jsonObject.getJSONObject("MING_JIAN_REPORT").getString("bj_details");
                if ("300.5".equals(bj_score) || "规则0：运营商数据缺失".equals(bj_details)){
                    markField = markField - MarkFieldsEnum.MINGJIAN_SUC.getValue();
                }
            }
            /**
             * 慧眼bj_score不为空时成功
             */
            if (jsonObject.getJSONObject("fenli_huiyan18") != null){
                markField = markField +  MarkFieldsEnum.HUIYAN.getValue();
                String huiyan_score = jsonObject.getJSONObject("fenli_huiyan18")
                        .getJSONObject("result")
                        .getJSONObject("GeneralRepaymentScore")
                        .getJSONObject("GeneralRepaymentScore")
                        .getString("bj_score");
                if (StringUtils.isNotEmpty(huiyan_score)){
                    markField = markField + MarkFieldsEnum.HUIYAN_SUC.getValue();
                }
            }
        }

        return markField;
    }

    public static void main(String[] args) {
        //scanPage("o2o-risk-result-test","2018-04");
        if (args.length < 2){
            log.info("参数错误，请检查参数！示例： test 2018-04");
            return;
        }
        String tableName;
        switch (args[0]){
            case "test":
                tableName = "o2o-risk-result-test";
                break;
            case "prod":
                tableName = "o2o-risk-result-prod";
                break;
            default:
                log.info("表名参数输入错误，可选参数：test | prod");
                return;
        }
        if (!args[1].contains("-") || !args[1].startsWith("20") || !(args[1].length() == 7)){
            log.info("月份参数错误：示例：2018-04");
            return;
        }

        scanPage(tableName,args[1]);
    }

}
