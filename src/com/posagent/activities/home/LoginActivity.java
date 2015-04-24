package com.posagent.activities.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.activity.FindPass;
import com.example.zf_android.entity.UserInfoEntity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;


/***
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private String name,pass,url,deviceToken;
    private ImageView loginImage;
    private CheckBox isremeber_cb;
    private Boolean isRemeber = true;
    private TextView login_text_forget, login_info;
    private EditText login_edit_name, login_edit_pass;
    private LinearLayout login_linear_deletename, login_linear_deletepass,zhuche_ll,
            login_linear_login, msg;
    private String sign, pass1, username, password;
    private SharedPreferences mySharedPreferences;
    private Editor editor;
    private Boolean isFirst;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
        new TitleMenuUtil(LoginActivity.this, "登录").show();

        findViewById(R.id.titleback_image_back).setVisibility(View.GONE);
    }

    private void initView() {
        mySharedPreferences = getSharedPreferences(Config.SHARED, MODE_PRIVATE);
        editor = mySharedPreferences.edit();

        login_text_forget = (TextView) findViewById(R.id.login_text_forget);
        //login_text_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        login_text_forget.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        FindPass.class);
                startActivity(i);
            }
        });
        msg = (LinearLayout) findViewById(R.id.msg);
        login_info = (TextView) findViewById(R.id.login_info);

        zhuche_ll= (LinearLayout) findViewById(R.id.zhuche_ll);
        zhuche_ll.setOnClickListener(this);

        login_edit_name = (EditText) findViewById(R.id.login_edit_name);
        login_edit_pass = (EditText) findViewById(R.id.login_edit_pass);

        login_linear_deletename = (LinearLayout) findViewById(R.id.login_linear_deletename);
        login_linear_deletepass = (LinearLayout) findViewById(R.id.login_linear_deletepass);
        login_linear_deletepass.setOnClickListener(this);
        login_linear_deletename.setOnClickListener(this);
        login_edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                msg.setVisibility(View.INVISIBLE);
                if (s.length() > 0) {
                    login_linear_deletename.setVisibility(View.VISIBLE);
                } else {
                    login_linear_deletename.setVisibility(View.GONE);
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
        login_edit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                msg.setVisibility(View.INVISIBLE);
                if (s.length() > 0)
                    login_linear_deletepass.setVisibility(View.VISIBLE);
                else
                    login_linear_deletepass.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        login_linear_login = (LinearLayout) findViewById(R.id.login_linear_login);
        login_linear_login.setOnClickListener(this);
        isFirst = mySharedPreferences.getBoolean("isRemeber", false);
        if (isFirst) {
            login_edit_pass.setText(mySharedPreferences.getString("password",
                    ""));
            login_edit_name.setText(mySharedPreferences.getString("username",
                    ""));
        }else{
            login_edit_name.setText(mySharedPreferences.getString("username",
                    ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_linear_login:
                if(check()){
                    login();
                }
                break;
            case R.id.zhuche_ll:
                startActivity(new Intent(LoginActivity.this, Register.class));
                break;
            case R.id.login_linear_deletename:
                login_edit_name.setText("");
                break;
            case R.id.login_linear_deletepass:
                login_edit_pass.setText("");
                break;
            default:
                break;
        }
    }

    // 用户登录完成
    public void onEventMainThread(Events.LoginCompleteEvent event) {

        if (event.getSuccess()) {
            UserInfoEntity userinfo = event.getEntity();
            //save user info
            MyApplication.setCurrentUser(userinfo);

            finish();
        } else {
            toast(event.getMessage());
        }
    }

    private void login() {
        JsonParams params = new JsonParams();
        params.put("username", username);
        params.put("password", password);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.DoLoginEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    private boolean check() {
        username = StringUtil.replaceBlank(login_edit_name.getText().toString());
        if(username.length()==0){
            Toast.makeText(getApplicationContext(), "请输入手机或邮箱",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        password = StringUtil.replaceBlank(login_edit_pass.getText().toString());
        if(password.length()==0){
            Toast.makeText(getApplicationContext(), "请输入密码",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        password = StringUtil.Md5(password);
        return true;
    }

}
