package com.pear.yellowthird.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pear.android.view.FullWebView;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.BillAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.BillVo;

import java.sql.Date;
import java.util.ArrayList;

/**
 * 充值
 */

public class RechargeActivity extends CommonHeadActivity {

    /**
     * 内置网址的页面
     */
    FullWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_recharge);
        initHeadBar("充值");

        mWebView = findViewById(R.id.web_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url=ServiceDisposeFactory.getInstance().getServiceDispose().getRechargeWebUrl();
        //String url="https://www.baidu.com/";
        mWebView.loadUrl(url);
    }

}