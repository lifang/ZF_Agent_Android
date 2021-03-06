package com.posagent.activities.agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.ChannelEntity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通道列表
 */
public class ChannelList extends BaseListActivity {

    private String selectAgentName;
    private List<ChannelEntity> myList = new ArrayList<ChannelEntity>();

    private String filtedIds;
    private List<String> listIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择支付通道").show();

        filtedIds = getIntent().getStringExtra("filtedIds");

        if (null != filtedIds) {
            listIds = new ArrayList<String>(Arrays.asList(filtedIds.split(",")));
        }

        getData();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ChannelEntity channel = myList.get(position);

        Intent intent = getIntent();
        intent.putExtra("id", channel.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {

        for(ChannelEntity channel: ((MyApplication)getApplication()).getChannels()) {
            if (!listIds.contains("" + channel.getId())) {
               myList.add(channel);
            }
        }

        reloadList();
    }


    // events
    public void reloadList() {
        for (ChannelEntity channel : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String agentName = channel.getName();
            item.put("name", agentName);
//            item.put("selected", TextUtils.isEmpty(agentName)
//                    || !agentName.equals(selectAgentName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }



}
