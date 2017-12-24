package com.pear.yellowthird.init;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 权限申请初始化
 * */
public class PermissionsRequestInit {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    /**权限申请结果回调标示码*/
    private static final int gDefaultRequestCoder=200;

    /**
     * 所有需要用到的权限
     * */
    private final String[] mAllNeedPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    /**需要原来一个主视图活动*/
    private Activity mActivity;

    public PermissionsRequestInit(Activity activity)
    {
        mActivity=activity;
    }

    /**
     * 初始化申请权限
     * @param successCallback 申请成功后的回调
     * */
    public void init(Runnable successCallback)
    {
        requestPermission(successCallback);
    }

    /**
     * 获取各种需要的权限
     * @param successCallback 获取权限成功后的回调
     */
    private void requestPermission(final Runnable successCallback) {
        AndPermission
                .with(mActivity)
                .requestCode(gDefaultRequestCoder)
                .permission(mAllNeedPermissions)
                .callback(new SelfPermissionListener(successCallback))
                .start();
    }

    /**
     * 监听权限申请回调结果
     * */
    class SelfPermissionListener implements PermissionListener {

        /**申请成功后的回调*/
        private Runnable mSuccessCallback;

        public SelfPermissionListener(Runnable successCallback)
        {
            mSuccessCallback=successCallback;
        }

        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            logger.debug("onSucceed requestCode:" + requestCode
                    +",grantedPermissions"+grantedPermissions.toString());

            if(requestCode != gDefaultRequestCoder)
                return;
            doingByRealPermission();
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            logger.debug("onFailed requestCode:" + requestCode
                    +",grantedPermissions"+deniedPermissions.toString());

            if(requestCode != gDefaultRequestCoder)
                return;
            doingByRealPermission();
        }


        /**
         * 为了兼容国产手机，不同的国产手机的权限策略是不一样的
         * */
        void doingByRealPermission()
        {
            boolean result= AndPermission.hasPermission(mActivity,mAllNeedPermissions);
            logger.debug("doingByRealPermission result:" + result);
            if(result)
                successDoing();
            else
                failedDoing();
        }

        void successDoing()
        {
            logger.debug("successDoing");
            mSuccessCallback.run();
        }


        void failedDoing()
        {
            logger.debug("failedDoing");
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, "请给我所有权限，我才能更好的工作", Toast.LENGTH_SHORT).show();
                }
            });

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    requestPermission(mSuccessCallback);
                }
            }, new Date(System.currentTimeMillis() + 10 * 1000));
        }

    };

}
