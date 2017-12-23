package com.pear.yellowthird.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pear.yellowthird.activitys.fragments.detailContent.FullImageFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroduceFragment;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.List;

/**
 * 电影介绍的PAGE 的适配器。三部电影的滑动
 */

public class VideoIntroducePageAdapter  extends CommonCacheAdapterAbstract {

    List<VideoIntroduceVo> mData;

    public VideoIntroducePageAdapter(FragmentManager fm, List<VideoIntroduceVo> data) {
        super(fm);
        this.mData=data;
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
