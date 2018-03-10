package com.hmy.ninegridlayout.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.hmy.ninegridlayout.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pear.yellowthird.GlideApp;
import com.pear.yellowthird.activitys.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/12
 */
public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    OnItemClickListener onItemClickListener;

    public NineGridTestLayout(Context context) {
        super(context);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

        ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        //imageView.setImageURI(url);
        /*
        ImageLoader loader=ImageLoaderUtil.getImageLoader(mContext);
        int memorySize=loader.getMemoryCache().keys().size();
        System.out.println("memorySize:"+memorySize);
        loader.clearMemoryCache();
        loader.displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
        */
        /*
        Picasso.with(mContext)
                .load(url)
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageView);
        */
        //imageView.setImageResource(R.drawable._image_sample_vertical);
        /*
        Glide.with(getContext())
                .load(url)
                .into(imageView) ;
        */
/*
        Glide.with(getActivity())
                .load(url)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
        */
         /**/
       GlideApp/*App*/.with(getContext())
               .load(url)
               .placeholder(R.drawable._image_loading)
               .error(R.drawable._image_load_fail)
               .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
               //.override(100, 100)
               //.fitCenter()
               //.skipMemoryCache(true)
               .into(imageView);
    }

    public enum MemoryDispose
    {
        resetMemoryDispose,     ///清空内存
        recoverMemoryDispose    ///恢复内存
    }

    /**
     * image实在是太耗内存了，
     * 这里在适当的时候管理一下内存。
     * 分分钟OOM给你看啊
     * @param dispose
     * */
    public void imageMemoryDispose(MemoryDispose dispose)
    {
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++)
        {
            View view=getChildAt(i);
            if(view instanceof ImageView)
            {
                RatioImageView imageView=(RatioImageView)view;
                switch (dispose)
                {
                    case resetMemoryDispose:
                        Glide.with(getContext()).clear(imageView);
                        break;
                    case recoverMemoryDispose:
                        displayImage(imageView, mUrlList.get(i));
                        break;
                }
            }
        }
    }

    @Override
    public void onClickImage(int i, String url, List<String> urlList) {
        //Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        if(null!=onItemClickListener)
            onItemClickListener.onItemClick(i,url,urlList);
    }


    public interface OnItemClickListener{
        void onItemClick(int i, String url, List<String> urlList);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
