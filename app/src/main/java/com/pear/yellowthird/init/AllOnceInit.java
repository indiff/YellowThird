package com.pear.yellowthird.init;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pear.yellowthird.activitys.MainActivity;
import com.pear.yellowthird.config.SystemConfig;
import com.pear.yellowthird.impl.net.ServiceDisposeImpl;
import com.pear.yellowthird.style.factory.StyleFactory;

/**
 * 所有程序初始化需要的一次性初始化程序
 */

public class AllOnceInit {


    /**
     * 初始化
     * */
    public static void init(MainActivity activity)
    {

        SystemConfig.getInstance().init(activity);
        Log4JConfig.init(activity);
        ServiceDisposeImpl.initDeviceId(activity);
        StyleFactory.init(activity);

        initImageLoader(activity);

        //new NewVersionInstall(activity,activity).checkAndInstall();
    }

    /**
     * 初始化图片的9宫格
     * */
    private static void initImageLoader(Activity activity) {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
        ImageLoader.getInstance().init(configuration);
    }


}
