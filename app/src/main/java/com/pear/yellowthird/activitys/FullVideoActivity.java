package com.pear.yellowthird.activitys;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

/**
 * 全屏播放电影
 */

public class FullVideoActivity  extends AppCompatActivity {
    private static final String TAG = "FullVideoActivity";

    /**播放的URI*/
    String mUrl;

    /**标题*/
    String mTitle;

    UniversalVideoView mVideoView;

    UniversalMediaController mMediaController;


    /**上一次的播放进度*/
    private int mSeekPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_play);
        mUrl = (String)getIntent().getSerializableExtra("url");
        mTitle = (String)getIntent().getSerializableExtra("title");

        mVideoView =  findViewById(R.id.video_view);

        mMediaController =  findViewById(R.id.media_controller);
        mMediaController.setEnabled(false);
        mMediaController.setTitle(mTitle);

        /**
         * 监听关闭按钮
         */
        mMediaController.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(mUrl);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Log.d("rrr", "onCompletion ");
            }
        });

    }

    /**开始播放*/
    @Override
    protected void onStart() {
        super.onStart();
        if (mSeekPosition > 0) {
            mVideoView.seekTo(mSeekPosition);
        }
        mVideoView.requestFocus();
        mVideoView.start();
    }


    /**暂停播放*/
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }



}