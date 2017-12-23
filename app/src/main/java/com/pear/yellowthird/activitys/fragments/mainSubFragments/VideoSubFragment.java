package com.pear.yellowthird.activitys.fragments.mainSubFragments;

import android.support.v4.app.Fragment;

import com.pear.yellowthird.activitys.constant.ViewIdConstant;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonContentFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroduceFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroducePageFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.databases.VideoDatabases;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.MenuTalVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.ArrayList;

/**
 * 电影子界面
 * */
public class VideoSubFragment extends CommonSubTabSubFragmentAbstract{

    private static int gPageStartIndex=200001;

    @Override
    public Fragment buildItem(int position, SubTabMenuStyleDataVo vo) {
        /*
        if(vo.getTitle().equals("美女"))
            return VideoIntroducePageFragment.newInstance(
                    VideoDatabases.getBelle(),
                    gPageStartIndex+position);
        if(vo.getTitle().equals("搞笑"))
            return VideoIntroducePageFragment.newInstance(
                    VideoDatabases.getFunny(),
                    gPageStartIndex+position);
        */
        return CommonContentFragment.newInstance(vo.getTitle(),"电影");
    }

    @Override
    protected int getPageId() {
        return ViewIdConstant.GVideoPageId;
    }

}
