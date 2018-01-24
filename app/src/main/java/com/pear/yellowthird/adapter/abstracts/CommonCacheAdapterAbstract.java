package com.pear.yellowthird.adapter.abstracts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的缓存fragment策略的适配器
 */

public abstract class CommonCacheAdapterAbstract extends FragmentPagerAdapter {

    public CommonCacheAdapterAbstract(FragmentManager fm) {
        super(fm);
    }

    private Map<Integer,Fragment> mFragmentMap=new HashMap<>();

    @Override
    public Fragment getItem(int position) {
        Fragment cacheFragment=mFragmentMap.get(position);
        if(null!=cacheFragment)
            return cacheFragment;
        cacheFragment=buildItem(position);
        mFragmentMap.put(position, cacheFragment);
        return cacheFragment;
    }

    /**
     * 创建一个指定下标新的视图
     * */
    public abstract Fragment buildItem(int position);

    public Map<Integer, Fragment> getFragmentMap() {
        return mFragmentMap;
    }

    public void setFragmentMap(Map<Integer, Fragment> fragmentMap) {
        this.mFragmentMap = fragmentMap;
    }

}
