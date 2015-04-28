package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class PayOrderInfo extends BaseEntity {
    private int order_id;
    private int shengyu_price;
    private int pay_status;
    private int order_totalPrice;
    private int price_dingjin;
    private int zhifu_dingjin;
    private String order_number;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getShengyu_price() {
        return shengyu_price;
    }

    public void setShengyu_price(int shengyu_price) {
        this.shengyu_price = shengyu_price;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public int getOrder_totalPrice() {
        return order_totalPrice;
    }

    public void setOrder_totalPrice(int order_totalPrice) {
        this.order_totalPrice = order_totalPrice;
    }

    public int getPrice_dingjin() {
        return price_dingjin;
    }

    public void setPrice_dingjin(int price_dingjin) {
        this.price_dingjin = price_dingjin;
    }

    public int getZhifu_dingjin() {
        return zhifu_dingjin;
    }

    public void setZhifu_dingjin(int zhifu_dingjin) {
        this.zhifu_dingjin = zhifu_dingjin;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
