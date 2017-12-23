package com.pear.yellowthird.style.interfaces;


/**
 * 样式视图的接口
 */

public interface StyleFragmentInterface<T> {

    /**
     * 根据当前子样式创建对应的视图
     * */
    android.support.v4.app.Fragment created(T data);

}
