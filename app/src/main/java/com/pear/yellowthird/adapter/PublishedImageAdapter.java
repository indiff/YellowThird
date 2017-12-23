package com.pear.yellowthird.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.Bimp;
import com.pear.yellowthird.activitys.published.FileUtils;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 发表说说的已选中的图片集合适配器
 * */
public class PublishedImageAdapter extends BaseAdapter {

    /**视图容器*/
    private Context context;

    public PublishedImageAdapter(Context context) {
        this.context=context;
    }

    /**
     * 刷新视图
     * */
    public void update() {
        loading();
    }

    public int getCount() {
        return (Bimp.bmp.size() + 1);
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        PublishedImageAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sub_published_sub_image_list_line,
                    parent, false);
            holder = new PublishedImageAdapter.ViewHolder();
            holder.imageView =convertView
                    .findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (PublishedImageAdapter.ViewHolder) convertView.getTag();
        }

        /**
         * 如果是刚好最后一张下标，则显示添加样式
         * */
        if (position == Bimp.bmp.size()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable._add_image));
            /**最多可以添加9张图片*/
            if (position == 9) {
                holder.imageView.setVisibility(View.GONE);
            }
        /**否则纯粹显示已选的图片*/
        } else {
            holder.imageView.setImageBitmap(Bimp.bmp.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
    }

    /**
     * 一张张显示已选中加载的图片
     * */
    public void loading() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                while (true) {
                    /**处理完毕*/
                    if (Bimp.max == Bimp.drr.size()) {
                        subscriber.onNext(null);
                        break;
                    } else {
                        try {
                            String path = Bimp.drr.get(Bimp.max);
                            System.out.println(path);
                            Bitmap bm = Bimp.revitionImageSize(path);
                            Bimp.bmp.add(bm);
                            String newStr = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm, "" + newStr);
                            Bimp.max += 1;
                            subscriber.onNext(null);;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Object>() {
            @Override
            public void call(Object data) {
                PublishedImageAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

}