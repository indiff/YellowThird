package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.log4j.Logger;

/**
 * 通用的内容界面
 * */
public class CommonContentFragment extends Fragment {

    /**日记*/
    private Logger log = Logger.getLogger(CommonContentFragment.class);

    /**
     * 内容数据
     * */
    private String mContent;

    /**
     * 当前的视图类别
     * */
    private String mType;

    private View cacheView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static Fragment newInstance(String content,String type) {
        CommonContentFragment fragment = new CommonContentFragment();
        fragment.mContent=content;
        fragment.mType=type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("ContentVideoFragment onCreateView");
        if(null!=cacheView)
        {
            log.debug("onCreateView return cache view ，content :"+mContent);
            return cacheView;
        }
        log.info("mContent："+mContent+"created");
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(mContent+mType);
        text.setTextSize(20 * getResources().getDisplayMetrics().density);
        text.setPadding(20, 20, 20, 20);
        text.setTextColor(getResources().getColor(android.R.color.holo_purple));

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);
        cacheView=layout;
        return layout;
    }

}

