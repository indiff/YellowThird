package com.pear.yellowthird.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.impl.net.ServiceDisposeImpl;
import com.pear.yellowthird.style.factory.StyleFactory;
import com.pear.yellowthird.vo.databases.TalkComment;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 评论的适配器
 */

public class CommentListAdapter extends BaseAdapter {

    /**
     * 日记
     */
    private static Logger log = Logger.getLogger(ServiceDisposeImpl.class);

    private Context mContext;

    private List<TalkComment> mTalk=new ArrayList<>();

    public List<TalkComment> getTalk() {
        return mTalk;
    }

    public void setTalk(List<TalkComment> talk) {
        this.mTalk = talk;
        notifyDataSetChanged();
    }

    public CommentListAdapter(Context content) {
        mContext = content;
    }

    @Override
    public int getCount() {
        return mTalk.size();
    }

    @Override
    public Object getItem(int position) {
        return mTalk.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_comment_list_line, null);
            holder=new CommentHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder=(CommentHolder)convertView.getTag();

        TalkComment currentData = mTalk.get(position);

        /**为了美观，第一条数据隐藏分割线*/
        holder.headLineView.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

        /**把图标弄成圆形的。*/
        Glide.with(mContext)
                .load(currentData.getUser().getThumb())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(holder.authorIcon);

        /**用户名称*/
        holder.userNameView.setText(currentData.getUser().getName());

        /**发表时间*/
        holder.publishTimeView.setText("" + currentData.getPublishTime());

        /**点赞*/
        /**点赞的有效范围*/
        holder.clickGoodLineView.setTag(position);
        onClickGood(holder.clickGoodLineView,holder);

        /**点赞数量*/
        holder.goodCountView.setText("" + currentData.getGoodCount());
        //log.debug("currentData.isAlreadyClickGood()"+currentData.getAlreadyClickGood());
        if(currentData.getAlreadyClickGood())
            setClickGoodIsSelect(holder);

        /**具体评论值*/
        holder.contentView.setText(currentData.getContent());
        return convertView;
    }

    /**
     * 点赞用户的评论
     * */
    void onClickGood(final View view,final CommentHolder holder)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position=(Integer) view.getTag();
                final TalkComment currentData = mTalk.get(position);
                changeToClickGood(currentData);
                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addVideoUserCommentClickGood(currentData.getId())
                        .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                    }
                });
            }

            /**
             * 修改为点赞的样式
             * */
            void changeToClickGood(final TalkComment currentData)
            {
                currentData.setAlreadyClickGood(true);
                currentData.setGoodCount(currentData.getGoodCount()+1);
                holder.goodCountView.setText(String.valueOf(currentData.getGoodCount()));
                setClickGoodIsSelect(holder);
            }
        });
    }

    /**
     * 设置点赞为选中样式
     */
    void setClickGoodIsSelect(CommentHolder holder)
    {
        log.debug("setClickGoodIsSelect");
        holder.clickGoodLineView.setOnClickListener(null);
        holder.clickGoodIcon.setSelected(true);
        holder.goodCountView.setTextColor(mContext.getResources().getColor(R.color.colorSelect));
    }

    /**用户评论*/
    class CommentHolder extends RecyclerView.ViewHolder{

        /**为了美观，第一条数据隐藏分割线*/
        View headLineView;

        /**用户名称*/
        TextView userNameView;

        /**把图标弄成圆形的。*/
        ImageView authorIcon;

        /**发表时间*/
        TextView publishTimeView;

        /**点赞*/
        /**点赞的有效范围*/
        View clickGoodLineView;

        /**点赞的图标*/
        ImageView clickGoodIcon;

        /**点赞数量*/
        TextView goodCountView;

        /**具体评论值*/
        TextView contentView;

        public CommentHolder(View itemView) {
            super(itemView);
            headLineView=itemView.findViewById(R.id.head_line);
            userNameView=itemView.findViewById(R.id.user_name);
            authorIcon=itemView.findViewById(R.id.author_icon);
            publishTimeView=itemView.findViewById(R.id.publish_time);
            clickGoodLineView=itemView.findViewById(R.id.click_good);
            clickGoodIcon=itemView.findViewById(R.id.click_good_icon);
            goodCountView=itemView.findViewById(R.id.good_count);
            contentView=itemView.findViewById(R.id.content);
        }
    }


}
