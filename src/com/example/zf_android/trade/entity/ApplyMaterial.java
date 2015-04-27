package com.example.zf_android.trade.entity;

/**
 * Created by Leo on 2015/3/6.
 */
public class ApplyMaterial {
    private int id;
    private int info_type;
    private int opening_requirements_id;

    private long created_at;
    private long updated_at;

    private String introduction;
    private String name;
    private String query_mark;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfo_type() {
        return info_type;
    }

    public void setInfo_type(int info_type) {
        this.info_type = info_type;
    }

    public int getOpening_requirements_id() {
        return opening_requirements_id;
    }

    public void setOpening_requirements_id(int opening_requirements_id) {
        this.opening_requirements_id = opening_requirements_id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery_mark() {
        return query_mark;
    }

    public void setQuery_mark(String query_mark) {
        this.query_mark = query_mark;
    }
}
