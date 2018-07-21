package com.javaweb.browser.downloadutils.state.impl;

import android.content.Context;
import android.os.Handler;
import com.javaweb.browser.downloadutils.download.FileDownloader;
import com.javaweb.browser.downloadutils.state.DownloadState;

public class CancleDownloadState implements DownloadState {
	@Override
	public void prepareDownload(Context context, Handler handler, String downloadurl, String filename) {

	}

	@Override
	public void startDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount) {

	}

	@Override
	public void pauseDownload(Context context, Handler handler, String downloadurl, String filename) {

	}

	@Override
	public void cancleDownload(Context context, Handler handler, String downloadurl, String filename) {
		FileDownloader.getInstance().init(context, handler, downloadurl);
		FileDownloader.getInstance().cancelDownload(downloadurl,filename);
	}
}
