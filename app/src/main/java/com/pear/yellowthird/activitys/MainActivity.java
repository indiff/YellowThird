package com.pear.yellowthird.activitys;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pear.android.view.NoScrollViewPager;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.databases.AllDatabases;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.impl.net.ServiceDisposeImpl;
import com.pear.yellowthird.init.PermissionsRequestInit;
import com.pear.yellowthird.style.factory.StyleFactory;
import com.pear.yellowthird.style.factory.StyleFragmentFactory;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.style.vo.StyleType;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.MainNavPageIndicator;

import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    /**
     * 底部菜单栏的适配器
     */
    MainBottomMenuAdapter adapter;

    /**
     * 底部菜单视图
     */
    MainNavPageIndicator indicator;

    /**
     * 预加载的界面
     */
    LoadingView loadingView = new LoadingView();

    /**
     * 上一次用户使用主界面的时间戳，用户超过一定时长，强制重新刷新。
     * */
    long lastUserUseMainViewTime=System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceDisposeImpl.initDeviceId(this);
        StyleFactory.init(this);

        /**全局背景色透明度设置*/
        {
            View backgroundView = findViewById(R.id.root_view);
            Drawable backgroundDrawable = backgroundView.getBackground();
            /**几乎透明*/
            backgroundDrawable.setAlpha(40);
        }

        adapter = new MainBottomMenuAdapter(getSupportFragmentManager());
        NoScrollViewPager pager = findViewById(R.id.pager);
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

        requestPermissions(refreshDataRun);
        loadingView.showPrepareLoadingView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        long alreadyPassTime=System.currentTimeMillis()-lastUserUseMainViewTime;
        System.out.println("alreadyPassTime:"+alreadyPassTime);
        /**超过一个小时重新强制刷新主界面*/
        int ONE_HOURS=1000*60*60;
        if(alreadyPassTime>ONE_HOURS)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        lastUserUseMainViewTime=System.currentTimeMillis();
    }


    /**
     * 请求权限
     */
    void requestPermissions(final Runnable refreshDataRun) {
        /**申请权限*/
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                new PermissionsRequestInit(MainActivity.this, new String[]{
                        Manifest.permission.INTERNET}
                ).init(new Runnable() {
                    @Override
                    public void run() {
                        refreshDataRun.run();
                    }
                });
                return null;
            }
        }.execute();
    }

    /**
     * 刷新数据
     */
    Runnable refreshDataRun = new Runnable() {
        @Override
        public void run() {

            /**连接服务器测试*/
            boolean serviceTest = true;
            if (serviceTest) {
                ServiceDisposeFactory.getInstance().getServiceDispose().queryMainMenu()
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String result) {
                                JsonUtil.write2ClassAsync(result, BottomNavigationMenuVo[].class)
                                        .subscribe(new Action1<BottomNavigationMenuVo[]>() {
                                            @Override
                                            public void call(BottomNavigationMenuVo[] menus) {
                                                adapter.setData(menus);
                                            }
                                        });

                            }
                        });
            } else
                adapter.setData(JsonUtil.write2Class(AllDatabases.getData(), BottomNavigationMenuVo[].class));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        /**
         * 显示加载界面
         * 显示倒计时，倒计时结束关闭加载图
         */
        void showPrepareLoadingView() {
            loadingLayout = findViewById(R.id.loading_layout);

            /**里面几张加载图，每次都随机一下。*/
            {
                loadingImageView = findViewById(R.id.loading_image);
                int randomImages[]=new int[]{R.drawable._main_loading_1,R.drawable._main_loading_2,R.drawable._main_loading_3};
                int randomIndex=new Random().nextInt(randomImages.length);
                loadingImageView.setImageDrawable(getResources().getDrawable(randomImages[randomIndex]));
            }
            timeDownView = findViewById(R.id.time_down);
            rootView = findViewById(R.id.root_view);

            Observable.create(new Observable.OnSubscribe<Integer>() {

                /**倒计时完毕退出*/
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    try {
                        for (int timeDown = 5; timeDown >= 0; timeDown--) {
                            Thread.sleep(800);
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
                            /**倒计时为0了结束倒计时*/
                            if (timeDown == 0) {
                                loadingLayout.setVisibility(View.GONE);
                                rootView.setVisibility(View.VISIBLE);
                                return;
                            }
                            timeDownView.setText(String.valueOf(timeDown));
                        }
                    });
        }

    }

}
