package com.demo.safe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safe.db.BlackNumberOpenHelper;
import com.demo.safe.entity.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXingLing on 2017/3/3.
 */

public class BlackNumberDao {
    private final BlackNumberOpenHelper numberOpenHelper;

    //BlackNumberDao单例模式
    //1私有构造方法
    private BlackNumberDao(Context context) {
        //创建数据库及表结构
        numberOpenHelper = new BlackNumberOpenHelper(context, null, null, 1);
    }

    //2声明一个当前类的对象
    private static BlackNumberDao blackNumberDao = null;

    //3提供一个方法，如果当前类的对象为空，创建一个新的
    public static BlackNumberDao getInstance(Context context) {
        if (blackNumberDao == null) {
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }

    public void insert(String phone, String mode) {
        SQLiteDatabase db = numberOpenHelper.getWritableDatabase();//开启数据库准备做写入操作
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("mode", mode);
        db.insert("blacknumber", null, contentValues);
        db.close();
    }

    public void delete(String phone) {
        SQLiteDatabase db = numberOpenHelper.getWritableDatabase();//开启数据库准备做写入操作
        db.delete("blacknumber", "phone=?", new String[]{phone});
        db.close();
    }

    public void update(String phone, String mode) {
        SQLiteDatabase db = numberOpenHelper.getWritableDatabase();//开启数据库准备做写入操作
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        db.update("blacknumber", contentValues, "phone=?", new String[]{phone});
        db.close();
    }

    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = numberOpenHelper.getWritableDatabase();//开启数据库准备做写入操作
        Cursor cursor = db.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");

        List<BlackNumberInfo> infoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);

            infoList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();

        return infoList;
    }

}
