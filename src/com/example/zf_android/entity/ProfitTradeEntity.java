package com.example.zf_android.entity;

/**
 * 
*    
*
* @version    
*
 */
public class ProfitTradeEntity extends BaseEntity {
    private int tradeTypeId;

    private String tradeTypeName;
    private String percent;

    public int getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(int tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
