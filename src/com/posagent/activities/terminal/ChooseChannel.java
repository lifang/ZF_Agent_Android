package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.BillingEntity;
import com.example.zf_android.entity.ChannelEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class ChooseChannel extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerChannel, spinnerBilling;

    private List<ChannelEntity> myList;
    private List<BillingEntity> billings;

    private Map<String, List<BillingEntity>> mapBillings;

    private int channelId, billingId;
    private String channelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_channel);
        new TitleMenuUtil(this, "选择支付通道").show();

        spinnerChannel = (Spinner) findViewById(R.id.spinner_channel);
        spinnerBilling = (Spinner) findViewById(R.id.spinner_billing);

        mapBillings = new HashMap<String, List<BillingEntity>>();

        ((Button)findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra("channel_id", channelId);
                i.putExtra("billing_id", billingId);
                i.putExtra("channel_name", channelName);
                setResult(RESULT_OK, i);
                finish();
            }
        });


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
        myList = event.getList();

        for (ChannelEntity channel : myList) {
            List<String> arr2 = new ArrayList<String>();
            billings = new ArrayList<BillingEntity>();
            String[] arrStrings2 = new String[channel.getBillings().size()];
            for (BillingEntity billing: channel.getBillings()) {
                if (null != billing) {
                    billings.add(billing);
                }
            }
            mapBillings.put(channel.getName(), billings);

        }


        ArrayAdapter<ChannelEntity> adapter = new ArrayAdapter<ChannelEntity>(this,  android.R.layout.simple_spinner_item, myList);
        spinnerChannel.setAdapter(adapter);
        spinnerChannel.setOnItemSelectedListener(this);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_channel) {
            ChannelEntity channel = myList.get(position);
            channelId = channel.getId();
            channelName = channel.getName();
            billings = mapBillings.get(channel.getName());
            ArrayAdapter<BillingEntity> adapter = new ArrayAdapter<BillingEntity>(this,  android.R.layout.simple_spinner_item, billings);
            spinnerBilling.setAdapter(adapter);
            spinnerBilling.setOnItemSelectedListener(this);
        } else {
            BillingEntity billing = billings.get(position);
            billingId = billing.getId();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
