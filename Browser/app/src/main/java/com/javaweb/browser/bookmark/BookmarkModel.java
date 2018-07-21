package com.javaweb.browser.bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BookmarkModel extends SQLiteOpenHelper implements BookmarkController{

	public static final String CREATE_BOOKMARK = "create table bookmark("
			+ "id integer primary key autoincrement,"
			+ "title text,"
			+ "url text)";
	public BookmarkModel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据库时，创建表
		db.execSQL(CREATE_BOOKMARK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1) {

	}

	@Override
	public int addBookmark(SQLiteDatabase db, String title, String url) {
		Cursor cursor = db.query("bookmark",null,null,null,null,null,null);
		while (cursor.moveToNext()) {
			System.out.println(cursor.getString(cursor.getColumnIndex("url")));
			if(url.equals(cursor.getString(cursor.getColumnIndex("url")))){
				return -1;//在数据库已存在
			}
		}
		cursor.close();
		ContentValues bookmark = new ContentValues();
		bookmark.put("title", title);
		bookmark.put("url", url);
		long result = db.insert("bookmark", null, bookmark);
		if(result!=-1){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public boolean deleteBookmark(SQLiteDatabase db, String id) {
		int result = db.delete("bookmark", "id = ?", new String[]{ id });
		if(result!=0){
			return true;
		}else{
			return false;
		}

	}

	@Override
	public ArrayList<Bookmark> getAllBookmark(SQLiteDatabase db) {
		String[] returnColmuns = new String[]{
				"id as _id",
				"title",
				"url"
		};

		Cursor bookmark = db.query("bookmark", returnColmuns, null, null, null, null, "id");

		ArrayList<Bookmark> arr = new ArrayList<>();
		while(bookmark.moveToNext()){
			String id = String.valueOf(bookmark.getInt(bookmark.getColumnIndex("_id")));
			String title = bookmark.getString(bookmark.getColumnIndex("title"));
			String url = bookmark.getString(bookmark.getColumnIndex("url"));

			Bookmark b = new Bookmark();
			b.setId(id);
			b.setTitle(title);
			b.setUrl(url);
			arr.add(0,b);
//			Log.v(TAG, "id:"+id+",title:"+title+",url:"+url);
		}
		bookmark.close();
		return arr;
	}

}