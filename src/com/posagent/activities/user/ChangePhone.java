package com.posagent.activities.user;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.MyApplication;
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

    ChangePhone _this = this;

    private EditText et_new_phone, et_verify_code;
    private TextView tv_old_phone;
    private Button btn_get_verify_code, btn_submit;


    private boolean sendingVerifyCode = false;

    private String oldPhone;
    static final int DOWN_COUNTER = 60;

    private int counter = DOWN_COUNTER;

    private Timer downCountTimer;


    public int getCounter() {
        return counter;
    }

    public int counterDown() {
        this.counter = --counter;
        return this.counter;
    }

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
        counter = DOWN_COUNTER;
        sendingVerifyCode = true;
        btn_get_verify_code.setText("等待(" + this.getCounter() + ")");
        btn_get_verify_code.setEnabled(false);
        btn_get_verify_code.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        downCountTimer = new Timer();
        downCountTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                _this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = _this.counterDown();
                        if (i < 1) {
                            downCountTimer.cancel();
                            btn_get_verify_code.getBackground().setColorFilter(null);
                        }

                        btn_get_verify_code.setText("等待(" + i + ")");
                    }
                });
            }
        },0,1000);

        Timer timer = new Timer();
        VerifyCodeReableTimerTask myTimerTask = new VerifyCodeReableTimerTask();
        timer.schedule(myTimerTask, DOWN_COUNTER * 1000);

        //do get verify code
        JsonParams params = new JsonParams();
        params.put("customerId",  MyApplication.user().getId());
        params.put("phone", tv_old_phone.getText().toString());
        String strParams = params.toString();
        Events.UserVerifyCodeEvent event = new Events.UserVerifyCodeEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    private boolean check() {
        if (et_new_phone.getText().toString().length() < 11) {
            toast("请输入有效手机号码");
            return false;
        }
        if (et_verify_code.getText().toString().length() < 3) {
            toast("请输入有效验证码");
            return false;
        }

        return true;
    }

    private void doSubmit() {
        if(check()) {
            JsonParams params = new JsonParams();

            params.put("customerId",  MyApplication.user().getId());
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
                    btn_get_verify_code.setEnabled(true);
                }
            });
        }

    }

}
