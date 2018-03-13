package com.pear.yellowthird.activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pear.android.view.NoScrollViewPager;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.databases.AllDatabases;
import com.pear.yellowthird.activitys.operations.DoubleBackClickOperation;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.config.SystemConfig;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.init.AllOnceInit;
import com.pear.yellowthird.interfaces.UpdateVersion;
import com.pear.yellowthird.style.factory.StyleFragmentFactory;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.style.vo.StyleType;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.MainNavPageIndicator;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements UpdateVersion {

    /**
     * 日记
     */
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    /**
     * 活动的上下文
     */
    Activity activity;

    /**
     * 底部菜单栏的适配器
     */
    MainBottomMenuAdapter adapter;

    /**
     * 所有子界面
     */
    NoScrollViewPager pager;

    /**
     * 底部菜单视图
     */
    MainNavPageIndicator indicator;

    /**
     * 预加载的界面
     */
    LoadingView loadingView;

    /**
     * 双击后退2次，退出程序
     * */
    DoubleBackClickOperation doubleBackClickOperation=new DoubleBackClickOperation(this);

    /**
     * 上一次用户使用主界面的时间戳，用户超过一定时长，强制重新刷新。
     */
    long lastUserUseMainViewTime = System.currentTimeMillis();

    /**
     * 是否从服务器中读取数据成功
     */
    boolean isRequestByServiceSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        setVersionCode();
        AllOnceInit.init(this);
        log.debug("onCreate");
        /**全局背景色透明度设置*/
        {
            View backgroundView = findViewById(R.id.root_view);
            Drawable backgroundDrawable = backgroundView.getBackground();
            /**几乎透明*/
            backgroundDrawable.setAlpha(40);
        }

        loadingView = new LoadingView();
        adapter = new MainBottomMenuAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);
                View view = fragment.getView();
                if (null != view) {
                    view.requestLayout();
                    System.out.println("is not null position:" + position);
                } else
                    System.out.println("is null position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**这里只用到了普通 Manifest.permission.INTERNET 权限，不需要申请 */
        requestDataByService();
    }

    /**
     * 把当前版本号更新到数据库里面
     */
    private void setVersionCode() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            //版本号
            SystemConfig.VERSION = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        log.debug("onStart");
        long alreadyPassTime = System.currentTimeMillis() - lastUserUseMainViewTime;
        System.out.println("alreadyPassTime:" + alreadyPassTime);
        /**超过2个小时重新强制刷新主界面*/
        int ONE_HOURS = 1000 * 60 * 60 * 3 ;
        if (alreadyPassTime > ONE_HOURS) {
            log.info("alreadyPassTime>ONE_HOURS :" + alreadyPassTime);
            restartApp();
        }
        lastUserUseMainViewTime = System.currentTimeMillis();
    }


    /**
     * 从服务器中刷新数据
     */
    private void requestDataByService() {

        /**连接服务器测试*/
        boolean serviceTest = true;
        if (serviceTest) {
            //先显示等待框
            loadingView.showPrepareLoadingView();
            ServiceDisposeFactory.getInstance().getServiceDispose().queryMainMenu()
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String result) {
                            JsonUtil.write2ClassAsync(result, BottomNavigationMenuVo[].class)
                                    .subscribe(new Action1<BottomNavigationMenuVo[]>() {
                                        @Override
                                        public void call(BottomNavigationMenuVo[] menus) {
                                            isRequestByServiceSuccess = true;
                                            adapter.setData(menus);
                                            //隐藏等待框
                                            loadingView.hide();
                                        }
                                    });
                        }
                    });
        } else {
            isRequestByServiceSuccess = true;
            loadingView.hide();
            adapter.setData(JsonUtil.write2Class(AllDatabases.getData(), BottomNavigationMenuVo[].class));
        }
    }


    /**
     * 底部大的菜单导航栏的数据填充适配器
     */
    class MainBottomMenuAdapter extends CommonCacheAdapterAbstract implements IconPagerAdapter {

        BottomNavigationMenuVo[] mData = new BottomNavigationMenuVo[0];

        public MainBottomMenuAdapter(FragmentManager fm) {
            super(fm);
        }

        public BottomNavigationMenuVo[] getData() {
            return mData;
        }

        public void setData(BottomNavigationMenuVo[] data) {
            this.mData = data;
            notifyDataSetChanged();
            indicator.notifyDataSetChanged();
        }

        /**
         * 生成不同样式的界面
         */
        @Override
        public Fragment buildItem(int position) {
            StyleType styleType = mData[position];
            return StyleFragmentFactory.create(styleType);
        }

        /**
         * 获取导航的菜单名称
         */
        public CharSequence getPageTitle(int position) {
            return mData[position].getTitle();
        }

        /**
         * 获取导航菜单对应的小图标
         */
        @Override
        public int getIconResId(int index) {
            String iconType = mData[index].getIcon();
            if ("video".equals(iconType))
                return R.drawable.main_nav_video;

            else if ("image".equals(iconType))
                return R.drawable.main_nav_image;

            else if ("community".equals(iconType))
                return R.drawable.main_nav_community;

            else if ("account".equals(iconType))
                return R.drawable.main_nav_account;

            else if ("introduction".equals(iconType))
                return R.drawable.main_nav_introduction;
            //TODO
            //先统一弄一张死的图标
            return R.drawable.main_nav_account;
        }

        @Override
        public int getCount() {
            return mData.length;
        }

    }


    /**
     * 预加载的界面
     */
    class LoadingView {

        /**
         * 加载界面
         */
        RelativeLayout loadingLayout;

        /**
         * 封面图片
         */
        ImageView loadingImageView;

        /**
         * 倒计时
         */
        TextView timeDownView;

        /**
         * 主内容
         */
        RelativeLayout rootView;

        LoadingView() {
            loadingLayout = findViewById(R.id.loading_layout);
            timeDownView = findViewById(R.id.time_down);
            rootView = findViewById(R.id.root_view);
        }

        /**
         * 显示加载界面
         * 显示倒计时，倒计时结束关闭加载图
         */
        void showPrepareLoadingView() {

            /**里面几张加载图，每次都随机一下。*/
            {
                loadingImageView = findViewById(R.id.loading_image);
                int randomImages[] = new int[]{R.drawable._main_loading_1, R.drawable._main_loading_2, R.drawable._main_loading_3};
                int randomIndex = new Random().nextInt(randomImages.length);
                loadingImageView.setImageDrawable(getResources().getDrawable(randomImages[randomIndex]));
            }

            Observable.create(new Observable.OnSubscribe<Integer>() {

                /**倒计时完毕退出*/
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    try {
                        for (int timeDown = 10; timeDown >= 0; timeDown--) {
                            Thread.sleep(1500);
                            subscriber.onNext(timeDown);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {

                        /**显示倒计时，倒计时结束关闭加载图*/
                        @Override
                        public void call(Integer timeDown) {

                            timeDownView.setText(String.valueOf(timeDown));
                            /**倒计时为0了结束倒计时*/
                            if (timeDown == 0 && (!isRequestByServiceSuccess)) {
                                Toast.makeText(activity, "网络好像有点问题哦，等一下再试吧!", Toast.LENGTH_LONG).show();
                                activity.finish();
                            }
                        }
                    });
        }

        /**
         * 隐藏进度界面
         */
        public void hide() {
            loadingLayout.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public String getSavePath() {
        //String folder=getFilesDir() + File.separator +"update";

        /**只能安装在sd卡上，不然android 7以下版本。会报包解析错误*/
        String folder = Environment.getExternalStorageDirectory() + "/external_path";
        new File(folder).mkdirs();
        return folder + File.separator + "update1.apk";
    }

    @Override
    public void updateVersion(String path) {
        Intent install = new Intent();
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setAction(Intent.ACTION_VIEW);
        Uri apkUri;

        /**android 7以上版本对文件读取权限更加严格了。 */
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            apkUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    new File(path));
        } else
            apkUri = Uri.fromFile(new File(path));
        log.debug("apkUri:" + apkUri);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(install);
    }


    @Override
    protected void onResume() {
        super.onResume();
        log.debug("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.debug("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log.debug("onDestroy");
    }


    /**
     * 这里要把Fragment的自动保存状态给干掉，
     * 否则新建活动的时候，会把上次的活动重新利用。但是视图和数据早就已经被清空了
     * 所以报一大堆的错误。
     * 搞死我了，花了好多好多时间。旋转播放视频，播放回来，老是崩溃我都哭了。
     * 找了很多资料，这是其中一种解决方案，没想到还真的就给解决了。我日。
     * 不给上层调用这个就好了。我日
     * */
    protected void onSaveInstanceState(Bundle outState) {
        log.debug("onSaveInstanceState");
    }

    /**
     * 检测回退建按下时间
     * @return true 拦截中了，false忽略
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        /**如果处理了，就返还false，忽略该事件*/
        if(doubleBackClickOperation.onKeyDown(keyCode,event))
            return false;
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重启app
     * */
    void restartApp()
    {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
