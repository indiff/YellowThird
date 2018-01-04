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

    public UserVo(String name) {
        this.name = name;
    }

    public UserVo(String name, String thumb, Integer gold) {
        this.name = name;
        this.thumb = thumb;
        this.gold = gold;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserVo userVo = (UserVo) o;

        if (name != null ? !name.equals(userVo.name) : userVo.name != null) return false;
        if (thumb != null ? !thumb.equals(userVo.thumb) : userVo.thumb != null) return false;
        return gold != null ? gold.equals(userVo.gold) : userVo.gold == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (thumb != null ? thumb.hashCode() : 0);
        result = 31 * result + (gold != null ? gold.hashCode() : 0);
        return result;
    }


}
