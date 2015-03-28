package com.example.zf_android.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.BaseActivity;
import com.example.zf_android.R;

import java.util.HashMap;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoExchangeCreateActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_cargo_exchange_create);
		new TitleMenuUtil(AgentCargoExchangeCreateActivity.this, "调货").show();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
//            put("ll_create_agent", AgentNewActivity.class);
//            put("ll_glph", AdressList.class);
//            put("ll_gltp", UserList.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();


        //配置 提交 按钮
        TextView viewSetRate = (TextView)findViewById(R.id.next_sure);
        viewSetRate.setText("提交");
        viewSetRate.setVisibility(View.VISIBLE);
        viewSetRate.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        super.onClick(v);
	}

	 
}
