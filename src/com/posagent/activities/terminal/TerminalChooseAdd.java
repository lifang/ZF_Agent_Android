package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
 * 输入终端
 *
 */
public class TerminalChooseAdd extends BaseActivity {

    private Button btn_submit;
    private EditText et_terminals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_add);
        new TitleMenuUtil(TerminalChooseAdd.this, "输入终端").show();

        initView();

    }

    private void initView() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        et_terminals = (EditText) findViewById(R.id.et_terminals);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
        }

        super.onClick(v);
    }

    private void doSubmit() {
        String terminals = et_terminals.getText().toString().trim();
        if (terminals.length() == 0) {
            toast("请输入终端号");
            return;
        }

        //do submit
        JsonParams params = new JsonParams();

        params.put("agentId", MyApplication.user().getAgentId());
        params.put("serialNum", terminals.split("\n"));
        String strParams = params.toString();
        Events.BatchTerminalNumberEvent event = new Events.BatchTerminalNumberEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.BatchTerminalNumberCompleteEvent event) {
        if (event.success()) {
            Intent i = new Intent(TerminalChooseAdd.this, TerminalChooseList.class);
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
