package com.tokyonth.clashpanel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokyonth.clashpanel.bean.SubscriptionBean;
import com.tokyonth.clashpanel.helper.DataBaseHelper;

import java.util.ArrayList;

public class DataBaseOperate {

    private DataBaseHelper helper;

    public DataBaseOperate(Context context) {
        helper = new DataBaseHelper(context);
    }

    public void add(SubscriptionBean bean){
        //获取操作数据库的工具类
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MetaData.SubscriptionTable.NAME, bean.getName());
        values.put(MetaData.SubscriptionTable.URL, bean.getUrl());
        values.put(MetaData.SubscriptionTable.COLOR, bean.getColor());
        //参数（表名，可以为NULL的列名，ContentValues）
        //合法：insert into dog(name,age) values('xx',2)
        //不合法：insert into dog(name) values(null)
        db.insert(MetaData.SubscriptionTable.TABLE_NAME, MetaData.SubscriptionTable.NAME,values);
        db.close();
    }

    //根据ID查询结果
    public SubscriptionBean findById(int id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {MetaData.SubscriptionTable._ID, MetaData.SubscriptionTable.NAME,
                MetaData.SubscriptionTable.URL, MetaData.SubscriptionTable.COLOR};
        //是否去除重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分离条件
        Cursor c = db.query(true, MetaData.SubscriptionTable.TABLE_NAME,
                columns, MetaData.SubscriptionTable._ID+"=?",new String[]{String.valueOf(id)
                },null,null,null,null);

        SubscriptionBean subscriptionBean = null;
        if (c.moveToNext()){
            subscriptionBean = new SubscriptionBean();
            subscriptionBean.setId(c.getInt(c.getColumnIndexOrThrow(MetaData.SubscriptionTable._ID)));
            subscriptionBean.setName(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.NAME)));
            subscriptionBean.setUrl(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.URL)));
            subscriptionBean.setColor(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.COLOR)));
        }
        c.close();
        db.close();
        return subscriptionBean;
    }

    //查询所有
    public ArrayList<SubscriptionBean> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {MetaData.SubscriptionTable._ID, MetaData.SubscriptionTable.NAME,
                MetaData.SubscriptionTable.URL, MetaData.SubscriptionTable.COLOR};
        //是否去除重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分离条件
        Cursor c = db.query(true, MetaData.SubscriptionTable.TABLE_NAME,columns,
                null,null,null,null,null,null);

        ArrayList<SubscriptionBean> list = new ArrayList<>();
        SubscriptionBean subscriptionBeans = null;
        while (c.moveToNext()){
            subscriptionBeans = new SubscriptionBean();
            subscriptionBeans.setId(c.getInt(c.getColumnIndexOrThrow(MetaData.SubscriptionTable._ID)));
            subscriptionBeans.setName(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.NAME)));
            subscriptionBeans.setColor(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.COLOR)));
            subscriptionBeans.setUrl(c.getString(c.getColumnIndexOrThrow(MetaData.SubscriptionTable.URL)));
            list.add(subscriptionBeans);
        }
        c.close();
        db.close();
        return list;
    }

    //删除
    public void delete(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause = MetaData.SubscriptionTable._ID+"=?";
        String[] whereArgs = {String.valueOf(id)};
        //表名，事件条件的值
        db.delete(MetaData.SubscriptionTable.TABLE_NAME,whereClause,whereArgs);
        db.close();
    }

    //更新
    public void update(SubscriptionBean bean) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MetaData.SubscriptionTable.NAME,bean.getName());
        values.put(MetaData.SubscriptionTable.URL,bean.getUrl());
        values.put(MetaData.SubscriptionTable.COLOR,bean.getColor());
        String whereClause = MetaData.SubscriptionTable._ID+"=?";
        String[] whereArgs = {String.valueOf(bean.getId())};
        //表名，ContentValues，事件，条件的值
        db.update(MetaData.SubscriptionTable.TABLE_NAME,values,whereClause,whereArgs);
        db.close();
    }

    //事务处理
    public void transaction(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();//开始事务
        try {
           // db.execSQL("insert into dog(name,age)values('duang',4)");
           // db.execSQL("insert into dog(name,age)values('guang',3)");
            db.setTransactionSuccessful();//设置事务的成功标记
        }finally {
            db.endTransaction();//结束事务,如果判断事务标记是否为true，true就提交事务，false就回滚
        }
    }

}
