package com.pear.yellowthird.init;

import com.lingju.voice.config.SystemConfig;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * Created by su on 16-10-10.
 */

/**
 *  日记文件配置
 *  log4j本身是不兼容android的，所以这里需要用到android-logging-log4j 来辅助兼容一下
 * */
public class Log4JConfig {

    /**
     * java代码日记文件的保存路径
     * */
    private static String mJavaLogFile;

    public static String getJavaLogFile() {
        if(null==mJavaLogFile)
            mJavaLogFile= SystemConfig.getPublicPath()+ File.separator + "output/JavaVoiceLog.txt";
        return mJavaLogFile;
    }

    /**
     * 初始化java相关代码的日记
     * **/
    public static void init()
    {
        try {
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(getJavaLogFile());
            logConfigurator.setMaxBackupSize(1);
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

    /**
     * 释放log4j的配置
     * */
    public static void unInit()
    {
        try {
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName("null.log");
            logConfigurator.setMaxBackupSize(1);
            logConfigurator.setRootLevel(Level.FATAL);
            logConfigurator.setLevel("org.apache", Level.FATAL);
            logConfigurator.setFilePattern("%t %d %-5p [%c{2}]-[%L] %m%n");
            logConfigurator.setLogCatPattern(":[%L] %m%n");
            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            logConfigurator.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
