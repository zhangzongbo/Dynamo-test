package dynamo.tool.model;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import dynamo.tool.common.config.Config;

/**
 * Created by zongbo.zhang on 4/3/18.
 */
public class DynamoClient {

    private AmazonDynamoDBClient client;

    public DynamoClient(){
        client = new AmazonDynamoDBClient(new BasicAWSCredentials(Config.getString("aws.dynamodb.key.name.target"), Config.getString("aws.dynamodb.key.value.target")));
        client.setEndpoint(Config.getString("aws.dynamodb.endpoint"));
    }

    public AmazonDynamoDBClient getClient() {
        return client;
    }

    public void setClient(AmazonDynamoDBClient client) {
        this.client = client;
    }
}
