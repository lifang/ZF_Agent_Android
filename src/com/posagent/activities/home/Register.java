package com.posagent.activities.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.CityProvinceActivity;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_PROVINCE;

public class Register extends BaseActivity {

    Register _this = this;


    private Button btn_get_verify_code;
    private TextView tv_phone;
    private CheckBox cb_kind_person, cb_kind_company, cb_agreement;

    private String mVerifyCode;
    private String cityName;
    private int cityId;

    private Province mMerchantProvince;
    private City mMerchantCity;

    private boolean sendingVerifyCode = false;

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
        setContentView(R.layout.activity_register);
        new TitleMenuUtil(Register.this, "申请成为合作伙伴").show();
        initView();
    }

    private void initView() {

        btn_get_verify_code = (Button)findViewById(R.id.btn_get_verify_code);
        btn_get_verify_code.setOnClickListener(this);
        tv_phone = (TextView)findViewById(R.id.tv_phone);

        cb_kind_person = (CheckBox)findViewById(R.id.cb_kind_person);
        cb_kind_person.setOnClickListener(this);

        cb_kind_company = (CheckBox)findViewById(R.id.cb_kind_company);
        cb_kind_company.setOnClickListener(this);

        cb_agreement = (CheckBox)findViewById(R.id.cb_agreement);
        cb_agreement.setOnClickListener(this);

        TextView tv_xieyi = (TextView)findViewById(R.id.tv_xieyi);
        tv_xieyi.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.ll_choose_city).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.tv_400).setOnClickListener(this);
        findViewById(R.id.tv_xieyi).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            case R.id.tv_400:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000090876")));
                break;
            case R.id.tv_xieyi:
                startActivity(new Intent(context, RegisterXieyi.class));
                break;
            case R.id.cb_kind_company:
                if (cb_kind_company.isChecked()) {
                    cb_kind_person.setChecked(false);
                } else {
                    cb_kind_person.setChecked(true);
                }
                break;
            case R.id.cb_kind_person:
                if (cb_kind_person.isChecked()) {
                    cb_kind_company.setChecked(false);
                } else {
                    cb_kind_company.setChecked(true);
                }
                break;
            case R.id.ll_choose_city:
                Intent intent = new Intent(context, CityProvinceActivity.class);

                intent.putExtra(SELECTED_PROVINCE, mMerchantProvince);
                intent.putExtra(SELECTED_CITY, mMerchantCity);

                startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CITY_CODE);
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:
                mMerchantProvince = (Province) data.getSerializableExtra(SELECTED_PROVINCE);
                mMerchantCity = (City) data.getSerializableExtra(SELECTED_CITY);

                setText("tv_city_name", mMerchantCity.getName());

                break;
            default:

        }
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
        params.put("codeNumber", tv_phone.getText().toString());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.VerifyCodeEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    private boolean check() {
        if (getText("tv_name").length() < 1) {
            toast("请输入姓名");
            return false;
        }
        if (tv_phone.getText().toString().length() < 11) {
            toast("请输入有效手机号码");
            return false;
        }
        if (!getText("tv_verify_code").equals(mVerifyCode)) {
            toast("手机验证码错误");
            return false;
        }
        if (null == mMerchantProvince) {
            toast("请选择所在城市");
            return false;
        }
        if (!cb_agreement.isChecked()) {
            toast("您需要同意使用协议");
            return false;
        }

        return true;
    }

    private void doSubmit() {
        if(!check()) {
            return;
        }

        JsonParams params = new JsonParams();
        params.put("name", getValue("tv_name"));
        params.put("phone", getValue("tv_phone"));
        String agentType = "个人";
        if (cb_kind_company.isChecked()) {
            agentType = "公司";
        }
        params.put("agentType", agentType);

        String address = mMerchantProvince.getId() + "_" + mMerchantCity.getId();
        params.put("address", address);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.RegisterEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }



    // evnets

    // 用户注册完成
    public void onEventMainThread(Events.RegisterCompleteEvent event) {
        toast(event.getMessage());

        if (event.getSuccess()) {
            // goto register success
            Intent i = new Intent(context, RegisterSuccess.class);
            startActivity(i);
        }
    }

    public void onEventMainThread(Events.VerifyCodeCompleteEvent event) {
        if (event.success()) {
            toast("发送完成，请查收短信");
            mVerifyCode = "" + event.getIntResult();
        } else {
            toast(event.getMessage());
            resetSendVerifyCode();
        }
    }

    private void resetSendVerifyCode() {
        downCountTimer.cancel();
        btn_get_verify_code.getBackground().setColorFilter(null);

        sendingVerifyCode = false;
        btn_get_verify_code.setText("获取验证码");
        btn_get_verify_code.setEnabled(true);
    }

    class VerifyCodeReableTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    resetSendVerifyCode();
                }
            });
        }

    }
}