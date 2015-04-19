package com.example.zf_android.entity;
/**
 * 
*    
*
* @version    
*
 */
public class PrepareEntity extends BaseEntity {

    private int id;
    private int quantity;

    private String company_name;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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
