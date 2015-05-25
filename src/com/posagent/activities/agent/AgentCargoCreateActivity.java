package com.posagent.activities.agent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
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
public class AgentCargoCreateActivity extends BaseActivity {

    private int sonAgentId;

    final static int REQUEST_TRADE_AGENT = 101;
    private String tradeAgentName;

    private List<String> selectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_cargo_create);
		new TitleMenuUtil(AgentCargoCreateActivity.this, "配货").show();

        initView();
	}

    private void initView() {
        findViewById(R.id.ll_choose_agent).setOnClickListener(this);
        findViewById(R.id.ll_choose_terminal).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        if (v.getId() == R.id.ll_choose_agent) {
            Intent iAgent = new Intent(AgentCargoCreateActivity.this, AgentListActivity.class);
            iAgent.putExtra(AGENT_NAME, tradeAgentName);
            startActivityForResult(iAgent, REQUEST_TRADE_AGENT);
            return;
        }
        if (v.getId() == R.id.ll_choose_terminal) {
            Intent i = new Intent(context, TerminalChoose.class);
            i.putExtra("agentId", MyApplication.user().getAgentId());
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
            case REQUEST_TRADE_AGENT:
                String agentName = data.getStringExtra(AGENT_NAME);
                int agentId = data.getIntExtra(AGENT_ID, 0);
                tradeAgentName = agentName;
                setText("trade_agent_name", agentName);
                sonAgentId = agentId;
                break;
        }
    }

    private void doSubmit() {

        if (!check()) {
            return;
        }

        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("customerId",  MyApplication.user().getId());

        //Fixme
        params.put("sonAgentId", sonAgentId);
        params.put("paychannelId", 0);
        params.put("goodId", 0);
        params.put("serialNums", selectedList);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.PrepareAddEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private boolean check() {
        if (sonAgentId == 0) {
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
    public void onEventMainThread(Events.PrepareAddCompleteEvent event) {
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.TerminalChooseFinishEvent event) {
        selectedList = event.getList();
        setText("tv_terminals", StringUtil.join(selectedList, ","));
    }

	 
}
