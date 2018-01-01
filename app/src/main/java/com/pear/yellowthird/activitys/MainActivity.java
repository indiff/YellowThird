package com.pear.yellowthird.activitys;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pear.android.listener.empty.EmptyRunnable;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.databases.AllDatabases;
import com.pear.yellowthird.activitys.published.PublishedActivity;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.android.view.NoScrollViewPager;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.impl.net.ServiceDisposeImpl;
import com.pear.yellowthird.init.PermissionsRequestInit;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;
import com.pear.yellowthird.style.factory.StyleFactory;
import com.pear.yellowthird.style.factory.StyleFragmentFactory;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.style.vo.StyleType;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.MainNavPageIndicator;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    /**
     * 底部菜单栏的适配器
     */
    MainBottomMenuAdapter adapter;

    /**
     * 底部菜单视图
     */
    MainNavPageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceDisposeImpl.initDeviceId(this);
        StyleFactory.init(this);

        /**全局背景色透明度设置*/
        {
            View backgroundView = findViewById(R.id.root);
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

        requestPermissions();
    }

    /**
     * 请求权限
     */
    void requestPermissions() {
        /**申请权限*/
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                new PermissionsRequestInit(MainActivity.this, new String[]{
                        Manifest.permission.INTERNET}
                ).init(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                });
                return null;
            }
        }.execute();
    }

    /**
     * 刷新数据
     */
    void refreshData() {
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

}
