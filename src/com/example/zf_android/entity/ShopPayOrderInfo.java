package com.example.zf_android.entity;

import java.util.List;
import java.util.Map;

/**
 * 
*    
*
* @version    
*
 */
public class ShopPayOrderInfo extends BaseEntity {
    private int total_price;
    private int actual_price;
    private int paytype;

    private String address;
    private String receiver;
    private String order_number;
    private List<Map<String, Object>> good;

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getActual_price() {
        return actual_price;
    }

    public void setActual_price(int actual_price) {
        this.actual_price = actual_price;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public List<Map<String, Object>> getGood() {
        return good;
    }

    public void setGood(List<Map<String, Object>> good) {
        this.good = good;
    }
}
