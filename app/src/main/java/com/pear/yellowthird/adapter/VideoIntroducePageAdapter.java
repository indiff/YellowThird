package com.pear.yellowthird.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroduceFragment;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 电影介绍的PAGE 的适配器。三部电影的滑动
 */

public class VideoIntroducePageAdapter  extends CommonCacheAdapterAbstract {

    List<VideoIntroduceVo> mData=new ArrayList<>();

    public VideoIntroducePageAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<VideoIntroduceVo> getData() {
        return mData;
    }

    public void setData(List<VideoIntroduceVo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment buildItem(int position) {
        return VideoIntroduceFragment.newInstance(mData.get(position));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

}
