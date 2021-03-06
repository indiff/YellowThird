package com.pear.yellowthird.vo.databases;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈的说说属性
 */

public class FriendsVo  implements Serializable {

    /**数据库Id*/
    Integer id;

    /**发表当前说说的用户信息*/
    UserVo user;

    /**说说内容*/
    String content="";

    /**发表时间*/
    String publishTime="";

    /**浏览次数*/
    Integer showCount;

    /**是否已经点赞*/
    Boolean alreadyClickGood=Boolean.FALSE;

    /**点赞数量*/
    Integer goodCount;

    /**发表图片集合*/
    List<String> images;

    /**评论内容*/
    List<TalkComment> comments=new ArrayList<>();


    public FriendsVo() {
    }

    public FriendsVo(List<String> images) {
        this.images = images;
    }

    public FriendsVo(List<String> images, List<TalkComment> comments) {
        this.images = images;
        this.comments = comments;
    }

    public FriendsVo(UserVo user, String content, String publishTime, Integer browseCount, boolean isAlreadyClickGood, Integer goodCount, List<String> images, List<TalkComment> comments) {
        this.user = user;
        this.content = content;
        this.publishTime = publishTime;
        this.showCount = browseCount;
        this.alreadyClickGood = isAlreadyClickGood;
        this.goodCount = goodCount;
        this.images = images;
        this.comments = comments;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
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

    public Integer getShowCount() {
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TalkComment> getComments() {
        return comments;
    }

    public void setComments(List<TalkComment> comments) {
        this.comments = comments;
    }

    public Boolean getAlreadyClickGood() {
        return alreadyClickGood;
    }

    public void setAlreadyClickGood(Boolean alreadyClickGood) {
        this.alreadyClickGood = alreadyClickGood;
    }
}
