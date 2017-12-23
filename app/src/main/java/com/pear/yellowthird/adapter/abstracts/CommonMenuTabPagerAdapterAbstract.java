package com.pear.yellowthird.adapter.abstracts;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pear.yellowthird.vo.MenuTalVo;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tab 菜单数据适配器公用抽象类
 * */
public abstract class CommonMenuTabPagerAdapterAbstract extends CommonCacheAdapterAbstract implements IconPagerAdapter {

    /**
     * tab 菜单集合
     * */
    private List<MenuTalVo> mData;

    public CommonMenuTabPagerAdapterAbstract(FragmentManager fm,List<MenuTalVo> data) {
        super(fm);
        mData=data;
    }

    /**
     * 根据当前菜单内容项获取对应的子视图
     * */
    public abstract Fragment getItem(int position,MenuTalVo vo);

    public Fragment buildItem(int position)
    {
        return getItem(position,mData.get(position));
    }

    /**
     * 获取指定下标的菜单名称
     * */
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).getTitle();
    }

    /**
     * 获取子菜单的对应下标的对应的小图标
     * */
    @Override public int getIconResId(int index) {
        return mData.get(index).getIcon();
    }

    /**
     * 获取子菜单的总量
     * */
    @Override
    public int getCount() {
        return mData.size();
    }

}

