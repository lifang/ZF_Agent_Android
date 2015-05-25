package com.posagent.activities.goods;

import android.os.Bundle;
import android.view.View.OnClickListener;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;

public class RequireMaterial extends BaseActivity implements OnClickListener {

    private String requirement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require_material);

        requirement = getIntent().getStringExtra("requirement");

        new TitleMenuUtil(RequireMaterial.this, "申请开通所需材料").show();

        setText("tv_requirement", requirement);

    }
}
