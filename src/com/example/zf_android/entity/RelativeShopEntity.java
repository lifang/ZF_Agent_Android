package com.example.zf_android.entity;

public class RelativeShopEntity {
    private int id;
    private int retail_price;
    private int volume_number;

    private String Title;
    private String url_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
    }

    public int getVolume_number() {
        return volume_number;
    }

    public void setVolume_number(int volume_number) {
        this.volume_number = volume_number;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl_path() {
        return url_path;
    }

    public void setUrl_path(String url_path) {
        this.url_path = url_path;
    }
}
