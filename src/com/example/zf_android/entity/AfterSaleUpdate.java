package com.example.zf_android.entity;

import java.util.List;

/**
 * 
*    
*
* @version    
*
 */
public class AfterSaleUpdate extends BaseEntity {
    private int id;
    private int status;

    private String apply_num;
    private String apply_time;
    private String merchant_name;
    private String brand_name;
    private String merchant_phone;
    private String brand_number;
    private String good_name;
    private String terminal_num;
    private String zhifu_pingtai;


    private List<Object> resource_info;

    private CommentList comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApply_num() {
        return apply_num;
    }

    public void setApply_num(String apply_num) {
        this.apply_num = apply_num;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }

    public String getBrand_number() {
        return brand_number;
    }

    public void setBrand_number(String brand_number) {
        this.brand_number = brand_number;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getTerminal_num() {
        return terminal_num;
    }

    public void setTerminal_num(String terminal_num) {
        this.terminal_num = terminal_num;
    }

    public String getZhifu_pingtai() {
        return zhifu_pingtai;
    }

    public void setZhifu_pingtai(String zhifu_pingtai) {
        this.zhifu_pingtai = zhifu_pingtai;
    }

    public List<Object> getResource_info() {
        return resource_info;
    }

    public void setResource_info(List<Object> resource_info) {
        this.resource_info = resource_info;
    }

    public CommentList getComments() {
        return comments;
    }

    public void setComments(CommentList comments) {
        this.comments = comments;
    }
}
