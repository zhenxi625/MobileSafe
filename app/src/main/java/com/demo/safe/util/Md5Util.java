package com.demo.safe.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ChenXingLing on 2017/2/23.
 */

public class Md5Util{

    public static String encoder(String pwd) {
        try {
            pwd = pwd + "mobileSafe"; //加盐
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(pwd.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes){
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2){
                    hexString = "0" + hexString;
                }
                builder.append(hexString);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
