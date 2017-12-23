package com.pear.yellowthird.adapter.abstracts;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pear.yellowthird.vo.MenuTalVo;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的滑动页面适配器公用抽象类
 * */
public abstract class CommonPagerAdapterAbstract<T> extends CommonCacheAdapterAbstract implements IconPagerAdapter {

    /**
     * 数据结合
     * */
    private List<T> mData=new ArrayList<>();

    public CommonPagerAdapterAbstract(FragmentManager fm) {
        super(fm);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 根据当前菜单内容项获取对应的子视图
     * */
    public abstract Fragment getItem(int position,T vo);

    public Fragment buildItem(int position)
    {
        return getItem(position,mData.get(position));
    }

    /**
     * 获取指定下标的数据
     * */
    public T getData(int position) {
        return mData.get(position);
    }

    public abstract CharSequence getPageTitle(int position);

    /**
     * 获取子菜单的总量
     * */
    @Override
    public int getCount() {
        return mData.size();
    }

}

