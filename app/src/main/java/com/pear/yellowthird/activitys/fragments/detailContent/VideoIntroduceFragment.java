package com.pear.yellowthird.activitys.fragments.detailContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmy.ninegridlayout.view.NineGridTestLayout;
import com.pear.android.utils.GlideUtils;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.AccountInfoFragment;
import com.pear.yellowthird.activitys.video.GoogleExoVideoActivity;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.CommentDisposeByServiceInterface;
import com.pear.yellowthird.view.DetailCommentListView;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;


/**
 * 电影介绍视图
 */

public class VideoIntroduceFragment extends Fragment {

    /**
     * 数据
     */
    private VideoIntroduceVo mData;

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

    /**多少个人评论了*/
    TextView commentCountView;

    /**价格*/
    TextView priceView;

    /**详细的评论*/
    DetailCommentListView detailCommentList;

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
            priceView.setText(mData.getIsFree()?"免费":"会员");
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

        detailCommentList=new DetailCommentListView(getContext(),mRootView,mData.getId(),new VideoCommentDisposeByServiceImpl());
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
                                startPlay(false);
                            } else {
                                Toast.makeText(getActivity(), json.getString("tip"), Toast.LENGTH_LONG).show();
                                startPlay(true);
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
             * 开始播放
             * */
            void startPlay(Boolean isTryOut) {
                Intent intent = new Intent(getActivity(), GoogleExoVideoActivity.class);

                intent.putExtra("url", mData.getVideoUri());
                intent.putExtra("title", mData.getTitle());
                intent.putExtra("jump_price", Integer.parseInt(mData.getPrice()));
                intent.putExtra("video_id", mData.getId());
                intent.putExtra("enable_speed", mData.getEnableSpeed());
                intent.putExtra("is_try_out", isTryOut);
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

    /**电影评论的实现*/
    class VideoCommentDisposeByServiceImpl implements CommentDisposeByServiceInterface
    {
        @Override
        public Observable<String> getCommentById(Integer id) {
            return ServiceDisposeFactory.getInstance().getServiceDispose()
                    .queryVideoComment(id);
        }

        @Override
        public Observable<Boolean> addComment(Integer userId,Integer pid, String content) {
            return ServiceDisposeFactory.getInstance().getServiceDispose()
                    .addVideoComment(String.valueOf(pid), content);
        }

        @Override
        public Observable<Boolean> addCommentClickGood(Integer id) {
            return ServiceDisposeFactory.getInstance().getServiceDispose()
                    .addVideoUserCommentClickGood(id);
        }

        @Override
        public void onCommentChange(int count) {
            commentCountView.setText(count + "评论");
        }

    }


    /**
     * 恢复image的内存
     * */
    void imageMemoryRecover()
    {
        multiImageView.imageMemoryDispose(NineGridTestLayout.MemoryDispose.recoverMemoryDispose);
        GlideUtils.loadImage(getContext(),coverView,mData.getCoverUri(), GlideUtils.ImageSize.fullHorizontalImageSize);
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
    }

    private static int gCommentListId = new String("video_introduce_comment_list_id").hashCode();

}
