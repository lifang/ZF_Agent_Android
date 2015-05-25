package com.posagent.activities.agent;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.epalmpay.agentPhone.R;

import java.util.HashMap;

/***
*
* 代理商管理首页
*
*/
public class AgentManageMainActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_manage_main);
		new TitleMenuUtil(AgentManageMainActivity.this, "下级代理商管理").show();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("ll_glxjdls", AgentManageActivity.class);
            put("ll_glph", AgentCargoActivity.class);
            put("ll_gltp", AgentCargoExchangeActivity.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        super.onClick(v);

	}

	 
}
