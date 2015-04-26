package com.example.zf_android.entity;

import java.util.List;

public class OrderEntity {
    private int order_id;
    private int order_status;
    private int zhifu_dingjin;
    private int shengyu_price;
    private int actual_price;
    private int shipped_quantity;
    private int price_dingjin;
    private int pay_status;
    private int total_quantity;
    private int order_goods_size;
    private int order_totalNum;
    private int order_psf;
    private int order_totalPrice;

    private String order_number;
    private String order_createTime;
    private String guishu_user;

    public int getOrder_totalNum() {
        return order_totalNum;
    }

    public void setOrder_totalNum(int order_totalNum) {
        this.order_totalNum = order_totalNum;
    }

    public int getOrder_psf() {
        return order_psf;
    }

    public void setOrder_psf(int order_psf) {
        this.order_psf = order_psf;
    }

    public int getOrder_totalPrice() {
        return order_totalPrice;
    }

    public void setOrder_totalPrice(int order_totalPrice) {
        this.order_totalPrice = order_totalPrice;
    }

    public String getGuishu_user() {
        return guishu_user;
    }

    public void setGuishu_user(String guishu_user) {
        this.guishu_user = guishu_user;
    }

    private List<Goodlist> order_goodsList;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getZhifu_dingjin() {
        return zhifu_dingjin;
    }

    public void setZhifu_dingjin(int zhifu_dingjin) {
        this.zhifu_dingjin = zhifu_dingjin;
    }

    public int getShengyu_price() {
        return shengyu_price;
    }

    public void setShengyu_price(int shengyu_price) {
        this.shengyu_price = shengyu_price;
    }

    public int getActual_price() {
        return actual_price;
    }

    public void setActual_price(int actual_price) {
        this.actual_price = actual_price;
    }

    public int getShipped_quantity() {
        return shipped_quantity;
    }

    public void setShipped_quantity(int shipped_quantity) {
        this.shipped_quantity = shipped_quantity;
    }

    public int getPrice_dingjin() {
        return price_dingjin;
    }

    public void setPrice_dingjin(int price_dingjin) {
        this.price_dingjin = price_dingjin;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public int getOrder_goods_size() {
        return order_goods_size;
    }

    public void setOrder_goods_size(int order_goods_size) {
        this.order_goods_size = order_goods_size;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_createTime() {
        return order_createTime;
    }

    public void setOrder_createTime(String order_createTime) {
        this.order_createTime = order_createTime;
    }

    public List<Goodlist> getOrder_goodsList() {
        return order_goodsList;
    }

    public void setOrder_goodsList(List<Goodlist> order_goodsList) {
        this.order_goodsList = order_goodsList;
    }
}
