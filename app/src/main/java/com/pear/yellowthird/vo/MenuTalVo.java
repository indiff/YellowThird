package com.pear.yellowthird.vo;


import java.io.Serializable;

/**
 * tab 每一行菜单附带的参数
 * */
public class MenuTalVo  implements Serializable {

    /**
     * 文字菜单
     * */
    private String title;

    /**
     * 文字菜单对应的图标
     * */
    private int icon;

    public MenuTalVo() {
    }

    public MenuTalVo(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}

