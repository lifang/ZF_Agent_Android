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

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 修改手机
 */
public class ChangePhone extends BaseActivity
{

    private EditText et_new_phone, et_verify_code;
    private TextView tv_old_phone;
    private Button btn_get_verify_code, btn_submit;


    private boolean sendingVerifyCode = false;

    private String oldPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        new TitleMenuUtil(ChangePhone.this, "修改手机").show();

        oldPhone = getIntent().getStringExtra("phone");

        initView();
    }

    private void initView() {
        tv_old_phone = (TextView) findViewById(R.id.tv_old_phone);
        tv_old_phone.setText(oldPhone);

        et_verify_code = (EditText) findViewById(R.id.et_verify_code);
        et_new_phone = (EditText) findViewById(R.id.et_new_phone);

        btn_get_verify_code = (Button) findViewById(R.id.btn_get_verify_code);
        btn_get_verify_code.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            default:
                break;
        }


        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    private void getVerifyCode() {
        if (sendingVerifyCode) {
            return;
        }
        sendingVerifyCode = true;
        btn_get_verify_code.setText("正在发送...");
        Timer timer = new Timer();
        VerifyCodeReableTimerTask myTimerTask = new VerifyCodeReableTimerTask();
        timer.schedule(myTimerTask, 60 * 1000);

        //do get verify code
        JsonParams params = new JsonParams();
        //Fixme
        params.put("customerId", 40);
        params.put("phone", tv_old_phone.getText().toString());
        String strParams = params.toString();
        Events.UserVerifyCodeEvent event = new Events.UserVerifyCodeEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    private boolean check() {
        return true;
    }

    private void doSubmit() {
        if(check()) {
            JsonParams params = new JsonParams();

            //Fixme
            params.put("customerId", 40);
            params.put("phone", et_new_phone.getText().toString());
            params.put("dentcode", et_verify_code.getText().toString());

            String strParams = params.toString();
            Events.ChangePhoneEvent event = new Events.ChangePhoneEvent();
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.UserVerifyCodeCompleteEvent event) {
        if (event.success()) {
            toast("发送完成，请查收短信");
        }
    }

    public void onEventMainThread(Events.ChangePhoneCompleteEvent event) {
        if (event.success()) {
            EventBus.getDefault().post(new Events.UserInfoReloadEvent());
            finish();
        }
        toast(event.getMessage());
    }

    class VerifyCodeReableTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    sendingVerifyCode = false;
                    btn_get_verify_code.setText("获取验证码");
                }
            });
        }

    }

}
