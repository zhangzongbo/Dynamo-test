package dynamo.tool.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.Charset;

import dynamo.tool.common.model.DynamoDBModel;
import dynamo.tool.common.util.Md5Util;
import dynamo.tool.model.DynamoDBContact;
import dynamo.tool.model.RiskResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zongbo.zhang on 6/14/18.
 */

@Slf4j
public class ScanFromFile {
    private static DynamoServiceImpl dynamoService = new DynamoServiceImpl();
    private static String CONTACT_TABLE = "crawler-contacts-prod";
    private static String RISK_TABLE = "o2o-risk-result-prod";

    private static AwsServiceImpl awsService = new AwsServiceImpl();

    public static void query(String orderNo, String openId){
        DynamoDBContact contact = queryModel(DynamoDBContact.class, Md5Util.toMd5(openId), CONTACT_TABLE);
        int contactNum = contact.getUserData().getContacts().size();
        RiskResult risk = queryModel(RiskResult.class, orderNo, RISK_TABLE);
        int regNum = 0;
        Double bjScore = 0.0;
        JSONObject mjReport = risk.getRiskJson().getJSONObject("MING_JIAN_REPORT");
        if (mjReport != null && mjReport.size() > 0){
            bjScore = Double.parseDouble(mjReport.getString("bj_score"));
            JSONObject report = mjReport.getJSONObject("report");
            if (report != null && report.size() > 0){
                JSONObject loan =  report.getJSONObject("loan_contact_stats");
                if (loan != null &&  loan.size() > 0){
                    JSONArray institutions = loan.getJSONArray("institutions");
                    if (institutions != null && institutions.size() > 0){
                        regNum = institutions.size();
                    }
                }
            }
        }
        log.info(orderNo + " | " + contactNum + " | " + regNum + " | " + bjScore);
    }

    public static  <T extends DynamoDBModel> T queryModel(Class<T> model, String primaryKey, String tableName){
        DynamoDBMapper dynamoDBMapper = dynamoService.getMapper(tableName);
        return dynamoDBMapper.load(model,primaryKey, DynamoDBMapperConfig.builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    public static void main(String[] args) throws IOException {
        log.info("OrderNo | contactNum | regNum | bjScore");
        String line;
        int count = 0;
        try (
                InputStream fis = new FileInputStream("/home/xyl/var/doc/order_order_no_user_open_id_SELECT__201806141158.csv");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                String[] tmp = line.replace("\"","").split(",");

                System.out.println(count + ": order_no: " + tmp[0] + " open_id: " + tmp[1]);

                query(tmp[0].trim().replace("\uFEFF","") ,tmp[1].trim());

                count ++;

            }
        }
    }
}
