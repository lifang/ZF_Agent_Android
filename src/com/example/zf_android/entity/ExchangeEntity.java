package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class ExchangeEntity extends BaseEntity {

    private int id;
    private int quantity;

    private String fromname;
    private String toname;
    private String terminal_list;
    private String created_at;
    private String creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getTerminal_list() {
        return terminal_list;
    }

    public void setTerminal_list(String terminal_list) {
        this.terminal_list = terminal_list;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
