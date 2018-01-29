package com.pear.android.app;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.pear.common.lang.exceptionHandler.CrashHandler;

/**
 * 编写自己的Application，管理全局状态信息，比如Context
 */
public class GlobalApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }

    /**
     * 返回全局上下文
     * */
    public static Context getContext(){
        return context;
    }


    /**
     * 电影全局的边下载边播放的缓存
     * */
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        GlobalApplication app=(GlobalApplication)context;
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(1)                      //最多只缓存一个文件
                .build();
    }

}