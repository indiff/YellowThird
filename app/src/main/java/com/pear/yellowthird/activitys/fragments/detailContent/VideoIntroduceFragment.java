package com.pear.yellowthird.activitys.fragments.detailContent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pear.android.view.LGNineGrideView;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.FullVideoActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.CommentListAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * 电影介绍视图
 */

public class VideoIntroduceFragment extends Fragment {

    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    /**
     * 数据
     */
    private VideoIntroduceVo mData;

    /**
     * root 视图
     */
    private View mRootView;

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


    /**评论相关*/

    /**
     * 作者的头像
     */
    ImageView authorIcon;

    /**
     * 用户的评论输入
     */
    EditText inputComment;

    /**
     * 评论列表
     */
    CommentListAdapter mCommentAdapter;


    public static Fragment newInstance(VideoIntroduceVo data) {
        VideoIntroduceFragment fragment = new VideoIntroduceFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log.debug("onCreat/**\n" +
                "     * 不能直接提供构造器来实现。会出现编译错误。\n" +
                "     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972\n" +
                "     */eView");
        if (null != mRootView) {
            log.debug("onCreateView return cache view ");
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.video_introduce, null);

        /**封面*/
        {
            ImageView coverView = mRootView.findViewById(R.id.cover);
            Glide.with(getActivity())
                    .load(mData.getCoverUri())
                    .into(coverView);
        }

        /**播放时长*/
        {
            TextView durationView = mRootView.findViewById(R.id.duration);
            durationView.setText(mData.getDuration());
        }

        /**开始播放*/
        {
            ImageView playButton = mRootView.findViewById(R.id.start_play);
            onListenerClickPlayVideo(playButton);
        }

        /**标题*/
        {
            TextView titleView = mRootView.findViewById(R.id.title);
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

        /**多少个人评论了*/
        {
            TextView commentCountView = mRootView.findViewById(R.id.comment_count);
            commentCountView.setText(mData.getAllTalkCount() + "评论");
        }


        /**点赞*/
        {
            videoClickGoodLinearLayout = mRootView.findViewById(R.id.click_good_line_view);

            clickGoodIcon= mRootView.findViewById(R.id.click_good);
            onVideoClickGood(videoClickGoodLinearLayout);
            /**多少个人点赞了*/
            {
                allClickGoodView = mRootView.findViewById(R.id.all_click_good);
                allClickGoodView.setText(String.valueOf(mData.getGoodCount()));
            }

            if (mData.isAlreadyClickGood())
                setClickVideoGoodIsSelect(mData.getGoodCount());
        }

        /**截图*/
        {
            LGNineGrideView multiImageView = mRootView.findViewById(R.id.screen_shorts_list);
            multiImageView.setUrls(mData.getScreenShortUrls());
            multiImageView.setOnItemClickListener(new LGNineGrideView.OnItemClickListener() {
                @Override
                public void onClickItem(int position, View view) {
                    FullImagePageActivity.ImageSize imageSize = new FullImagePageActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    FullImagePageActivity.startImagePagerActivity(getActivity(), mData.getScreenShortUrls(), position, imageSize);
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
                        public void call(UserVo user) {
                            Glide.with(getActivity())
                                    .load(user.getThumb())
                                    .apply(bitmapTransform(new CropCircleTransformation()))
                                    .into(authorIcon);
                        }
                    });

        }

        /**用户的输入评论框*/
        {
            inputComment = mRootView.findViewById(R.id.input_comment);
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
                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .requestPlayVideo(mData.getId()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        try {
                            JSONObject json=new JSONObject(data);
                            if(json.getBoolean("pay"))
                            {
                                Toast.makeText(getActivity(),json.getString("tip"),Toast.LENGTH_LONG).show();
                                startPlay();
                            }
                            else{
                                Toast.makeText(getActivity(),json.getString("tip"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            /**
             * 开始播放
             * */
            void startPlay()
            {
                Intent intent = new Intent(getActivity(), FullVideoActivity.class);
                intent.putExtra("url", mData.getVideoUri());
                intent.putExtra("title", mData.getTitle());
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
                /**Params, Progress, Result*/
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return ServiceDisposeFactory.getInstance().getServiceDispose().addVideoClickGoodById(mData.getId());
                    }
                }.execute();
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
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    addComment();
                }
                return true;
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
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return ServiceDisposeFactory.getInstance().getServiceDispose()
                                .addVideoComment(String.valueOf(mData.getId()), text);
                    }

                    protected void onPostExecute(Boolean result) {
                        if (!result)
                            return;

                        //清空输入框
                        inputComment.setText("");
                        inputComment.clearFocus();

                        hideSoftInput(getActivity(), inputComment);
                        Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                        refreshComment();
                    }

                }.execute();
                return;
            }

            /**
             * 必须手动隐藏键盘
             * */
            void hideSoftInput(Activity activity, EditText input) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }

        });
    }

    /**
     * 刷新评论
     */
    void refreshComment() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return ServiceDisposeFactory.getInstance().getServiceDispose().queryVideoComment(mData.getId());
            }

            @Override
            protected void onPostExecute(String result) {
                TalkComment[] datas = JsonUtil.write2Class(result, TalkComment[].class);
                if (null != datas && datas.length > 0)
                    mCommentAdapter.setTalk(Arrays.asList(datas));
            }

        }.execute();
    }

    private static int gCommentListId = new String("video_introduce_comment_list_id").hashCode();

}
