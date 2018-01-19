package com.pear.yellowthird.init;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pear.common.utils.net.FileDownload;
import com.pear.yellowthird.config.SystemConfig;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.UpdateVersion;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 安装新版本
 */

public class NewVersionInstall {

    /**日记*/
    private static Logger log = Logger.getLogger(NewVersionInstall.class);

    UpdateVersion updateVersion;

    Activity activity;


    NewVersionInstall(Activity activity,UpdateVersion updateVersion)
    {
        this.activity=activity;
        this.updateVersion=updateVersion;
    }

    /**
     * 检查和安装新版本
     */
    public void checkAndInstall() {
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .getNewsVersion()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        try {
                            JSONObject json = new JSONObject(data);
                            int version = json.getInt("version");
                            if (SystemConfig.VERSION >= version)
                                return;
                            final String href = json.getString("href");

                            /**发表过程中一直等待*/
                            final MaterialDialog progressDialog=new MaterialDialog.Builder(activity)

                                    .title("检测到新版本。")
                                    .content("自动更新程序需要用到读取sd卡权限。\n请给我权限，否则我将不能正常工作")
                                    .positiveText("知道了")
                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            switch (which)
                                            {
                                                case POSITIVE:
                                                    downUpdateApk(href);
                                                    break;
                                            }
                                        }
                                    })
                                    .canceledOnTouchOutside(false)
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 下载更新包
     * */
    private void downUpdateApk(final String href) {
        Runnable downFile=new Runnable() {
            @Override
            public void run() {
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String apkSavePath =updateVersion.getSavePath();
                        boolean result = FileDownload.downloadFile(href, apkSavePath);
                        if (result)
                            subscriber.onNext(apkSavePath);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Action1<String>() {
                                    @Override
                                    public void call(String path) {
                                        notifyUserApkWillUpdate(path);
                                    }
                                });
            }
        };

        new PermissionsRequestInit(activity).init(downFile);
    }

    /**
     * 通知用户apk即将会更新
     * */
    private void notifyUserApkWillUpdate(final String path) {

        /**发表过程中一直等待*/
        final MaterialDialog progressDialog=new MaterialDialog.Builder(activity)

                .title("检测到重要的新版本。你必须安装后才能正常使用。")
                .content("1. 优化了播放速度。\n2. 修复某些BUG。 \n3. 加入了一些很有趣的功能")
                .positiveText("我现在就装")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which)
                        {
                            case POSITIVE:
                                updateVersion.updateVersion(path);
                                break;
                        }
                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    /**
     * 根据apk的路径，来更新当前程序
     * */
    /*
    private void updateByLocalApk(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri= FileProvider.getUriForFile(
                activity,
                "com.pear.android.app.GlobalApplication.file_provider",
                new File(apkPath));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }
    */

}