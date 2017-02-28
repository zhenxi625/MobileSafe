package com.demo.safe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safe.util.LogUtil;

/**
 * Created by ChenXingLing on 2017/2/28.
 */

public class AddressDao extends Object {

    private static final String tag = "AddressDao";

    //1.指定访问数据库的路径
    public static String path = "data/data/com.demo.safe.activity/files/mAddress.db";
    public static String mAddress = "未知号码";

    public static String getAddress(String phone){
        mAddress = "未知号码";
        String reg = "^1[3-8]\\d{9}";
        //2.只读形式打开数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        if (phone.matches(reg)){
            //3.查询数据
            Cursor cursor = db.query("data1",new String[]{"outkey"},"id=?",new String[]{phone},null,null,null);
            if (cursor.moveToNext()){
                String outKey = cursor.getString(0);
                LogUtil.i(tag,outKey);
                Cursor cursor1 = db.query("data2",new String[]{"location"},"id=?",new String[]{outKey},null,null,null);
                if (cursor1.moveToNext()){
                    mAddress = cursor1.getString(0);
                    LogUtil.i(tag, mAddress);
                }
            } else {
                mAddress = "未知号码";
            }
        } else {
            int len = phone.length();
            switch (len){
                case 3:
                    mAddress = "报警电话";
                    break;
                case 4:
                    mAddress = "模拟器";
                    break;
                case 5:
                    mAddress = "服务电话";
                    break;
                case 7:
                    mAddress = "本地电话";
                    break;
                case 8:
                    mAddress = "本地电话";
                    break;
                case 11:
                    //(3+8)
                    String area = phone.substring(1,3);
                    Cursor cursor = db.query("data2",new String[]{"location"},"area=?",new String[]{area},null,null,null);
                    if (cursor.moveToNext()){
                        mAddress = cursor.getString(0);
                    } else {
                        mAddress = "未知号码";
                    }
                    break;
                case 12:
                    //(4+7)
                    String area1 = phone.substring(1,3);
                    Cursor cursor1 = db.query("data2",new String[]{"location"},"area=?",new String[]{area1},null,null,null);
                    if (cursor1.moveToNext()){
                        mAddress = cursor1.getString(0);
                    } else {
                        mAddress = "未知号码";
                    }
                    break;
                default:
                    break;
            }
        }
        return mAddress;
    }
}
