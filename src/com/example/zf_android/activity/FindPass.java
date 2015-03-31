package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;

public class FindPass extends BaseActivity implements OnClickListener{
	//�������������ʼ�
	private EditText login_edit_name;
	private LinearLayout login_linear_deletename,login_linear_in;
	private TextView tv_msg;
	private Boolean isMail=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findpass);
		new TitleMenuUtil(FindPass.this,"�һ�����").show();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		tv_msg=(TextView) findViewById(R.id.tv_msg);
		login_edit_name=(EditText) findViewById(R.id.login_edit_name);
		login_linear_deletename=(LinearLayout) findViewById(R.id.login_linear_deletename);
		login_linear_in=(LinearLayout) findViewById(R.id.login_linear_in);
		login_linear_deletename.setOnClickListener(this);
		login_linear_in.setOnClickListener(this);
		login_edit_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			 
				if (s.length() > 0) {
					login_linear_deletename.setVisibility(View.VISIBLE);
				} else {
					login_linear_deletename.setVisibility(View.GONE);
				}
				if(s.toString().contains("@")){
					tv_msg.setText("�������������ʼ�");
					isMail=true;
				}else{
					tv_msg.setText("������֤��");
					isMail=false;
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_linear_deletename:
			login_edit_name.setText("");
			break;
		case R.id.login_linear_in:
			if(isMail){
				//�����ʼ�����
				Intent i = new Intent(getApplicationContext(),
						FindLogin.class);
				startActivity(i);
			}else{
				//�����ֻ�����
				Intent i = new Intent(getApplicationContext(),
						FindPassword.class);
				i.putExtra("phone", login_edit_name.getText().toString());
				startActivity(i);
			}
		 
			
			
			break;
		default:
			break;
		}
	}
}
