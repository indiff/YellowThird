package com.pear.yellowthird.style.vo;


/**
 * 底部菜单的导航栏
 */

public class BottomNavigationMenuVo extends StyleType{

    /**
     * 菜单标题
     * */
    protected String title;

    /**
     * 菜单的图标
     */
    protected String icon;


    public BottomNavigationMenuVo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
