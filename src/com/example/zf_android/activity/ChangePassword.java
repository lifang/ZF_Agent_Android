package com.example.zf_android.activity;
 
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
 * 
*    
* 修改密码
*
 */
public class ChangePassword extends BaseActivity {

    private EditText et_password, et_new_password, et_new_password_confirm;
    private Button btn_submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		new TitleMenuUtil(ChangePassword.this, "修改密码").show();

        initView();
	}



    private void initView() {

        et_password = (EditText) findViewById(R.id.et_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_confirm = (EditText) findViewById(R.id.et_new_password_confirm);

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
        String pwd1 = et_new_password.getText().toString();
        String pwd2 = et_new_password_confirm.getText().toString();
        if (!pwd1.equals(pwd2)) {
            toast("两次密码不一致");
            return false;
        }
        return true;
    }

    private void doSubmit() {
        if(check()) {
            JsonParams params = new JsonParams();

            params.put("customerId", MyApplication.user().getId());
            params.put("passwordOld", StringUtil.Md5(et_password.getText().toString()));
            params.put("password", StringUtil.Md5(et_new_password.getText().toString()));

            String strParams = params.toString();
            Events.ChangePasswordEvent event = new Events.ChangePasswordEvent();
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.ChangePasswordCompleteEvent event) {
        if (event.success()) {
            EventBus.getDefault().post(new Events.UserInfoReloadEvent());
            finish();
        }
        toast(event.getMessage());
    }
	 
}
