package com.pear.yellowthird.vo.databases;

import java.util.Date;

/**
 * 账单
 */

public class BillVo {

    /**概要*/
    String description;

    /**产生时间*/
    String payTime;

    /**金额*/
    double gold;

    public BillVo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }
}
