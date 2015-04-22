package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class ChannelTradeEntity extends BaseEntity {

	private int id;
	private String trade_value;
	private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrade_value() {
        return trade_value;
    }

    public void setTrade_value(String trade_value) {
        this.trade_value = trade_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
