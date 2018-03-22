import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wukaiwei on 3/20/18.
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws InterruptedException {
        log.info("hello");

        String data = "aaaa11dddd@@@@22dd$$$d33afaf^^^wf566";

        // 内容
        String content = "aaa{{bbb}}ccc{{ddd}}eee";
        // 正则
        Pattern p = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher m = p.matcher(content);

        // 返回结果
        StringBuffer result = new StringBuffer();
        while (m.find()) {
            System.out.println("正则匹配到的内容：" + m.group());
            System.out.println("花括号里面的内容：" + m.group(1));
            // 替换为 -
            String rep = "\"" + m.group(1) + "\"";
            m.appendReplacement(result, rep);
        }
        // 替换之前匹配到的内容
        m.appendTail(result);

        System.out.println("\n原始文本：" + content);
        System.out.println("替换后的文本：" + result.toString());

        log.info("testttttttttttttttttttttttttttttttttts");

        //data = filter(data);
        //Pattern pattern = Pattern.compile("\\d");
        //Matcher matcher = pattern.matcher(data);
        //
        //
        //while (matcher.find()){
        //    System.out.println(matcher.group(0));
        //}
        //data.replaceAll("\\d","");
        //System.out.println(data);
        //Thread.sleep(100000);
    }

    private static String filter(String data){
        String regEx="[`!@#$%^&*()+=|{}':;',//[//].<>/?！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern   =   Pattern.compile(regEx);
        Matcher matcher   =   pattern.matcher(data);
        return   matcher.replaceAll("").trim();
    }
}
