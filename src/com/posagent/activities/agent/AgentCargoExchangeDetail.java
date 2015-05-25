package com.posagent.activities.agent;


import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.ExchangeEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoExchangeDetail extends BaseActivity {


    private int id;
    private ExchangeEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_cargo_exchange_detail);
		new TitleMenuUtil(AgentCargoExchangeDetail.this, "调货详情").show();

        id = getIntent().getIntExtra("id", 0);

        initView();

	}

    private void initView() {
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("id", id);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ExchangeInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.ExchangeInfoCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

	@Override
	public void onClick(View v) {

        super.onClick(v);
	}

    private void updateView() {
        setText("tv_company_from", entity.getFromname());
        setText("tv_company_to", entity.getToname());
        setText("tv_count", "总计" + entity.getQuantity() + "件");
        setText("tv_create_time", entity.getCreated_at());
        setText("tv_creator", entity.getCreator());
        setText("tv_terminals", entity.getTerminals_list());
    }

	 
}
