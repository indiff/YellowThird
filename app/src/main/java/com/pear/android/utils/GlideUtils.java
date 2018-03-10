package com.pear.android.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.pear.yellowthird.GlideApp;
import com.pear.yellowthird.activitys.R;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Glide加载图片工具
 */

public class GlideUtils {

    public enum ImageSize {
        headIconImageSize,
        thumbnailImageSize,
        fullHorizontalImageSize
    }

    private static Map<ImageSize, Integer> imageSizeConfig
            = new HashMap<ImageSize, Integer>() {{
        put(ImageSize.headIconImageSize, 130);
        put(ImageSize.thumbnailImageSize, 200);
        put(ImageSize.fullHorizontalImageSize, 520);
    }};

    /**
     * 默认的加载方式
     */
    public static void loadImage(Context context, ImageView view, String url) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable._image_loading)
                .error(R.drawable._image_load_fail)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                //.override(200)
                .into(view);
    }

    /**
     * 默认的加载方式
     */
    public static void loadImage(Context context, ImageView view, String url, ImageSize size) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable._image_loading)
                .error(R.drawable._image_load_fail)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .override(imageSizeConfig.get(size))
                .into(view);
    }

    public static void loadHeadIconImage(Context context, ImageView view, String url) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable._image_loading)
                .error(R.drawable._image_load_fail)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .apply(bitmapTransform(new CropCircleTransformation()))
                .override(imageSizeConfig.get(ImageSize.headIconImageSize))
                .into(view);
    }

}
