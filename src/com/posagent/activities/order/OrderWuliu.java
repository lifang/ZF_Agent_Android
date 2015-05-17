package com.posagent.activities.order;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;

public class OrderWuliu extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_wuliu);
        new TitleMenuUtil(this, "查看物流").show();

        setText("tv_name", getIntent().getStringExtra("logistics_name"));
        setText("tv_number", getIntent().getStringExtra("logistics_number"));

    }

}
