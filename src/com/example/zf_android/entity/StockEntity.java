package com.example.zf_android.entity;

/**
 * Created by holin on 4/12/15.
 */
public class StockEntity {

    private String good_brand;
    private String picurl;
    private String paychannel;
    private String goodname;
    private String Model_number;

    private int hoitoryCount;
    private int totalCount;
    private int openCount;
    private int paychannel_id;
    private int agentCount;
    private int good_id;

    public String getGood_brand() {
        return good_brand;
    }

    public void setGood_brand(String good_brand) {
        this.good_brand = good_brand;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(String paychannel) {
        this.paychannel = paychannel;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getModel_number() {
        return Model_number;
    }

    public void setModel_number(String model_number) {
        Model_number = model_number;
    }

    public int getHoitoryCount() {
        return hoitoryCount;
    }

    public void setHoitoryCount(int hoitoryCount) {
        this.hoitoryCount = hoitoryCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

    public int getPaychannel_id() {
        return paychannel_id;
    }

    public void setPaychannel_id(int paychannel_id) {
        this.paychannel_id = paychannel_id;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }
}
