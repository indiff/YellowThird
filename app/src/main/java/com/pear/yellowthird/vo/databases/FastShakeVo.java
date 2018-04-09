package com.pear.yellowthird.vo.databases;

/**
 * 快抖
 */
public class FastShakeVo {

    Integer id;

    /**标题*/
    String title;

    /**发表用户*/
    UserVo user;

    /**喜爱人数*/
    Integer goodCount;

    /**播放地址*/
    String videoUri;

    /**发表时间*/
    String publishTime;

    /**
     * 播放次数
     * */
    private Integer playCount;

    public FastShakeVo() {
    }

    public FastShakeVo(String title, UserVo user, Integer goodCount, String videoUri) {
        this.title = title;
        this.user = user;
        this.goodCount = goodCount;
        this.videoUri = videoUri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
