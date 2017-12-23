package com.pear.yellowthird.vo.databases;

import java.io.Serializable;

/**
 * 用户信息
 */

public class UserVo  implements Serializable {

    /**用户名称*/
    String name;

    /**头像地址*/
    String thumb;

    /**余额*/
    Integer gold;

    public UserVo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

}
