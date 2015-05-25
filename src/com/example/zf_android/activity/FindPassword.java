package com.example.zf_android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

public class FindPassword extends BaseActivity   implements OnClickListener{
    private TextView tv_code,tv_msg,tv_check;
    private EditText login_edit_email,login_edit_code,login_edit_pass,login_edit_pass2;
    private LinearLayout login_linear_deletemali,login_linear_deletcode,login_linear_deletpass
            ,login_linear_deletpass2,login_linear_signin;
    private int Countmun=120;
    private Thread myThread;
    private Boolean isRun=true;
    private ImageView img_check,img_check_n;
    public  String vcode="";
    private String url,email,pass;
    private Runnable runnable;
    final Handler handler = new Handler(){          // handle  
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    if(Countmun==0){

                        isRun=false;
                        tv_code.setClickable(true);

                        tv_code.setText("重发验证码");
                    }else{
                        Countmun--;
                        tv_code.setText(Countmun + "秒");
                    }

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpass);
        new TitleMenuUtil(FindPassword.this, "找回密码").show();
        initView();
        url=Config.FINDPASS;
        runnable = new Runnable() {
            @Override
            public void run() {
                if(Countmun==0){

                    Countmun=120;
                    tv_code.setClickable(true);
                    tv_code.setText("重发验证码");
                }else{

                    Countmun--;
                    tv_code.setText( Countmun+"秒后重新获取");

                    handler.postDelayed(this, 1000);
                }

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // myThread.stop();
        //myThread.destroy();
    }
    @Override
    public void onClick(View v) {
        switch ( v.getId()) {
            case R.id.tv_code:
                tv_code.setClickable(false);
                tv_code.setText("120=");
                getCode();
                break;
            case R.id.tv_check:

                if(login_edit_code.getText().toString().equals(vcode)){
                    img_check.setVisibility(View.VISIBLE);
                    img_check_n.setVisibility(View.GONE);
                }else{
                    img_check.setVisibility(View.GONE);
                    img_check_n.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.login_linear_signin:

                if(check()){
                    sure();
                }

                break;
            case R.id.login_linear_deletemali:
                login_edit_email.setText("");
                break;
            case R.id.login_linear_deletcode:
                login_edit_code.setText("");
                break;
            case R.id.login_linear_deletpass:
                login_edit_pass.setText("");
                break;
            case R.id.login_linear_deletpass2:
                login_edit_pass2.setText("");
                break;
            default :
                break;
        }
    }
    private boolean check() {
        email=StringUtil.replaceBlank(login_edit_email.getText().toString());
        if(email.length()==0){
            Toast.makeText(getApplicationContext(), "Email地址不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(StringUtil.replaceBlank(login_edit_code.getText().toString()).length()==0){
            Toast.makeText(getApplicationContext(), "验证码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!login_edit_code.getText().toString().endsWith(vcode)){
            Toast.makeText(getApplicationContext(), "验证码错误",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        pass=StringUtil.replaceBlank(login_edit_pass.getText().toString());
        if(pass.length()==0){
            Toast.makeText(getApplicationContext(), "密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!login_edit_pass2.getText().toString().equals(pass)){
            Toast.makeText(getApplicationContext(), "两次密码不一致",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //	pass=StringUtil.Md5(pass);
        return true;
    }

    private void sure() {

        JsonParams params = new JsonParams();

        params.put("username", login_edit_email.getText().toString());

        pass=StringUtil.replaceBlank(login_edit_pass.getText().toString());
        pass=StringUtil.Md5(pass);
        params.put("password", pass);

        params.put("code", login_edit_code.getText().toString());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.UpdatePasswordEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    // events
    public void onEventMainThread(Events.SendPhoneVerificationCodeCompleteEvent event) {
        toast(event.getMessage());
    }


    public void onEventMainThread(Events.UpdatePasswordCompleteEvent event) {
        if (event.success()) {
            Intent i = new Intent(FindPassword.this, FindPasswordDone.class);
            startActivity(i);
        }
        toast(event.getMessage());

    }

    /**
     *
     */
    private void getCode() {
        handler.postDelayed(runnable, 1000);

        JsonParams params = new JsonParams();
        params.put("codeNumber", login_edit_email.getText().toString());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SendPhoneVerificationCodeEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }
    private void initView() {
        tv_check=(TextView) findViewById(R.id.tv_check);
        tv_check.setOnClickListener(this);
        img_check=(ImageView) findViewById(R.id.img_check);
        img_check_n=(ImageView) findViewById(R.id.img_check_n);
        tv_code=(TextView) findViewById(R.id.tv_code);
        tv_code.setOnClickListener(this);

        tv_msg=(TextView) findViewById(R.id.tv_msg);

        login_linear_signin=(LinearLayout) findViewById(R.id.login_linear_signin);
        login_linear_signin.setOnClickListener(this);
        login_linear_deletemali=(LinearLayout) findViewById(R.id.login_linear_deletemali);
        login_linear_deletemali.setOnClickListener(this);
        login_edit_email=(EditText) findViewById(R.id.login_edit_email);
        String s=getIntent().getStringExtra("phone");
        login_edit_email.setText(s);
        login_edit_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                    login_linear_deletemali.setVisibility(View.VISIBLE);
                } else {
                    login_linear_deletemali.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //	login_linear_deletpass
        login_linear_deletcode=(LinearLayout) findViewById(R.id.login_linear_deletcode);
        login_linear_deletcode.setOnClickListener(this);
        login_edit_code=(EditText) findViewById(R.id.login_edit_code);
        login_edit_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                    login_linear_deletcode.setVisibility(View.VISIBLE);
                } else {
                    login_linear_deletcode.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //	login_linear_deletpass
        login_linear_deletpass=(LinearLayout) findViewById(R.id.login_linear_deletpass);
        login_linear_deletpass.setOnClickListener(this);
        login_edit_pass=(EditText) findViewById(R.id.login_edit_pass);
        login_edit_pass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                    login_linear_deletpass.setVisibility(View.VISIBLE);
                } else {
                    login_linear_deletpass.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        login_linear_deletpass2=(LinearLayout) findViewById(R.id.login_linear_deletpass2);
        login_linear_deletpass2.setOnClickListener(this);
        login_edit_pass2=(EditText) findViewById(R.id.login_edit_pass2);
        login_edit_pass2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    login_linear_deletpass2.setVisibility(View.VISIBLE);
                } else {
                    login_linear_deletpass2.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public class MyThread implements Runnable{      // thread
        @Override
        public void run(){

            while(isRun){
                try{
                    Thread.sleep(1000);     // sleep 1000ms
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }catch (Exception e) {

                }
            }
        }
    }

}
