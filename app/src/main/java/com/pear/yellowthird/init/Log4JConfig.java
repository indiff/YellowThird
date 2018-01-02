package com.pear.yellowthird.init;

import android.content.Context;
import org.apache.log4j.Level;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 *  日记文件配置
 *  log4j本身是不兼容android的，所以这里需要用到android-logging-log4j 来辅助兼容一下
 * */
public class Log4JConfig {

    /**
     * java代码日记文件的保存路径
     * */
    public static String LOG_FILE;

    /**
     * 初始化java相关代码的日记
     * **/
    public static void init(Context context)
    {
        try {
            LOG_FILE=context.getCacheDir().getAbsolutePath()+ "/log.txt";
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(LOG_FILE);
            logConfigurator.setMaxBackupSize(2);
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.setFilePattern("%t %d %-5p [%c{2}]-[%L] %m%n");
            logConfigurator.setLogCatPattern(":[%L] %m%n");
            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            logConfigurator.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
