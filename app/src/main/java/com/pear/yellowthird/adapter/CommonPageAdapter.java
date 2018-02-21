package com.pear.yellowthird.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pear.yellowthird.activitys.fragments.detailContent.ImageIntroduceFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroduceFragment;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片介绍的PAGE 的适配器。三部电影的滑动
 */

public abstract class CommonPageAdapter<DATA_TYPE> extends CommonCacheAdapterAbstract {

    protected List<DATA_TYPE> mData=new ArrayList<>();

    public CommonPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<DATA_TYPE> getData() {
        return mData;
    }

    public void setData(List<DATA_TYPE> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /*
    @Override
    public Fragment buildItem(int position) {
        return ImageIntroduceFragment.newInstance(mData.get(position));
    }
    */

    @Override
    public int getCount() {
        return mData.size();
    }

}
