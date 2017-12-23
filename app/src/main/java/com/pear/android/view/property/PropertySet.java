package com.pear.android.view.property;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.pear.common.utils.net.UriLoadData;

/**
 * 界面属性设置
 */

public class PropertySet {

    /**
     * 设置图片Url。
     * */
    public static void onSetImage(final Activity activity, final ImageView imageView, final String uri)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap data= UriLoadData.getImageFromNet(uri);
                if(null==data) {
                    //Toast.makeText(activity,"图片下载出错 uri："+uri,Toast.LENGTH_SHORT).show();
                    return;
                }
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(data);
                            }
                        }
                );
            }
        }).start();
    }

}
