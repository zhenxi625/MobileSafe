package com.demo.safe.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ChenXingLing on 2017/2/21.
 */
public class StreamUtil {

    public static String streamToString(InputStream inputStream) {
        //讲读取的内容读取至缓存中，转换成字符串返回
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int temp;
        try {
            while ((temp = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer,0,temp);
            }
            return outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
