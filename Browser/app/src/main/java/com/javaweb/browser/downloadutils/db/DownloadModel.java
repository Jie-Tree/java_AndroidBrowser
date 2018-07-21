package com.javaweb.browser.downloadutils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.javaweb.browser.downloadutils.bean.DownloadInfo;

import java.util.ArrayList;

public class DownloadModel extends SQLiteOpenHelper implements DownloadController {

	public static final String CREATE_HISTORY = "create table download("
			+ "id integer primary key autoincrement,"
			+ "title text,"
			+ "url text,"
			+ "isfinish text)";

	public DownloadModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
	public boolean addDownload(SQLiteDatabase db, String title, String url) {

		ContentValues download = new ContentValues();
		download.put("title", title);
		download.put("url", url);
		download.put("isfinish", "0");
		long result = db.insert("download", null, download);
		if(result!=-1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteDownload(SQLiteDatabase db, String id) {
		int result = db.delete("download", "id = ?", new String[]{ id });
		if(result!=0){
			return true;
		}else{
			return false;
		}

	}


	@Override
	public ArrayList<DownloadInfo> getAllDownload(SQLiteDatabase db) {
		String[] returnColmuns = new String[]{
				"id as _id",
				"title",
				"url",
				"isfinish"
		};

		Cursor download = db.query("download", returnColmuns, null, null, null, null, "id");

		ArrayList<DownloadInfo> arr = new ArrayList<>();
		while(download.moveToNext()){
			String id = String.valueOf(download.getInt(download.getColumnIndex("_id")));
			String title = download.getString(download.getColumnIndex("title"));
			String url = download.getString(download.getColumnIndex("url"));
			String isfinish = download.getString(download.getColumnIndex("isfinish"));

			DownloadInfo b = new DownloadInfo();
			b.setId(id);
			b.setTitle(title);
			b.setUrl(url);
			b.setIsfinish(isfinish);
			arr.add(0,b);
//			Log.v(TAG, "id:"+id+",title:"+title+",url:"+url);
		}
		download.close();
		return arr;
	}



}