package com.pear.yellowthird.style.vo;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 子菜单样式的数据格式
 */

public class SubTabMenuStyleDataVo extends StyleType{

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
