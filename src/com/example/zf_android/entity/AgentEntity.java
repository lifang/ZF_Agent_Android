package com.example.zf_android.entity;
/***
 * 
*    
*
* @version    
*
 */
public class AgentEntity extends BaseEntity {

    private int city_id;
    private int id;
    private long created_at;
    private int customer_id;

    private String phone;
    private String title;
    private String legal_person_card_id;
    private String legal_person_name;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLegal_person_card_id() {
        return legal_person_card_id;
    }

    public void setLegal_person_card_id(String legal_person_card_id) {
        this.legal_person_card_id = legal_person_card_id;
    }

    public String getLegal_person_name() {
        return legal_person_name;
    }

    public void setLegal_person_name(String legal_person_name) {
        this.legal_person_name = legal_person_name;
    }
}