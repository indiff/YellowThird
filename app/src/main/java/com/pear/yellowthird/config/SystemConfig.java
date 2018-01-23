package com.pear.yellowthird.config;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 系统配置参数
 * */
public class SystemConfig {

    public static int VERSION=0;

    /**查询时间*/
    public static final String QUERY_TIME="query_time";

    /**是否开启调试日期*/
    public static final String DEBUG_TIME_SWITCH="debug_time_switch";

    /**全局的单一实例*/
    private static SystemConfig gInstance;

    /**键值数据库句柄*/
    SharedPreferences mSharedPref;

    /**
     * 默认的查询时间
     * */
    final long defaultQueryTime=System.currentTimeMillis();

    /**默认不使用调试日期*/
    final Boolean defaultDebugTimeSwitch=false;

    private SystemConfig()
    {
    }

    /**
     * 获取全局的单一实例
     * */
    public static SystemConfig getInstance()
    {
        if(null==gInstance)
            gInstance=new SystemConfig();
        return gInstance;
    }

    /**
     * 初始化
     * @param activity 主程序的活动实例
     * @note:主要是初始化数据库
     * @throws RuntimeException
     * */
    public void init(Activity activity)
    {
        mSharedPref=activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void setQueryTime(long queryTime) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong(QUERY_TIME, queryTime);
        editor.commit();
    }

    public long getQueryTime() {
        return mSharedPref.getLong(QUERY_TIME,defaultQueryTime);
    }

    public void setDebugTimeSwitch(Boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(DEBUG_TIME_SWITCH, value);
        editor.commit();
    }

    public Boolean getDebugTimeSwitch() {
        return mSharedPref.getBoolean(DEBUG_TIME_SWITCH,defaultDebugTimeSwitch);
    }

}
