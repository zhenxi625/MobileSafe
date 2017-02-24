package com.demo.safe.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ChenXingLing on 2017/2/23.
 */

public class SpUtils {

    private static SharedPreferences preferences;

    /**
     *
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点值
     */
    public static void putBoolean(Context context,String key,boolean value){
        if (null == preferences){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        if (null == preferences){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return preferences.getBoolean(key,defaultValue);
    }

    /**
     *
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点值
     */
    public static void putString(Context context,String key,String value){
        if (null == preferences){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        preferences.edit().putString(key, value).apply();
    }

    public static String getString(Context context,String key,String defaultValue){
        if (null == preferences){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return preferences.getString(key,defaultValue);
    }

    public static void remove(Context context, String key) {
        if (null == preferences){
            preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        preferences.edit().remove(key).apply();
    }
}
