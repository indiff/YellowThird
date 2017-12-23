package com.pear.yellowthird.style.impl.fragments;

import android.support.v4.app.Fragment;

import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroducePageFragment;
import com.pear.yellowthird.style.interfaces.StyleFragmentInterface;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.List;

/**
 * 电影介绍的子视图
 */
public class VideoIntroduceStyleFragment implements StyleFragmentInterface<List<VideoIntroduceVo>> {

    public static final String STYLE="video_introduce";

    @Override
    public Fragment created(List<VideoIntroduceVo> data) {
        return VideoIntroducePageFragment.newInstance(data);
    }

}
