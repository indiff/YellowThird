package com.pear.android.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.pear.common.lang.exceptionHandler.CrashHandler;

/**
 * 编写自己的Application，管理全局状态信息，比如Context
 */
public class GlobalApplication extends MultiDexApplication {

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


}