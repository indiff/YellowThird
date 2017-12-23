package com.pear.yellowthird.activitys.fragments.mainSubFragments;

import android.support.v4.app.Fragment;

import com.pear.yellowthird.activitys.constant.ViewIdConstant;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonContentFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.ImageIntroduceFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.databases.ImageDatabases;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.MenuTalVo;


/**
 * 图片子界面
 * */
public class ImageSubFragment  extends CommonSubTabSubFragmentAbstract {


    @Override
    public Fragment buildItem(int position, SubTabMenuStyleDataVo vo) {
        /*
        if(vo.getTitle().equals("趣图"))
            return ImageIntroduceFragment.newInstance(ImageDatabases.getFunny());
        if(vo.getTitle().equals("动物"))
            return ImageSubFragment.this.buildView(ImageDatabases.getZoology());
        if(vo.getTitle().equals("植物"))
            return ImageSubFragment.this.buildView(ImageDatabases.getBotany());
        return CommonContentFragment.newInstance(vo.getTitle(),"社区");
        */
        return null;
    }

    /**
     * 创建视图
     * */
    Fragment buildView(ImageIntroduceVo data)
    {
        //return ImageIntroduceFragment.newInstance(data);
        return null;
    }


    @Override
    protected int getPageId() {
        return ViewIdConstant.GImagePageId;
    }


}
