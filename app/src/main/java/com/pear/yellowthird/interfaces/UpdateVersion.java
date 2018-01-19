package com.pear.yellowthird.interfaces;

/**
 * Created by su on 2018/1/18.
 */

public interface UpdateVersion {

    /**获取文件的保存路径*/
    String getSavePath();

    /**更新程序*/
    void updateVersion(String path);

}
