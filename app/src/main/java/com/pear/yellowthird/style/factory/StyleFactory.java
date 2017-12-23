package com.pear.yellowthird.style.factory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pear.yellowthird.activitys.R;

/**
 * 样式工厂
 */

public class StyleFactory {

    private static Context gContext;

    public static void init(Context context)
    {
        gContext=context;
    }

    /**
     * 设置内容体的左右内边框
     * */
    public static void setContentPadding(View view)
    {
        if(null==gContext)
            throw new NullPointerException("not init");
        view.setPadding(
                gContext.getResources().getInteger(R.integer.content_padding_left),
                gContext.getResources().getInteger(R.integer.content_padding_top),
                gContext.getResources().getInteger(R.integer.content_padding_right),
                gContext.getResources().getInteger(R.integer.content_padding_bottom));
    }


    public static class Comment
    {
        /**
         * 获取评论体的作者头像大小
         * */
        public static ViewGroup.LayoutParams getAuthorIconSize()
        {
            return new ViewGroup.LayoutParams(90,90);
        }

    }
}
