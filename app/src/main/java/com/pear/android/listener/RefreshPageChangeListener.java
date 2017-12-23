package com.pear.android.listener;

/**
 * Created by su on 2017/11/6.
 */

import android.support.v4.app.Fragment;
import android.view.View;

import com.pear.android.listener.empty.EmptyOnPageChangeListener;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;

/**
 * 当前子tab 界面切换的监听器。
 * 如果page 变动了，需要刷新子view的视图，不然视图可能会不刷新，导致界面显示不准确。
 * */
public class RefreshPageChangeListener extends EmptyOnPageChangeListener
{
    CommonCacheAdapterAbstract mAdapter;

    public RefreshPageChangeListener(CommonCacheAdapterAbstract adapter)
    {
        mAdapter=adapter;
    }

    /**
     * 需要主动刷新子视图。
     * */
    @Override
    public void onPageSelected(int position) {
        Fragment fragment=mAdapter.getItem(position);
        View view=fragment.getView();
        if(null!=view)
        {
            view.requestLayout();
            System.out.println("is not null position:"+position);
        }else
            System.out.println("is null position:"+position);
    }
};