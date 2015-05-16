package com.posagent.activities.home;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;

public class RegisterXieyi extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_xieyi);
        new TitleMenuUtil(RegisterXieyi.this, "华尔街金融平台用户使用协议").show();
    }
}