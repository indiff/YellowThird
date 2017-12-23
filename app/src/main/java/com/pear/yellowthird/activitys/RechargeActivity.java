package com.pear.yellowthird.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;

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
     * 内置网址的充值页面
     */
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_recharge);
        initHeadBar("充值");

        mWebView = findViewById(R.id.web_view);

        /**不跳出浏览器打开，直接在内置的web_view打开*/
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webSetting();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url=ServiceDisposeFactory.getInstance().getServiceDispose().getRechargeWebUrl();
        mWebView.loadUrl(url);
    }

    /**
     * web view 的参数设置
     * */
    void webSetting() {
        /**声明WebSettings子类*/
        WebSettings webSettings = mWebView.getSettings();

        /**如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript*/
        webSettings.setJavaScriptEnabled(true);

        /**支持插件。你说的插件是上面插件*/
        webSettings.setPluginState(WebSettings.PluginState.ON);

        /**设置自适应屏幕，两者合用*/
        webSettings.setUseWideViewPort(true);
         /**将图片调整到适合webview的大小*/
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        //webSettings.setSupportZoom(true);
        // 支持缩放，默认为true。是下面那个的前提。
        //webSettings.setBuiltInZoomControls(true);

        //设置内置的缩放控件。若为false，则该WebView不可缩放
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);


        //其他细节操作
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }

}