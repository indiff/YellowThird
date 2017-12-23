package com.pear.yellowthird.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    List<TalkComment> datas=new ArrayList<>();

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
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
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

        /**用户名称*/
        TextView userNameView = convertView.findViewById(R.id.user_name);
        //userNameView.setText("" + currentData.getGoodCount());

        /**具体评论值*/
        TextView contentView = convertView.findViewById(R.id.content);
        contentView.setText(currentData.getContent());

        return convertView;
    }

}
