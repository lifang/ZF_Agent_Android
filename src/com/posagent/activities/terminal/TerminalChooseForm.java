package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;

/***
 * 选择终端
 *
 */
public class TerminalChooseForm extends BaseActivity {

    private LinearLayout ll_enter_terminal, ll_filter_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_form);
        new TitleMenuUtil(TerminalChooseForm.this, "选择终端").show();

        initView();

    }

    private void initView() {
        ll_enter_terminal = (LinearLayout) findViewById(R.id.ll_enter_terminal);
        ll_enter_terminal.setOnClickListener(this);

        ll_filter_pos = (LinearLayout) findViewById(R.id.ll_filter_pos);
        ll_filter_pos.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_enter_terminal:
                Intent i = new Intent(TerminalChooseForm.this, TerminalChooseAdd.class);
                startActivity(i);
                break;
            case R.id.ll_filter_pos:
                Intent i2 = new Intent(TerminalChooseForm.this, TerminalChoosePos.class);
                startActivity(i2);
                break;

        }

        super.onClick(v);
    }

    //events
    public void onEventMainThread(Events.TerminalChooseFinishEvent event) {
        finish();
    }
}
