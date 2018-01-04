package com.pear.yellowthird.vo.databases;



import com.pear.yellowthird.factory.ServiceDisposeFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 电影介绍的实体
 */

public class VideoIntroduceVo  implements Serializable {

    /** 数据库的Id */
    int id;

    /**标题*/
    String title;

    /**发表时间*/
    String publishTime="";

    /**封面*/
    String coverUri;

    /**概要*/
    String summary;

    /**播放地址*/
    String videoUri;

    /**作者*/
    String author;

    /**电影的主题，类别*/
    String topic;

    /**播放时长*/
    String duration;

    /**评分*/
    String grade;

    /**截图地址集合。*/
    List<String> screenShortUrls;

    /**播放次数*/
    Integer playCount;

    /**价格*/
    Integer price;

    /**点赞数量*/
    Integer goodCount;

    /**是否已经点赞*/
    Boolean alreadyClickGood=Boolean.FALSE;

    /**是否已经评分*/
    Boolean alreadyGrade=Boolean.FALSE;

    /**评论总数*/
    Integer allTalkCount;

    /**评论内容*/
    List<TalkComment> talkComment=new ArrayList<>();


    public VideoIntroduceVo() {
    }

    public VideoIntroduceVo(int id,
                            String title,
                            String coverUri,
                            String summary,
                            String videoUri,
                            String author,
                            String duration,
                            String grade,
                            List<String> screenShortUrls,
                            List<TalkComment> talkComment) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.coverUri = coverUri;
        this.videoUri = videoUri;
        this.author = author;
        this.duration = duration;
        this.grade = grade;
        this.screenShortUrls = screenShortUrls;
        this.talkComment = talkComment;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public String getDuration() {
        return duration;
    }

    public String getGrade() {
        return grade;
    }

    public List<String> getScreenShortUrls() {
        return screenShortUrls;
    }


    public List<TalkComment> getTalkComment() {
        return talkComment;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setScreenShortUrls(List<String> screenShortUrls) {
        this.screenShortUrls = screenShortUrls;
    }

    public void setTalkComment(List<TalkComment> talkComment) {
        this.talkComment = talkComment;
    }

    public Integer getAllTalkCount() {
        return allTalkCount;
    }

    public void setAllTalkCount(Integer allTalkCount) {
        this.allTalkCount = allTalkCount;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }


    public Boolean getAlreadyClickGood() {
        return alreadyClickGood;
    }

    public void setAlreadyClickGood(Boolean alreadyClickGood) {
        this.alreadyClickGood = alreadyClickGood;
    }

    public Boolean getAlreadyGrade() {
        return alreadyGrade;
    }

    public void setAlreadyGrade(Boolean alreadyGrade) {
        this.alreadyGrade = alreadyGrade;
    }
}
