package com.example.zf_android.entity;

import java.util.List;

/**
 * 
*    
*
* @version    
*
 */
public class GoodsCategoryEntity extends BaseEntity {

    private int id;
    private String value;

    private List<IdValueEntity> son;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<IdValueEntity> getSon() {
        return son;
    }

    public void setSon(List<IdValueEntity> son) {
        this.son = son;
    }
}
