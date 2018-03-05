package com.pear.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pear.yellowthird.activitys.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by su on 2018/3/4.
 */

public class ImageTest extends Activity{

    List<ImageView> list=new ArrayList<>();

    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__test);
        linear=findViewById(R.id.linear);
    }

    @Override
    protected void onStart(){
        super.onStart();
        for(int i=0;i<30;i++)
        {
            System.out.println("i："+i);
            ImageView imageView=new ImageView(this);
            /*
            Picasso.with(this)
                    .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1520175265524&di=632d478d9c64993a738ac03e0a13e365&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-10-14%2F59e1bb9f01314.jpg")
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
            */
            new DownloadImageTask(imageView).execute("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1520175265524&di=632d478d9c64993a738ac03e0a13e365&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-10-14%2F59e1bb9f01314.jpg");
            linear.addView(imageView);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                free();
            }
        },5*1000);

    }

    void free()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("free ");
                for(int i=0;i<linear.getChildCount();i++)
                {
                    //releaseImageViewResouce((ImageView)linear.getChildAt(i));
                }
            }
        });
    }

    /**
     * 释放图片资源的方法
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                //bitmap.recycle();
                //bitmap=null;
            }
        }
        imageView.setImageBitmap(null);
        //System.gc();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, SoftReference<Bitmap>> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected SoftReference<Bitmap> doInBackground(String... urls) {
            String urldisplay = urls[0];
            SoftReference<Bitmap> mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = new SoftReference<Bitmap>(BitmapFactory.decodeStream(in));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(SoftReference<Bitmap> result) {

            bmImage.setImageBitmap(result.get());
        }
    }

}
