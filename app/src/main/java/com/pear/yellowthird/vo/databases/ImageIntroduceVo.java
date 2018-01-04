package com.pear.yellowthird.vo.databases;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片介绍的实体
 */

public class ImageIntroduceVo  implements Serializable{

    /** 数据库的Id */
    int id;

    /**标题*/
    String title="";

    /**图片的概要*/
    String content="";

    /**下面的子图片集合。*/
    List<String> images=new ArrayList<>();

    /**点赞数量*/
    Integer goodCount;

    /**发表时间*/
    String publishTime="";

    /**浏览次数*/
    Integer browseCount;

    /**是否已经点赞*/
    Boolean alreadyClickGood=Boolean.FALSE;

    public ImageIntroduceVo() {
    }

    public ImageIntroduceVo(String title, String content, List<String> images, Integer goodCount, String publishTime, Integer browseCount) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.goodCount = goodCount;
        this.publishTime = publishTime;
        this.browseCount = browseCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Boolean getAlreadyClickGood() {
        return alreadyClickGood;
    }

    public void setAlreadyClickGood(Boolean alreadyClickGood) {
        this.alreadyClickGood = alreadyClickGood;
    }
}
