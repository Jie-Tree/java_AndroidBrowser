package com.javaweb.browser.history;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public interface HistoryController {

	//添加历史
	public boolean addHistory(SQLiteDatabase db, String title, String url);

	//删除
	public boolean deleteHistory(SQLiteDatabase db, String id);

	//修改
	public boolean modifyHistory(SQLiteDatabase db, String title, String url, String id);

	//查询
	public ArrayList<?> getAllHistory(SQLiteDatabase db);

	//判断是否书签是否车重复，根据URL
	public boolean checkHistory(SQLiteDatabase db, String url);

}