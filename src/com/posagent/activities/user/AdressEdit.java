package com.posagent.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.AdressEntity;
import com.example.zf_android.trade.CitySelectActivity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.CommonInputer;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 
*    
* 类名称：AdressEdit   
* 类描述：  编辑/新加
* 创建人： ljp 
* 创建时间：2015-3-9 下午5:10:08   
* @version    
*
 */
public class AdressEdit extends BaseActivity {

    static final String TAG = "AdressEdit";

    private TextView tvContent, tvCityName;

    private String cityName;
    private int cityId;

    private AdressEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adress_edit);

        String json = getIntent().getStringExtra("json");
        if (null != json) {
            entity = gson.fromJson(json, AdressEntity.class);
            new TitleMenuUtil(AdressEdit.this, "修改地址").show();
        } else {
            new TitleMenuUtil(AdressEdit.this, "新增地址").show();
        }


        initView();

    }

    private void initView() {
        String tag = getString(R.string.tag_destination_get);
        ArrayList<View> views = (ArrayList<View>) ViewHelper.getViewsByTag((ViewGroup)this.findViewById(android.R.id.content), tag);
        for (View _view: views) {
            _view.setOnClickListener(this);
        }

        findViewById(R.id.btn_save).setOnClickListener(this);

        tvCityName = (TextView) findViewById(R.id.tv_city_name);

        if (null != entity) {

            cityId = entity.getCityId();
            setText("tv_city_name", ((MyApplication)getApplication()).cityNameForId(cityId));
            setText("tv_receiver", entity.getReceiver());
            setText("tv_moblephone", entity.getMoblephone());
            setText("tv_zipCode", entity.getZipCode());
            setText("tv_address", entity.getAddress());
            CheckBox cb_isDefault = (CheckBox)findViewById(R.id.cb_isDefault);
            boolean isDefault = false;
            if (null != entity.getIsDefault() && entity.getIsDefault().equals("1")) {
                isDefault = true;
            }
            cb_isDefault.setChecked(isDefault);

        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_save) {
            // Save data
            doSubmit();
            return;
        }

        if (v.getId() == R.id.ll_select_city) {
            Intent intent = new Intent(this, CitySelectActivity.class);

            if (cityName != null) {
                intent.putExtra(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME, cityName);
            }
            startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CITY_CODE);
            return;
        }

        String tag = getString(R.string.tag_destination_get);
        if (v.getTag().equals(tag)) {
            Intent intent = new Intent(this, CommonInputer.class);

            tag = getString(R.string.tag_destination_name);
            TextView tv = (TextView) v.findViewWithTag(tag);
            intent.putExtra(Constants.CommonInputerConstant.TITLE_KEY, tv.getText());

            tag = getString(R.string.tag_destination);
            tvContent = tv = (TextView) v.findViewWithTag(tag);
            intent.putExtra(Constants.CommonInputerConstant.PLACEHOLDER_KEY, tv.getText());


            startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CODE);
        }

        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CODE:
                String content = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);
                tvContent.setText(content);
                Log.d(TAG, data.toString());
                break;
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:
                cityName = bundle.getString(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME);
                cityId = bundle.getInt(com.example.zf_android.trade.Constants.CityIntent.CITY_ID);
                tvCityName.setText(cityName);
                break;
        }
    }

    public void onEventMainThread(Events.CreateAddressCompleteEvent event) {
        Toast.makeText(getApplicationContext(),
                event.getMessage(),
                Toast.LENGTH_SHORT).show();

        if (event.getSuccess()) {
            EventBus.getDefault().post(new Events.AddressListReloadEvent());
            finish();
        }
    }

    private void doSubmit() {
        // prepare data
        String strParams = this.data();

        // submit
        Events.CommonRequestEvent event = new Events.CreateAddressEvent();
        if (null != entity) {
            event = new Events.UpdateAddressEvent();
        }
        event.setParams(strParams);
        EventBus.getDefault().post(event);
        Toast.makeText(this.getApplicationContext(), "正在提交", Toast.LENGTH_SHORT).show();

    }

    private String data() {
        JsonParams data = new JsonParams();
        if (null != entity) {
            data.put("id", entity.getId());
        }
        data.put("cityId", cityId);
        data.put("customerId", MyApplication.user().getId());

        String[] arr = {"tv_receiver", "tv_moblephone",
                "tv_zipCode", "tv_address",
                "cb_isDefault"};



        for (String strId : arr) {
            int resouceId = resouceId(strId, "id");
            if (strId.startsWith("tv_")) {
                TextView tv = (TextView) findViewById(resouceId);
                if (tv != null) {
                    data.put(strId.replace("tv_", ""), tv.getText().toString());
                }
            } else if (strId.startsWith("et_")) {
                EditText tv = (EditText) findViewById(resouceId);
                if (tv != null) {
                    data.put(strId.replace("et_", ""), tv.getText().toString());
                }
            } else if (strId.startsWith("cb_")) {
                CheckBox tv = (CheckBox) findViewById(resouceId);
                if (tv != null) {
                    int flag = tv.isChecked() ? 1 : 2;
                    data.put(strId.replace("cb_", ""), flag);
                }
            }
        }

        return data.toString();
    }
}
