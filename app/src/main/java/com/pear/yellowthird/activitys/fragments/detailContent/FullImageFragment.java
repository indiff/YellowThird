package com.pear.yellowthird.activitys.fragments.detailContent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pear.android.view.property.PropertySet;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.ImageSummary;

/**
 * 全屏单张图片显示
 */

public class FullImageFragment extends Fragment {

    /**
     * 当前图片的概要
     * */
    private ImageSummary mData;

    /**root 视图*/
    private View mRootView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static Fragment newInstance(ImageSummary data) {
        FullImageFragment fragment = new FullImageFragment();
        fragment.mData=data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("TitleContentFragment onCreateView");
        if(null!=mRootView)
        {
            System.out.println("TitleContentFragment onCreateView return cache view ");
            return mRootView;
        }

        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.sub_full_image, null);


        /**封面*/
        ImageView cover =  mRootView.findViewById(R.id.image);
        Glide.with(getActivity())
                .load(Uri.parse(mData.getUri()))
                .into(cover);

        /**内容介绍*/
        TextView contentView = mRootView.findViewById(R.id.content);
        contentView.setText(mData.getSummary());

        /**
         * 内容介绍的背景图
         * */
        LinearLayout contentBackgroundView = mRootView.findViewById(R.id.content_background);
        /**如果介绍内容为空的，则把介绍相关的视图都隐藏起来。*/
        contentBackgroundView.setVisibility(mData.getSummary().isEmpty()?View.GONE:View.VISIBLE);

        return mRootView;
    }

}