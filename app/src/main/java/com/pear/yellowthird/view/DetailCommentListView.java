package com.pear.yellowthird.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pear.android.utils.GlideUtils;
import com.pear.android.utils.SoftInputUtils;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.constant.ViewIdConstant;
import com.pear.yellowthird.adapter.CommentListAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.CommentDisposeByServiceInterface;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 详细的评论
 */

public class DetailCommentListView {

    Context context;

    /**跟服务器交互的评论的接口*/
    CommentDisposeByServiceInterface commentDisposeByService;

    /**数据库实体对应的seq_id*/
    Integer seqId;

    /**主视图*/
    View contentView;

    /**用户信息*/
    UserVo user;

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

    public DetailCommentListView(Context context,View contentView,Integer seqId,CommentDisposeByServiceInterface commentDisposeByService)
    {
        this.context=context;
        this.contentView=contentView;
        this.seqId=seqId;
        this.commentDisposeByService=commentDisposeByService;
        initView();
    }

    /**初始化界面*/
    private void initView()
    {
        /**左侧用户头像*/
        {
            authorIcon = contentView.findViewById(R.id.author_icon);
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .getUser()
                    .subscribe(new Action1<UserVo>() {
                        @Override
                        public void call(UserVo callUser) {
                            user=callUser;
                            GlideUtils.loadHeadIconImage(context,authorIcon,user.getThumb());
                        }
                    });
        }

        /**吸引光标*/
        {
            attractFocusView = contentView.findViewById(R.id.attract_focus);
        }
        /**用户的输入评论框*/
        {
            inputComment = contentView.findViewById(R.id.input_comment);
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

            LinearLayoutLikeListView commentList = contentView.findViewById(R.id.comment_list);

            //如果之类加上抽象类，调试模式会无限卡死。我日。什么鬼
            mCommentAdapter = new CommentListAdapter(context) ;{/*
                @Override
                public Observable<Boolean> addCommentClickGood(Integer id) {
                    return commentDisposeByService.addCommentClickGood(id);
                }*/
            };

            commentList.setAdapter(mCommentAdapter);

        }

        onAddTalkComment();
        refreshUserIcon();
        refreshComment();
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
                    Toast.makeText(context, "评论不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                commentDisposeByService.addComment(user.getId(),seqId, text)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                                //清空输入框
                                inputComment.setText("");
                                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                                refreshComment();
                                SoftInputUtils.hideSoftInput(context, inputComment);
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
        commentDisposeByService.getCommentById(seqId)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        System.out.println("data:"+data);
                        TalkComment[] datas = JsonUtil.write2Class(data, TalkComment[].class);
                        System.out.println("datas length:"+datas.length);
                        if (null != datas && datas.length > 0)
                        {
                            commentDisposeByService.onCommentChange(datas.length);
                            mCommentAdapter.setTalk(Arrays.asList(datas));
                        }
                    }
                });
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
                        Glide.with(context)
                                .load(user.getThumb())
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(authorIcon);
                    }
                });
    }
}
