package com.demo.safe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ChenXingLing on 2017/2/28.
 */

public class AddressDao extends Object {

    private static final String tag = "AddressDao";

    //1.指定访问数据库的路径
    public static String path = "data/data/com.demo.cxl.safedemo/files/address.db";
    public static String mAddress = "未知号码";

    public static String getAddress(String phone) {
        mAddress = "未知号码";
        String reg = "^1[3-8]\\d{9}";
        //2.只读形式打开数据库

        if (phone.matches(reg) && phone.length() > 7) {
            phone = phone.substring(0, 7);
            //3.查询数据
            query(phone);
        } else {
            int len = phone.length();
            switch (len) {
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
                    query(phone.substring(0, 7));
                    break;
                case 8:
                    query(phone.substring(0, 7));
                    break;
                case 11:
                    //(3+8)
                    String area = phone.substring(1, 3);
                    query(area);
                    break;
                case 12:
                    //(4+7)
                    String area1 = phone.substring(1, 3);
                    query(area1);
                    break;
                default:
                    break;
            }
        }
        return mAddress;
    }

    private static String query(String phone) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("info", new String[]{"cardtype"}, "mobileprefix=?", new String[]{phone}, null, null, null);
        if (cursor.moveToNext()) {
            mAddress = cursor.getString(0);
        } else {
            mAddress = "未知号码";
        }
        return mAddress;
    }
}
