package com.posagent.activities.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/**
 * 修改邮箱
 */
public class ChangeEmail extends BaseActivity
{

    private EditText et_new_email;
    private TextView tv_email;
    private Button btn_submit;

    private String oldEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        new TitleMenuUtil(ChangeEmail.this, "修改邮箱").show();

        oldEmail = getIntent().getStringExtra("email");

        initView();
    }

    private void initView() {
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_email.setText(oldEmail);

        et_new_email = (EditText) findViewById(R.id.et_new_email);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

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

            //Fixme
            params.put("customerId", 40);
            params.put("email", et_new_email.getText().toString());

            String strParams = params.toString();
            Events.ChangeEmailEvent event = new Events.ChangeEmailEvent();
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.ChangeEmailCompleteEvent event) {
        if (event.success()) {
            EventBus.getDefault().post(new Events.UserInfoReloadEvent());
            finish();
        }
        toast(event.getMessage());
    }


}
