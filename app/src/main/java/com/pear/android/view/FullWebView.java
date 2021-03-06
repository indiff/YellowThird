package com.pear.android.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 完整的web 组件实现
 */

public class FullWebView extends WebView {


    /**
     * 加载进度条
     * */
    ProgressBar loadingProgress;

    public FullWebView(Context context) {
        super(context);
        init();
    }

    public FullWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        /**不跳出浏览器打开，直接在内置的web_view打开*/
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("alipays://platformapi")){
                    boolean visit = checkAliPayInstalled(getContext());
                    if(visit){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getContext().startActivity(intent);
                    }
                    else
                        return false;
                }
                else{
                    view.loadUrl(url);
                }
                return true;
            }

            //判断是否安装支付宝app
            public boolean checkAliPayInstalled(Context context) {
                Uri uri = Uri.parse("alipays://platformapi/startApp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ComponentName componentName = intent.resolveActivity(context.getPackageManager());
                return componentName != null;
            }

        });
        compatibility();
        webSetting();
    }

    /**
     * 兼容alert方式
     * */
    private void compatibility()
    {

        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(null==loadingProgress)
                    return;
                if(newProgress==100){
                    //加载完网页进度条消失
                    loadingProgress.setVisibility(View.GONE);
                }
                else{
                    //开始加载网页时显示进度条
                    loadingProgress.setVisibility(View.VISIBLE);
                    //设置进度值
                    loadingProgress.setProgress(newProgress);
                }
            }

        });
    }


    /**
     * web view 的参数设置
     */
    void webSetting() {

        /**声明WebSettings子类*/
        WebSettings webSettings = getSettings();


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

        //不使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    public ProgressBar getLoadingProgress() {
        return loadingProgress;
    }

    public void setLoadingProgress(ProgressBar loadingProgress) {
        this.loadingProgress = loadingProgress;
    }

}
