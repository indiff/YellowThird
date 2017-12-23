package com.pear.yellowthird.activitys;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.github.ogapants.playercontrolview.PlayerControlView;
import com.pear.android.listener.empty.EmptyOnPageChangeListener;
import com.pear.yellowthird.adapter.FullImageAdapter;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * 全屏播放电影
 */

public class FullVideoActivity  extends AppCompatActivity {

    VideoView mVideoView;
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = (String)getIntent().getSerializableExtra("url");

        RelativeLayout relativeLayout=new RelativeLayout(this);
        relativeLayout.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

        mVideoView=new VideoView(this);
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        //mVideoView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        Button closeButton=new Button(this);
        closeButton.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        closeButton.setText("关闭");
        onCloseListener(closeButton);

        relativeLayout.addView(mVideoView);
        relativeLayout.addView(closeButton);

        setContentView(relativeLayout);
    }


    @Override
    protected void onStart() {
        super.onStart();
        playVideo();
    }

    void playVideo()
    {

        //网络视频
        Uri uri = Uri.parse(mUrl);

        PlayerControlView playerControlView = new PlayerControlView(this);
        playerControlView.attach(this);
        //playerControlView.setPlayer(mVideoView);

        //设置视频控制器
        mVideoView.setMediaController(new MediaController(this));

        //播放完成回调
        //mVideoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        mVideoView.setVideoURI(uri);

        //开始播放视频
        mVideoView.start();



    }

    /**
     * 监听关闭按钮
     */
    void onCloseListener(final Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}