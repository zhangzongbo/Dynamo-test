package dynamo.tool.service.impl;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.List;
import java.util.Map;
import dynamo.tool.model.DynamoClient;
import dynamo.tool.service.api.DynamoService;
import lombok.extern.slf4j.Slf4j;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.builder;

/**
 * Created by zongbo.zhang on 4/3/18.
 */

@Slf4j

public class DynamoServiceImpl implements DynamoService {

    AmazonDynamoDBClient client = new DynamoClient().getClient();

    @Override
    public AmazonDynamoDBClient getClient() {

        return client;
    }

    @Override
    public DynamoDBMapper getMapper(String tableName) {
        DynamoDBMapperConfig mapperConfig = builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName))
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.ITERATION_ONLY)
                .build();
        return new DynamoDBMapper(client,mapperConfig);
    }

    @Override
    public Table getTable(String tableName) {

        DynamoDB dynamoDB = new DynamoDB(client);
        return dynamoDB.getTable(tableName);
    }

    @Override
    public List<Object> scanTable(String tableName, String classPath, DynamoDBScanExpression dynamoDBScanExpression) throws ClassNotFoundException {

        Class dynamoClass = Class.forName(classPath);
        List<Object> scanResult = getMapper(tableName).scan(dynamoClass,dynamoDBScanExpression);

        return scanResult;
    }

    @Override
    public ScanResult scanPage(String tableName, Map<String, Condition> filter,  Map<String, AttributeValue> lastKeyEvaluated) {

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(tableName)
                    .withScanFilter(filter)
                    .withLimit(10)
                    .withExclusiveStartKey(lastKeyEvaluated)
                    ;


        return client.scan(scanRequest);
    }

    @Override
    public void saveToTable(Object o, String tableName) {
        getMapper(tableName).save(o,builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    @Override
    public void saveToTable(Map<String, AttributeValue> item, String tableName) {
        PutItemRequest putItemRequest = new PutItemRequest()
                .withItem(item)
                .withTableName(tableName)
                ;
        try {
            client.putItem(putItemRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
