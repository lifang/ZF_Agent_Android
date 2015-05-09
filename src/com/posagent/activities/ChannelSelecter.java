package com.posagent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.BillingEntity;
import com.example.zf_android.entity.ChannelEntity;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.widget.WheelView;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Leo on 2015/2/11.
 */
public class ChannelSelecter extends BaseActivity {

    private List<ChannelEntity> mChannels;
    private List<BillingEntity> mBills;
    private static final int OFFSET = 5;

    private ChannelEntity mChannel;
    private BillingEntity mBill;

    private int channelId;
    private int billId;
    private String channelName;
    private String billName;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        channelId = getIntent().getIntExtra("channelId", 0);
        billId = getIntent().getIntExtra("billId", 0);

        LinearLayout contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout titleBack = (RelativeLayout) getLayoutInflater().inflate(R.layout.title_back, null);
        TextView confirm = (TextView) titleBack.findViewById(R.id.next_sure);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("channelId", mChannel.getId());
                intent.putExtra("billId", mBill.getId());
                intent.putExtra("channelName", mChannel.getName());
                intent.putExtra("billName", mBill.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        contentView.addView(titleBack, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(this, 50)));

        LinearLayout contentWheels = new LinearLayout(this);
        contentWheels.setOrientation(LinearLayout.HORIZONTAL);
        contentView.addView(contentWheels, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                screenWidth / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        final WheelView provinceWheel = new WheelView(this);
        final WheelView cityWheel = new WheelView(this);
        contentWheels.addView(provinceWheel, lp);
        contentWheels.addView(cityWheel, lp);
        setContentView(contentView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        new TitleMenuUtil(this, "选择支付通道").show();

        provinceWheel.setOffset(OFFSET);
        cityWheel.setOffset(OFFSET);

        provinceWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, Object item) {
                ChannelEntity channel = mChannels.get(selectedIndex - OFFSET);
                mBills = channel.getBillings();
                mBill = mBills.get(0);
                channelId = channel.getId();
                channelName = channel.getName();
                cityWheel.setItems(mBills);
                cityWheel.invalidate();
            }
        });
        cityWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, Object item) {
                mBill = mBills.get(selectedIndex - OFFSET);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (mChannels.size() > 0) {
                    mChannel = mChannels.get(0);
                }

                provinceWheel.setItems(mChannels);
                if (null != mChannel && null != mBill) {
                    provinceWheel.setSeletion(mChannels.indexOf(0));
                    mBills = mChannel.getBillings();
                    cityWheel.setItems(mBills);
                    if (mBills.size() > 0) {
                        cityWheel.setSeletion(mBills.indexOf(0));
                    }
                } else {
                    provinceWheel.setSeletion(mChannels.indexOf(0));
                    mBills = mChannel.getBillings();
                    cityWheel.setItems(mBills);
                    if (mBills.size() > 0) {
                        cityWheel.setSeletion(mBills.indexOf(0));
                    }
                }

            }
        };

        getData();


    }

    private void getData() {
        JsonParams params = new JsonParams();
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ApplyChannelListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.ApplyChannelListCompleteEvent event) {
        mChannels = event.getList();
        Message message = new Message();
        handler.sendMessage(message);
    }
}
