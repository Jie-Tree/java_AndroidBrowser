package com.javaweb.browser.downloadutils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.javaweb.browser.downloadutils.bean.ThreadInfo;

import java.util.ArrayList;
import java.util.List;


public class DBManager {

    private DBHelper helper;
    public static DBManager sDBManager;

    public static DBManager getInstance(Context context) {
        if (sDBManager == null) {
            synchronized (DBManager.class) {
                if (sDBManager == null) {
                    sDBManager = new DBManager(context);
                }
            }
        }
        return sDBManager;
    }

    private DBManager(Context context) {
        helper = new DBHelper(context);
    }

    public void saveInfo(ThreadInfo info) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into threadinfo(thread_id,start_pos, end_pos,compelete_size,url) values ( ?,?,?,?,? )",//占位
                new Object[]{info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl()});
    }


    /**
     * 查看数据库中是否有数据
     */
    public boolean isHasInfos(String urlstr) {
        SQLiteDatabase db = helper.getWritableDatabase();
//        String sql = "select count(*) from threadinfo where url=?";
        Cursor cursor = db.query("threadinfo", null, "url = ?", new String[]{urlstr},
                null, null, null);
        boolean exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }


    public void closeDb() {
        helper.close();
    }

    public void deleteInfo(final String url) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("threadinfo", "url=?", new String[]{url});
//        db.close();
    }

    public void updataInfos(int threadId, int compeleteSize, String urlstr) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "update threadinfo set compelete_size=? where thread_id=? and url=?";
        Object[] bindArgs = {compeleteSize, threadId, urlstr};
        db.execSQL(sql, bindArgs);
    }

    /**
     * 得到下载具体信息
     */
    public List<ThreadInfo> getInfos(String urlstr) {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        String sql = "select thread_id, start_pos, end_pos,compelete_size,url from threadinfo where url=?";
        Cursor cursor = db.rawQuery(sql, new String[]{urlstr});
        while (cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo(cursor.getInt(0),
                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getString(4));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public ThreadInfo getInfo(String urlstr, int threadid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select thread_id, start_pos, end_pos,compelete_size,url from threadinfo where url=? and thread_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{urlstr, String.valueOf(threadid)});
        cursor.moveToFirst();
        ThreadInfo info = new ThreadInfo(cursor.getInt(cursor.getColumnIndex("thread_id")),
                cursor.getInt(cursor.getColumnIndex("start_pos")), cursor.getInt(cursor.getColumnIndex("end_pos")),
                cursor.getInt(cursor.getColumnIndex("compelete_size")), cursor.getString(cursor.getColumnIndex("url")));
        Log.d("DB", "get db info print: " + cursor.getInt(cursor.getColumnIndex("thread_id")) + " " +
                cursor.getInt(cursor.getColumnIndex("start_pos")) + " " + cursor.getInt(cursor.getColumnIndex("end_pos")) + " " + cursor.getInt(cursor.getColumnIndex("compelete_size")) + " ");
        cursor.close();
        return info;
    }


}
