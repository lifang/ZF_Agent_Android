package com.posagent.activities.agent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.CitySelectActivity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/***
*
* 创建代理商
*
*/
public class AgentNewActivity extends BaseActivity {

    private MyTabWidget tabWidget;
    private LinearLayout ll_choose_city;
    private Button btn_submit;
    private CheckBox cb_is_profit;


    private int cityId;
    private String cityName;
    private int types = 1;

    private String cardPhotoPath = "cardPhotoPath";
    private String licensePhotoPath = "licensePhotoPath";
    private String taxPhotoPath = "taxPhotoPath";


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

        cb_is_profit = (CheckBox) findViewById(R.id.cb_is_profit);
        ll_choose_city = (LinearLayout) findViewById(R.id.ll_choose_city);
        ll_choose_city.setOnClickListener(this);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        tabWidget = (MyTabWidget)findViewById(R.id.tab_widget);
        tabWidget.addTab("公司");
        tabWidget.addTab("个人");

        tabWidget.setOnTabSelectedListener(new MyTabWidget.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.d(TAG, "select postion " + position);
                types = position + 1;
                if (position == 1) {
                    hideByTag("company_items");
                } else {
                    showByTag("company_items");
                }
            }
        });

        tabWidget.updateTabs(0);


	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.ll_choose_city) {
            Intent intent = new Intent(AgentNewActivity.this, CitySelectActivity.class);
            if (cityName != null) {
                intent.putExtra(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME, cityName);
            }
            startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CITY_CODE);
            return;
        }
        if (v.getId() == R.id.btn_submit) {
            doSubmit();
            return;
        }


        super.onClick(v);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:
                cityName = bundle.getString(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME);
                cityId = bundle.getInt(com.example.zf_android.trade.Constants.CityIntent.CITY_ID);
                setText("tv_city_name", cityName);
                break;
        }
    }

    private void doSubmit() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("agentsId", 1);
        params.put("agentType", types);
        params.put("cityId", cityId);

        params.put("loginId", getValue("et_username"));
        params.put("agentName", getValue("et_name"));
        params.put("agentCardId", getValue("et_idcard"));
        params.put("companyName", getValue("et_company_name"));
        params.put("companyId", getValue("et_license"));
        params.put("phoneNum", getValue("et_phone"));
        params.put("emailStr", getValue("et_email"));
        params.put("addressStr", getValue("et_address"));
        params.put("pwd", getValue("et_password"));
        params.put("pwd1", getValue("et_password_confirm"));
        params.put("isProfit", cb_is_profit.isChecked() ? 2 : 1);
        params.put("taxNumStr", getValue("et_tax_license"));
        params.put("loginId", getValue("et_username"));

        params.put("cardPhotoPath", cardPhotoPath);
        params.put("licensePhotoPath", licensePhotoPath);
        params.put("taxPhotoPath", taxPhotoPath);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SonAgentCreateEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.SonAgentCreateCompleteEvent event) {
        toast(event.getMessage());
        if (event.success()) {
            EventBus.getDefault().post(new Events.SonAgentListReloadEvent());
            finish();
        }
    }

	 
}
