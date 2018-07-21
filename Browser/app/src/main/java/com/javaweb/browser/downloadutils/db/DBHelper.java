package com.javaweb.browser.downloadutils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "down.db";
    private static final int VERSION = 1;
    private final String CREATE_TABLE="create table if not exists threadinfo" +
            "(_id integer PRIMARY KEY AUTOINCREMENT," +
            " thread_id integer," +
            "start_pos integer, " +
            "end_pos integer," +
            " compelete_size integer," +
            "url char)";

    public DBHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
