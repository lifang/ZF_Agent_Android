package com.example.zf_android.entity;

public class AdressEntity {
//"id":16,"customerId":80,"receiver":"123","isDefault":2,"address":"123",
	private int id;
	private String customerId;
    private String isDefault;

    private String receiver;
	private Boolean Ischeck;
	private String address;
	//,"cityId":123,"zipCode":"123","moblephone":"123","telphone":"123
	private int cityId;
	private String zipCode;
	private String moblephone;
	private String telphone;
	public Boolean getIscheck() {
		return Ischeck;
	}
	public void setIscheck(Boolean ischeck) {
		Ischeck = ischeck;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMoblephone() {
		return moblephone;
	}
	public void setMoblephone(String moblephone) {
		this.moblephone = moblephone;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
