package com.example.zf_android.entity;

/**
 * Created by holin on 4/12/15.
 */
public class StockAgentEntity {

    private int hoitoryCount;
    private int agent_id;
    private int openCount;

    private String company_name;
    private String lastPrepareTime;
    private String lastOpenTime;

    public int getHoitoryCount() {
        return hoitoryCount;
    }

    public void setHoitoryCount(int hoitoryCount) {
        this.hoitoryCount = hoitoryCount;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLastPrepareTime() {
        return lastPrepareTime;
    }

    public void setLastPrepareTime(String lastPrepareTime) {
        this.lastPrepareTime = lastPrepareTime;
    }

    public String getLastOpenTime() {
        return lastOpenTime;
    }

    public void setLastOpenTime(String lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }
}
