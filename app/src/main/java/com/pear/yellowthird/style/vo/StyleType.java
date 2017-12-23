package com.pear.yellowthird.style.vo;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;

import java.io.Serializable;

/**
 * 样式类别
 */

public class StyleType implements Serializable {

    /**显示样式*/
    String style;

    /**数据来源*/
    String dataSource;

    /**数据类型*/
    String dataType;

    /**
     * 具体数据
     * */
    String data;

    /**
     * 填充data的sql
     * */
    String sql;

    public StyleType() {
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
        if(!TextUtils.isEmpty(sql))
            data= ServiceDisposeFactory.getInstance().getServiceDispose().queryByUrl(sql);
    }
}
