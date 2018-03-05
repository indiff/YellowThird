package com.pear.yellowthird.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmy.ninegridlayout.view.NineGridTestLayout;
import com.pear.android.utils.SoftInputUtils;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.PublishedActivity;
import com.pear.yellowthird.adapter.abstracts.BaseRecycleViewAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 图片适配器
 */
public class ImageIntroduceAdapter extends BaseRecycleViewAdapter {

    private Context context;
    public ImageIntroduceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_introduce_advanced, parent, false);
        RecyclerView.ViewHolder viewHolder = new ImageViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ImageViewHolder holder = (ImageViewHolder) viewHolder;
        final ImageIntroduceVo friendData = (ImageIntroduceVo) datas.get(position);

        holder.titleView.setText(friendData.getTitle());
        if (TextUtils.isEmpty(friendData.getTitle()))
            holder.titleView.setVisibility(View.GONE);
        else
            holder.titleView.setVisibility(View.VISIBLE);

        holder.publishTimeView.setText(friendData.getPublishTime());
        if (TextUtils.isEmpty(friendData.getPublishTime()))
            holder.publishTimeView.setVisibility(View.GONE);
        else
            holder.publishTimeView.setVisibility(View.VISIBLE);

        holder.contentView.setText(friendData.getContent());
        if (TextUtils.isEmpty(friendData.getContent()))
            holder.contentView.setVisibility(View.GONE);
        else
            holder.contentView.setVisibility(View.VISIBLE);

        holder.browseCountView.setText("浏览" + String.valueOf(friendData.getBrowseCount()) + "次");

        holder.goodCountView.setText(String.valueOf(friendData.getGoodCount()));
        onImageClickGood(holder.clickGoodLinear,friendData,holder);
        if(friendData.getAlreadyClickGood())
            setClickImageGoodIsSelect(holder,friendData.getGoodCount());

        /**
         * 换了这个9宫图，终于好看点了。折腾死我了。
         * NineGridTestLayout
         * */
        holder.multiImageView.setUrlList(friendData.getImages());
        holder.multiImageView.setOnItemClickListener(new NineGridTestLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int index, String url, List<String> urlList) {
                //添加浏览次数
                ServiceDisposeFactory.getInstance().getServiceDispose().addImageShowCount(friendData.getId());
                //imagesize是作为loading时的图片size
                FullImagePageActivity.startImagePagerActivity(context, urlList, index, null);
            }
        });
    }

    /**
     * 点赞
     * */
    void onImageClickGood(final View view,final ImageIntroduceVo friendData,final ImageViewHolder holder)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**服务器没有传送id或者是本地模拟数据*/
                if(0>friendData.getId())
                    return;
                /**先改样式，后通知服务器接口即可。体验好*/
                friendData.setAlreadyClickGood(true);
                friendData.setGoodCount(friendData.getGoodCount()+1);
                setClickImageGoodIsSelect(holder,friendData.getGoodCount());
                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addImageClickGood(friendData.getId())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                            }
                        });
            }
        });
    }


    /**
     * 设置图片点赞为已选状态
     * */
    public void setClickImageGoodIsSelect(final ImageViewHolder holder ,int goodCount)
    {
        holder.goodCountIconView.setSelected(true);
        holder.goodCountView.setText(String.valueOf(goodCount));
        holder.goodCountView.setTextColor(context.getResources().getColor(R.color.colorSelect));
        holder.clickGoodLinear.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 图片的模板
     * */
    public class ImageViewHolder extends RecyclerView.ViewHolder {

        /**标题*/
        TextView titleView ;

        /**发表时间*/
        TextView publishTimeView;

        /**内容介绍*/
        TextView contentView;

        /** 图片*/
        public NineGridTestLayout multiImageView;

        /**浏览次数*/
        TextView browseCountView;

        /**
         * 监听点赞
         * */
        LinearLayout clickGoodLinear;

        /**点赞图标*/
        ImageView goodCountIconView;

        /**总赞量*/
        TextView goodCountView;

        public ImageViewHolder(View itemView, int viewType) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            publishTimeView=itemView.findViewById(R.id.publish_time);
            contentView = itemView.findViewById(R.id.content);
            browseCountView = itemView.findViewById(R.id.browse_count);
            clickGoodLinear = itemView.findViewById(R.id.click_good);
            goodCountIconView = itemView.findViewById(R.id.good_count_icon);
            goodCountView = itemView.findViewById(R.id.good_count);

            multiImageView = itemView.findViewById(R.id.multi_image);
            multiImageView.setIsShowAll(true);
        }

    }

}
