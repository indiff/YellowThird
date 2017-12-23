package com.pear.yellowthird.constants;

/**
 * 视图Id常量
 */

public class ViewIdConstant {

    /**
     * 所有动态需要获取的view id 起止下标
     * */
    private static int VIEW_ID_START=100000;


    public static int getGlobalUniqueId()
    {
        return ++VIEW_ID_START;
    }

}
