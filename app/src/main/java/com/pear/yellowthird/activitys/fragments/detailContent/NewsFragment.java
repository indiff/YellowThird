package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hmy.ninegridlayout.util.ImageLoaderUtil;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.vo.databases.NewsVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;


/**
 * 新闻样式
 */

public class NewsFragment extends Fragment {

    /**
     * 内容数据
     */
    private List<NewsVo> datas=new ArrayList<>();

    /**
     * root 视图
     */
    private ScrollView mRootView;

    private int containerId;

    private boolean forceRefresh=false;
    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static NewsFragment newInstance(NewsVo[] data) {
        final NewsFragment fragment = new NewsFragment();
        fragment.datas=new ArrayList<>(Arrays.asList(data));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("TitleContentFragment onCreateView");
        if (null != mRootView) {
            //System.out.println("TitleContentFragment onCreateView return cache view ");
            return mRootView;
        }

        ScrollView scrollView = new ScrollView(getActivity());

        /**取消显示滚动条*/
        //scrollView.setVerticalScrollBarEnabled(false);
        final int commonScreenMargin = (int)getResources().getDimension(R.dimen.common_screen_margin);
        scrollView.setLayoutParams(
                new ScrollView.LayoutParams(
                        ScrollView.LayoutParams.MATCH_PARENT,
                        ScrollView.LayoutParams.MATCH_PARENT));

        /**scroll 是父类，设置margin没用，要设置padding才行*/
        scrollView.setPadding(
                commonScreenMargin,
                0,
                commonScreenMargin,
                commonScreenMargin
        );
        mRootView = scrollView;
        refreshViewByData();
        return mRootView;
    }

    /**
     * 数据变动了，重新绘制视图
     * */
    private void refreshViewByData()
    {
        mRootView.removeAllViews();
        LinearLayout scrollLayout = new LinearLayout(getActivity());

        scrollLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        scrollLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < datas.size(); i++) {
            NewsVo vo = datas.get(i);
            View subNewsView = getActivity().getLayoutInflater().inflate(R.layout.sub_news, null);

            /**标题*/
            {
                TextView titleView = subNewsView.findViewById(R.id.title);
                titleView.setText(vo.getTitle());
                titleView.setVisibility(TextUtils.isEmpty(vo.getTitle()) ? View.GONE : View.VISIBLE);
            }

            /**发表时间*/
            {
                TextView createdTimeView = subNewsView.findViewById(R.id.created_time);
                createdTimeView.setText(vo.getPublishTime());
                createdTimeView.setVisibility(TextUtils.isEmpty(vo.getPublishTime()) ? View.GONE : View.VISIBLE);
            }

            /**内容介绍*/
            {
                TextView contentView = subNewsView.findViewById(R.id.content);
                contentView.setText(vo.getContent());
                contentView.setVisibility(TextUtils.isEmpty(vo.getContent()) ? View.GONE : View.VISIBLE);
            }

            /**是否带有图片显示*/
            {
                ImageView imageView = subNewsView.findViewById(R.id.image);
                if (TextUtils.isEmpty(vo.getImageUri()))
                    imageView.setVisibility(View.GONE);
                else {
                    /**这里不使用缓存，否则简介图片超级模糊，不知道为什么*/
                    /*
                    //这样还是模糊

                    Glide.with(getActivity())
                            .load(vo.getImageUri())
                            .apply(RequestOptions.skipMemoryCacheOf(true).diskCacheStrategy(DiskCacheStrategy.NONE) )
                            .into(imageView);
                    //*/
                    /**试试不带缓存的图片库看看*/
                    //这个显示清晰了，可是没有图片默认load动画

                    Picasso.with(getActivity())
                            .load(vo.getImageUri())
                            .into(imageView);
                    /* 这个会不会耗很多内存？
                    ImageLoaderUtil
                            .getImageLoader(getActivity())
                            .displayImage(
                                    vo.getImageUri(),
                                    imageView,
                                    ImageLoaderUtil.getPhotoImageOption());
                                    */
                }
            }
            scrollLayout.addView(subNewsView);
        }
        mRootView.addView(scrollLayout);
    }

}
