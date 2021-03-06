package com.pear.common.lang.exceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pear.common.utils.files.LogFileExtractUtils;
import com.pear.yellowthird.config.SystemConfig;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.init.Log4JConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
 *  
 * @author user 
 *  
 */  
public class CrashHandler implements UncaughtExceptionHandler {
      
    public static final String TAG = "CrashHandler";
      
    //系统默认的UncaughtException处理类   
    private UncaughtExceptionHandler mDefaultHandler;

    //CrashHandler实例  
    private static CrashHandler INSTANCE = new CrashHandler();

    //程序的Context对象  
    private Context mContext;

    //用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();
  
    //用于格式化日期,作为日志文件名的一部分  
    private DateFormat formatter = new SimpleDateFormat("mm-ss");
  
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     * 初始化 
     *  
     * @param context 
     */  
    public void init(Context context) {
        mContext = context;  
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }  
  
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {  
            //如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */  
    private boolean handleException(Throwable ex) {
        if (ex == null) {  
            return false;  
        }
        ex.printStackTrace();

        //收集设备参数信息   
        collectDeviceInfo(mContext);

        //获取出错日记
        String crashMessage=getCrashInfo(ex);
        sendCrashLogToService(crashMessage);

        toastShow(getSingleCause(ex));
        return true;
    }

    private void toastShow(final String crashMessage)
    {
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "哎呀，谁拿平底锅砸我!"/*\n"+crashMessage*/, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传错误日记到服务器上
     * */
    private void sendCrashLogToService(String crashMessage){

        /**最近运行的日记*/
        String lastSimpleLog=LogFileExtractUtils.getLastSimpleLog(Log4JConfig.LOG_FILE,10);
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .sendAppCrashServer(
                        "lastSimpleLog:"+lastSimpleLog+"/n"
                                +"crashMessage:"+crashMessage);
    }
      
    /** 
     * 收集设备参数信息 
     * @param ctx 
     */  
    public void collectDeviceInfo(Context ctx) {
        try {  
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }  
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }  
        }  
    }  
  
    /** 
     * 获取错误信息
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private String getCrashInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");  
        }

        sb.append(getSingleCause(ex));
        sb.append("\n");
        sb.append("timestamp :"+System.currentTimeMillis()+"\n");
        sb.append("time :"+formatter.format(new Date())+"\n");
        sb.append("version :"+ SystemConfig.VERSION+"\n");
        return sb.toString();
    }


    /**
     * 获取简单的cause原因
     * */
    private String getSingleCause(Throwable ex)
    {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }

}  