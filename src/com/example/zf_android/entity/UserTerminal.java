package com.example.zf_android.entity;

/**
 * Created by holin on 4/14/15.
 */
public class UserTerminal extends BaseEntity {
    private int id;
    private String serial_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }
}
