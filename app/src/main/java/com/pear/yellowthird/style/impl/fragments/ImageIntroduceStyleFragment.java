package com.pear.yellowthird.style.impl.fragments;

import android.support.v4.app.Fragment;

import com.pear.yellowthird.activitys.fragments.detailContent.ImageIntroduceFragment;
import com.pear.yellowthird.style.interfaces.StyleFragmentInterface;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;

/**
 * 图片介绍的子视图
 */
public class ImageIntroduceStyleFragment implements StyleFragmentInterface<ImageIntroduceVo> {

    @Override
    public Fragment created(ImageIntroduceVo data) {
        //return ImageIntroduceFragment.newInstance(data);
        return null;
    }

}
