package util;


import com.google.gson.Gson;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import org.apache.commons.lang.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dynamo.tool.common.config.Config;
import entity.MobileLocation;
import enums.MarkFieldsEnum;
import lombok.extern.slf4j.Slf4j;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.builder;
import dynamo.tool.common.util.FileUtil;

/**
 * Created by zhangzb on 3/16/18.
 */

@Slf4j
public class DynamoDBUtil {

    /**
     * 获取数据源mapper
     */
    private static DynamoDBMapper getMapperSource(String tableName) {
        /**
         * 第一个字符串是秘钥ID,第二个字符串是秘钥内容,秘钥的获取是从aws网站生成的.
         */
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name.source"), Config.getString("aws.dynamodb.key.value.source")));

        /**
         * 这个是aws中国地区的终端节点.
         */
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));

        /**
         * 在aws客户端建立表后,使用aws-java-sdk-dynamodb包提供的mapper对表格进行操作
         */
        DynamoDBMapperConfig mapperConfig = builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName))
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.ITERATION_ONLY)
                .build();

        return new DynamoDBMapper(client, mapperConfig);
    }

    /**
     * 获取目标表mapper
     */
    private static DynamoDBMapper getMapperTarget(String tableName) {
        /**
         * 第一个字符串是秘钥ID,第二个字符串是秘钥内容,秘钥的获取是从aws网站生成的.
         */
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name.target"), Config.getString("aws.dynamodb.key.value.target")));

        /**
         * 这个是aws中国地区的终端节点.
         */
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));

        /**
         * 在aws客户端建立表后,使用aws-java-sdk-dynamodb包提供的mapper对表格进行操作
         */
        DynamoDBMapperConfig mapperConfig = builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName))
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.ITERATION_ONLY)
                .build();


        return new DynamoDBMapper(client, mapperConfig);
    }

    private static AmazonDynamoDBClient getSourceClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name.source"), Config.getString("aws.dynamodb.key.value.source")));
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));
        return client;
    }

    private static AmazonDynamoDBClient getTargetClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name.target"), Config.getString("aws.dynamodb.key.value.target")));
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));
        return client;
    }

    private static Table getTable(String tableName) {
        /**
         * 第一个字符串是秘钥ID,第二个字符串是秘钥内容,秘钥的获取是从aws网站生成的.
         */
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name"), Config.getString("aws.dynamodb.key.value")));

        /**
         * 这个是aws中国地区的终端节点.
         */
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));
        DynamoDB dynamoDB = new DynamoDB(client);
        return dynamoDB.getTable(tableName);
    }

//    private static void saveToTarget(DynomaDBMobileInfo mobileInfo, String tableName){
////        getMapperTarget().save(mobileInfo, DynamoDBMapperConfig.builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
//        getMapperTarget(tableName).save(mobileInfo, builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
//    }


    private static void saveToTable(Object o, String tableName) {
        getMapperTarget(tableName).save(o, builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    private static void saveToTable(Map<String, AttributeValue> item, String tableName) {

        AmazonDynamoDBClient targetClient = getTargetClient();
        //File file = new File("/home/xyl/var/doc/" + tableName + ".txt");
        File file = new File("/data/operator-data/file/" + tableName + ".txt");

        PutItemRequest putItemRequest = new PutItemRequest()
                .withItem(item)
                .withTableName(tableName);

        try {
            targetClient.putItem(putItemRequest);
        } catch (Exception e) {
            log.info("length:" + item.toString().length());
            saveToFile(item, file);
        }
    }

    private static void saveToFile(Object object, File file) {
        Gson gson = new Gson();
        FileUtil.writeFile(String.valueOf(gson.toJson(object)) + "\n", file);
    }



    /**
     * 使用Mapper扫描
     */

    private static void scanTable(String tableName, String classPath) throws ClassNotFoundException {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
        Map<String, Condition> filter = new HashMap<>();
//        filter.put("_id",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("1")));
        filter.put("mobile_no_seqment", new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("1")));
        dynamoDBScanExpression.setScanFilter(filter);

        log.info("begin download:");

        Class test = Class.forName(classPath);
        List<Object> scanResult = getMapperSource(tableName).scan(test, dynamoDBScanExpression);
        log.info("begin save");
        int count = 0;
        File file = new File("/home/xyl/var/doc/crawler-operator-prod.txt");
        for (Object location : scanResult) {
            MobileLocation mobileLocation = (MobileLocation) location;
            log.info(mobileLocation.toString());
            count++;
            if (count % 100 == 0) {
                log.info("*-*-**-*-**-*-*copy " + count + "th! *-*-**-*-**-*-**-*-*");
            }
            saveToTable(location, tableName);
//            saveToFile(MobileLocation,file);
        }
        log.info("end save");

        log.info("*-*-**-*-**-*-*copy finished! total:" + count + "*-*-**-*-**-*-**-*-*");

    }

    /**
     * 分页扫描　table
     */
    private static void scanPage(String tableName) {

//        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name"), Config.getString("aws.dynamodb.key.value")));
        AmazonDynamoDBClient client = getTargetClient();
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));
        Map<String, AttributeValue> lastKeyEvaluated = null;
        log.info("begin scan table:" + tableName);
        int count = 0;
        Map<String, Condition> filter = new HashMap<>();
        filter.put("update_time",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("2018-03")));
        filter.put("service_name",new Condition().withComparisonOperator(ComparisonOperator.BEGINS_WITH).withAttributeValueList(new AttributeValue().withS("ICE")));
        //Map<String,Condition> filter = new HashMap<>();
        //filter.put("AttributeValueList",new Condition().withAttributeValueList(new AttributeValue().withS("2017-12-06")));
        //filter.put("ComparisonOperator",new Condition().withComparisonOperator("BEGINS_WITH"));
        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withScanFilter(filter)
                    .withLimit(10)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = client.scan(scanRequest);
            for (Map<String, AttributeValue> item : result.getItems()) {
                count++;
                saveToTable(item,tableName);
            }
            lastKeyEvaluated = result.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);
        log.info(tableName + " scan finished,toatl:" + count);
    }

    private static void scanFile(String tableName, File file) {

    }

    private static HashMap<String, AttributeValue> parseString(String itemString) {

        String data = "{update_time={S: 2017-08-11 16:27:03,}, data={M: {sms_info={L: [{M: {sms_date={S: 1502439800384,}, sms_content={S: 01058511900已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502438640184,}, sms_content={S: 所有的成功，都来自不倦的努力和奔跑。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502436147462,}, sms_content={S: 人生最重要的不是所站的位置，而是所去的方向。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502435386064,}, sms_content={S: 生命中最美的相遇，是你;岁月中最美的季节，是你！谢谢你！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502435017194,}, sms_content={S: 生命中最美的相遇，是你;岁月中最美的季节，是你！谢谢你！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502433850329,}, sms_content={S: 人生最重要的不是所站的位置，而是所去的方向。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502427481918,}, sms_content={S: 所有的成功，都来自不倦的努力和奔跑。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502415243155,}, sms_content={S: 所有的成功，都来自不倦的努力和奔跑。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502411825118,}, sms_content={S: 人生最重要的不是所站的位置，而是所去的方向。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502372345063,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502370025264,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502368913742,}, sms_content={S: 02259780459已被多位用户标记为疑似广告推销，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502367510788,}, sms_content={S: 02258303322已被多位用户标记为疑似疑似骚扰电话，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502365975737,}, sms_content={S: 02258701440已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502362947817,}, sms_content={S: 02259780473已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502340137659,}, sms_content={S: 02258965550已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502273171505,}, sms_content={S: 073188040162已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502272341553,}, sms_content={S: 073188040162已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502271513068,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502270598444,}, sms_content={S: 073188040162已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502269629859,}, sms_content={S: 073188040162已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502268718257,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502249415291,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502247448578,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502246091410,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502199175418,}, sms_content={S: 02258320057已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502197782683,}, sms_content={S: 02258307707已被多位用户标记为疑似疑似骚扰电话，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502195175687,}, sms_content={S: 02258326678已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502192161108,}, sms_content={S: 一声问候、一个愿望、一串祝福，望你心中常有快乐涌现。,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502164047066,}, sms_content={S: 02258920238已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502113140608,}, sms_content={S: 02258909463已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502112343816,}, sms_content={S: 02258307499已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502111120708,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502109018583,}, sms_content={S: 02258701498已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502107925356,}, sms_content={S: 02258320058已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502106343465,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502098199902,}, sms_content={S: 中国移动和彩印提醒您：您正在接听的电话被用户举报，请谨慎处理！,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502096098551,}, sms_content={S: 02258920236已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502071478801,}, sms_content={S: 02258701413已被多位用户标记为疑似骚扰，请谨慎接听。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10658085,}},}, {M: {sms_date={S: 1502438658271,}, sms_content={S: 现在无法接听。能稍后再打给我吗？,}, sms_type={S: 1,}, sms_number={S: 15154870097,}},}, {M: {sms_date={S: 1502436718194,}, sms_content={S: 【话费流量提醒】您好！截至08月11日15时31分，您当月已消费：39.86元，您的话费余额为：49.84元。已使用移动数据流量9G721.29M。其中国内流量剩余500.00M，省内流量剩余7G826.71M，回复666可调整您的话费提醒频次。缴费、查询、办理移动业务，请关注“中国移动10086”微信公众号，随时随地为您提供服务。【中国移动】,}, sms_type={S: 1,}, sms_number={S: 10086,}},}, {M: {sms_date={S: 1502020087954,}, sms_content={S: 好的\uD83D\uDC4C,}, sms_type={S: 2,}, sms_number={S: 13887941944,}},}, {M: {sms_date={S: 1502020057617,}, sms_content={S: 宁洱王朝国际KTV会所\n" +
                "\n" +
                "尊敬的顾客朋友，您所存的乐堡啤酒，大理V6啤酒即将到期，请近期到我公司取出，逾期后将视为作废。详情到我公司收银台咨询，谢谢您的光临。\n" +
                ",}, sms_type={S: 1,}, sms_number={S: 13887941944,}},}, {M: {sms_date={S: 1501918178306,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的18287918126在08月05日09时36分给您来电1次。详情点击http://yyxx.10086.cn/z/hyy?ctyl8pob【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 18287918126,}},}, {M: {sms_date={S: 1501918177421,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的17687097403在08月04日16时51分给您来电1次，请方便时回复。【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1501918176492,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的17687097403在08月05日02时02分给您来电1次，请方便时回复。【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1501810483403,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的17687097403在08月03日23时55分给您来电1次，请方便时回复。【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1501015355714,}, sms_content={S: 我听到你哭了,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1501015346685,}, sms_content={S: 你确定你进去啦,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084854426,}, sms_content={S: 额,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084844442,}, sms_content={S: 嗯嗯,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084831450,}, sms_content={S: 额,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084790712,}, sms_content={S: 我还没回到住处，回去到我上去,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084775835,}, sms_content={S: 好，,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084711482,}, sms_content={S: 住处,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084700176,}, sms_content={S: 你在哪里,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499084685490,}, sms_content={S: 在哪   来陪我,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499082929925,}, sms_content={S: 我走啦,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499073659572,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的17687097403在07月03日17时20分给您来电并留言（参考摘要：今天是什么？你还不还行。） 免费拨打 1259950004 或点击链接收听留言 http://yyxx.10086.cn/z/hyy?r58ec6d7 温馨提示：参考摘要是由和留言提供的智能语音转文字服务（暂时仅支持普通话），方便您及时了解留言内容。如需继续使用该功能，请发送KZX到10658112。【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499072000636,}, sms_content={S: 哄你，你也要给我一个机会吧，不然我怎么哄，去梦里哄你吗,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071814191,}, sms_content={S: 嗯嗯。,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071627512,}, sms_content={S: 哦,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071528707,}, sms_content={S: 你都不愿意理我了，我怎么哄你，一直挂我电话，我怎么去哄你,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071345409,}, sms_content={S: 我哄了，昨晚我一直再哄，我电话不停的打，你就是不接，罗晶告诉我你在广场，我找了五遍，要不是她说你在李晓东处睡着，昨晚我应该一直再找你,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071256429,}, sms_content={S: 你就不会哄我么   你以前不是这样的,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499071227226,}, sms_content={S: 哦,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499066300842,}, sms_content={S: 你也是够了,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499065200798,}, sms_content={S: 什么意思,}, sms_type={S: 1,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499026716914,}, sms_content={S: 谢谢，这久你陪着我，谢谢你，让拥有你这么久，虽然不是很长，我已知足，四年之约，我不知道对你来说，还算不算数，但是我会等你，如果你不会等我，那就算我白等吧，,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1499026576504,}, sms_content={S: 分手了么，意思朋友都不可以做嘛,}, sms_type={S: 2,}, sms_number={S: 17687097403,}},}, {M: {sms_date={S: 1501810514689,}, sms_content={S: 和留言(语音信箱)提醒您：云南普洱的15125567548在08月04日08时36分给您来电1次，请方便时回复。【中国移动　和留言】,}, sms_type={S: 1,}, sms_number={S: 15125567548,}},}],}},}, client_customer_id={S: BDBC3ADC7C4C40FFAD7667FECB23FA52,}, _id={S: 7f2798cfa4aea98af9936a35958394be,}, version={S: v2,}, merge_times={S: 0,}, account={S: BDBC3ADC7C4C40FFAD7667FECB23FA52,}}";
        //Item item = ItemConverter.convert(data);


        //Item item = new Item().withJSON("document", data);
        //Map<String,AttributeValue> attributes = InternalUtils.toAttributeValues(item);

        //return attributes.get("document").getM();
        //Map<String,AttributeValue> item =
        data = data.trim()
                .replace("{M", "{\"M\"")
                .replace("{S", "{\"S\"")
                .replace("{L", "{\"L\"")
                .replace("{N", "{\"N\"")
                .replace(",}", "}")
        ;
        Pattern p = Pattern.compile("(\\{[^{}]*:[^{}]*})");
        Matcher m = p.matcher(data);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String[] strs = m.group(1).split(": ");
            String rgx = strs[0] + ": \"" + strs[1].replace("}", "") + "\"" + "}";
            //log.info("text:{}",rgx);
            m.appendReplacement(sb, rgx);
        }

        data = sb.toString();

        log.info(data);
        Pattern pattern = Pattern.compile(", ([^{}].*?)\\=\\{");
        Matcher matcher = pattern.matcher(data);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {

            log.info("group:{}", matcher.group(0));
            String regx = ", \"" + matcher.group(1) + "\":{";
            //log.info("text:{}",matcher.group(1));
            matcher.appendReplacement(stringBuffer, regx);
        }
        log.info("result:{}", stringBuffer.toString());
        //HashMap<String, AttributeValue> item = new HashMap<>();

        return null;
    }


    public static void main(String[] args) {
//        DynomaDBMobileInfo mobileInfo = DynamoDBUtil.queryMobileInfo("18045285092");

        //if (args == null || args.length < 1){
        //    return;
        //}
//        Class.forName(args[1])
//        scanTable("mobileLocation-test","com.cafintech.crawler.entity.MobileLocation");
//        scanTable(args[0],args[1]);
//        String tableName = "mobileLocation-test";
//        File file = new File("/home/xyl/var/doc/" + tableName + ".txt");

        //scanFile(tableName, file);
        //parseString("");

        //scanPage("crawler-contacts-prod");

        //scanPage(args[0],args[1]);

        scanPage("o2o-risk-result-prod");

//        saveToTarget(mobileInfo,Config.getString("aws.dynamodb.operator.targetTable.name"));
    }
}
