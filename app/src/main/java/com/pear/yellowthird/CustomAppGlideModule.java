package com.pear.yellowthird;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 *
 * http://blog.csdn.net/adarcy/article/details/79253205
 * 记住加注解@GlideModule，我就是忘了这个

 然后点击菜单里Build->Make Project,系统会自动生成GlideApp，

 然后通过GlideApp.with()方式就乐意使用Glide的Generated API了。
 */

@GlideModule
public class CustomAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //builder.setMemoryCache(new LruResourceCache(20 * 1024 * 1024));
        //builder.setMemoryCache(null);
    }
}