package com.posagent.activities.goods;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.RequireMaterialEntity;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RequireMaterial extends BaseActivity implements OnClickListener {

    private List<RequireMaterialEntity> pra = new ArrayList<RequireMaterialEntity>();
    private List<RequireMaterialEntity> pub = new ArrayList<RequireMaterialEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require_material);

        try {
            pra = gson.fromJson("json_pra",
                    new TypeToken<List<RequireMaterialEntity>>() {}.getType());
        } catch (RuntimeException ex) {
            Log.d(TAG, ex.getMessage());
        }

        try {
            pub = gson.fromJson("json_pub",
                    new TypeToken<List<RequireMaterialEntity>>() {}.getType());
        } catch (RuntimeException ex) {
            Log.d(TAG, ex.getMessage());
        }


        new TitleMenuUtil(RequireMaterial.this, "申请开通所需材料").show();

        initView();


    }

    private void initView() {
        RequireMaterialEntity entity;
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout ll;

        //pra
        LinearLayout ll_pra = (LinearLayout)findViewById(R.id.ll_pra);
        for (int i = 0; i < pra.size(); i++) {
            entity = pra.get(i);
            ll = (LinearLayout)inflater.inflate(R.layout.item_require_material, null);
            TextView tv = (TextView)ll.findViewById(R.id.tv_material);
            tv.setText("" + i + ". " + entity.getName() + "("+ entity.getIntroduction() +")");
            ll_pra.addView(ll);
        }


        LinearLayout ll_pub = (LinearLayout)findViewById(R.id.ll_pub);
        for (int i = 0; i < pub.size(); i++) {
            entity = pub.get(i);
            ll = (LinearLayout)inflater.inflate(R.layout.item_require_material, null);
            TextView tv = (TextView)ll.findViewById(R.id.tv_material);
            tv.setText("" + i + ". " + entity.getName() + "("+ entity.getIntroduction() +")");
            ll_pub.addView(ll);
        }
    }
}
