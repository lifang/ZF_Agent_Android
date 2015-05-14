package com.example.zf_android.trade.entity;

/**
 * Created by Leo on 2015/2/12.
 */
public class TradeDetail {

    private String tradedTimeStr;
    private int agent_id;
    private String terminalNumber;
    private int types;
    private int amount;
    private int pay_channel_id;
    private String tradeNumber;
    private int profitPrice;
    private int poundage;
    private String batchNumber;
    private String payIntoAccount;
    private String merchant_number;
    private int tradedStatus;
    private String merchant_name;
    private String agentName;
    private String payFromAccount;
    private String paychannel;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTradedTimeStr() {
        return tradedTimeStr;
    }

    public void setTradedTimeStr(String tradedTimeStr) {
        this.tradedTimeStr = tradedTimeStr;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public int getProfitPrice() {
        return profitPrice;
    }

    public void setProfitPrice(int profitPrice) {
        this.profitPrice = profitPrice;
    }

    public int getPoundage() {
        return poundage;
    }

    public void setPoundage(int poundage) {
        this.poundage = poundage;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getPayIntoAccount() {
        return payIntoAccount;
    }

    public void setPayIntoAccount(String payIntoAccount) {
        this.payIntoAccount = payIntoAccount;
    }

    public int getTradedStatus() {
        return tradedStatus;
    }

    public void setTradedStatus(int tradedStatus) {
        this.tradedStatus = tradedStatus;
    }

    public String getPayFromAccount() {
        return payFromAccount;
    }

    public void setPayFromAccount(String payFromAccount) {
        this.payFromAccount = payFromAccount;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getPay_channel_id() {
        return pay_channel_id;
    }

    public void setPay_channel_id(int pay_channel_id) {
        this.pay_channel_id = pay_channel_id;
    }

    public String getMerchant_number() {
        return merchant_number;
    }

    public void setMerchant_number(String merchant_number) {
        this.merchant_number = merchant_number;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(String paychannel) {
        this.paychannel = paychannel;
    }
}
