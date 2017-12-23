package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pear.android.listener.RefreshPageChangeListener;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.VideoIntroducePageAdapter;
import com.pear.yellowthird.constants.ViewIdConstant;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.List;

/**
 * 每个具体的电影介绍界面，集合。滑动翻页
 */

public class VideoIntroducePageFragment extends Fragment{

    /**
     * 内容数据
     * */
    private List<VideoIntroduceVo> mData;

    /**
     * 当前的整个内容视图
     * */
    protected LinearLayout mContentView;

    /**page 滑动的Id*/
    private int mPageId;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static Fragment newInstance(List<VideoIntroduceVo> data) {
        VideoIntroducePageFragment fragment = new VideoIntroducePageFragment();
        fragment.mData=data;
        fragment.mPageId= ViewIdConstant.getGlobalUniqueId();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView");

        /**必须要有一个缓冲机制。不然每次都new一个视图出来。我的天啊，视图你永远都不知道为什么总是不对，总是刷新错误。*/
        if(null!=mContentView)
        {
            System.out.println("cache view return");
            return mContentView;
        }

        LinePageIndicator indicator=new LinePageIndicator(getContext());
        indicator.setPadding(15,15,15,15);
        indicator.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        ViewPager pager =new ViewPager(getContext());
        pager.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        pager.setId(mPageId);

        VideoIntroducePageAdapter adapter=new VideoIntroducePageAdapter(getFragmentManager(),mData);
        pager.setAdapter(adapter);

        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new RefreshPageChangeListener(adapter));

        mContentView = new LinearLayout(getActivity());
        mContentView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        mContentView.setOrientation(LinearLayout.VERTICAL);

        mContentView.addView(indicator);
        mContentView.addView(pager);

        return mContentView;
    }

}
