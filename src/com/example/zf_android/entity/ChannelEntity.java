package com.example.zf_android.entity;

import java.util.List;

/**
 * 
*    
*
* @version    
*
 */
public class ChannelEntity extends BaseEntity {

    private int id;
    private String name;

    private List<BillingEntity> billings;
    private List<ChannelTradeEntity> trades;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BillingEntity> getBillings() {
        return billings;
    }

    public void setBillings(List<BillingEntity> billings) {
        this.billings = billings;
    }

    public List<ChannelTradeEntity> getTrades() {
        return trades;
    }

    public void setTrades(List<ChannelTradeEntity> trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        return getName();
    }
}
