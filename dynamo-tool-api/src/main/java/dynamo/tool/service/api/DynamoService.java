package dynamo.tool.service.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.List;
import java.util.Map;

/**
 * Created by zongbo.zhang on 4/3/18.
 */
public interface DynamoService {

    /**
     * 获取DynamoDB Client
     * @return DynamoDB Client
     */
    AmazonDynamoDBClient getClient();

    /**
     * 通过表名获取 DynamoDB mapper
     * @param tableName 表名
     * @return 对象映射mapper
     */

    DynamoDBMapper getMapper(String tableName);

    /**
     * 通过表名获取DynamoDB table
     * @param tableName 表名
     * @return DynamoDB table
     */
    Table getTable(String tableName);

    /**
     * 用Mapper方式扫描指定表
     * @param tableName　要扫描的表名
     * @param classPath　与DynamoDB table 映射的类路径
     * @return
     */
    List<Object> scanTable(String tableName, String classPath, DynamoDBScanExpression dynamoDBScanExpression) throws ClassNotFoundException;

    /**
     *
     * @param tableName 要扫描的表名
     * @param filter　过滤条件
     * @return
     */
    ScanResult scanPage(String tableName, Map<String, Condition> filter,  Map<String, AttributeValue> lastKeyEvaluated);

    /**
     * Mapper方式 保存到指定表
     * @param o
     * @param tableName
     */
    void saveToTable(Object o, String tableName);

    /**
     * item 方式保存到Table
     * @param item
     * @param tableName
     */
    void saveToTable(Map<String, AttributeValue> item, String tableName);

}
