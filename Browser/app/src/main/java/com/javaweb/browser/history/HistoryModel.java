package com.javaweb.browser.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.javaweb.browser.history.History;

import java.util.ArrayList;

public class HistoryModel extends SQLiteOpenHelper implements HistoryController {

	public static final String CREATE_HISTORY = "create table history("
			+ "id integer primary key autoincrement,"
			+ "title text,"
			+ "url text)";

	public HistoryModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据库时，创建表
		db.execSQL(CREATE_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1) {

	}

	@Override
	public boolean addHistory(SQLiteDatabase db, String title, String url) {

		ContentValues history = new ContentValues();
		history.put("title", title);
		history.put("url", url);
		long result = db.insert("history", null, history);
		if(result!=-1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteHistory(SQLiteDatabase db, String id) {
		int result = db.delete("history", "id = ?", new String[]{ id });
		if(result!=0){
			return true;
		}else{
			return false;
		}

	}

	@Override
	public boolean modifyHistory(SQLiteDatabase db, String title, String url, String id) {
		ContentValues history = new ContentValues();
		history.put("title", title);
		history.put("url", url);
		int number = db.update("history", history, "id = ?", new String[]{id});
		if(number!=0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public ArrayList<History> getAllHistory(SQLiteDatabase db) {
		String[] returnColmuns = new String[]{
				"id as _id",
				"title",
				"url"
		};

		Cursor history = db.query("history", returnColmuns, null, null, null, null, "id");

		ArrayList<History> arr = new ArrayList<>();
		while(history.moveToNext()){
			String id = String.valueOf(history.getInt(history.getColumnIndex("_id")));
			String title = history.getString(history.getColumnIndex("title"));
			String url = history.getString(history.getColumnIndex("url"));

			History b = new History();
			b.setId(id);
			b.setTitle(title);
			b.setUrl(url);
			arr.add(0,b);
//			Log.v(TAG, "id:"+id+",title:"+title+",url:"+url);
		}
		history.close();
		return arr;
	}

	@Override
	public boolean checkHistory(SQLiteDatabase db, String url) {
		Cursor result = db.query("history",null, "url=?", new String[]{url},null, null,null);
		if(result.getCount()>0){
			result.close();
			return true;
		}else{
			result.close();
			return false;
		}
	}

}