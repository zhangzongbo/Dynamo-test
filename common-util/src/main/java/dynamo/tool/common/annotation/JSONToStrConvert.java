package dynamo.tool.common.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenwen on 16/11/20.
 */
@DynamoDBTypeConverted(
        converter = JSONToStrConvert.Converter.class
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface JSONToStrConvert {

    public static final class Converter implements DynamoDBTypeConverter<String, JSONObject> {
        @Override
        public String convert(JSONObject json) {
            if (json == null){
                return null;
            }
            return json.toJSONString();
        }

        @Override
        public JSONObject unconvert(String s) {
            if (StringUtils.isEmpty(s)){
                return null;
            }
            return JSON.parseObject(s);
        }
    }
}
