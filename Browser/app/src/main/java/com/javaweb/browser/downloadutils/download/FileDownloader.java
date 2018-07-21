package com.javaweb.browser.downloadutils.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.javaweb.browser.downloadutils.bean.ThreadInfo;
import com.javaweb.browser.downloadutils.constant.Constant;
import com.javaweb.browser.downloadutils.db.DBManager;
import com.javaweb.browser.downloadutils.utils.ThreadPoolsUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;



public class FileDownloader {

    private final String TAG = FileDownloader.class.getSimpleName();

    public static FileDownloader sFileDownloader;
    private Context context;
    private Handler handler;
    private String downloadurl;
    private int filesize;
    private String filename;
    private int threadCount;
    private HashMap<String, Integer> downloadStatemap;
    //Map中不允许重复的键

    public static FileDownloader getInstance() {
        if (sFileDownloader == null) {
            synchronized (FileDownloader.class) {
                if (sFileDownloader == null) {
                    sFileDownloader = new FileDownloader();
                }
            }
        }
        return sFileDownloader;
    }

    public synchronized FileDownloader init(Context context, Handler handler,
                                            String downloadurl, int filesize,
                                            String filename, int threadCount) {
        Log.d(TAG, "Run in init");
        this.context = context;
        this.handler = handler;
        this.downloadurl = downloadurl;
        this.filesize = filesize;
        this.filename = filename;
        this.threadCount = threadCount;
        if (downloadStatemap == null) {
            downloadStatemap = new HashMap<>();
        }
        initDatas();
        return this;
    }

    public synchronized FileDownloader init(Context context, Handler handler, String downloadurl) {
        Log.d(TAG, "Run in init");
        this.context = context;
        this.handler = handler;
        this.downloadurl = downloadurl;
        return this;
    }



    private void initDatas() {
        RandomAccessFile accessFile = null;
        File file;
        int block = (filesize % threadCount == 0) ? filesize / threadCount : filesize / threadCount + 1;
        try {
            file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!DBManager.getInstance(context).isHasInfos(downloadurl)) {
                Log.d(TAG, "run download info");
                for (int i = 0; i < threadCount; i++) {
                    ThreadInfo info = new ThreadInfo(i, i * block, (i + 1) * block, 0, downloadurl);
                    //分配任务
                    DBManager.getInstance(context).saveInfo(info);
                }
            }
            accessFile = new RandomAccessFile(file, "rw");
            if (accessFile.length() == filesize) {
                return;
            }
            accessFile.setLength(filesize);//预分配空间
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void pauseDownload(String downloadurl) {
        if (downloadStatemap != null && downloadStatemap.containsKey(downloadurl)) {
            downloadStatemap.put(downloadurl, Constant.DOWNLOAD_STATE_PAUSE);
        }
    }

    public void pauseAll() {
        if (downloadStatemap == null) {
            return;
        }
        for (String key:downloadStatemap.keySet()){
            downloadStatemap.put(key,Constant.DOWNLOAD_STATE_PAUSE);
        }

    }

    public int getDownloadState(String downloadurl) {
        int downloadstate = -1;
        if (downloadStatemap != null) {
            downloadstate = downloadStatemap.get(downloadurl);
        }
        return downloadstate;
    }

    public void putDownloadState(String downloadurl, int state) {
        if (downloadStatemap != null) {
            downloadStatemap.put(downloadurl, state);
        }
    }

    private void sendMessage(int what, int arg1, int arg2, Object obj) {
        Message message = new Message();
        message.what = what;
        if (arg1 != -1) {
            message.arg1 = arg1;
        }
        if (arg2 != -1) {
            message.arg2 = arg2;
        }
        if (obj != null) {
            message.obj = obj;
        }
        message.setTarget(handler);
        message.sendToTarget();
    }


    public synchronized void startDownload() {
        if (downloadStatemap.get(downloadurl) != null && downloadStatemap.get(downloadurl) == Constant.DOWNLOAD_STATE_START) {//下载中
            Log.d(TAG, "download return");
            return;
        }
        sendMessage(Constant.DOWNLOAD_START, filesize, -1, null);
        for (int i = 0; i < threadCount; i++) {
            ThreadPoolsUtil.getInstance().getCachedThreadPool().execute(new DownloadTask(context, handler, downloadurl, filesize, filename, i));
        }
    }

    public synchronized void cancelDownload(String downloadurl, String filename) {
        if (downloadStatemap != null && downloadStatemap.containsKey(downloadurl)) {
            downloadStatemap.put(downloadurl, Constant.DOWNLOAD_STATE_CANCLE);
        }
        sendMessage(Constant.DOWNLOAD_ClLEAN, -1, -1, downloadurl);
        File file;
        try {
            file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProgress() {
        if (DBManager.getInstance(context).isHasInfos(downloadurl)) {     //判断是否存在未完成的该任务
            sendMessage(Constant.DOWNLOAD_PREPARE, calculateCompeltesize(), getFilesizeFromDB(), null);
        }
    }

    private int calculateCompeltesize() {
        int compeltesize = 0;
        List<ThreadInfo> infos = DBManager.getInstance(context).getInfos(downloadurl);
        if (infos == null) {
            return 0;
        }
        for (ThreadInfo info : infos) {
            compeltesize += info.getCompeleteSize();
        }
        return compeltesize;
    }

    private int getFilesizeFromDB() {
        List<ThreadInfo> infos = DBManager.getInstance(context).getInfos(downloadurl);
        if (infos == null) {
            return 0;
        }

        return infos.get(infos.size() - 1).getEndPos();
    }

}
