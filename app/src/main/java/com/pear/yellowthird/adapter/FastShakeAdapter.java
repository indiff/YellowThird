package com.pear.yellowthird.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.pear.android.utils.DensityUtils;
import com.pear.android.utils.GlideUtils;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.video.PlayerManager;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.CommentDisposeByServiceInterface;
import com.pear.yellowthird.view.DetailCommentListView;
import com.pear.yellowthird.vo.databases.FastShakeVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;


/**
 * 快抖的适配器
 */
public class FastShakeAdapter extends BaseRecycleViewAdapter implements View.OnClickListener {

    private Activity activity;

    /**当前资源的评论数据*/
    private List<TalkComment> commentData;

    public FastShakeAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fast_shake_line, parent, false);
        return new FastShakeViewHolder(headView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final FastShakeViewHolder holder = (FastShakeViewHolder) viewHolder;
        final FastShakeVo fastShakeVo = (FastShakeVo) datas.get(position);
        holder.nameView.setText("@"+fastShakeVo.getUser().getName());
        holder.loveCountView.setText(String.valueOf(fastShakeVo.getGoodCount()));
        holder.titleView.setText(fastShakeVo.getTitle());
        holder.playCountView.setText(fastShakeVo.getPublishTime()+"•"+fastShakeVo.getPlayCount()+"次播放");

        holder.fastShakeVo=fastShakeVo;

        holder.playerView.getController().getCloseButton().setOnClickListener(this);

        /**重复播放，妈的重复播放需要重新loading。流量伤不起啊。等以后有空优化好短视频全缓存再说吧。*/

        holder.playerView.setPlayFinishListener(new Runnable() {
            @Override
            public void run() {
                holder.playerView.getController().hide();
                /*
                holder.player.getPlayer().seekTo(0);
                holder.player.getPlayer().setPlayWhenReady(true);*/
            }
        });
        trySeeImpose(holder);
        refreshComment(holder,fastShakeVo);
        loveOnClick(holder.loveIconView,holder,fastShakeVo);
        listenerClickShowComment(holder.commentIconView,holder);
    }

    /**
     * 非会员设置试看限制
     */
    void trySeeImpose(final FastShakeViewHolder holder)
    {
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .getUser()
                .subscribe(new Action1<UserVo>() {
                    @Override
                    public void call(UserVo userVo) {
                        if(userVo.getIsVip())
                            return;
                        setImpose();
                    }

                    /**
                     * 设置限制
                     * */
                    void setImpose()
                    {
                        holder.playerView.getController().setPlayPositionChange(new PlaybackControlView.PlayPositionChangeInterface() {
                            boolean hasTip=false;
                            @Override
                            public void change(long currentTimeMs) {
                                //20秒太多了。没人充钱
                                //10秒试试
                                //10秒都多了，弄个7秒
                                //7秒太少了
                                //13秒吧，太少吧客户度吓走了
                                //13秒流量伤不起
                                int seconds6=1000*3;
                                if(seconds6>=currentTimeMs)
                                    return;
                                if(!hasTip)
                                {
                                    hasTip=true;
                                    Toast.makeText(activity,"由于视频带宽极其昂贵，非会员每部只能看前面部分。请先充值会员",Toast.LENGTH_LONG).show();
                                }
                                if(null==holder.player||null==holder.player.getPlayer())
                                    return;
                                holder.player.getPlayer().setPlayWhenReady(false);
                            }
                        });
                    }
                });
    }


    /**
     * 选择当前界面默认播放
     * */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //等真正选中了之后再暂停。实际上这个体验也还不错。暂时就这样吧
        /* */
        FastShakeViewHolder fastShakeHolder=((FastShakeViewHolder)holder);
        fastShakeHolder.player=new PlayerManager(activity,fastShakeHolder.fastShakeVo.getVideoUri(),PlayerManager.LoadingBuffer.spareFlowLoadingBuffer);
        fastShakeHolder.player.init(activity, fastShakeHolder.playerView);
        fastShakeHolder.player.getPlayer().setPlayWhenReady(true);
        fastShakeHolder.playerView.getController().hideStretchControl();

        ///短视频还快进尼玛啊
        //算了，还是保留快进吧。没快进实在不行啊
        //fastShakeHolder.playerView.getController().getTimeBar().setEnabled(false);

        /**播放一次就添加一次浏览次数*/
        ServiceDisposeFactory.getInstance().getServiceDispose().addFastShakeShowCount(fastShakeHolder.fastShakeVo.getId())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean json) {
                    }
                });
    }

    /**
     * 隐藏当前界面默认暂停
     * 年轻人，听过OOM吗，
     * 我在这里只花1个礼拜就解决了咯，快舔我的吊。
     * */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        FastShakeViewHolder fastShakeHolder=((FastShakeViewHolder)holder);
        if(null==fastShakeHolder.player)
            return;
        fastShakeHolder.player.getPlayer().setPlayWhenReady(false);
        fastShakeHolder.player.reset();
        fastShakeHolder.player=null;
    }

    @Override
    public int getItemCount() {
        return datas.size() ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.exo_close:
                activity.finish();
                break;
        }
    }

    /**
     * 查询评论
     * */
    private void refreshComment(final FastShakeViewHolder holder,final FastShakeVo fastShakeVo)
    {
        ServiceDisposeFactory.getInstance().getServiceDispose().getFastShakeCommentById(fastShakeVo.getId())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String json) {
                        TalkComment[] data = JsonUtil.write2Class(json, TalkComment[].class);
                        commentData=new ArrayList(Arrays.asList(data));
                        holder.commentCountView.setText(String.valueOf(commentData.size()));
                    }
                });
    }

    /**
     * 添加爱心
     * */
    private void loveOnClick(final View loveView,final FastShakeViewHolder holder,final FastShakeVo fastShakeVo)
    {
        loveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**只能点一次赞，而且是先模拟数据，加快处理速度*/
                loveView.setOnClickListener(null);
                holder.loveIconView.setSelected(true);
                fastShakeVo.setGoodCount(fastShakeVo.getGoodCount()+1);
                holder.loveCountView.setText(String.valueOf(fastShakeVo.getGoodCount()));

                ServiceDisposeFactory.getInstance().getServiceDispose().addFastShakeLove(fastShakeVo.getId())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean data) {
                            }
                        });
            }
        });
    }

    /**
     * 点击显示评论
     * */
    void listenerClickShowComment(View clickCommentView,final FastShakeViewHolder holder)
    {
        clickCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComment(holder);
            }

            void showComment(FastShakeViewHolder holder)
            {
                FastShakeDetailCommentDialogFromBottom ft = new FastShakeDetailCommentDialogFromBottom(activity,holder);
                ft.setContentView(R.layout.detail_comment_list);
                ft.show();
            }
        });
    }

    public class FastShakeViewHolder extends RecyclerView.ViewHolder {

        /**
         * 视频播放
         */
        public SimpleExoPlayerView playerView;

        //作者名称
        public TextView nameView;

        //标题
        public TextView titleView;

        //赞的图标
        ImageView loveIconView;

        //赞的数量
        TextView loveCountView;

        //评论的图标
        ImageView commentIconView;

        //评论的数量
        TextView commentCountView;

        //播放次数
        TextView playCountView;

        /**
         * 视频播放管理
         */
        PlayerManager player;

        /**对应的数据*/
        FastShakeVo fastShakeVo;

        public FastShakeViewHolder(View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.player_view);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            //鸡肋功能
            playerView.getController().getJumpButton().setVisibility(View.GONE);
            nameView = itemView.findViewById(R.id.name);
            titleView = itemView.findViewById(R.id.title);
            loveIconView = itemView.findViewById(R.id.love_icon);
            loveCountView = itemView.findViewById(R.id.love_count);
            commentIconView = itemView.findViewById(R.id.comment_icon);
            commentCountView = itemView.findViewById(R.id.comment_count);
            playCountView = itemView.findViewById(R.id.play_count);
        }
    }


    class FastShakeDetailCommentDialogFromBottom extends Dialog {
        private final static int mAnimationDuration = 200; // 持有 ContentView，为了做动画
        private View mContentView;
        private boolean mIsAnimating = false;

        DetailCommentListView commentListView;
        FastShakeViewHolder holder;

        public FastShakeDetailCommentDialogFromBottom(@NonNull Context context,FastShakeViewHolder holder) {
            super(context , R.style.CommentDialog);
            this.holder=holder;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 在底部，宽度撑满
            getWindow().getDecorView().setPadding(
                    DensityUtils.dipTopx(getContext(),10),
                    0,
                    DensityUtils.dipTopx(getContext(),10),
                    0);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.height = DensityUtils.dipTopx(getContext(),400);//ViewGroup.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;//dialog从哪里弹出 //弹出窗口的宽高

            params.width = ViewGroup.LayoutParams.MATCH_PARENT;//screenWidth < screenHeight ? screenWidth : screenHeight;
            getWindow().setAttributes(params);
            setCanceledOnTouchOutside(true);

            commentListView=new DetailCommentListView(getContext(),mContentView,holder.fastShakeVo.getId(),new FastShakeCommentListView());
        }

        @Override
        public void setContentView(int layoutResID) {
            mContentView = LayoutInflater.from(getContext()).inflate(layoutResID,
                    null);
            super.setContentView(mContentView);
        }

        @Override
        public void setContentView(@NonNull View view) {
            mContentView = view;
            super.setContentView(view);
        }

        @Override
        public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
            mContentView = view;
            super.setContentView(view, params);
        }

        /**
         * BottomSheet升起动画
         */
        private void animateUp() {
            if (mContentView == null) {
                return;
            }
            TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            AlphaAnimation alpha = new AlphaAnimation(0, 1);
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(translate);
            set.addAnimation(alpha);
            set.setInterpolator(new DecelerateInterpolator());
            set.setDuration(mAnimationDuration);
            set.setFillAfter(true);
            mContentView.startAnimation(set);
        }

        /**
         * BottomSheet降下动画
         */
        private void animateDown() {
            if (mContentView == null) {
                return;
            }
            TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f);
            AlphaAnimation alpha = new AlphaAnimation(1, 0);
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(translate);
            set.addAnimation(alpha);
            set.setInterpolator(new DecelerateInterpolator());
            set.setDuration(mAnimationDuration);
            set.setFillAfter(true);
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsAnimating = false;
                    mContentView.post(new Runnable() {
                        @Override
                        public void run() {
                            // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager // 在dismiss的时候可能已经detach了，简单try-catch一下
                            try {
                                FastShakeDetailCommentDialogFromBottom.super.dismiss();
                            } catch (Exception e) {
                                //这里处理异常
                            }
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            mContentView.startAnimation(set);
        }

        @Override
        public void show() {
            super.show();
            animateUp();
        }

        @Override
        public void dismiss() {
            if (mIsAnimating) {
                return;
            }
            animateDown();
        }


        /**快抖评论的实现*/
        class FastShakeCommentListView implements CommentDisposeByServiceInterface
        {

            //FastShakeAdapter.FastShakeViewHolder holder;

            public FastShakeCommentListView() {
                //this.holder=holder;
            }

            @Override
            public Observable<String> getCommentById(Integer id) {
                return ServiceDisposeFactory.getInstance().getServiceDispose()
                        .getFastShakeCommentById(id);
            }

            @Override
            public Observable<Boolean> addComment(Integer userId,Integer pid,  String content) {
                return ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addFastShakeComment(userId, pid,content);
            }

            @Override
            public Observable<Boolean> addCommentClickGood(Integer id) {
                return ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addFastShakeCommentGoodCount(id);
            }

            @Override
            public void onCommentChange(int count) {
                //holder.commentCountView.setText(String.valueOf(commentData.size()));
            }

        }
    }


}
