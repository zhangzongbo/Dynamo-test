package dynamo.tool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import dynamo.tool.common.model.DynamoDBModel;
import dynamo.tool.common.util.NumberFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "crawler-contacts-prod")
public class DynamoDBContact extends DynamoDBModel {

    @DynamoDBHashKey(attributeName = "_id")
    public String _id;

    @DynamoDBAttribute(attributeName = "version")
    public String version;

    @DynamoDBAttribute(attributeName = "update_time")
    public String updateTime;

    @DynamoDBAttribute(attributeName = "client_customer_id")
    public String clientCustomerId;

    @DynamoDBAttribute(attributeName = "account")
    private String account;
    @DynamoDBAttribute(attributeName = "merge_times")
    private String mergeTimes;

    @DynamoDBAttribute(attributeName = "data")
    private AuthContactsDTO userData;

    @Data
    @DynamoDBDocument
    public static class AuthContactsDTO {

        @DynamoDBAttribute(attributeName = "contacts")
        private List<Contacts> contacts;
    }

    @Data
    @DynamoDBDocument
    public static class Contacts implements DynamoDBMd5<Contacts> {

        @DynamoDBAttribute(attributeName = "name")
        public String name;

        @DynamoDBAttribute(attributeName = "mobile")
        public String mobile;

        @DynamoDBAttribute(attributeName = "update_time")
        public String updateTime;

        @Override
        public String md5Value() {
            return getMobile() + getName();
        }

        @Override
        public Contacts validData() {
            if (StringUtils.isEmpty(getMobile())) {
                return null;
            }
            String mobile = NumberFormatUtil.formatPhoneNumber(getMobile());
            if (StringUtils.isEmpty(mobile)) {
                return null;
            }
            setMobile(mobile);
            return this;
        }
    }
}
