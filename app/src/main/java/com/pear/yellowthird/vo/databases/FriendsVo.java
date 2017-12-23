package com.pear.yellowthird.vo.databases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈的属性
 */

public class FriendsVo  implements Serializable {

    List<String> images;

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
}
