package com.posagent.activities.agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/**
 * 设置默认分润
 */
public class AgentDefaultProfit extends BaseActivity
{

    private EditText et_profit;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profit);

        new TitleMenuUtil(AgentDefaultProfit.this, "默认分润比例").show();

        initView();
    }

    private void initView() {

        et_profit = (EditText) findViewById(R.id.et_profit);

        getData();


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }


    private void getData() {

    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
            default:
                break;
        }


        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }



    private boolean check() {
        return true;
    }

    private void doSubmit() {
        if(check()) {
            JsonParams params = new JsonParams();

            params.put("agentsId", MyApplication.user().getAgentId());

            params.put("defaultProfit", et_profit.getText().toString());

            String strParams = params.toString();

            Events.CommonRequestEvent event = new Events.ChangeProfitEvent();
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.ChangeProfitCompleteEvent event) {
        toast(event.getMessage());
        if (event.success()) {
            Intent i = getIntent();
            i.putExtra("value", et_profit.getText().toString());
            setResult(RESULT_OK, i);
            finish();
        }
    }


}
