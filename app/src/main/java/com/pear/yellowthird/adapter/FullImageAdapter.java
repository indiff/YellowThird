package com.pear.yellowthird.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pear.yellowthird.activitys.fragments.detailContent.FullImageFragment;
import com.pear.yellowthird.adapter.abstracts.CommonCacheAdapterAbstract;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.ImageSummary;

import java.util.List;

/**
 * 全屏图片的适配器
 */

public class FullImageAdapter extends CommonCacheAdapterAbstract{

    List<ImageSummary> mUris;

    public FullImageAdapter(FragmentManager fm,List<ImageSummary> uris) {
        super(fm);
        this.mUris=uris;
    }

    @Override
    public Fragment buildItem(int position) {
        return FullImageFragment.newInstance(mUris.get(position));
    }

    @Override
    public int getCount() {
        return mUris.size();
    }

}
