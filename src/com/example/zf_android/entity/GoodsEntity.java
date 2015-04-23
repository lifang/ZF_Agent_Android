package com.example.zf_android.entity;

import java.util.List;

public class GoodsEntity {
    private int commentsCount;

    private FactoryEntity factory;
    private GoodinfoEntity goodinfo;
    private List<String> goodPics;

    private PayChannelInfoEntity paychannelinfo;
    private List<PayChannelEntity> payChannelList;
    private List<RelativeShopEntity> relativeShopList;

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public FactoryEntity getFactory() {
        return factory;
    }

    public void setFactory(FactoryEntity factory) {
        this.factory = factory;
    }

    public GoodinfoEntity getGoodinfo() {
        return goodinfo;
    }

    public void setGoodinfo(GoodinfoEntity goodinfo) {
        this.goodinfo = goodinfo;
    }

    public List<String> getGoodPics() {
        return goodPics;
    }

    public void setGoodPics(List<String> goodPics) {
        this.goodPics = goodPics;
    }

    public PayChannelInfoEntity getPaychannelinfo() {
        return paychannelinfo;
    }

    public void setPaychannelinfo(PayChannelInfoEntity paychannelinfo) {
        this.paychannelinfo = paychannelinfo;
    }

    public List<PayChannelEntity> getPayChannelList() {
        return payChannelList;
    }

    public void setPayChannelList(List<PayChannelEntity> payChannelList) {
        this.payChannelList = payChannelList;
    }

    public List<RelativeShopEntity> getRelativeShopList() {
        return relativeShopList;
    }

    public void setRelativeShopList(List<RelativeShopEntity> relativeShopList) {
        this.relativeShopList = relativeShopList;
    }
}
