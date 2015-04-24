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
import com.example.zf_android.trade.CitySelectActivity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 新增/编辑用户
 */
public class UserForm extends BaseActivity implements View.OnClickListener
{

    private TextView tv_city_name;
    private EditText et_username, et_mobile, et_verify_code,
            et_password, et_password_confirm;
    private LinearLayout ll_choose_city;
    private Button btn_get_verify_code, btn_create_user;

    private int cityId;
    private String cityName;

    private boolean sendingVerifyCode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        new TitleMenuUtil(UserForm.this, "创建用户").show();

        initView();
    }

    private void initView() {
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
        et_username = (EditText) findViewById(R.id.et_username);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_verify_code = (EditText) findViewById(R.id.et_verify_code);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        et_username = (EditText) findViewById(R.id.et_username);

        ll_choose_city = (LinearLayout) findViewById(R.id.ll_choose_city);
        ll_choose_city.setOnClickListener(this);

        btn_get_verify_code = (Button) findViewById(R.id.btn_get_verify_code);
        btn_get_verify_code.setOnClickListener(this);
        btn_create_user = (Button) findViewById(R.id.btn_create_user);
        btn_create_user.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.ll_choose_city:
                Intent intent = new Intent(UserForm.this, CitySelectActivity.class);

                if (cityName != null) {
                    intent.putExtra(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME, cityName);
                }
                startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CITY_CODE);

                break;
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            case R.id.btn_create_user:
                createUser();
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
        params.put("codeNumber", et_mobile.getText().toString());
        String strParams = params.toString();
        Events.VerifyCodeEvent event = new Events.VerifyCodeEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    private boolean check() {
        if (!et_password.getText().toString().equals(et_password_confirm.getText().toString())) {
            toast("两次密码不匹配");
            return false;
        }
        return true;
    }

    private void createUser() {
        if(check()) {
            JsonParams params = new JsonParams();

            params.put("agentId",  MyApplication.user().getAgentId());
            params.put("codeNumber", et_mobile.getText().toString());
            params.put("name", et_username.getText().toString());
            params.put("password", et_password.getText().toString());
            params.put("cityId", cityId);

            String strParams = params.toString();
            Events.CreateUserEvent event = new Events.CreateUserEvent();
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.TerminalBindCompleteEvent event) {
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.VerifyCodeCompleteEvent event) {
        if (event.success()) {
            toast("发送完成，请查收短信");
        }
    }

    public void onEventMainThread(Events.CreateUserCompleteEvent event) {
        if (event.success()) {
            EventBus.getDefault().post(new Events.UserListReloadEvent());
            finish();
        }
        toast(event.getMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:
                cityName = bundle.getString(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME);
                cityId = bundle.getInt(com.example.zf_android.trade.Constants.CityIntent.CITY_ID);
                tv_city_name.setText(cityName);
                break;
        }
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
