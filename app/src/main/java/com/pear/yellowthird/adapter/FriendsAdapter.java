package com.pear.yellowthird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.android.view.MultiImageView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.vo.databases.FriendsVo;

/**
 * 朋友圈的适配器
 */
public class FriendsAdapter extends BaseRecycleViewAdapter {

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
        }else{
            final int circlePosition = position - HEAD_VIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;

            FriendsVo friendData = (FriendsVo) datas.get(circlePosition);
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    public class CircleViewHolder extends RecyclerView.ViewHolder{

        /** 图片*/
        public MultiImageView multiImageView;

        /**评论列表*/
        public LinearLayoutLikeListView commentListView;

        public FriendSimpleCommentAdapter commentAdapter;

        public CircleViewHolder(View itemView, int viewType) {
            super(itemView);

            multiImageView = itemView.findViewById(R.id.multi_image);
            commentListView=itemView.findViewById(R.id.comment_list);

            commentAdapter=new FriendSimpleCommentAdapter(context);
            commentListView.setAdapter(commentAdapter);
        }

    }

}
