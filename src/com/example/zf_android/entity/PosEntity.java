package com.example.zf_android.entity;

public class PosEntity {

	private Boolean has_lease;
	private int volume_number;
	private int id;
	private int lease_deposit;
	private int purchase_number;
	private int purchase_price;
	private int floor_price;
	private String good_brand;
    private String url_path;
    private int total_score;


    private int floor_purchase_quantity;
    private int retail_price;
    private String pay_channe;
    private String Title;
    private String second_title;
    private String Model_number;


    public String getUrl_path() {
        return url_path;
    }

    public void setUrl_path(String url_path) {
        this.url_path = url_path;
    }

    public int getFloor_purchase_quantity() {
        return floor_purchase_quantity;
    }

    public void setFloor_purchase_quantity(int floor_purchase_quantity) {
        this.floor_purchase_quantity = floor_purchase_quantity;
    }
	public int getVolume_number() {
		return volume_number;
	}
	public void setVolume_number(int volume_number) {
		this.volume_number = volume_number;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGood_brand() {
		return good_brand;
	}
	public void setGood_brand(String good_brand) {
		this.good_brand = good_brand;
	}
	public int getTotal_score() {
		return total_score;
	}
	public void setTotal_score(int total_score) {
		this.total_score = total_score;
	}
	public int getRetail_price() {
		return retail_price;
	}
	public void setRetail_price(int retail_price) {
		this.retail_price = retail_price;
	}
	public String getPay_channe() {
		return pay_channe;
	}
	public void setPay_channe(String pay_channe) {
		this.pay_channe = pay_channe;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getModel_number() {
		return Model_number;
	}
	public void setModel_number(String model_number) {
		Model_number = model_number;
	}


    public Boolean getHas_lease() {
        return has_lease;
    }
    public void setHas_lease(Boolean has_lease) {
        this.has_lease = has_lease;
    }

    public int getLease_deposit() {
        return lease_deposit;
    }

    public void setLease_deposit(int lease_deposit) {
        this.lease_deposit = lease_deposit;
    }

    public int getPurchase_number() {
        return purchase_number;
    }

    public void setPurchase_number(int purchase_number) {
        this.purchase_number = purchase_number;
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public int getFloor_price() {
        return floor_price;
    }

    public void setFloor_price(int floor_price) {
        this.floor_price = floor_price;
    }

    public String getSecond_title() {
        return second_title;
    }

    public void setSecond_title(String second_title) {
        this.second_title = second_title;
    }
}
