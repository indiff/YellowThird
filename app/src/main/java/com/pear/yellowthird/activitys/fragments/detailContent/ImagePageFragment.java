package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.pear.android.listener.RefreshPageChangeListener;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.CommonPageAdapter;
import com.pear.yellowthird.adapter.ImageIntroduceAdapter;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.viewpagerindicator.LinePageIndicator;

import org.apache.log4j.Logger;

import java.util.List;

import io.github.kshitij_jain.indicatorview.IndicatorView;

/**
 * 每个具体的电影介绍界面，集合。滑动翻页
 */
public class ImagePageFragment extends Fragment{

    private static int pageIncrementId=0;

    /**
     * 日记
     */
    private static Logger log = Logger.getLogger(ImagePageFragment.class);

    /**
     * 当前的整个内容视图
     * */
    protected View mContentView;

    List<ImageIntroduceVo> data;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static ImagePageFragment newInstance(List data) {
        ImagePageFragment fragment=new ImagePageFragment();
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
        mContentView = inflater.inflate(R.layout.image_introduce_page, null);
        final RecyclerViewPager pager = (RecyclerViewPager) mContentView.findViewById(R.id.pager);
        //这里需要取绝对值
        int pageId=Math.abs(
                new String(
                        "ImagePageFragment"
                                +(pageIncrementId++)
                ).hashCode());
        //pager.setId(pageId);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        pager.setLayoutManager(layout);
        ImageIntroduceAdapter adapter=new ImageIntroduceAdapter(getContext());
        pager.setAdapter(adapter);

        final IndicatorView pageIndicatorView = mContentView.findViewById(R.id.circle_indicator_view);
        pageIndicatorView.setPageIndicators(data.size()); // specify total count of indicators
        pageIndicatorView.setCurrentPage(0);
        if(null!=data)
        {
            adapter.setDatas(data);
            /**只有一个数据，不显示小的绿色导航了。好看一点*/
            if(data.size()==1)
                pageIndicatorView.setVisibility(View.GONE);
        }
        pager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int beforePosition, int targetPosition) {
                Log.d("test", "oldPosition:" + beforePosition + " newPosition:" + targetPosition);
                pageIndicatorView.setCurrentPage(targetPosition);
            }
        });
        return mContentView;
    }
}
