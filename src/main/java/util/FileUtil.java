//package util;
//
///**
// * Created by zhangzb on 3/19/18.
// */
//
//import com.amazonaws.util.IOUtils;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import javax.servlet.http.HttpUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * File tools
// */
//
//@Slf4j
//
//public class FileUtil {
//
//
//    /**
//     * keep the byte array to a file
//     */
//    public static File getFileFromBytes(byte[] bytes, String outputFile) {
//        File file;
//        BufferedOutputStream stream = null;
//        FileOutputStream fileOutputStream = null;
//        try {
//            file = new File(outputFile);
//            fileOutputStream = new FileOutputStream(file);
//            stream = new BufferedOutputStream(fileOutputStream);
//            stream.write(bytes);
//            return file;
//        } catch (IOException e) {
//            log.error("the data stream error",e);
//            return null;
//        } finally {
//            try {
//                if (stream != null) {
//                    stream.close();
//                }
//
//                if(fileOutputStream != null) {
//                    fileOutputStream.close();
//                }
//            } catch (IOException e) {
//                log.error("the data stream error",e);
//            }
//        }
//
//    }
//
//    /**
//     * File into a byte array
//     */
//    public static byte[] getBytesFromFile(File file) {
//        byte[] data;
//        FileInputStream fileInputStream = null;
//        ByteArrayOutputStream byteArrayOutputStream = null;
//        try {
//            if (file == null) {
//                return null;
//            }
//
//            fileInputStream = new FileInputStream(file);
//            byteArrayOutputStream = new ByteArrayOutputStream(4096);
//            byte[] b = new byte[4096];
//            int n;
//            while ((n = fileInputStream.read(b)) != -1) {
//                byteArrayOutputStream.write(b, 0, n);
//            }
//
//            fileInputStream.close();
//            byteArrayOutputStream.close();
//            data = byteArrayOutputStream.toByteArray();
//            return data;
//        } catch (IOException e) {
//            log.error("the data stream error",e);
//            return null;
//        } finally {
//            safeClose(byteArrayOutputStream);
//            safeClose(fileInputStream);
//        }
//    }
//
//    public static void safeClose(InputStream is) {
//        if (is != null) {
//            try {
//                is.close();
//            } catch (IOException e) {
//                // ignore
//            }
//        }
//    }
//
//    public static void safeClose(OutputStream os) {
//        if (os != null) {
//            try {
//                os.close();
//            } catch (IOException e) {
//                // ignore
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        InputStream is = FileUtil.class.getResourceAsStream("/policies/policy");
//        System.out.println(IOUtils.toString(is));
//    }
//}
