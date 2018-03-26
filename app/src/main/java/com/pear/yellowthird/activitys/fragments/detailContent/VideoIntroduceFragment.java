package com.pear.yellowthird.activitys.fragments.detailContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmy.ninegridlayout.view.NineGridTestLayout;
import com.pear.android.utils.GlideUtils;
import com.pear.android.utils.SoftInputUtils;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.AccountInfoFragment;
import com.pear.yellowthird.activitys.video.GoogleExoVideoActivity;
import com.pear.yellowthird.adapter.CommentListAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * 电影介绍视图
 */

public class VideoIntroduceFragment extends Fragment {

    /**
     * 数据
     */
    private VideoIntroduceVo mData;

    /**用户信息*/
    UserVo user;

    /**
     * root 视图
     */
    private View mRootView;

    /**标题*/
    TextView titleView;

    /**封面*/
    ImageView coverView;

    /**
     * 开始播放
     */
    ImageView playButton;

    /**
     * 电影点赞
     */
    LinearLayout videoClickGoodLinearLayout;

    /**
     * 点赞的图标
     */
    ImageView clickGoodIcon;

    /**
     * 多少个人点赞了
     */
    TextView allClickGoodView;

    /**截图*/
    NineGridTestLayout multiImageView;


    /**评论相关*/


    /**多少个人评论了*/
    TextView commentCountView;

    /**
     * 作者的头像
     */
    ImageView authorIcon;

    /**
     * 吸引光标
     */
    LinearLayout attractFocusView;
    /**
     * 用户的评论输入
     */
    EditText inputComment;

    /**
     * 评论列表
     */
    CommentListAdapter mCommentAdapter;

    /*价格*/
    TextView priceView;


    public static Fragment newInstance(VideoIntroduceVo data) {
        VideoIntroduceFragment fragment = new VideoIntroduceFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
        log.debug("onCreat/**\n" +
                "     * 不能直接提供构造器来实现。会出现编译错误。\n" +
                "     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972\n" +
                "      eView");
        */
        if (null != mRootView) {
            System.out.println("cache onCreate view"+titleView.getText());
            imageMemoryRecover();
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.video_introduce, null);

        /**封面*/
        {
            coverView = mRootView.findViewById(R.id.cover);
            GlideUtils.loadImage(getContext(),coverView,mData.getCoverUri(), GlideUtils.ImageSize.fullHorizontalImageSize);
        }

        /**播放时长*/
        {
            TextView durationView = mRootView.findViewById(R.id.duration);
            durationView.setText(mData.getDuration());
        }

        /**开始播放*/
        {
            playButton = mRootView.findViewById(R.id.start_play);
            onListenerClickPlayVideo(playButton);
        }

        /**标题*/
        {
            titleView = mRootView.findViewById(R.id.title);
            titleView.setText(mData.getTitle());
        }

        /**发表时间*/
        {
            TextView publishTimeView = mRootView.findViewById(R.id.publish_time);
            publishTimeView.setText(mData.getPublishTime());
        }

        /**评分 */
        {
            TextView gradeLab = mRootView.findViewById(R.id.grade);
            gradeLab.setText(mData.getGrade() + "分");
        }

        /**类别*/
        {
            TextView typeView = mRootView.findViewById(R.id.type);
            typeView.setText("•" + mData.getTopic());
        }

        /**播放量*/
        {
            TextView allPlayCountView = mRootView.findViewById(R.id.all_play_count);
            allPlayCountView.setText("•" + mData.getPlayCount() + "次播放");
        }

        /**价格*/
        {
            priceView = mRootView.findViewById(R.id.price);
            priceView.setText(mData.getPrice() + " ");
        }

        /**多少个人评论了*/
        {
            commentCountView = mRootView.findViewById(R.id.comment_count);
            commentCountView.setText(mData.getAllTalkCount() + "评论");
        }

        /**点赞*/
        {
            videoClickGoodLinearLayout = mRootView.findViewById(R.id.click_good_line_view);

            clickGoodIcon = mRootView.findViewById(R.id.click_good);
            onVideoClickGood(videoClickGoodLinearLayout);
            /**多少个人点赞了*/
            {
                allClickGoodView = mRootView.findViewById(R.id.all_click_good);
                allClickGoodView.setText(String.valueOf(mData.getGoodCount()));
            }

            if (mData.getAlreadyClickGood())
                setClickVideoGoodIsSelect(mData.getGoodCount());
        }

        /**截图*/
        {
            multiImageView = mRootView.findViewById(R.id.screen_shorts_list);
            multiImageView.setUrlList(mData.getScreenShortUrls());
            multiImageView.setOnItemClickListener(new NineGridTestLayout.OnItemClickListener() {
                @Override
                public void onItemClick(int index, String url, List<String> urlList) {
                    FullImagePageActivity.startImagePagerActivity(getActivity(), mData.getScreenShortUrls(), index, null);
                }
            });
        }

        /**左侧用户头像*/
        {
            authorIcon = mRootView.findViewById(R.id.author_icon);
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .getUser()
                    .subscribe(new Action1<UserVo>() {
                        @Override
                        public void call(UserVo callUser) {
                            user=callUser;
                            GlideUtils.loadHeadIconImage(getContext(),authorIcon,user.getThumb());
                        }
                    });
        }

        /**吸引光标*/
        {
            attractFocusView = mRootView.findViewById(R.id.attract_focus);
        }
        /**用户的输入评论框*/
        {
            inputComment = mRootView.findViewById(R.id.input_comment);
            inputComment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    inputComment.setFocusable(true);
                    inputComment.setFocusableInTouchMode(true);
                    return false;
                }
            });
        }

        /**评论列表*/
        {
            LinearLayoutLikeListView commentList = mRootView.findViewById(R.id.comment_list);
            commentList.setId(++gCommentListId);

            mCommentAdapter = new CommentListAdapter(getActivity());
            commentList.setAdapter(mCommentAdapter);
        }

        eventInit();
        refreshUserIcon();
        refreshComment();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();


        /**为了干掉这个焦点自动获取。我也是哭了*/
        /*
        inputComment.clearFocus();
        //这样是否正常工作。
        //attractFocusView.requestFocus();

        //上面的不行，那这个呢
        inputComment.post(
                new Runnable() {
                    @Override
                    public void run() {
                        inputComment.clearFocus();
                    }
                }
        );
        */
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageMemoryReset();
    }

    void eventInit() {
        onAddTalkComment();
    }

    /**
     * 监听播放按钮的点击，进入播放流程
     */
    void onListenerClickPlayVideo(final View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);

                /**用户花费绿币看了电影。*/
                AccountInfoFragment.REFRESH_GOLD = true;

                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .requestPlayVideo(mData.getId()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        button.setEnabled(true);
                        try {
                            JSONObject json = new JSONObject(data);
                            if (json.getBoolean("pay")) {
                                Toast.makeText(getActivity(), json.getString("tip"), Toast.LENGTH_LONG).show();
                                startPlay();
                            } else {
                                Toast.makeText(getActivity(), json.getString("tip"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        button.setEnabled(true);
                        Toast.makeText(getActivity(), "哎呀，我刚才晕过去了", Toast.LENGTH_SHORT).show();
                    }
                });

            }


            /**
             * 播放电影之前，需要存取的权限
             * */
            /*
            private void playRequestPermissionsTip()
            {
                new PermissionsRequestInit(getActivity())
                        .permissionTipAndRequest(
                                "",
                                "播放电影需要缓冲保存的空间，\n请给我权限，否则我将不能正常工作",
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        startPlay();
                                    }
                                }
                        );
            }
*/
            /**
             * 开始播放
             * */
            void startPlay() {
                Intent intent = new Intent(getActivity(), GoogleExoVideoActivity.class);

                intent.putExtra("url", mData.getVideoUri());
                intent.putExtra("title", mData.getTitle());
                intent.putExtra("jump_price", Integer.parseInt(mData.getPrice()));
                intent.putExtra("video_id", mData.getId());
                intent.putExtra("enable_speed", mData.getEnableSpeed());
                startActivity(intent);
            }

        });
    }

    /**
     * 点赞
     */
    void onVideoClickGood(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**先改样式，后通知服务器接口。体验好*/
                setClickVideoGoodIsSelect(mData.getGoodCount() + 1);

                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addVideoClickGoodById(mData.getId())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                            }
                        });
            }
        });
    }

    /**
     * 设置电影点赞为已选状态
     */
    public void setClickVideoGoodIsSelect(int goodCount) {
        clickGoodIcon.setSelected(true);
        allClickGoodView.setText(String.valueOf(goodCount));
        allClickGoodView.setTextColor(getResources().getColor(R.color.colorSelect));
        videoClickGoodLinearLayout.setOnClickListener(null);
    }

    /**
     * 刷新用户的头像
     */
    void refreshUserIcon() {
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .getUser()
                .subscribe(new Action1<UserVo>() {
                    @Override
                    public void call(UserVo user) {
                        Glide.with(getActivity())
                                .load(user.getThumb())
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(authorIcon);
                    }
                });
    }

    /**
     * 添加评 论
     */
    void onAddTalkComment() {
        /**
         * 监听回车时间
         * */
        inputComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addComment();
                    handled = true;
                }
                return handled;
            }

            /**
             * 添加评论
             * */
            void addComment() {
                final String text = inputComment.getText().toString();
                if (TextUtils.isEmpty(text) || text.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "评论不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addVideoComment(String.valueOf(mData.getId()), text)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                                //清空输入框
                                inputComment.setText("");
                                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                                refreshComment();
                                SoftInputUtils.hideSoftInput(getActivity(), inputComment);
                                inputComment.clearFocus();
                                inputComment.setFocusableInTouchMode(false);
                                inputComment.setFocusable(false);
                            }
                        });
            }


        });
    }

    /**
     * 刷新评论
     */
    void refreshComment() {
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .queryVideoComment(mData.getId())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        TalkComment[] datas = JsonUtil.write2Class(data, TalkComment[].class);
                        if (null != datas && datas.length > 0)
                        {
                            commentCountView.setText(datas.length + "评论");
                            mCommentAdapter.setTalk(Arrays.asList(datas));
                        }
                    }
                });
    }



    /**
     * 恢复image的内存
     * */
    void imageMemoryRecover()
    {
        multiImageView.imageMemoryDispose(NineGridTestLayout.MemoryDispose.recoverMemoryDispose);
        GlideUtils.loadImage(getContext(),coverView,mData.getCoverUri(), GlideUtils.ImageSize.fullHorizontalImageSize);
        if(null!=user)
            GlideUtils.loadHeadIconImage(getContext(),authorIcon,user.getThumb());
    }

    /**
     * 清空image的内存
     * OOM很可怕
     * */
    void imageMemoryReset()
    {
        /**如果activity销毁了，就返还去吧*/
        if(getActivity().isDestroyed())
            return;

        multiImageView.imageMemoryDispose(NineGridTestLayout.MemoryDispose.resetMemoryDispose);
        Glide.with(getContext()).clear(coverView);
        Glide.with(getContext()).clear(authorIcon);
    }

    private static int gCommentListId = new String("video_introduce_comment_list_id").hashCode();

}
