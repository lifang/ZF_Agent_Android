package com.posagent.activities.goods;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.FactoryEntity;
import com.posagent.activities.BaseActivity;

public class FactoryInfo extends BaseActivity implements OnClickListener {

    private FactoryEntity factoryEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_info);
        String json = getIntent().getStringExtra("json");
        factoryEntity = gson.fromJson(json, FactoryEntity.class);
        initView();

        new TitleMenuUtil(FactoryInfo.this, "厂家信息").show();

    }

    private void initView() {

        if (null != factoryEntity) {
            ImageView iv_factory_logo = (ImageView)findViewById(R.id.iv_factory_logo);
            ImageCacheUtil.IMAGE_CACHE.get(factoryEntity.getLogo_file_path(), iv_factory_logo);
            setText("tv_factory_name", factoryEntity.getName());
            setText("tv_factory_desc", factoryEntity.getDescription());
        }
    }

    @Override
    public void onClick(View v) {




        super.onClick(v);
    }
}
