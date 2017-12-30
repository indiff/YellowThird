package com.pear.yellowthird.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.android.view.MultiImageView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.ChooseCataloguePicActivity;
import com.pear.yellowthird.activitys.published.ChooseImageActivity;
import com.pear.yellowthird.activitys.published.PublishedActivity;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.vo.databases.FriendsVo;

import org.w3c.dom.Text;

import java.io.Serializable;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
        }else{
            final int circlePosition = position - HEAD_VIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;

            FriendsVo friendData = (FriendsVo) datas.get(circlePosition);

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
            holder.browseCountView.setText("浏览"+String.valueOf(friendData.getBrowseCount())+"次");

            holder.clickGoodEventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.clickGoodIconView.setSelected(friendData.isAlreadyClickGood());
            holder.goodCountView.setText(String.valueOf(friendData.getGoodCount()));

            holder.multiImageView.setVisibility(View.VISIBLE);
            holder.multiImageView.setList(friendData.getImages());
            holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //imagesize是作为loading时的图片size
                        //ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                        //ImagePagerActivity.startImagePagerActivity(context, photos, position, imageSize);
                    }
                });

            holder.commentAdapter.setDatas(friendData.getComments());

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

        public HeaderViewHolder(View itemView) {
            super(itemView);
            addPublishView = itemView.findViewById(R.id.add_publish);
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

    }

}
