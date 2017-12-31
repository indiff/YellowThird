package com.pear.common.utils.net;

import java.io.File;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 文件上传
 */
public class FileUploadUtils {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    /**
     * @param url
     * @param content
     * @param userId
     * @param files
     * @param callback
     * */
    public static void uploadImg(String url,String content,String userId,List<String> files,Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <files.size() ; i++) {
            File f=new File(files.get(i));
            builder.addFormDataPart("pic", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        }
        builder.addFormDataPart("user.id",userId);
        builder.addFormDataPart("content", content);

        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        new OkHttpClient().newCall(request).enqueue(callback);
    }
}
