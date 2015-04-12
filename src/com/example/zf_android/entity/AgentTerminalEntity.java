package com.example.zf_android.entity;

/**
 * Created by holin on 4/12/15.
 */
public class AgentTerminalEntity {
    private int id;
    private int status;

    private String good_brand;
    private String serial_num;
    private String Model_number;

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

    public String getGood_brand() {
        return good_brand;
    }

    public void setGood_brand(String good_brand) {
        this.good_brand = good_brand;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }

    public String getModel_number() {
        return Model_number;
    }

    public void setModel_number(String model_number) {
        Model_number = model_number;
    }
}
