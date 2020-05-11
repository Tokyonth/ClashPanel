package com.tokyonth.clashpanel.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "clash_panel.db";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE_SUBSCRIPTION = "create table subscription(_id integer primary key autoincrement," +
            "name text,url text,color text)";
    private static final String DROP_TABLE_SUBSCRIPTION = "drop table if exists subscription";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME , null, VERSION);
    }

    //如果数据表不存在，则调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLiteDatabase 用于操作数据库的工具栏
        db.execSQL(CREATE_TABLE_SUBSCRIPTION);
    }

    //升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SUBSCRIPTION);
        db.execSQL(CREATE_TABLE_SUBSCRIPTION);
    }

}
