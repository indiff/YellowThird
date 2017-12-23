package com.pear.yellowthird.vo.databases;

import java.io.Serializable;

/**
 * 点评实体类
 */
public class CommentVo  implements Serializable{

    //赞数量
    int goodCount;

    //踩数量
    int badCount;

    public CommentVo() {
    }

    public CommentVo(int goodCount, int badCount) {
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public int getBadCount() {
        return badCount;
    }

}
