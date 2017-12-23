package com.pear.android.view.vo;

/**
 * 丰富view 里面的TAG属性
 * 用于扩展属性和对应的下标
 * 因为有时候例如linearLayout不是list 。而是addView这种。基本上都是通过TAG来获取标志和数据的
 */

public class TagByIndexObject<T> {

    /**对应的对象参数*/
    T mObject;

    /**当前所在的下标*/
    Integer mIndex;

    public TagByIndexObject(T object, Integer index) {
        this.mObject = object;
        this.mIndex = index;
    }

    public T getObject() {
        return mObject;
    }

    public Integer getIndex() {
        return mIndex;
    }
}
