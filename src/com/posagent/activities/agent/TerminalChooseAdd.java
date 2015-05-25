package com.posagent.activities.agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;
import com.posagent.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Intent i = getIntent();
        List<String> arr = new ArrayList<String>(Arrays.asList(terminals.split("\n")));
        i.putExtra(Constants.DefaultSelectedNameKey, StringUtil.join(arr, ","));
        setResult(RESULT_OK, i);
        finish();
    }

}
