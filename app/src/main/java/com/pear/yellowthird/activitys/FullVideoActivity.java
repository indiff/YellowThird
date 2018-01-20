package com.pear.yellowthird.activitys;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * 全屏播放电影
 */

public class FullVideoActivity  extends AppCompatActivity {
    private static final String TAG = "FullVideoActivity";

    /**跳跃位置为最后5分钟*/
    private static final int JUMP_LAST_MILLI=1000*60*5;

    /**
     * 不重新播放，继续播放
     * */
    static Map<String,Integer> gSeekHistory=new HashMap<>();

    private Context mContext;

    /**播放的URI*/
    String mUrl;

    /**标题*/
    String mTitle;

    /**跳跃的价格*/
    Integer mJumpPrice;

    /**电影在数据库中的id值*/
    Integer mVideoId;

    UniversalVideoView mVideoView;

    UniversalMediaController mMediaController;

    //屏幕电源管理
    PowerManager.WakeLock wakeLock;

    /**上一次的播放进度*/
    private int mSeekPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_play);

        mContext=this;
        mUrl = (String)getIntent().getSerializableExtra("url");
        mTitle = (String)getIntent().getSerializableExtra("title");
        mJumpPrice= (Integer) getIntent().getSerializableExtra("jump_price");
        mVideoId= (Integer) getIntent().getSerializableExtra("video_id");

        mVideoView =  findViewById(R.id.video_view);

        mMediaController =  findViewById(R.id.media_controller);
        //mMediaController.setEnabled(false);
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
        mMediaController.setJumpListener(mJumpClickListener);

        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(mUrl);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                /**播放完成，清空播放记录*/
                if(gSeekHistory.containsKey(mUrl))
                    gSeekHistory.remove(mUrl);
            }
        });

    }

    /**开始播放*/
    @Override
    protected void onResume() {
        super.onResume();
        if (gSeekHistory.containsKey(mUrl)) {
            mVideoView.seekTo(gSeekHistory.get(mUrl));
        }
        mVideoView.requestFocus();
        mVideoView.start();

        /**保持屏幕常亮*/
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
    }



    /**暂停播放*/
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            gSeekHistory.put(mUrl,mVideoView.getCurrentPosition());
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }

        /**关闭屏幕常亮*/
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    /**跳到你懂得位置*/
    View.OnClickListener mJumpClickListener=new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if (mVideoView == null || !mVideoView.isPlaying())
                return;

            /**还剩下多少时间没有播放*/
            long remainMilli=mVideoView.getDuration()-mVideoView.getCurrentPosition();
            if(JUMP_LAST_MILLI>remainMilli)
            {
                Toast.makeText(mContext,"现在已经是精彩时刻了",Toast.LENGTH_SHORT).show();
                return;
            }
            askUserConfig();
        }

        /**
         * 请求用户确认，是否要跳到精彩时刻
         * */
        void askUserConfig()
        {
            /**发表过程中一直等待*/
            final MaterialDialog progressDialog=new MaterialDialog.Builder(mContext)
                    .title("快进到精彩时刻")
                    .content("该快进功能将会花费你"+mJumpPrice+"绿币，\n而且还会破坏你对这部电影的情节了解。\n建议你不要使用该功能！")
                    .positiveText("没时间了，快快快")
                    .negativeText("慢慢看")
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            switch (which)
                            {
                                case NEGATIVE:
                                    break;
                                case POSITIVE:
                                    tryStartJump();
                                    break;
                            }
                        }
                    })
                    .show();
        }

        /**
         * 尝试跳跃，但是这里有个扣费的过程。
         * */
        void tryStartJump()
        {
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .requestJumpPlayVideo(mVideoId).subscribe(new Action1<String>() {
                @Override
                public void call(String data) {
                    try {
                        JSONObject json = new JSONObject(data);
                        if (json.getBoolean("pay")) {
                            Toast.makeText(mContext, json.getString("tip"), Toast.LENGTH_LONG).show();
                            startJump();
                        } else {
                            Toast.makeText(mContext, json.getString("tip"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /**
         * 开始跳跃
         * */
        void startJump()
        {
            mVideoView.pause();
            int jumpSeek=mVideoView.getDuration()-JUMP_LAST_MILLI;
            mVideoView.seekTo(jumpSeek);
            mVideoView.requestFocus();
            mVideoView.start();
        }

    };


}