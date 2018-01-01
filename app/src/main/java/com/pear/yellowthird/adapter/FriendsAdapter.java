package com.pear.yellowthird.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
import com.pear.android.utils.SoftInputUtils;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.android.view.MultiImageView;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.PublishedActivity;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import org.w3c.dom.Text;

import java.io.Serializable;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 朋友圈的适配器
 */
public class FriendsAdapter extends BaseRecycleViewAdapter implements View.OnClickListener{

    /**用户头部属性*/
    public final static int TYPE_HEAD = 0;

    /**图片类型数据*/
    public final static int TYPE_IMAGE = 2;

    /**只有一条头部的用户属性*/
    public static final int HEAD_VIEW_SIZE = 1;

    private Context context;

    public FriendsAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        /**头部属性放在第一位*/
        if(position == 0){
            return TYPE_HEAD;
        }
        return TYPE_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_HEAD){
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_friend_circle_head, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_friend_circle_line, parent, false);
            viewHolder = new CircleViewHolder(view, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if(getItemViewType(position)==TYPE_HEAD){
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            holder.addPublishView.setOnClickListener(this);
            holder.updateUserView();
        }else{
            final int circlePosition = position - HEAD_VIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;

            final FriendsVo friendData = (FriendsVo) datas.get(circlePosition);

            Glide.with(context)
                    .load(friendData.getUser().getThumb())
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(holder.userHeadView);

            holder.userNameView.setText(friendData.getUser().getName());
            if(TextUtils.isEmpty(friendData.getContent()))
                holder.contentView.setVisibility(View.GONE);
            else
            {
                holder.contentView.setText(friendData.getContent());
                holder.contentView.setVisibility(View.VISIBLE);
            }

            holder.timeView.setText(friendData.getPublishTime());
            holder.browseCountView.setText("浏览"+String.valueOf(friendData.getShowCount())+"次");

            holder.updateClickGoodView(friendData.getGoodCount(),friendData.isAlreadyClickGood());

            holder.onGoodCountListener(friendData);

            holder.onListenerAddComment(friendData);

            holder.multiImageView.setVisibility(View.VISIBLE);
            holder.multiImageView.setList(friendData.getImages());
            holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //添加浏览次数
                        ServiceDisposeFactory.getInstance().getServiceDispose().addFriendShowCount(friendData.getId());
                        //imagesize是作为loading时的图片size
                        FullImagePageActivity.ImageSize imageSize = new FullImagePageActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                        FullImagePageActivity.startImagePagerActivity(context, friendData.getImages(), position, imageSize);
                     }
                });

            holder.commentAdapter.setDatas(friendData.getComments());
            if(friendData.getComments().isEmpty())
                holder.commentListView.setVisibility(View.GONE);
            else
                holder.commentListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;//有head需要加1
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            /**转向发表说说的主页*/
            case R.id.add_publish:
            {
                context.startActivity(new Intent(context,PublishedActivity.class));;
                break;
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        /**添加新的说说*/
        private ImageView addPublishView;

        /**用户头像*/
        private ImageView userHeadIcon;

        /**用户名称*/
        private TextView userNameView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            addPublishView = itemView.findViewById(R.id.add_publish);
            userHeadIcon = itemView.findViewById(R.id.user_head_icon);
            userNameView = itemView.findViewById(R.id.user_name);
        }

        /**
         * 更新用户数据
         * */
        void updateUserView()
        {
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .getUser().subscribe(new Action1<UserVo>() {
                @Override
                public void call(UserVo user) {
                    Glide.with(context)
                            .load(user.getThumb())
                            .into(userHeadIcon);
                    userNameView.setText(user.getName());
                }
            });
        }


    }

    public class CircleViewHolder extends RecyclerView.ViewHolder{

        /**用户头像*/
        ImageView userHeadView;

        /**用户名称*/
        TextView userNameView;

        /**说说内容*/
        TextView contentView;

        /**发表时间*/
        TextView timeView;

        /**浏览次数*/
        TextView browseCountView;

        /**点赞事件*/
        LinearLayout clickGoodEventView;

        /**点赞图标*/
        ImageView clickGoodIconView;

        /**点赞数量*/
        TextView goodCountView;


        /**用户的输入评论框*/
        EditText inputCommentView;

        /** 图片*/
        public MultiImageView multiImageView;

        /**评论列表*/
        public LinearLayoutLikeListView commentListView;

        /**评论适配器*/
        public FriendSimpleCommentAdapter commentAdapter;

        public CircleViewHolder(View itemView, int viewType) {
            super(itemView);

            userHeadView=itemView.findViewById(R.id.user_head);
            userNameView=itemView.findViewById(R.id.user_name);
            contentView=itemView.findViewById(R.id.content);
            timeView=itemView.findViewById(R.id.time);
            browseCountView=itemView.findViewById(R.id.browse_count);
            clickGoodEventView=itemView.findViewById(R.id.click_good_event);
            clickGoodIconView=itemView.findViewById(R.id.click_good_icon);
            goodCountView=itemView.findViewById(R.id.good_count);
            inputCommentView=itemView.findViewById(R.id.input_comment);

            multiImageView = itemView.findViewById(R.id.multi_image);
            commentListView=itemView.findViewById(R.id.comment_list);

            commentAdapter=new FriendSimpleCommentAdapter(context);
            commentListView.setAdapter(commentAdapter);
        }

        /**
         * 更新点赞状态为是否已经点赞样式
         */
        void updateClickGoodView(Integer goodCount,boolean result)
        {
            goodCountView.setText(String.valueOf(goodCount));
            clickGoodIconView.setSelected(result);
            if(result)
                goodCountView.setTextColor(context.getResources().getColor(R.color.colorSelect));
            else
                goodCountView.setTextColor(context.getResources().getColor(R.color.colorMinorContent));
        }

        /**
         * 监听点赞事件
         * */
        void onGoodCountListener(final FriendsVo friendData)
        {
            if(friendData.isAlreadyClickGood())
                clickGoodEventView.setOnClickListener(null);
            else
            {
                clickGoodEventView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceDisposeFactory.getInstance().getServiceDispose()
                                .friendClickGood(friendData.getId())
                                .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                                clickGoodEventView.setOnClickListener(null);
                                friendData.setAlreadyClickGood(true);
                                friendData.setGoodCount(friendData.getGoodCount()+1);
                                updateClickGoodView(friendData.getGoodCount(),true);
                            }
                        });
                    }
                });
            }

        }


        /**
         * 监听添加评论
         */
        void onListenerAddComment(final FriendsVo friendData) {
            /**
             * 监听回车时间
             * */
            inputCommentView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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
                    final String text = inputCommentView.getText().toString();
                    if (TextUtils.isEmpty(text) || text.trim().isEmpty()) {
                        Toast.makeText(context, "评论不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ServiceDisposeFactory.getInstance().getServiceDispose()
                            .addFriendComment(friendData.getId(),inputCommentView.getText().toString())
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean result) {
                                    ServiceDisposeFactory.getInstance().getServiceDispose()
                                            .getUser().subscribe(new Action1<UserVo>() {
                                        @Override
                                        public void call(UserVo userVo) {
                                            friendData.getComments()
                                                    .add(0,new TalkComment(inputCommentView.getText().toString(), userVo));
                                            commentAdapter.setDatas(friendData.getComments());
                                            commentListView.setVisibility(View.VISIBLE);
                                            //清空输入框
                                            inputCommentView.setText("");
                                            inputCommentView.clearFocus();
                                            SoftInputUtils.hideSoftInput(context,inputCommentView);
                                        }
                                    });
                                }
                            });
                }
            });
        }


    }

}
