package com.demo.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ChenXingLing on 2017/3/3.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public BlackNumberOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "blacknumber.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "create table blacknumber (_id integer primary key autoincrement,phone varchar(20),mode varchar(5));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
