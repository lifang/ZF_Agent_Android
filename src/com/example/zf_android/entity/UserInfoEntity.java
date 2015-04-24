package com.example.zf_android.entity;

import java.util.List;

/**
*
* @version    
*
 */
public class UserInfoEntity extends BaseEntity {
    private int types;
    private int agentId;
    private int agentCityId;
    private int agentUserId;
    private int parent_id;
    private int id;
    private int status;

    private String createdAt;
    private String name;
    private String lastLoginedAt;
    private String username;
    private String updatedAt;

    private boolean is_have_profit;

    private List<UserRole> machtigingen;

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAgentCityId() {
        return agentCityId;
    }

    public void setAgentCityId(int agentCityId) {
        this.agentCityId = agentCityId;
    }

    public int getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(int agentUserId) {
        this.agentUserId = agentUserId;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastLoginedAt() {
        return lastLoginedAt;
    }

    public void setLastLoginedAt(String lastLoginedAt) {
        this.lastLoginedAt = lastLoginedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIs_have_profit() {
        return is_have_profit;
    }

    public void setIs_have_profit(boolean is_have_profit) {
        this.is_have_profit = is_have_profit;
    }

    public List<UserRole> getMachtigingen() {
        return machtigingen;
    }

    public void setMachtigingen(List<UserRole> machtigingen) {
        this.machtigingen = machtigingen;
    }
}
