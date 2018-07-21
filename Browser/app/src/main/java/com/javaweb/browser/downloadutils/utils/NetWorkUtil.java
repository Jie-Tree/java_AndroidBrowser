package com.javaweb.browser.downloadutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetWorkUtil {//网络可不可用

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivity=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity!=null){
            NetworkInfo networkinfo=connectivity.getActiveNetworkInfo();
            if (networkinfo!=null&&networkinfo.isConnected()){
                if (networkinfo.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

}
