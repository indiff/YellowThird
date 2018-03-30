package com.pear.yellowthird.vo.databases;

import java.io.Serializable;
import java.sql.Date;

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

    /**是否是管理员*/
    Boolean isAdmin=false;

    private Boolean isVip;
    private Date expire;

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


    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Date getExpire() {
        return expire;
    }
    public void setExpire(Date expire) {
        this.expire = expire;
    }
    public Boolean getIsVip() {
        return expire.getTime()>=new Date(System.currentTimeMillis()).getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserVo userVo = (UserVo) o;

        if (name != null ? !name.equals(userVo.name) : userVo.name != null) return false;
        if (thumb != null ? !thumb.equals(userVo.thumb) : userVo.thumb != null) return false;
        if (gold != null ? !gold.equals(userVo.gold) : userVo.gold != null) return false;
        if (isAdmin != null ? !isAdmin.equals(userVo.isAdmin) : userVo.isAdmin != null)
            return false;
        if (isVip != null ? !isVip.equals(userVo.isVip) : userVo.isVip != null) return false;
        return expire != null ? expire.equals(userVo.expire) : userVo.expire == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (thumb != null ? thumb.hashCode() : 0);
        result = 31 * result + (gold != null ? gold.hashCode() : 0);
        result = 31 * result + (isAdmin != null ? isAdmin.hashCode() : 0);
        result = 31 * result + (isVip != null ? isVip.hashCode() : 0);
        result = 31 * result + (expire != null ? expire.hashCode() : 0);
        return result;
    }

}
