package com.example.zf_android.trade.entity;

import com.example.zf_android.entity.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Leo on 2015/3/4.
 */
public class TerminalItem extends BaseEntity {

	private int id;
	@SerializedName("serial_num")
	private String terminalNumber;
	private int status;
    private int retail_price;

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTerminalNumber() {
		return terminalNumber;
	}

	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
