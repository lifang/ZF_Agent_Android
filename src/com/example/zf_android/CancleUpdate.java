package com.example.zf_android;

 

import com.examlpe.zf_android.util.ClientUpdate;

import android.app.Activity;
import android.os.Bundle;
/***
 * 
*    
* �����ƣ�CancleUpdate   
* ��������   ȡ������ҳ�� ������APPȻ����ʧ����ʾԭ��ͣ����ҳ��
* �����ˣ� ljp 
*  
* @version    
*
 */
 

public class CancleUpdate extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		String str = getIntent().getStringExtra("quxiao");
		if(str != null && str.equals("ȡ��")){
			ClientUpdate.mUpdate.cancel(true);
		}
		
		finish();
		super.onCreate(savedInstanceState);
	}

}
