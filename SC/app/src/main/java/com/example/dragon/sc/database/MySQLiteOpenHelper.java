package com.example.dragon.sc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dragon on 2017/8/9.
 */


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //创建pkt表
    String createMypktTable = "create table mypkt(pckId integer,pktCtrlNbr varchar(30),update_state varchar(20))";

    //创建carton表
    String createCartonTable = "create table carton(cartonCode varchar(5),pktCtrlNbr varchar(30),mergeSeq varchar(10),total Integer,catonType varchar(20),mergeNbr varchar(15),pick_flag varchar(1))";

    String createt_w = "create table t_w(wid varchar(5),w varchar(10))";

    String createt_c = "create table t_c(cid varchar(5),c varchar(10),wid varchar(5))";

    String createt_d = "create table t_d(did varchar(5),d varchar(10),cid varchar(5))";


    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createMypktTable);
        db.execSQL(createCartonTable);
        db.execSQL(createt_w);
        db.execSQL(createt_c);
        db.execSQL(createt_d);
        Log.i("执行了", "数据库");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

