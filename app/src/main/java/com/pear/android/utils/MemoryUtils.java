package com.pear.android.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by su on 2018/3/3.
 */

public class MemoryUtils {

    /**
     * 释放图片资源的方法
     * @param imageView
     */
    public static void releaseImageView(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        imageView.setImageDrawable(null);
        imageView.setImageResource(0);
    }

}
