package com.pear.yellowthird.init;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    /**权限申请结果回调标示码*/
    private static final int gDefaultRequestCoder=200;

    /**
     * 所有需要用到的权限
     * */
    public String[] mAllNeedPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**需要原来一个主视图活动*/
    private Activity mActivity;

    public PermissionsRequestInit(Activity activity)
    {
        mActivity=activity;
    }

    public PermissionsRequestInit(Activity activity,String[] allNeedPermissions)
    {
        mActivity=activity;
        mAllNeedPermissions=allNeedPermissions;
    }

    /**
     * 初始化申请权限
     * @param successCallback 申请成功后的回调
     * */
    private void init(Runnable successCallback)
    {
        //requestPermission(successCallback);
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
            log.info("onSucceed requestCode:" + requestCode
                    +",grantedPermissions"+grantedPermissions.toString());

            if(requestCode != gDefaultRequestCoder)
                return;
            doingByRealPermission();
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            log.error("onFailed requestCode:" + requestCode
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
            log.debug("doingByRealPermission result:" + result);
            if(result)
                successDoing();
            else
                failedDoing();
        }

        void successDoing()
        {
            log.debug("successDoing");
            /**运行在UI线程*/
            mActivity.runOnUiThread(mSuccessCallback);
        }


        void failedDoing()
        {
            log.error("failedDoing");
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, "请给我权限，给我权限请到设置界面设置", Toast.LENGTH_SHORT).show();
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


    /**
     * 权限提示和请求
     * @param title 提示标题
     * @param content 提示内容
     * @param successCallback 申请权限成功后的回掉
     * */
    public void permissionTipAndRequest(String title, String content, final Runnable successCallback)
    {
        if(AndPermission.hasPermission(mActivity,mAllNeedPermissions))
        {
            successCallback.run();
            return;
        }
        else
        {
            /**权限申请的提示*/
            final MaterialDialog progressDialog=new MaterialDialog.Builder(mActivity)

                    .title(title)
                    .content(content)
                    .positiveText("知道了")
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            switch (which)
                            {
                                case POSITIVE:
                                    requestPermission(successCallback);
                                    break;
                            }
                        }
                    })
                    .canceledOnTouchOutside(false)
                    .show();
        }
    }

    /**
     * 权限提示和请求
     * @param title 提示标题
     * @param content 提示内容
     * @param successCallback 申请权限成功后的回掉
     * */
    public void permissionTipAndRequest(String[] allNeedPermissions,String title, String content, final Runnable successCallback)
    {
        mAllNeedPermissions=allNeedPermissions;
        permissionTipAndRequest(title,content,successCallback);
    }

}
