package com.posagent.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/**
 * 新增/编辑用户
 */
public class UserForm extends BaseActivity implements View.OnClickListener
{

    private TextView tv_username;
    private EditText et_terminals;
    private LinearLayout ll_choose_users;
    private Button btn_bind_terminal;

    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_user_bind);
        new TitleMenuUtil(UserForm.this, "为用户绑定").show();

        initView();
    }

    private void initView() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setOnClickListener(this);
        et_terminals = (EditText) findViewById(R.id.et_terminals);
        et_terminals.setOnClickListener(this);
        ll_choose_users = (LinearLayout) findViewById(R.id.ll_choose_users);
        ll_choose_users.setOnClickListener(this);
        btn_bind_terminal = (Button) findViewById(R.id.btn_bind_terminal);
        btn_bind_terminal.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.ll_choose_users:
                Intent i = new Intent(UserForm.this, UserList.class);
                i.putExtra("forSelect", true);
                startActivity(i);
                break;
            case R.id.btn_bind_terminal:
                doBind();
                break;
            default:
                break;
        }


        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    private void doBind() {
        JsonParams params = new JsonParams();

        params.put("userId", userId);
        params.put("terminalsNum", et_terminals.getText().toString());
        String strParams = params.toString();
        Events.TerminalBindEvent event = new Events.TerminalBindEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.TerminalBindCompleteEvent event) {
        toast(event.getMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                String username = bundle.getString("username");
                tv_username.setText(username);
                userId = bundle.getInt("userId", 0);
                break;
        }
    }

}
