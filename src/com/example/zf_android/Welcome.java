package com.example.zf_android;

import com.posagent.activities.home.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		Intent i =new Intent(Welcome.this,Main.class);
		startActivity(i);
		finish();
	}

 
}
