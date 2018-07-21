package com.javaweb.browser.bookmark;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public interface BookmarkController {

	//添加书签
	public int addBookmark(SQLiteDatabase db, String title, String url);

	//删除书签
	public boolean deleteBookmark(SQLiteDatabase db, String id);

	//查询所有书签
	public ArrayList<?> getAllBookmark(SQLiteDatabase db);

}