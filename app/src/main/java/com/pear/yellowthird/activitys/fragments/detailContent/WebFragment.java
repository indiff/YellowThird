package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pear.android.view.FullWebView;
import com.pear.yellowthird.activitys.R;

/**
 * 网页界面
 */
public class WebFragment extends Fragment {

    private View mContentView;

    /**
     * 内置网址的页面
     */
    FullWebView mWebView;

    /**
     * 加载进度条
     * */
    ProgressBar loadingProgress;

    /**打开网址*/
    private String url;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        fragment.url=url;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mContentView)
            return mContentView;

        mContentView=inflater.inflate(R.layout.sub_web,null);
        mWebView=mContentView.findViewById(R.id.web_view);
        loadingProgress= mContentView.findViewById(R.id.loading_progress);
        mWebView.setLoadingProgress(loadingProgress);

        mWebView.loadUrl(url);
        return mContentView;
    }


}
