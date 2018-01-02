package com.pear.android.app;

import android.app.Application;
import android.content.Context;

/**
 * 编写自己的Application，管理全局状态信息，比如Context
 */
public class GlobalApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * 返回全局上下文
     * */
    public static Context getContext(){
        return context;
    }

}