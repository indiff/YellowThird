package com.pear.yellowthird.vo.databases;

import java.io.Serializable;
import java.util.Date;

/**
 * 一个标题，和对应的内容的实体
 */

public class NewsVo implements Serializable{

    /**标题*/
    String title="";

    /**内容*/
    String content="";

    /**图片地址*/
    String imageUri="";

    /**发表时间*/
    String publishTime="";


    public NewsVo() {
    }

    public NewsVo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NewsVo(String title, String content, String imageUri) {
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
