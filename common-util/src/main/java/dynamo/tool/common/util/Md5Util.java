package dynamo.tool.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zongbo.zhang on 6/14/18.
 */
public class Md5Util {
    /**
     *
     * @param str
     *            明文
     * @return 32位密文
     */
    public static String toMd5(String str) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public static void main(String[] args) {
        System.out.print(Md5Util.toMd5("92C6492E09D04752AA08CFC9AC78638E"));
    }
}
