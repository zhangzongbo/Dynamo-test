package dynamo.tool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
public interface DynamoDBMd5<T> {

    @DynamoDBIgnore
    String md5Value();

    T validData();
}
