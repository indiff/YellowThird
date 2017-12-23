package com.pear.yellowthird.activitys.fragments.mainSubFragments;

import android.support.v4.app.Fragment;

import com.pear.databases.VoteDatabases;
import com.pear.yellowthird.activitys.constant.ViewIdConstant;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonContentFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.NewsFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VoteFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.databases.CommunityDatabases;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.NewsVo;

import java.util.List;

/**
 * Created by su on 2017/11/1.
 */

public class CommunitySubFragment  extends CommonSubTabSubFragmentAbstract {

    @Override
    public Fragment buildItem(int position, SubTabMenuStyleDataVo vo) {
        if(vo.getTitle().equals("简介"))
            return buildView(CommunityDatabases.getIntroductionData());
        if(vo.getTitle().equals("预告"))
            return buildView(CommunityDatabases.getForeShowData());
        if(vo.getTitle().equals("小说"))
            return buildView(CommunityDatabases.getFictionData());
        if(vo.getTitle().equals("问题"))
            return buildView(CommunityDatabases.getProblemData());
        if(vo.getTitle().equals("分享"))
            return buildView(CommunityDatabases.getShareData());
        if(vo.getTitle().equals("投票"))
        {
            VoteFragment fragment=VoteFragment.newInstance();
            fragment.setDatas(VoteDatabases.getData());
            return fragment;
        }
        return CommonContentFragment.newInstance(vo.getTitle(),"社区");
    }

    @Override
    protected int getPageId() {
        return ViewIdConstant.GCommunityPageId;
    }

    /**
     * 创建视图
     * */
    Fragment buildView(List<NewsVo> data)
    {
        //return NewsFragment.newInstance(data);
        return null;
    }


}
