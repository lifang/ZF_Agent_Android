package com.posagent.activities.agent;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.SonAgentInfo;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.ImageViewer;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/***
*
* 代理商详情
*
*/
public class AgentDetailActivity extends BaseActivity {

    TextView viewSetRate;
    private CheckBox cb_is_profit;

    private int sonAgentsId;
    private SonAgentInfo entity;
    private int profitStatus = 1;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_detail);
		new TitleMenuUtil(AgentDetailActivity.this, "代理商详情").show();

        sonAgentsId = getIntent().getIntExtra("id", 0);

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        initView();
	}

    private void initView() {
        //配置 设置分润 按钮
        viewSetRate = (TextView)findViewById(R.id.next_sure);
        viewSetRate.setText("设置分润");
        viewSetRate.setOnClickListener(this);

        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("sonAgentsId", sonAgentsId);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SonAgentInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.next_sure) {
            Intent i = new Intent(this, AgentProfitList.class);
            i.putExtra("id", sonAgentsId);
            startActivity(i);
            return;
        }

        super.onClick(v);
	}

    public void onEventMainThread(Events.SonAgentInfoCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    private void updateView() {
        boolean isCompany = entity.getTypes() == 1;
        if (isCompany) {
            showByTag("company_items");
        } else {
            hideByTag("company_items");
        }
        String kind = isCompany ? "代理商类型: 公司" : "代理商类型: 个人";
        setText("tv_types", kind);
        setText("tv_company_name", entity.getCompany_name());
        setText("tv_license", entity.getBusiness_license());
        setText("tv_tax_license", entity.getTax_registered_no());
        setText("tv_name", entity.getName());
        setText("tv_idcard", entity.getCard_id());
        setText("tv_phone", entity.getPhone());
        setText("tv_email", entity.getEmail());
        setText("tv_city_name", entity.getCityName());
        setText("tv_address", entity.getAddress());
        setText("tv_username", entity.getLoginId());

//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date(entity.getCreated_at()));


        setText("tv_create_time", "" + entity.getCreated_at());
        setText("tv_sold_count", "" + entity.getSoldnum());
        setText("tv_open_count", "" + entity.getOpennum());
        setText("tv_left_count", "" + (entity.getAllQty() - entity.getSoldnum()));

        cb_is_profit = (CheckBox) findViewById(R.id.cb_is_profit);
        cb_is_profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSetProfit();
            }
        });
        boolean is_profit = entity.getIs_have_profit().equals("2");
        cb_is_profit.setChecked(is_profit);
        if (is_profit) {
            viewSetRate.setVisibility(View.VISIBLE);
        }
        final SonAgentInfo info = entity;
        //photo
        findViewById(R.id.iv_idcard_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageViewer.class);
                i.putExtra("url", info.getCardpath());
                i.putExtra("justviewer", true);
                startActivity(i);
            }
        });

        findViewById(R.id.iv_license_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageViewer.class);
                i.putExtra("url", info.getLicensepath());
                i.putExtra("justviewer", true);
                startActivity(i);
            }
        });

        findViewById(R.id.iv_tax_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageViewer.class);
                i.putExtra("url", info.getTaxpath());
                i.putExtra("justviewer", true);
                startActivity(i);
            }
        });

    }

    private void toggleSetProfit() {
        if (cb_is_profit.isChecked()) {
            profitStatus = 2;
            viewSetRate.setVisibility(View.VISIBLE);
        } else {
            profitStatus = 1;
            viewSetRate.setVisibility(View.GONE);
        }
        saveProfitStatus();
    }

    private void saveProfitStatus() {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("sonAgentsId", sonAgentsId);
        params.put("isProfit", profitStatus);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SetIsProfitEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }
	 
}
