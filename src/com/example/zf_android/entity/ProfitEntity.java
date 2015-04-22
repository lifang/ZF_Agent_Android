package com.example.zf_android.entity;

import java.util.List;

/**
 * 
*    
*
* @version    
*
 */
public class ProfitEntity extends BaseEntity {

    private String id;
    private String channelName;

    private List<ProfitTradeEntity> detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<ProfitTradeEntity> getDetail() {
        return detail;
    }

    public void setDetail(List<ProfitTradeEntity> detail) {
        this.detail = detail;
    }

    private ChannelEntity channel;

    public ChannelEntity getChannel() {
        return channel;
    }

    public void setChannel(ChannelEntity channel) {
        this.channel = channel;
    }
}