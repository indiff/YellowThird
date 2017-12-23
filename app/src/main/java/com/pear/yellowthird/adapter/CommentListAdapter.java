package com.pear.yellowthird.adapter;

import android.content.Context;
import android.os.AsyncTask;
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
import com.pear.yellowthird.style.factory.StyleFactory;
import com.pear.yellowthird.vo.databases.TalkComment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 评论的适配器
 */

public class CommentListAdapter extends BaseAdapter {

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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_comment_list_line, null);
        }

        TalkComment currentData = mTalk.get(position);

        /**为了美观，第一条数据隐藏分割线*/
        convertView.findViewById(R.id.head_line)
                .setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

        /**把图标弄成圆形的。*/
        ImageView authorIcon = (ImageView) convertView.findViewById(R.id.author_icon);
        Glide.with(mContext)
                .load(currentData.getUser().getThumb())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(authorIcon);


        /**用户名称*/
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        userNameView.setText("" + currentData.getUser().getName());

        /**发表时间*/
        TextView publishTimeView = (TextView) convertView.findViewById(R.id.publish_time);
        publishTimeView.setText("" + currentData.getPublishTime());

        /**点赞*/
        /**点赞的有效范围*/
        View clickGoodLineView= convertView.findViewById(R.id.click_good);
        clickGoodLineView.setTag(position);
        onClickGood(clickGoodLineView);

        /**点赞的图标*/
        ImageView clickGoodIcon = (ImageView) convertView.findViewById(R.id.click_good_icon);
        /**点赞数量*/
        TextView goodCountView = (TextView) convertView.findViewById(R.id.good_count);
        goodCountView.setText("" + currentData.getGoodCount());
        if(currentData.isAlreadyClickGood())
            setClickGoodIsSelect(clickGoodLineView,clickGoodIcon,goodCountView);

        /**具体评论值*/
        TextView contentView = (TextView) convertView.findViewById(R.id.content);
        contentView.setText(currentData.getContent());
        return convertView;
    }

    /**
     * 点赞用户的评论
     * */
    void onClickGood(final View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... strings) {
                        Integer position=(Integer) view.getTag();
                        TalkComment currentData = mTalk.get(position);
                        boolean result=ServiceDisposeFactory.getInstance().getServiceDispose()
                                .addVideoUserCommentClickGood(currentData.getId());
                        if(result){
                            currentData.setAlreadyClickGood(true);
                            currentData.setGoodCount(currentData.getGoodCount()+1);
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if(result)
                            CommentListAdapter.this.notifyDataSetChanged();
                    }

                }.execute();
            }
        });
    }

    /**
     * 设置点赞为选中样式
     */
    void setClickGoodIsSelect(View clickGoodLineView,
                              ImageView clickGoodIcon,
                              TextView goodCountView)
    {
        clickGoodLineView.setOnClickListener(null);
        clickGoodIcon.setSelected(true);
        goodCountView.setTextColor(mContext.getResources().getColor(R.color.colorSelect));
    }

}
