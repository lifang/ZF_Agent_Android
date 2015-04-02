package com.example.zf_android;

 

import android.app.Activity;
import android.os.Bundle;

import com.examlpe.zf_android.util.ClientUpdate;
/***
 * 
*
*  
* @version    
*
 */
 

public class CancleUpdate extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		String str = getIntent().getStringExtra("quxiao");
		if(str != null && str.equals("dd")){
			ClientUpdate.mUpdate.cancel(true);
		}
		
		finish();
		super.onCreate(savedInstanceState);
	}

}
