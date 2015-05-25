package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;

public class RegisterSuccess extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        new TitleMenuUtil(RegisterSuccess.this, "申请成为合作伙伴").show();

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}