package com.example.zf_android.activity;
 
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.epalmpay.agentPhone.R;
/***
 * 
*    
* ����ƣ�About   
* ��������   ����ҳ��
* �����ˣ� ljp 
* ����ʱ�䣺2015-1-27 ����7:55:20   
* @version    
*
 */
public class About extends BaseActivity implements OnClickListener{
	private int a;
	private int index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new TitleMenuUtil(About.this, "����").show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	 
}
