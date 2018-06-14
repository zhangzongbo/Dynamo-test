package dynamo.tool.service.api;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dynamo.tool.common.model.DynamoDBModel;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
public interface AwsService {
    /**
     * 保存数据到dynamoDb
     * @param model
     * @param tableName
     */
    void save(DynamoDBModel model, String tableName);

    /**
     * 从dynamodb中查询数据
     * @param model
     * @param primaryKey
     * @param <T>
     * @return
     */
    <T extends DynamoDBModel> T queryModel(Class<T> model,String primaryKey,String tableName);

    /**
     * 查询列表
     * @param model
     * @param tableName
     * @param scanExpression
     * @param <T>
     * @return
     */
    <T extends DynamoDBModel> List<T> queryModelList(Class<T> model, String tableName, DynamoDBScanExpression scanExpression);

    /**
     * 删除文件
     * @param primaryKey
     */
    void deleteS3Object(String primaryKey);

    /**
     * 上传文件
     * @param primaryKey
     * @param inputStream
     * @param objectMetadata
     */
    void putS3Object(String primaryKey, InputStream inputStream, ObjectMetadata objectMetadata);

    /**
     * 下载文件
     * @param primaryKey
     * @return
     * @throws IOException
     */
    InputStream getS3Object(String primaryKey) throws IOException;
}

