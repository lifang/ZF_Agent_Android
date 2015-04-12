package com.example.zf_android.entity;

/**
 * Created by holin on 4/13/15.
 */
public class TerminalApplyEntity {
    private int id;
    private int status;
    private String serial_num;

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

    public String getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(String serial_num) {
        this.serial_num = serial_num;
    }
}
