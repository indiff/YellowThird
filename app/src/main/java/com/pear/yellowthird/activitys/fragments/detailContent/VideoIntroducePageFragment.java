package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pear.android.listener.RefreshPageChangeListener;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.VideoIntroducePageAdapter;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;
import com.viewpagerindicator.LinePageIndicator;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * 每个具体的电影介绍界面，集合。滑动翻页
 */

public class VideoIntroducePageFragment extends Fragment{

    private static int pageIncrementId=0;

    /**
     * 日记
     */
    private static Logger log = Logger.getLogger(VideoIntroducePageFragment.class);

    /**
     * 当前的整个内容视图
     * */
    protected View mContentView;

    List<VideoIntroduceVo> data;

    /**电影集合的适配器*/
    VideoIntroducePageAdapter adapter;

    LinePageIndicator indicator;

    ViewPager pager;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static VideoIntroducePageFragment newInstance(List<VideoIntroduceVo> data) {
        VideoIntroducePageFragment fragment = new VideoIntroducePageFragment();
        fragment.data=data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("onCreateView");

        /**必须要有一个缓冲机制。不然每次都new一个视图出来。我的天啊，视图你永远都不知道为什么总是不对，总是刷新错误。*/
        if(null!=mContentView)
        {
            //System.out.println("cache view return");
            return mContentView;
        }

        mContentView = inflater.inflate(R.layout.video_introduce_page, null);

        indicator=mContentView.findViewById(R.id.indicator);
        pager = mContentView.findViewById(R.id.pager);

        //这里需要取绝对值
        int pageId=Math.abs(new String("VideoIntroducePageFragment"+(pageIncrementId++)).hashCode());
        pager.setId(pageId);

        adapter=new VideoIntroducePageAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        if(null!=data)
        {
            adapter.setData(data);
            /**只有一个数据，不显示小的绿色导航了。好看一点*/
            if(data.size()==1)
                indicator.setVisibility(View.GONE);
        }
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new RefreshPageChangeListener(adapter));
        return mContentView;
    }

}
