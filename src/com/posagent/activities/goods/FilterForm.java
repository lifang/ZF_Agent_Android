package com.posagent.activities.goods;


import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.GoodsSearchEntity;
import com.google.gson.reflect.TypeToken;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.Map;

import de.greenrobot.event.EventBus;

/***
*
* 过滤表单
*
*/
public class FilterForm extends BaseActivity {

    private GoodsSearchEntity entity;

    private Map<String, String> mapFilter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_form);

        String json = getIntent().getStringExtra("json");
        mapFilter = gson.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());

        new TitleMenuUtil(FilterForm.this, "筛选").show();


        initView();

	}

    private void initView() {

        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("cityId", MyApplication.user().getAgentCityId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.GoodsSearchItemEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void updateView() {
        if (null == entity) {
            return;
        }
    }

    // events
    public void onEventMainThread(Events.GoodsSearchItemCompleteEvent event) {
        entity = event.getEntity();

        updateView();
    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        super.onClick(v);
	}

	 
}
