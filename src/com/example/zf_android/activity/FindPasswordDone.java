package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.home.LoginActivity;

public class FindPasswordDone extends BaseActivity {


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_find_password_done);

            findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(FindPasswordDone.this, LoginActivity.class);
                    startActivity(i);
                }
            });

		}
}
