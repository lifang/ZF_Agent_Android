package com.posagent.activities.agent;


import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.PrepareEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoDetail extends BaseActivity {


    private int id;
    private PrepareEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_cargo_detail);
		new TitleMenuUtil(AgentCargoDetail.this, "配货详情").show();

        id = getIntent().getIntExtra("id", 0);

        initView();

	}

    private void initView() {
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("id", id);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.PrepareInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.PrepareInfoCompleteEvent event) {
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
        setText("tv_company_name", entity.getCompany_name());
        setText("tv_count", "总计" + entity.getQuantity() + "件");
        setText("tv_create_time", entity.getCreated_at());
        setText("tv_creator", entity.getCreator());
        setText("tv_terminals", entity.getTerminal_list());
    }

	 
}
