package com.pear.yellowthird.activitys.video;

import android.content.Context;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.factory.ServiceDisposeFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * google实现的全屏播放电影
 */

public class GoogleExoVideoActivity extends AppCompatActivity implements View.OnClickListener,PlaybackControlView.PlayPositionChangeInterface {

    private static final String TAG = "GoogleExoVideoActivity";

    /**跳跃位置为最后5分钟*/
    private static final int JUMP_LAST_MILLI=1000*60*5;

    /**
     * 不重新播放，继续播放
     * */
    static Map<String,Long> gSeekHistory=new HashMap<>();

    private Context mContext;

    /**播放的URI*/
    String mUrl;

    /**标题*/
    String mTitle;

    /**跳跃的价格*/
    Integer mJumpPrice;

    /**电影在数据库中的id值*/
    Integer mVideoId;

    /**是否启用快进*/
    Boolean mEnableSpeed;

    /**是否试播放*/
    Boolean mIsTryOut;

    /**播放器*/
    private PlayerManager player;

    /**
     * 视频界面
     * */
    private SimpleExoPlayerView playerView;

    /**具体的控制器*/
    PlaybackControlView controlView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_by_google_exo);
        mContext=this;

        playerView = findViewById(R.id.player_view);
        controlView=playerView.getController();

        mUrl = (String)getIntent().getSerializableExtra("url");
        //mUrl=mUrl.substring(0,mUrl.length()-3);
        mTitle = (String)getIntent().getSerializableExtra("title");
        mJumpPrice= (Integer) getIntent().getSerializableExtra("jump_price");
        mVideoId= (Integer) getIntent().getSerializableExtra("video_id");
        mEnableSpeed= (Boolean) getIntent().getSerializableExtra("enable_speed");
        mIsTryOut= (Boolean) getIntent().getSerializableExtra("is_try_out");

        // Create a player instance.
        player = new PlayerManager(this,mUrl);

        controlView.getTitleView().setText(mTitle);
        controlView.getCloseButton().setOnClickListener(this);
        controlView.getJumpButton().setOnClickListener(mJumpClickListener);
        //鸡肋功能
        controlView.getJumpButton().setVisibility(View.GONE);

        controlView.getTimeBar().setEnabled(mEnableSpeed);

        if(mIsTryOut)
            controlView.setPlayPositionChange(this);

        player.init(this, playerView);
        clearHistoryByPlayFinish();
    }

    /**开始播放*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume ");
        if (gSeekHistory.containsKey(mUrl)) {
            player.getPlayer().seekTo(gSeekHistory.get(mUrl));
            player.getPlayer().setPlayWhenReady(true);
        }
        player.getPlayer().setPlayWhenReady(true);
    }

    /**暂停播放*/
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        player.getPlayer().setPlayWhenReady(false);
        cachePlaySeek();
    }

    /**缓存播放进度*/
    void cachePlaySeek()
    {
        long currentPosition=player.getPlayer().getCurrentPosition();
        if(player.getPlayer().getCurrentPosition()>0)
        {
            int hypothesisLastFinishMilli=1000*10;
            boolean isFinish=
                    hypothesisLastFinishMilli >
                            (player.getPlayer().getDuration()-player.getPlayer().getCurrentPosition());
            if(!isFinish)
                gSeekHistory.put(mUrl,currentPosition);
        }
    }

    @Override
    public void onDestroy() {
        player.reset();
        player.release();
        super.onDestroy();
    }


    /**
     * 播放完毕清空历史记录
     * */
    private void clearHistoryByPlayFinish()
    {
        playerView.setPlayFinishListener(new Runnable() {
            @Override
            public void run() {
                /**播放完成，清空播放记录*/
                if(gSeekHistory.containsKey(mUrl))
                    gSeekHistory.remove(mUrl);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.exo_close:
                finish();
                break;
        }
    }

    /**跳到你懂得位置*/
    View.OnClickListener mJumpClickListener=new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            /**不是正在播放中，不能跳跃*/
            if(player.getPlayer().getPlaybackState()!= PlaybackState.STATE_PLAYING)
                return;
            /**还剩下多少时间没有播放*/
            long remainMilli=player.getPlayer().getDuration()-player.getPlayer().getCurrentPosition();
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
                    .title("快进到高潮的位置")
                    .content("该功能将会花费你"+mJumpPrice+"绿币，\n而且还会破坏你对这部电影的情节了解。\n建议你不要使用该功能！")
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
            controlView.getJumpButton().setEnabled(false);
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
                            controlView.getJumpButton().setEnabled(true);
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
            player.getPlayer().setPlayWhenReady(false);
            long jumpSeek=player.getPlayer().getDuration()-JUMP_LAST_MILLI;
            player.getPlayer().seekTo(jumpSeek);
            player.getPlayer().setPlayWhenReady(true);
        }

    };

    /**
     * 检测回退建按下直接退出当前播放
     * @return true 拦截中了，false忽略
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }


    /**非会员免费试看5分钟*/
    @Override
    public void change(long currentTimeMs) {
        //5分钟流量伤不起
        //1分钟就好了
        int minute5milli=1000*60*1;
        if(minute5milli>=currentTimeMs)
            return;

        //清空监听
        controlView.setPlayPositionChange(null);
        //暂停视频
        player.getPlayer().setPlayWhenReady(false);
        /**发表过程中一直等待*/
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)

                .title("试看完毕。")
                .content("由于视频在线播放会来带极其高昂的服务器成本，只有会员才能完整视频播放。\n现在充值会员有优惠，请给我们一些支持，让我们继续为你带更精彩的内容吧")
                .positiveText("知道了")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //switch (which) {
                            GoogleExoVideoActivity.this.finish();
                        //}
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }
}