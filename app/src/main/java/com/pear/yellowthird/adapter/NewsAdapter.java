package com.pear.yellowthird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pear.android.utils.GlideUtils;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.vo.databases.NewsVo;


/**
 * 新闻适配器
 */
public class NewsAdapter extends BaseRecycleViewAdapter {

    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_news, parent, false);
        RecyclerView.ViewHolder viewHolder = new NewsViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final NewsViewHolder holder = (NewsViewHolder) viewHolder;
        NewsVo vo = (NewsVo)datas.get(position);

        holder.titleView.setText(vo.getTitle());
        holder.titleView.setVisibility(TextUtils.isEmpty(vo.getTitle()) ? View.GONE : View.VISIBLE);

        holder.createdTimeView.setText(vo.getPublishTime());
        holder.createdTimeView.setVisibility(TextUtils.isEmpty(vo.getPublishTime()) ? View.GONE : View.VISIBLE);

        holder.contentView.setText(vo.getContent());
        holder.contentView.setVisibility(TextUtils.isEmpty(vo.getContent()) ? View.GONE : View.VISIBLE);

        if (TextUtils.isEmpty(vo.getImageUri()))
            holder.imageView.setVisibility(View.GONE);
        else {
            holder.imageUrl=vo.getImageUri();
            //这个不清晰，但是先看看毫不毫内存
            //简介用纯文字表达就好了。我日
            GlideUtils.loadImage(context,holder.imageView,vo.getImageUri(), GlideUtils.ImageSize.fullHorizontalImageSize);
            holder.imageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 年轻人，听过OOM吗，
     * 我在这里只花1个礼拜就解决了咯，快舔我的吊。
     * */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        NewsViewHolder imageHolder=(NewsViewHolder)holder;
        Glide.with(context).clear(imageHolder.imageView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        NewsViewHolder imageHolder=(NewsViewHolder)holder;
        if(null!=imageHolder.imageUrl)
            GlideUtils.loadImage(context,imageHolder.imageView,imageHolder.imageUrl, GlideUtils.ImageSize.fullHorizontalImageSize);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 新闻的模板
     * */
    public class NewsViewHolder extends RecyclerView.ViewHolder {

        /**标题*/
        TextView titleView ;

        /**发表时间*/
        TextView createdTimeView;

        /**内容介绍*/
        TextView contentView;

        /**是否带有图片显示*/
        ImageView imageView;

        /**
         * You must not call setTag() on a view Glide is targeting
         * 用了glide不能用set tag 保存url。 那就用成员属性吧
         * */
        String imageUrl;

        public NewsViewHolder(View itemView, int viewType) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            createdTimeView=itemView.findViewById(R.id.created_time);
            contentView = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.image);
        }

    }

}
