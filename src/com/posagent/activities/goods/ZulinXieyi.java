package com.posagent.activities.goods;

import android.os.Bundle;
import android.view.View.OnClickListener;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.GoodinfoEntity;
import com.posagent.activities.BaseActivity;

public class ZulinXieyi extends BaseActivity implements OnClickListener {

    private GoodinfoEntity goodinfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zulinxieyi);

        String json = getIntent().getStringExtra("json");
        goodinfoEntity = gson.fromJson(json, GoodinfoEntity.class);


        new TitleMenuUtil(ZulinXieyi.this, "租赁说明").show();

        initView();


    }

    private void initView() {
        setText("tv_min_month", goodinfoEntity.getLease_time() + "个月");
        setText("tv_max_month", goodinfoEntity.getReturn_time() + "个月");
        setText("tv_desc", goodinfoEntity.getLease_description());
        setText("tv_xieyi", goodinfoEntity.getLease_agreement());
        setText("tv_yajin", "￥" + StringUtil.priceShow(goodinfoEntity.getLease_time()));
    }
}
