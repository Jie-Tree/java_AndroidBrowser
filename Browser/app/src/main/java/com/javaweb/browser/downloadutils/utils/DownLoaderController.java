package com.javaweb.browser.downloadutils.utils;

import android.content.Context;
import android.os.Handler;
import com.javaweb.browser.downloadutils.state.DownloadState;



public class DownLoaderController {

    private DownloadState mState;

    public void setDownloadState(DownloadState state){
        mState=state;
    }

    public void prepareDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount){
        mState.prepareDownload(context, handler, downloadurl, filename);
    }

    public void stopDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount){
        mState.pauseDownload(context, handler, downloadurl, filename);
    }

    public void startDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount){
        mState.startDownload(context, handler, downloadurl, filename, threadcount);
    }

    public void cancleDownload(Context context, Handler handler, String downloadurl, String filename, int threadcount){
        mState.cancleDownload(context, handler, downloadurl, filename);
    }

}
