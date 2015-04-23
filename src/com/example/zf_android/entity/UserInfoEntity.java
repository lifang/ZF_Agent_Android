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

}
