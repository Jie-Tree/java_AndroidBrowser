package com.javaweb.browser.downloadutils.state;

import android.content.Context;
import android.os.Handler;



public interface DownloadState {

    void prepareDownload(Context context, Handler handler, String downloadurl, String filename);
    void startDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount);
    void pauseDownload(Context context, Handler handler, String downloadurl, String filename);
    void cancleDownload(Context context, Handler handler, String downloadurl, String filename);

}
