package dynamo.tool.service.api;

import java.io.File;

/**
 * Created by zongbo.zhang on 4/3/18.
 */
public interface FileService {

    /**
     * 保存到文件
     * @param object　要保存的对象
     * @param file　目标文件
     */
    void saveToFile(Object object, File file);

    /**
     * 从文件中读取
     * @param tableName　
     * @param file　源文件
     */
    void scanFile(String tableName, File file);
}
