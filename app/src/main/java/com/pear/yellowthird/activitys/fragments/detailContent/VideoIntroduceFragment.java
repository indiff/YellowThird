package com.pear.yellowthird.activitys.fragments.detailContent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.android.view.ScrollListView;
import com.pear.android.view.property.PropertySet;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.FullVideoActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.CommentListAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.style.factory.StyleFactory;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import rx.functions.Action1;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

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
    private ScrollView mRootView;


    /**电影点赞*/
    LinearLayout videoClickGoodLinearLayout;
    /**点赞的图标*/
    ImageView clickGoodIcon;
    /**多少个人点赞了*/
    TextView allClickGoodView;


    /**评论相关*/

    /**作者的头像*/
    ImageView authorIcon;
    /**用户的评论输入*/
    EditText inputComment;
    /**评论列表*/
    CommentListAdapter mCommentAdapter;

        /**
         * 不能直接提供构造器来实现。会出现编译错误。
         * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
         */
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
        log.debug("onCreateView");
        if (null != mRootView) {
            log.debug("onCreateView return cache view ");
            return mRootView;
        }

        mRootView = new ScrollView(getContext());
        mRootView.setLayoutParams(
                new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

        {
            /**整个滚动区域*/
            LinearLayout scrollRoot = new LinearLayout(getContext());
            scrollRoot.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            scrollRoot.setOrientation(LinearLayout.VERTICAL);
            {
                /**大的图片介绍*/
                {
                    RelativeLayout relativeLayout = new RelativeLayout(getContext());
                    relativeLayout.setLayoutParams(
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                    /**封面图片*/
                    {
                        ImageView coverImage = new ImageView(getContext());
                        coverImage.setLayoutParams(
                                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        Glide.with(getActivity())
                                .load(mData.getCoverUri())
                                .into(coverImage);
                        coverImage.setAdjustViewBounds(true);
                        coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        relativeLayout.addView(coverImage);
                    }
                    /**时长*/
                    {
                        LinearLayout linearLayout=new LinearLayout(getContext());

                        RelativeLayout.LayoutParams relativeParams
                                = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        linearLayout.setLayoutParams(relativeParams);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        {
                            TextView durationView = new TextView(getContext());
                            LinearLayout.LayoutParams layoutParams
                                    = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0,0,20,20);
                            //durationView.setPadding(10,10,10,10);
                            durationView.setLayoutParams(layoutParams);
                            durationView.getPaint().setFakeBoldText(true);
                            durationView.setText(mData.getDuration());
                            durationView.setGravity(Gravity.CENTER);
                            durationView.setTextSize(getResources().getDimension(R.dimen.comment_image_text));
                            durationView.setTextColor(getResources().getColor(R.color.colorImageText));
                            durationView.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_image_text_background));

                            linearLayout.addView(durationView);
                        }
                        relativeLayout.addView(linearLayout);
                    }

                    /**开始播放*/
                    {
                        ImageButton playButton = new ImageButton(getContext());
                        RelativeLayout.LayoutParams layoutParams
                                = new RelativeLayout.LayoutParams(
                                200,
                                200);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        playButton.setLayoutParams(layoutParams);

                        playButton.setImageResource(R.drawable.video_play);
                        /**image button 自带灰白色的背景，把它干掉*/
                        playButton.getBackground().setAlpha(0);
                        playButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        onListenerClickPlayVideo(playButton);
                        relativeLayout.addView(playButton);
                    }

                    scrollRoot.addView(relativeLayout);
                }
                /**标题*/
                {
                    TextView titleView = new TextView(getContext());
                    titleView.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    titleView.setPadding(
                            getResources().getInteger(R.integer.content_padding_left),
                            getResources().getInteger(R.integer.content_padding_title_top),
                            getResources().getInteger(R.integer.content_padding_right),
                            getResources().getInteger(R.integer.content_padding_title_bottom));

                    titleView.getPaint().setFakeBoldText(true);
                    titleView.setTextSize(getResources().getInteger(R.integer.content_video_title));
                    titleView.setText(mData.getTitle());

                    scrollRoot.addView(titleView);
                }
                /**评分 类别 播放量*/
                {
                    LinearLayout horizontalLayout = new LinearLayout(getContext());
                    horizontalLayout.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLayout.setPadding(
                            getResources().getInteger(R.integer.content_padding_left),
                            0,
                            getResources().getInteger(R.integer.content_padding_right),
                            0);
                    /**评分 */
                    {
                        TextView gradeLab = new TextView(getContext());
                        gradeLab.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        gradeLab.setText(mData.getGrade() + "分");
                        gradeLab.setTextSize(getResources().getInteger(R.integer.content_video_content));

                        gradeLab.setTextColor(getResources().getColor(R.color.colorGradleText));

                        horizontalLayout.addView(gradeLab);
                    }

                    /**类别*/
                    {
                        TextView typeView = new TextView(getContext());
                        typeView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        typeView.setText("•" + mData.getTopic());
                        typeView.setTextSize(getResources().getInteger(R.integer.content_video_content));
                        typeView.setTextColor(getResources().getColor(R.color.colorIntroduceContent));
                        horizontalLayout.addView(typeView);
                    }

                    /**播放量*/
                    {
                        TextView allPlayCountView = new TextView(getContext());
                        allPlayCountView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        allPlayCountView.setText("•" + mData.getPlayCount() + "次播放");
                        allPlayCountView.setTextSize(getResources().getInteger(R.integer.content_video_content));
                        allPlayCountView.setTextColor(getResources().getColor(R.color.colorIntroduceContent));
                        horizontalLayout.addView(allPlayCountView);
                    }
                    scrollRoot.addView(horizontalLayout);
                }
                /**点赞样式*/
                {
                    FrameLayout commentLayout = new FrameLayout(getContext());
                    commentLayout.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    commentLayout.setPadding(
                            getResources().getInteger(R.integer.content_padding_left),
                            70,
                            getResources().getInteger(R.integer.content_padding_right),
                            30);
                    {
                        {
                            LinearLayout linearLayout = new LinearLayout(getContext());
                            linearLayout.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT));
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            /**评论好看的图标*/
                            {
                                ImageView commentIcon = new ImageView(getContext());
                                commentIcon.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                80,
                                                80) {{
                                            setMargins(
                                                    10,
                                                    0,
                                                    10,
                                                    0);
                                        }}
                                );
                                commentIcon.setImageDrawable(getResources().getDrawable(R.drawable.comment_message));
                                linearLayout.addView(commentIcon);
                            }
                            /**多少个人评论了*/
                            {
                                TextView commentCountView = new TextView(getContext());

                                commentCountView.setLayoutParams(
                                        new FrameLayout.LayoutParams(
                                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                                FrameLayout.LayoutParams.MATCH_PARENT) {{
                                            setMargins(
                                                    20,
                                                    0,
                                                    0,
                                                    0);
                                        }});
                                commentCountView.setText(mData.getAllTalkCount() + "评论");
                                commentCountView.setTextSize(getResources().getInteger(R.integer.content_video_content));
                                commentCountView.setTextColor(getResources().getColor(R.color.colorTitle));
                                commentCountView.setGravity(Gravity.CENTER);
                                linearLayout.addView(commentCountView);
                            }
                            commentLayout.addView(linearLayout);
                        }

                        /**点赞*/
                        {
                            videoClickGoodLinearLayout = new LinearLayout(getContext());
                            videoClickGoodLinearLayout.setLayoutParams(
                                    new FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.WRAP_CONTENT,
                                            FrameLayout.LayoutParams.MATCH_PARENT,
                                            Gravity.RIGHT | Gravity.CENTER_VERTICAL));
                            videoClickGoodLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                            onVideoClickGood(videoClickGoodLinearLayout);
                            {
                                clickGoodIcon = new ImageView(getContext());
                                clickGoodIcon.setLayoutParams(
                                        new FrameLayout.LayoutParams(80, 80, Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                                );
                                clickGoodIcon.setImageDrawable(getResources().getDrawable(R.drawable.click_good));
                                videoClickGoodLinearLayout.addView(clickGoodIcon);
                            }
                            /**多少个人点赞了*/
                            {
                                allClickGoodView = new TextView(getContext());

                                allClickGoodView.setLayoutParams(
                                        new FrameLayout.LayoutParams(
                                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                                FrameLayout.LayoutParams.MATCH_PARENT) {{
                                            setMargins(20, 0, 0, 0);
                                        }});
                                allClickGoodView.setText(String.valueOf(mData.getGoodCount()));
                                allClickGoodView.setTextSize(getResources().getInteger(R.integer.content_video_content));
                                allClickGoodView.setTextColor(getResources().getColor(R.color.colorIntroduceContent));
                                allClickGoodView.setGravity(Gravity.CENTER);
                                videoClickGoodLinearLayout.addView(allClickGoodView);
                            }

                             if(mData.isAlreadyClickGood())
                                setClickVideoGoodIsSelect(mData.getGoodCount());
                            commentLayout.addView(videoClickGoodLinearLayout);
                        }
                    }
                    scrollRoot.addView(commentLayout);
                }
                {
                    LinearLayout lineView = new LinearLayout(getContext());
                    lineView.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    lineView.setBackgroundColor(getResources().getColor(R.color.colorMinorTitle));
                    scrollRoot.addView(lineView);
                }
                {
                    /**标题*/
                    {
                        TextView titleView = new TextView(getContext());
                        titleView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        titleView.setPadding(
                                getResources().getInteger(R.integer.content_padding_left),
                                getResources().getInteger(R.integer.content_padding_title_top) + 15,
                                getResources().getInteger(R.integer.content_padding_right),
                                getResources().getInteger(R.integer.content_padding_title_bottom));

                        titleView.getPaint().setFakeBoldText(true);
                        titleView.setTextSize(getResources().getInteger(R.integer.content_video_title));
                        titleView.setText("截图");

                        //titleView.setVisibility(View.GONE);
                        scrollRoot.addView(titleView);
                    }

                    /**截图*/
                    {
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        linearLayout.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayout.setPadding(
                                getResources().getInteger(R.integer.content_padding_left),
                                getResources().getInteger(R.integer.content_padding_content_top),
                                getResources().getInteger(R.integer.content_padding_right),
                                0);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                        List<String> screenShorts = mData.getScreenShortUrls();
                        for (int i = 0; i < screenShorts.size(); i++) {
                            final boolean isFirst = i == 0;
                            final boolean isLast = i == screenShorts.size() - 1;
                            ImageView image = new ImageView(getContext());
                            image.setLayoutParams(
                                    new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1) {{
                                        setMargins(isFirst ? 0 : 10, 0, isLast ? 0 : 10, 0);
                                    }}
                            );
                            Glide.with(getActivity())
                                    .load(screenShorts.get(i))
                                    .into(image);
                            image.setAdjustViewBounds(true);
                            linearLayout.addView(image);
                        }
                        //linearLayout.setVisibility(View.GONE);
                        scrollRoot.addView(linearLayout);
                    }

                    {
                        View lineView = new View(getContext());
                        lineView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1) {{
                                    setMargins(0, 80, 0, 0);
                                }});
                        lineView.setBackgroundColor(getResources().getColor(R.color.colorMinorTitle));
                        scrollRoot.addView(lineView);
                    }

                }
                /**评论标题*/
                {
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    linearLayout.setPadding(
                            getResources().getInteger(R.integer.content_padding_left),
                            getResources().getInteger(R.integer.content_padding_content_top) + 20,
                            getResources().getInteger(R.integer.content_padding_right),
                            getResources().getInteger(R.integer.content_padding_title_bottom));
                    {
                        TextView titleView = new TextView(getContext());
                        titleView.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                        titleView.setText("评论");
                        titleView.getPaint().setFakeBoldText(true);
                        titleView.setTextSize(getResources().getInteger(R.integer.content_video_title));
                        titleView.setGravity(Gravity.CENTER);
                        linearLayout.addView(titleView);
                    }

                    {
                        TextView commentCountView = new TextView(getContext());

                        commentCountView.setLayoutParams(
                                new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.MATCH_PARENT) {{
                                    setMargins(
                                            20,
                                            0,
                                            0,
                                            0);
                                }});
                        commentCountView.setText(String.valueOf(mData.getAllTalkCount() ));
                        commentCountView.setTextSize(getResources().getInteger(R.integer.content_video_content));
                        commentCountView.setTextColor(getResources().getColor(R.color.colorIntroduceContent));
                        commentCountView.setGravity(Gravity.CENTER);
                        linearLayout.addView(commentCountView);
                    }

                    scrollRoot.addView(linearLayout);
                }
                /**评论列表与评论*/
                {
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    {
                        LinearLayout inputLayout = new LinearLayout(getContext());
                        inputLayout.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT)
                                {{
                                    setMargins(0,0,0,50);
                                }});
                        inputLayout.setOrientation(LinearLayout.HORIZONTAL);

                        /**Android取消EditText自动默认获取焦点行为*/
                        inputLayout.setFocusable(true);
                        inputLayout.setFocusableInTouchMode(true);

                        ViewGroup.LayoutParams authorSize= StyleFactory.Comment.getAuthorIconSize();
                        StyleFactory.setContentPadding(inputLayout);

                        {
                            authorIcon = new ImageView(getContext());
                            authorIcon.setLayoutParams(authorSize);
                            inputLayout.addView(authorIcon);
                        }
                        /**用户的输入评论框*/
                        {
                            inputComment = new EditText(getContext());
                            inputComment.setHint("说出您的看法");
                            inputComment.setImeOptions(EditorInfo.IME_ACTION_SEND);
                            inputComment.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            0,
                                            authorSize.height,
                                            1)
                                    {{
                                        setMargins(getResources().getInteger(R.integer.content_padding_left),
                                                0,
                                                0,
                                                0);
                                    }}
                                    );

                            inputComment.setGravity(Gravity.CENTER_VERTICAL);
                            inputComment.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_edit_sharp));
                            inputComment.setTextSize(getResources().getInteger(R.integer.content_video_content));
                            inputComment.setImeOptions(EditorInfo.IME_ACTION_SEND);
                            inputLayout.addView(inputComment);
                        }

                        linearLayout.addView(inputLayout);
                    }
                    /**评论列表*/
                    {
                        LinearLayoutLikeListView commentList = new LinearLayoutLikeListView(getContext());
                        StyleFactory.setContentPadding(commentList);
                        commentList.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
                        commentList.setId(gCommentListId);

                        mCommentAdapter=new CommentListAdapter(getContext());
                        commentList.setAdapter(mCommentAdapter);
                        linearLayout.addView(commentList);
                    }

                    scrollRoot.addView(linearLayout);
                }
            }
            mRootView.addView(scrollRoot);
        }
        eventInit();
        refreshUserIcon();
        refreshComment();
        return mRootView;
    }


    void eventInit()
    {
        onAddTalkComment();
    }

    /**
     * 监听播放按钮的点击，进入播放流程
     */
    void onListenerClickPlayVideo(final View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullVideoActivity.class);
                intent.putExtra("url", mData.getVideoUri());
                startActivity(intent);
            }
        });
    }

    /**
     * 点赞
     * */
    void onVideoClickGood(final View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**Params, Progress, Result*/
                new AsyncTask<String,String,Boolean>(){
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return ServiceDisposeFactory.getInstance().getServiceDispose().addVideoClickGoodById(mData.getId());
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if(result)
                            setClickVideoGoodIsSelect(mData.getGoodCount()+1);
                    }
                }.execute("");
            }
        });
    }

    /**
     * 设置电影点赞为已选状态
     * */
    public void setClickVideoGoodIsSelect(int goodCount)
    {
        clickGoodIcon.setSelected(true);
        allClickGoodView.setText(String.valueOf(goodCount));
        allClickGoodView.setTextColor(getResources().getColor(R.color.colorSelect));
        videoClickGoodLinearLayout.setOnClickListener(null);
    }

    /**刷新用户的头像*/
    void refreshUserIcon()
    {
        ServiceDisposeFactory.getInstance().getServiceDispose()
                .getUser()
                .subscribe(new Action1<UserVo>() {
                    @Override
                    public void call(UserVo user) {
                        if(null==user)
                            return;
                        Glide.with(getContext())
                                .load(user.getThumb())
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(authorIcon);
                    }
                });
    }

    /**
     * 添加评论
     */
    void onAddTalkComment()
    {
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
                return false;
            }

            /**
             * 添加评论
             * */
            void addComment()
            {
                final String text=inputComment.getText().toString();
                if(TextUtils.isEmpty(text)||text.trim().isEmpty())
                {
                    Toast.makeText(getContext(),"评论不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        return ServiceDisposeFactory.getInstance().getServiceDispose()
                                .addVideoComment(String.valueOf(mData.getId()),text);
                    }

                    protected void onPostExecute(Boolean result) {
                        if(!result)
                            return;

                        //清空输入框
                        inputComment.setText("");
                        inputComment.clearFocus();

                        hideSoftInput(getActivity(),inputComment);
                        Toast.makeText(getContext(),"评论成功",Toast.LENGTH_SHORT).show();
                        refreshComment();
                    }

                }.execute();
                return ;
            }

            /**
             * 必须手动隐藏键盘
             * */
            void hideSoftInput(Activity activity,EditText input)
            {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(),0);
            }

        });
    }

    /**
     * 刷新评论
     * */
    void refreshComment()
    {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return ServiceDisposeFactory.getInstance().getServiceDispose().queryVideoComment(mData.getId());
            }

            @Override
            protected void onPostExecute(String result) {
                TalkComment[] datas = JsonUtil.write2Class(result, TalkComment[].class);
                mCommentAdapter.setTalk(Arrays.asList(datas));
            }

        }.execute();
    }

    private static final int gCommentListId = 100011;

}
