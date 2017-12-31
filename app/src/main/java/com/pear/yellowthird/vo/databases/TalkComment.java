package com.pear.yellowthird.vo.databases;

import java.io.Serializable;

/**
 * 对话评论。
 */
public class TalkComment implements Serializable {

    /**资源ID*/
    Integer id;

    /**评论内容*/
    String content;

    /**赞数量*/
    int goodCount;

    /**发表时间*/
    String publishTime;

    /**评论的用户*/
    UserVo user;

    /**是否已经点赞*/
    boolean isAlreadyClickGood=false;


    public TalkComment() {
    }

    public TalkComment(String content , int goodCount, int badCount) {
        this.content = content;
        this.goodCount=goodCount;
    }

    public TalkComment(String content, UserVo user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public boolean isAlreadyClickGood() {
        return isAlreadyClickGood;
    }

    public void setAlreadyClickGood(boolean alreadyClickGood) {
        isAlreadyClickGood = alreadyClickGood;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
