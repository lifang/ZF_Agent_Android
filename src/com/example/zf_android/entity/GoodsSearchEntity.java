package com.example.zf_android.entity;

import java.util.List;

/**
*
* @version    
*
*/
public class GoodsSearchEntity extends BaseEntity {

    private List<IdValueEntity> brands;
    private List<IdValueEntity> pay_card;
    private List<IdValueEntity> pay_channel;
    private List<IdValueEntity> sale_slip;
    private List<IdValueEntity> tDate;
    private List<IdValueEntity> trade_type;
    private List<GoodsCategoryEntity> category;

    public List<IdValueEntity> getBrands() {
        return brands;
    }

    public void setBrands(List<IdValueEntity> brands) {
        this.brands = brands;
    }

    public List<IdValueEntity> getPay_card() {
        return pay_card;
    }

    public void setPay_card(List<IdValueEntity> pay_card) {
        this.pay_card = pay_card;
    }

    public List<IdValueEntity> getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(List<IdValueEntity> pay_channel) {
        this.pay_channel = pay_channel;
    }

    public List<IdValueEntity> getSale_slip() {
        return sale_slip;
    }

    public void setSale_slip(List<IdValueEntity> sale_slip) {
        this.sale_slip = sale_slip;
    }

    public List<IdValueEntity> gettDate() {
        return tDate;
    }

    public void settDate(List<IdValueEntity> tDate) {
        this.tDate = tDate;
    }

    public List<IdValueEntity> getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(List<IdValueEntity> trade_type) {
        this.trade_type = trade_type;
    }

    public List<GoodsCategoryEntity> getCategory() {
        return category;
    }

    public void setCategory(List<GoodsCategoryEntity> category) {
        this.category = category;
    }
}
