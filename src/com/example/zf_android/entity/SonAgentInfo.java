package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class SonAgentInfo extends BaseEntity {
    private int cityId;
    private int soldnum;
    private int opennum;
    private int allQty;
    private String created_at;
    private int types;

    private String is_have_profit;

    private String loginId;
    private String phone;
    private String cityName;
    private String provinceName;
    private String business_license;
    private String card_id;
    private String tax_registered_no;
    private String company_name;
    private String email;
    private String address;
    private String name;
    private String licensepath;
    private String taxpath;
    private String cardpath;


    public int getSoldnum() {
        return soldnum;
    }

    public void setSoldnum(int soldnum) {
        this.soldnum = soldnum;
    }

    public int getOpennum() {
        return opennum;
    }

    public void setOpennum(int opennum) {
        this.opennum = opennum;
    }

    public String getLicensepath() {
        return licensepath;
    }

    public void setLicensepath(String licensepath) {
        this.licensepath = licensepath;
    }

    public String getTaxpath() {
        return taxpath;
    }

    public void setTaxpath(String taxpath) {
        this.taxpath = taxpath;
    }

    public String getCardpath() {
        return cardpath;
    }

    public void setCardpath(String cardpath) {
        this.cardpath = cardpath;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public int getAllQty() {
        return allQty;
    }

    public void setAllQty(int allQty) {
        this.allQty = allQty;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getIs_have_profit() {
        return is_have_profit;
    }

    public void setIs_have_profit(String is_have_profit) {
        this.is_have_profit = is_have_profit;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(String business_license) {
        this.business_license = business_license;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getTax_registered_no() {
        return tax_registered_no;
    }

    public void setTax_registered_no(String tax_registered_no) {
        this.tax_registered_no = tax_registered_no;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
