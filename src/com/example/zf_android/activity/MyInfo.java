package com.example.zf_android.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.UserInfo;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.home.LoginActivity;
import com.posagent.activities.user.AddressList;
import com.posagent.activities.user.ChangeEmail;
import com.posagent.activities.user.ChangePhone;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
*
* 我的信息
*
*/
public class MyInfo extends BaseActivity {
	
	private Button btn_exit;
	private LinearLayout ll_change_password, ll_address_manage, ll_email, ll_phone;
	private TextView tv_email, tv_phone;

    private TextView tvContent;


    private UserInfo entity;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo);
		new TitleMenuUtil(MyInfo.this, "我的信息").show();
		initView();

	}
	 

	private void initView() {
		btn_exit=(Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);

        ll_change_password = (LinearLayout)findViewById(R.id.ll_change_password);
        ll_address_manage = (LinearLayout)findViewById(R.id.ll_address_manage);
        ll_email = (LinearLayout)findViewById(R.id.ll_email);
        ll_phone = (LinearLayout)findViewById(R.id.ll_phone);
        ll_change_password.setOnClickListener(this);
        ll_address_manage.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        ll_phone.setOnClickListener(this);

        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_phone = (TextView)findViewById(R.id.tv_phone);

        getData();
		
	}


    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("customerId", 40);

        String strParams = params.toString();
        Events.UserInfoEvent event = new Events.UserInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.UserInfoCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateInfo();
        } else {
            toast(event.getMessage());
        }
    }

    public void onEventMainThread(Events.UserInfoReloadEvent event) {
        getData();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exit:
			exit();
			break;
		case  R.id.ll_email:
            tvContent = tv_email;

            Intent i2 = new Intent(MyInfo.this, ChangeEmail.class);
            i2.putExtra("email", tv_email.getText().toString());
            startActivity(i2);

            break;
		case  R.id.ll_phone:
            tvContent = tv_phone;

            Intent i = new Intent(MyInfo.this, ChangePhone.class);
            i.putExtra("phone", tv_phone.getText().toString());
            startActivity(i);

            break;
		case  R.id.ll_address_manage:
			startActivity(new Intent(MyInfo.this, AddressList.class));
			break;
		case  R.id.ll_change_password:
			startActivity(new Intent(MyInfo.this,ChangePassword.class));
			break;
		default:
			break;
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CODE:
                String content = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);
                tvContent.setText(content);
                Log.d(TAG, data.toString());
                break;
        }
    }

	private void exit() {
		startActivity(new Intent(MyInfo.this,LoginActivity.class));
	}

    private void updateInfo() {
        String kind = "公司";
        if (entity.getTypes() == Constants.UserConstant.USER_KIND_PESONAL) {
            kind = "个人";
            hide("company_content1");
            hide("company_content2");

        }
        setText("tv_kind", "代理商类型：" + kind);

        setText("tv_companyName", entity.getCompany_name());
        setText("tv_businessLicense", entity.getBusiness_license());
        setText("tv_taxRegisteredNo", entity.getTax_registered_no());
        setText("tv_name", entity.getName());
        setText("tv_idCard", entity.getCard_id());
        setText("tv_phone", entity.getPhone());
        setText("tv_email", entity.getEmail());
        setText("tv_city_name", "" + entity.getCity_id());
        setText("tv_address", entity.getAddress());
        setText("tv_username", entity.getUsername());


    }

	 
}
