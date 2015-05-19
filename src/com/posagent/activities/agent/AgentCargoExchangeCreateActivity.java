package com.posagent.activities.agent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.trade.TradeAgentActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoExchangeCreateActivity extends BaseActivity {

    private int fromSonAgentId;
    private int toSonAgentId;

    final static int REQUEST_TRADE_AGENT_0 = 101;
    final static int REQUEST_TRADE_AGENT_1 = 102;

    private String toname;
    private String fromname;

    private List<String> selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agent_cargo_exchange_create);
        new TitleMenuUtil(AgentCargoExchangeCreateActivity.this, "调货").show();

        initView();
    }

    private void initView() {
        findViewById(R.id.ll_choose_from).setOnClickListener(this);
        findViewById(R.id.ll_choose_to).setOnClickListener(this);
        findViewById(R.id.ll_choose_terminal).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        if (v.getId() == R.id.ll_choose_from) {
            Intent iAgent = new Intent(AgentCargoExchangeCreateActivity.this, AgentListActivity.class);
            iAgent.putExtra(AGENT_NAME, fromname);
            startActivityForResult(iAgent, REQUEST_TRADE_AGENT_0);
            return;
        }
        if (v.getId() == R.id.ll_choose_to) {
            Intent iAgent = new Intent(AgentCargoExchangeCreateActivity.this, TradeAgentActivity.class);
            iAgent.putExtra(AGENT_NAME, toname);
            startActivityForResult(iAgent, REQUEST_TRADE_AGENT_1);
            return;
        }
        if (v.getId() == R.id.ll_choose_terminal) {
            if (fromSonAgentId < 1) {
                toast("请选择被调货代理商");
                return ;
            }

            Intent i = new Intent(AgentCargoExchangeCreateActivity.this, TerminalChoose.class);
            i.putExtra("agentId", fromSonAgentId);
            startActivity(i);
            return;
        }
        if (v.getId() == R.id.btn_submit) {
            doSubmit();
            return;
        }

        super.onClick(v);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_TRADE_AGENT_0:
                String agentName = data.getStringExtra(AGENT_NAME);
                int agentId = data.getIntExtra(AGENT_ID, 0);
                fromname = agentName;
                setText("tv_fromname", agentName);
                fromSonAgentId = agentId;
                break;
            case REQUEST_TRADE_AGENT_1:
                String agentName2 = data.getStringExtra(AGENT_NAME);
                int agentId2 = data.getIntExtra(AGENT_ID, 0);
                toname = agentName2;
                setText("tv_toname", agentName2);
                toSonAgentId = agentId2;
                break;
        }
    }

    private void doSubmit() {


        if (!check()) {
            return;
        }


        JsonParams params = new JsonParams();
        params.put("customerId", MyApplication.user().getId());

        params.put("toAgentId", toSonAgentId);
        params.put("fromAgentId", fromSonAgentId);
        params.put("serialNums", selectedList);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ExchangeAddEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    private boolean check() {

        if (fromSonAgentId == 0) {
            toast("请选择被调货代理商");
            return false;
        }

        if (toSonAgentId == 0) {
            toast("请选择调货代理商");
            return false;
        }
        
        if (null == selectedList || selectedList.size() < 1) {
            toast("请选择终端号");
            return false;
        }

        return true;
    }

    //Events
    public void onEventMainThread(Events.ExchangeAddCompleteEvent event) {
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.TerminalChooseFinishEvent event) {
        selectedList = event.getList();
        setText("tv_terminals", StringUtil.join(selectedList, ","));
    }



}
