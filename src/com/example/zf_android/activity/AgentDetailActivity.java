package com.example.zf_android.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;

import java.util.HashMap;

/***
*
* 代理商详情
*
*/
public class AgentDetailActivity extends BaseActivity {

    TextView viewSetRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_detail);
		new TitleMenuUtil(AgentDetailActivity.this, "代理商详情").show();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
//            put("ll_glxjdls", AdressList.class);
//            put("ll_glph", AdressList.class);
//            put("ll_gltp", UserList.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        //配置 设置分润 按钮
        viewSetRate = (TextView)findViewById(R.id.next_sure);
        viewSetRate.setText("设置分润");
        viewSetRate.setVisibility(View.VISIBLE);
        viewSetRate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.next_sure) {
            //TODO go to 设置分润
            startActivity(new Intent(this, AgentDetailActivity.class));
            return;
        }

        super.onClick(v);
	}

	 
}
