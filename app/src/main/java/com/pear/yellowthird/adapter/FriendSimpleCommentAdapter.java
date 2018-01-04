package com.pear.yellowthird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.vo.databases.TalkComment;

import java.util.ArrayList;
import java.util.List;


/**
 * 朋友圈 简单回复的适配器
 */

public class FriendSimpleCommentAdapter extends BaseAdapter {


    Context context;

    /**所有评论*/
    List<TalkComment> datas=new ArrayList<>();

    /**
     * 当前显示的评论数量
     * */
    int currentShowCount=0;

    public FriendSimpleCommentAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<TalkComment> getDatas() {
        return datas;
    }

    public void setDatas(List<TalkComment> datas) {
        this.datas = datas;

        /**需要重置显示的条数才行*/
        currentShowCount=0;
        increaseShowCount(3);
    }

    @Override
    public int getCount() {
        return currentShowCount;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_friend_simple_comment, null);
        }

        TalkComment currentData = datas.get(position);
        /**
         * 如果当前是最后一项，和还有更多的评论可以显示出来。
         * 则把更多视图切换出来。
         * */
        boolean isBottomIndex=(position+1)==currentShowCount;
        boolean hasMore=datas.size()>currentShowCount;
        if(isBottomIndex&&hasMore)
        {
            ShowMoreHolder moreHolder = new ShowMoreHolder(convertView);
            moreHolder.showMoreLineView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseShowCount(3);
                }
            });
        }
        else
        {
            UserCommentHolder userCommentHolder = new UserCommentHolder(convertView);
            userCommentHolder.userNameView.setText(null!=currentData.getUser()?currentData.getUser().getName():"");
            userCommentHolder.contentView.setText(currentData.getContent());
        }
        return convertView;
    }

    /**
     * 递增显示
     * @param increaseCount 递增的显示条数
     * */
    void increaseShowCount(int increaseCount)
    {
        if(datas.isEmpty())
            currentShowCount=0;
        else if(currentShowCount+increaseCount>datas.size())
            currentShowCount=datas.size();
        else
            currentShowCount+=increaseCount;

        notifyDataSetChanged();
    }


    /**用户评论*/
    class UserCommentHolder extends RecyclerView.ViewHolder{
        /**用户名称*/
        public TextView userNameView;

        /**具体评论值*/
        public TextView contentView;

        /**显示更多*/
        public TextView showMoreLineView;

        public UserCommentHolder(View itemView) {
            super(itemView);
            userNameView=itemView.findViewById(R.id.user_name);
            contentView=itemView.findViewById(R.id.content);

            showMoreLineView=itemView.findViewById(R.id.show_more_line);
            showMoreLineView.setVisibility(View.GONE);
        }
    }


    /**显示更多*/
    class ShowMoreHolder extends RecyclerView.ViewHolder
    {

        /**显示更多*/
        TextView showMoreLineView;

        /**用户评论界面*/
        public LinearLayout userCommentLineView;


        public ShowMoreHolder(View itemView) {
            super(itemView);
            showMoreLineView=itemView.findViewById(R.id.show_more_line);
            userCommentLineView=itemView.findViewById(R.id.user_comment_line);
            userCommentLineView.setVisibility(View.GONE);
        }
    }

}
