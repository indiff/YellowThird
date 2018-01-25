package com.pear.yellowthird.activitys;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.pear.android.view.FullWebView;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.factory.ServiceDisposeFactory;

/**
 * 充值
 */

public class RechargeActivity extends CommonHeadActivity {

    /**
     * 内置网址的页面
     */
    FullWebView mWebView;

    /**
     * 加载进度条
     * */
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_recharge);
        initHeadBar("充值");

        mWebView = findViewById(R.id.web_view);
        loadingProgress= findViewById(R.id.loading_progress);
        mWebView.setLoadingProgress(loadingProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url=ServiceDisposeFactory.getInstance().getServiceDispose().getRechargeWebUrl();
        //String url="https://www.baidu.com/";
        mWebView.loadUrl(url);
    }

}