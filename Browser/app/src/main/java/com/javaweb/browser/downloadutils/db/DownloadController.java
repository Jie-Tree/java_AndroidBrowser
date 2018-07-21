package com.javaweb.browser.downloadutils.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public interface DownloadController {

	public boolean addDownload(SQLiteDatabase db, String title, String url);

	public boolean deleteDownload(SQLiteDatabase db, String id);

	public ArrayList<?> getAllDownload(SQLiteDatabase db);

}