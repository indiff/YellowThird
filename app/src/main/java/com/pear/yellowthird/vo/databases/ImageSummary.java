package com.pear.yellowthird.vo.databases;


import java.io.Serializable;

/**
 * 图片和对应的概要
 */
public class ImageSummary implements Serializable
{

    /**图片的地址*/
    String uri;

    /**图片的概要*/
    String summary;

    public ImageSummary() {
    }

    public ImageSummary(String uri, String summary) {
        this.uri = uri;
        this.summary = summary;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}