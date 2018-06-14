package dynamo.tool.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
public class NumberFormatUtil {

    public static String formatPhoneNumber(String phone){
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        phone = phone.replaceAll("[ |-]", "");

        Pattern patternMobile = Pattern.compile("^(\\(\\+86\\)|\\(86\\)|\\+86|86)?(\\d*)$");
        Matcher matcherMobile = patternMobile.matcher(phone);
        if (matcherMobile.find()) {
            return matcherMobile.group(2);
        } else {
            return null;
        }
    }
}