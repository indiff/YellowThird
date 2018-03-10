package com.hmy.ninegridlayout.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pear.yellowthird.activitys.R;

/**
 * Created by HMY
 */
public class ImageLoaderUtil {

    static DisplayImageOptions gOptions=null;
    public static ImageLoader getImageLoader(Context context) {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getPhotoImageOption() {
        if(gOptions!=null)
            return gOptions;
        Integer extra = 1;
        DisplayImageOptions options =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(false)
                        .cacheOnDisk(true)
                        .showImageForEmptyUri(R.drawable._image_load_fail)
                        .showImageOnFail(R.drawable._image_load_fail)
                        .showImageOnLoading(R.drawable._image_loading)
                        .extraForDownloader(extra)
                        .delayBeforeLoading(1000*3)
                        .bitmapConfig(Bitmap.Config.ALPHA_8).build();
        return options;
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options) {
        //imageView.setImageResource(R.drawable._image_sample_vertical);
        //getImageLoader(context).displayImage(url, imageView, options);
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        //getImageLoader(context).displayImage(url, imageView, options, listener);
    }
}
