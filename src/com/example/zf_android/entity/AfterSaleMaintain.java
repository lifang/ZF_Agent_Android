package com.example.zf_android.entity;

/**
 * 
*    
*
* @version    
*
 */
public class AfterSaleMaintain extends BaseEntity {
    private int id;
    private int status;
    private int terminals_quantity;

    private String apply_num;
    private String apply_time;
    private String terminals_list;
    private String reason;
    private String address;


    private CommentList comments;

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

    public int getTerminals_quantity() {
        return terminals_quantity;
    }

    public void setTerminals_quantity(int terminals_quantity) {
        this.terminals_quantity = terminals_quantity;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getTerminals_list() {
        return terminals_list;
    }

    public void setTerminals_list(String terminals_list) {
        this.terminals_list = terminals_list;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApply_num() {
        return apply_num;
    }

    public void setApply_num(String apply_num) {
        this.apply_num = apply_num;
    }

    public CommentList getComments() {
        return comments;
    }

    public void setComments(CommentList comments) {
        this.comments = comments;
    }
}
