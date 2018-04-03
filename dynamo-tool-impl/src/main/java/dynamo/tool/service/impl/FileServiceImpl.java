package dynamo.tool.service.impl;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import dynamo.tool.common.util.FileUtil;
import dynamo.tool.service.api.FileService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zongbo.zhang on 4/3/18.
 */

@Slf4j
public class FileServiceImpl implements FileService{
    @Override
    public void saveToFile(Object object, File file) {
        Gson gson = new Gson();
        FileUtil.writeFile(String.valueOf(gson.toJson(object)),file);
    }

    @Override
    public void scanFile(String tableName, File file) {
        try {
            String encoding = "UTF-8";

            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                StringBuilder itemString = new StringBuilder();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (StringUtils.isNotEmpty(lineTxt)) {
                        if (!lineTxt.endsWith("}}")) {
                            itemString.append(lineTxt);
                        } else {
                            itemString.append(lineTxt);
                            log.info("lineText:{}", itemString);
                            itemString = new StringBuilder();
                        }
                    }
//                    HashMap<String,AttributeValue> item_values = parseString(itemString);

//                    saveToTable(item_values,tableName);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

}
