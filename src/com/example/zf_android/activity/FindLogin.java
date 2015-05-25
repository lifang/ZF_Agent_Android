package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.posagent.activities.BaseActivity;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.home.LoginActivity;

public class FindLogin extends BaseActivity {

        private String email;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.findlogin);

            email = getIntent().getStringExtra("email");
            setText("tv_email", email);

            findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(FindLogin.this, LoginActivity.class);
                    startActivity(i);
                }
            });

		}
}
