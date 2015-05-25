package com.posagent.activities.agent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.terminal.TerminalChooseChannelList;
import com.posagent.activities.terminal.TerminalChooseList;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
 * 选择终端
 *
 */
public class TerminalChoose extends BaseActivity {

    private LinearLayout ll_enter_terminal, ll_choose_pos, ll_choose_channel;
    private int agentId;
    private int channelId;
    private int goodId;
    private String serialNums;

    private String posName;
    private String channelName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_agent);
        new TitleMenuUtil(TerminalChoose.this, "选择终端").show();
        agentId = getIntent().getIntExtra("agentId", 0);

        initView();

    }

    private void initView() {
        ll_enter_terminal = (LinearLayout) findViewById(R.id.ll_enter_terminal);
        ll_enter_terminal.setOnClickListener(this);

        ll_choose_pos = (LinearLayout) findViewById(R.id.ll_choose_pos);
        ll_choose_pos.setOnClickListener(this);

        ll_choose_channel = (LinearLayout) findViewById(R.id.ll_choose_channel);
        ll_choose_channel.setOnClickListener(this);

        findViewById(R.id.btn_submit).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_enter_terminal:
                Intent i = new Intent(TerminalChoose.this, com.posagent.activities.agent.TerminalChooseAdd.class);
                i.putExtra("serialNums", serialNums);
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.ll_choose_pos:
                Intent i3 = new Intent(context, TerminalChoosePos.class);
                if (agentId > 0) {
                    i3.putExtra("agentId", agentId);
                }
                i3.putExtra(Constants.DefaultSelectedNameKey, posName);
                startActivityForResult(i3, Constants.REQUEST_CODE3);
                break;
            case R.id.ll_choose_channel:
                Intent i2 = new Intent(context, TerminalChooseChannelList.class);
                if (agentId > 0) {
                    i2.putExtra("agentId", agentId);
                }
                i2.putExtra(Constants.DefaultSelectedNameKey, channelName);
                startActivityForResult(i2, Constants.REQUEST_CODE2);
                break;

        }

        super.onClick(v);
    }

    private boolean check() {
        if (goodId < 1) {
            toast("请选择POS机");
            return false;
        }
        if (channelId < 1) {
            toast("请选择支付通道");
            return false;
        }

        return true;
    }

    private void doSubmit() {
        if (!check()) {
            return;
        }


        JsonParams params = new JsonParams();

        params.put("agentId", agentId);
        params.put("goodId", goodId);
        params.put("paychannelId", channelId);
        if (null != serialNums) {
            params.put("serialNums", serialNums.split(","));
        }
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SearchAgentTerminalListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case Constants.REQUEST_CODE3:
                posName = data.getStringExtra(Constants.DefaultSelectedNameKey);
                goodId = data.getIntExtra(Constants.DefaultSelectedIdKey, 0);
                setText("tv_pos", posName);
                break;
            case Constants.REQUEST_CODE2:
                channelName = data.getStringExtra(Constants.DefaultSelectedNameKey);
                channelId = data.getIntExtra(Constants.DefaultSelectedIdKey, 0);
                setText("tv_channel", channelName);
                break;
            case Constants.REQUEST_CODE:
                serialNums = data.getStringExtra(Constants.DefaultSelectedNameKey);
                setText("tv_terminal_number", serialNums);
                break;
        }


    }

    //events
    public void onEventMainThread(Events.SearchAgentTerminalListCompleteEvent event) {
        if (event.success()) {
            Intent i = new Intent(context, TerminalChooseList.class);
            i.putExtra("json", gson.toJson(event.getList()));
            startActivity(i);
        } else {
            toast(event.getMessage());
        }

    }
    public void onEventMainThread(Events.TerminalChooseFinishEvent event) {
        finish();
    }
}
