package com.pear.android.view;

import android.content.Context;
import android.widget.ImageView;

import com.hmy.ninegridlayout.util.ImageLoaderUtil;

/**
 * Created by guizhigang on 16/7/14.
 */
public class DefaultImageCreator implements LGNineGrideView.ImageCreator {

    private static DefaultImageCreator defaultImageCreator;

    private DefaultImageCreator(){
    }

    public static DefaultImageCreator getInstance(){
        if(defaultImageCreator == null){
            synchronized (MyImageLoader.class){
                if(defaultImageCreator == null)
                    defaultImageCreator = new DefaultImageCreator();
            }
        }
        return defaultImageCreator;
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ColorFilterImageView(context);
        //imageView.setImageDrawable(context.getResources().getDrawable(R.drawable._image_loading));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        ImageLoaderUtil
                .getImageLoader(context)
                .displayImage(
                        url,
                        imageView,
                        ImageLoaderUtil.getPhotoImageOption());
    }

}
