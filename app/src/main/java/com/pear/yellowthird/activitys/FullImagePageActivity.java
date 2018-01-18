package com.pear.yellowthird.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.pear.yellowthird.activitys.R.dimen;
import static com.pear.yellowthird.activitys.R.drawable;
import static com.pear.yellowthird.activitys.R.id;
import static com.pear.yellowthird.activitys.R.layout;

/**
 * 全屏浏览图片
 */
public class FullImagePageActivity extends Activity {
    public static final String INTENT_IMG_URLS = "img_urls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGE_SIZE = "image_size";

    /**
     * 下面的进度下标导航栏
     * */
    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;

    /**优先使用的图片大小*/
    public ImageSize imageSize;

    /**开始下标*/
    private int startPos;

    /**图片集合数据*/
    private ArrayList<String> imgUrls;

    /**
     * 开始图片浏览
     * */
    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize){
        Intent intent = new Intent(context, FullImagePageActivity.class);
        intent.putStringArrayListExtra(INTENT_IMG_URLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGE_SIZE, imageSize);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.image_full_page_activity);
        ViewPager viewPager = findViewById(id.pager);
        guideGroup = findViewById(id.guide_group);

        getIntentData();

        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<guideViewList.size(); i++){
                    guideViewList.get(i).setSelected(i==position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        viewPager.setCurrentItem(startPos);
        addGuideView(guideGroup, startPos, imgUrls);
    }

    /**
     * 获取上层传过来的参数
     * */
    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMG_URLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGE_SIZE);
    }


    /**
     * 添加下标导航栏
     * */
    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if(imgUrls!=null && imgUrls.size()>0){
            guideViewList.clear();
            for (int i=0; i<imgUrls.size(); i++){
                View view = new View(this);
                view.setBackgroundResource(drawable.image_full_page_selector_guide_bg);
                view.setSelected(i==startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(dimen.image_full_page_guide_view_height),
                        getResources().getDimensionPixelSize(dimen.image_full_page_guide_view_height));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    /**
     * 多图全屏的适配器
     * */
    private static class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Activity activity;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setDatas(List<String> datas) {
            if(datas != null )
                this.datas = datas;
        }
        public void setImageSize(ImageSize imageSize){
            this.imageSize = imageSize;
        }

        public ImageAdapter(Activity activity){
            this.activity = activity;
            this.inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            if(datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(layout.image_full_list_line, container, false);
            if(view != null){
                final ImageView imageView = view.findViewById(id.image);
                if(imageSize!=null){
                    //预览imageView
                    smallImageView = new ImageView(activity);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout)view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(activity);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout)view).addView(loading);

                final String img_url = datas.get(position);

                Glide.with(activity)
                        .load(img_url)
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .apply(new RequestOptions().error(drawable.default_load))
                        .into(new DrawableImageViewTarget(imageView){
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                                loading.setVisibility(View.GONE);
                            }

                        });

                /**点击关闭全屏浏览*/
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height){
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
