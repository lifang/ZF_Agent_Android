package com.example.zf_android.activity;

 
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.epalmpay.agentPhone.R;
/**
 * 
*    
* 消息详情
*
 */
public class SystemDetail extends BaseActivity implements View.OnClickListener{
	private TextView tv_titel,tv_time,tv_content;
	private int id;
	private String url;
	private ImageView search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_detail);
        new TitleMenuUtil(SystemDetail.this, "消息详情").show();
		initView();
		getData();
	}
	private void getData() {
		// TODO Auto-generated method stub
 
	}
	private void initView() {
		// TODO Auto-generated method stub
		tv_titel=(TextView) findViewById(R.id.msg_title);
		tv_time=(TextView) findViewById(R.id.msg_time);
		tv_content=(TextView) findViewById(R.id.msg_conten);
		search=(ImageView) findViewById(R.id.search);
		search.setVisibility(View.VISIBLE);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ִ��ɾ�����
			}
		});
	}

    @Override
    public void onClick(View v) {

    }
}
