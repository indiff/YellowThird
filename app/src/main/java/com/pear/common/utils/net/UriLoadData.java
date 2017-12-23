package com.pear.common.utils.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by su on 2017/11/1.
 */

public class UriLoadData {

    private static Logger log = Logger.getLogger(UriLoadData.class);

    public static Bitmap getImageFromNet(String url) {
        HttpURLConnection conn;
        conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间

            conn.connect(); //开始连接

            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
                return bitmap;
            } else {
                //访问失败
                log.debug("访问失败===responseCode：" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
        return null;
    }


}
