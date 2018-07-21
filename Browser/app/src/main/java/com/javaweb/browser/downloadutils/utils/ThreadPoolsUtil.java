package com.javaweb.browser.downloadutils.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolsUtil {

    private ExecutorService cachedThreadPool;
    private static ThreadPoolsUtil sThreadPoolsUtil;

    public static ThreadPoolsUtil getInstance(){
        if(sThreadPoolsUtil==null){
            synchronized (ThreadPoolsUtil.class){
                if (sThreadPoolsUtil==null){
                    sThreadPoolsUtil=new ThreadPoolsUtil();
                }
            }
        }
        return sThreadPoolsUtil;
    }

    private ThreadPoolsUtil(){
        cachedThreadPool=Executors.newCachedThreadPool();
    }
    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }


}
