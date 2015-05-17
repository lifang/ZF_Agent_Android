package com.example.zf_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.posagent.activities.BaseActivity;
import com.posagent.activities.home.GuideActivity;
import com.posagent.activities.home.LoginActivity;

public class Welcome extends BaseActivity {

    private static final int STOPSPLASH = 0;
    // time in milliseconds
    private static final long SPLASHTIME = 1000;
    private static final String GUIDE_SHOWN_KEY = "shown_v1";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences("GuideStatus", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent;
                if (sharedPreferences.getBoolean(GUIDE_SHOWN_KEY, false)) {
                    mainIntent = new Intent(context, LoginActivity.class);
                } else {
                    editor.putBoolean(GUIDE_SHOWN_KEY, true);
                    editor.commit();
                    mainIntent = new Intent(context, GuideActivity.class);
                }
                Welcome.this.startActivity(mainIntent);
                Welcome.this.finish();
            }

        }, SPLASHTIME);


	}
 
}
