package com.example.zf_android.activity;


import android.os.Bundle;
import android.view.View;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;
import com.example.zf_android.trade.widget.MyTabWidget;

import java.util.HashMap;

/***
*
* 创建代理商
*
*/
public class AgentNewActivity extends BaseActivity {

    MyTabWidget tabWidget;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_agent);
		new TitleMenuUtil(AgentNewActivity.this, "创建下级代理商").show();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
//            put("ll_glxjdls", AdressList.class);
//            put("ll_glph", AdressList.class);
//            put("ll_gltp", UserList.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        tabWidget = (MyTabWidget)findViewById(R.id.tab_widget);
        tabWidget.addTab("公司");
        tabWidget.addTab("个人");
        tabWidget.updateTabs(0);


	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        super.onClick(v);
	}

	 
}
