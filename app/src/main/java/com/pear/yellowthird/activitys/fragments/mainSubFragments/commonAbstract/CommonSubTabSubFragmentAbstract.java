package com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pear.android.listener.RefreshPageChangeListener;
import com.pear.yellowthird.activitys.R;
import com.pear.android.view.NoScrollViewPager;
import com.pear.yellowthird.adapter.abstracts.CommonPagerAdapterAbstract;
import com.pear.yellowthird.constants.ViewIdConstant;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.viewpagerindicator.SubNavPageIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的附带子tab切换界面的抽象类
 * */
public abstract class CommonSubTabSubFragmentAbstract extends Fragment {

    /**
     * 当前的整个内容视图
     * */
    protected LinearLayout mContentView;

    /**
     * tab下面对应的子视图。
     * */
    protected Map<String,Fragment> mSubView=new HashMap<>();

    /**菜单的适配器*/
    CommonMenuTabFragmentPagerAdapter adapter;

    /**菜单的什么鬼*/
    SubNavPageIndicator indicator;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static <C extends CommonSubTabSubFragmentAbstract>C newInstance(Class<C> classType) {
        try {
            C fragment = classType.newInstance();
            return fragment;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMenuData(List<SubTabMenuStyleDataVo> data) {
        adapter.setData(data);
        indicator.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取当前子视图。
     * @param position 获取的子视图的下标
     * @param vo 当前子tab附带的信息体
     * @note 有一个缓冲机制，如果已经初始化过了，拿缓存中的视图即可
     * */
    public Fragment getItem(int position, SubTabMenuStyleDataVo vo) {
        Fragment fragment=mSubView.get(vo.getTitle());
        if(null!=mSubView.get(vo.getTitle()))
            return fragment;
        fragment= buildItem(position,vo);
        mSubView.put(vo.getTitle(),fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
    }

    /**
     * 当前子tab 的适配器，子tab 的视图的生产器。
     * */
    private class CommonMenuTabFragmentPagerAdapter extends CommonPagerAdapterAbstract<SubTabMenuStyleDataVo>
    {
        public CommonMenuTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position, SubTabMenuStyleDataVo vo) {
            return CommonSubTabSubFragmentAbstract.this.getItem(position,vo);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getData(position).getTitle();
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView");

        /**必须要有一个缓冲机制。不然每次都new一个视图出来。我的天啊，视图你永远都不知道为什么总是不对，总是刷新错误。*/
        if(null!=mContentView)
        {
            System.out.println("cache view return");
            return mContentView;
        }

        indicator=new SubNavPageIndicator(getActivity());
        indicator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        ViewPager pager =new ViewPager(getActivity());
        pager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1));
        pager.setId(getPageId());

        adapter=new CommonMenuTabFragmentPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new RefreshPageChangeListener(adapter));

        mContentView = new LinearLayout(getActivity());
        mContentView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));

        mContentView.setOrientation(LinearLayout.VERTICAL);

        mContentView.addView(indicator);
        mContentView.addView(pager);
        return mContentView;
    }



    /**
     * 第一次视图加载需要新创建视图。
     * */
    public abstract Fragment buildItem(int position,SubTabMenuStyleDataVo vo);

    /**
     * 当前tab page 书友ground view 类型，必须指定一个view id。
     * 而且子类的ID都不能是一样的，不然就当做只有一个page来处理了。
     * */
    protected int getPageId(){
        return ViewIdConstant.getGlobalUniqueId();
    }

























    /**下面是调试作用。*/
    @Override
    public void onStart()
    {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null!=getView())
        {
            getView().requestLayout();
            System.out.println("onResume yes");
        }
        else
            System.out.println("onResume null");
        System.out.println("onResume");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        System.out.println("onDestroy");
    }

}

