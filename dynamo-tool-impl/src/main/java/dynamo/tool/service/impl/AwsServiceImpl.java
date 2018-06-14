package dynamo.tool.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dynamo.tool.common.config.Config;
import dynamo.tool.common.model.DynamoDBModel;
import dynamo.tool.service.api.AwsService;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
public class AwsServiceImpl implements AwsService {

    private static DynamoServiceImpl dynamoService = new DynamoServiceImpl();

    AmazonS3Client amazonS3Client;

    //DynamoDBMapper dynamoDBMapper;

    DynamoDBMapper dynamoDBMapper = dynamoService.getMapper("crawler-contacts-prod");

    @Override
    public void save(DynamoDBModel model, String tableName){
        dynamoDBMapper.save(model, DynamoDBMapperConfig.builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    @Override
    public <T extends DynamoDBModel> T queryModel(Class<T> model,String primaryKey,String tableName){
        return dynamoDBMapper.load(model,primaryKey, DynamoDBMapperConfig.builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    @Override
    public <T extends DynamoDBModel> List<T> queryModelList(Class<T> model, String tableName, DynamoDBScanExpression scanExpression){
        return dynamoDBMapper.scan(model,scanExpression, DynamoDBMapperConfig.builder().withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build());
    }

    @Override
    public void deleteS3Object(String primaryKey) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(Config.getString("aws.bucket.name"), primaryKey));
    }

    @Override
    public void putS3Object(String primaryKey, InputStream inputStream, ObjectMetadata objectMetadata){
        amazonS3Client.putObject(new PutObjectRequest(Config.getString("aws.bucket.name"), primaryKey, inputStream, objectMetadata));
    }

    @Override
    public InputStream getS3Object(String primaryKey) throws IOException {
        S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(Config.getString("aws.bucket.name"), primaryKey));
        return s3object.getObjectContent();
    }
}
