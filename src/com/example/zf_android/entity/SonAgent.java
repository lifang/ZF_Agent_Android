package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class SonAgent extends BaseEntity {
    private int types;
    private int soldNum;
    private int default_profit;
    private int openNum;
    private int id;
    private int status;


    private String isProfitStr;
    private String statusStr;
    private String company_name;
    private String created_at;
    private String allQtyStr;

    private boolean is_have_profit;

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }

    public int getDefault_profit() {
        return default_profit;
    }

    public void setDefault_profit(int default_profit) {
        this.default_profit = default_profit;
    }

    public int getOpenNum() {
        return openNum;
    }

    public void setOpenNum(int openNum) {
        this.openNum = openNum;
    }

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

    public String getIsProfitStr() {
        return isProfitStr;
    }

    public void setIsProfitStr(String isProfitStr) {
        this.isProfitStr = isProfitStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAllQtyStr() {
        return allQtyStr;
    }

    public void setAllQtyStr(String allQtyStr) {
        this.allQtyStr = allQtyStr;
    }

    public boolean isIs_have_profit() {
        return is_have_profit;
    }

    public void setIs_have_profit(boolean is_have_profit) {
        this.is_have_profit = is_have_profit;
    }
}
