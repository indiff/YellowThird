package com.pear.yellowthird.activitys.fragments.detailContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pear.yellowthird.activitys.FastShakeActivity;
import com.pear.yellowthird.activitys.R;

import org.apache.log4j.Logger;

/**
 * 快抖内容界面
 * */
public class FastShakeFragment extends Fragment {

    /**日记*/
    private Logger log = Logger.getLogger(FastShakeFragment.class);

    private View cacheView;

    //进入快抖
    Button comeInView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     * */
    public static Fragment newInstance() {
        FastShakeFragment fragment = new FastShakeFragment();
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
            return cacheView;
        }
        cacheView = LayoutInflater.from(getActivity()).inflate(R.layout.fast_shake_introduce, null);

        comeInView=cacheView.findViewById(R.id.come_in);
        comeInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FastShakeActivity.class);
                startActivity(intent);
            }
        });
        return cacheView;
    }

}

