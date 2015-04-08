package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.activity.LoginActivity;
import com.example.zf_android.activity.OrderList;
import com.example.zf_android.activity.StockList;
import com.example.zf_android.activity.Terminal;
import com.example.zf_android.activity.TerminalOpenApply;
import com.example.zf_android.activity.UserList;
import com.example.zf_android.entity.PicEntity;
import com.example.zf_android.trade.AfterSaleGridActivity;
import com.example.zf_android.trade.CitySelectActivity;
import com.example.zf_android.trade.TradeFlowActivity;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.BaseActivity;
import com.posagent.fragments.HMSlideFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.zf_android.trade.Constants.CityIntent.CITY_ID;
import static com.example.zf_android.trade.Constants.CityIntent.CITY_NAME;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_PROVINCE;


public class Main extends BaseActivity implements OnClickListener{

    private ImageView testbutton;

    private View citySelect;
    private TextView cityTextView;
    private int cityId;
    private String cityName;

    private Province province;
    private City city;
    public static final int REQUEST_CITY = 1;
    public static final int REQUEST_CITY_WHEEL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("main_buy_pos", AllProduct.class);
            put("main_order_manage", OrderList.class);
            put("main_stock_manage", StockList.class);
            put("main_trade_list", TradeFlowActivity.class);
            put("main_terminal_manage", Terminal.class);
            put("main_user_list", UserList.class);
            put("click_apply_open", TerminalOpenApply.class);
            put("click_after_sale", AfterSaleGridActivity.class);
        }};
        this.setClickableMap(clickableMap);

        setupCommonViews();

        focusTabAtIndex(0);

        initSlider();

        Intent i = new Intent(Main.this, LoginActivity.class);
        startActivity(i);
    }

    private void initView() {
        citySelect = findViewById(R.id.titleback_linear_back);
        cityTextView = (TextView) findViewById(R.id.tv_city);
        citySelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleback_linear_back:
                Intent intent = new Intent(Main.this, CitySelectActivity.class);
                intent.putExtra(CITY_NAME, cityName);
                startActivityForResult(intent, REQUEST_CITY);
                break;
            default:
                break;
        }

        super.onClick(v);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CITY:
                cityId = data.getIntExtra(CITY_ID, 0);
                cityName = data.getStringExtra(CITY_NAME);
                cityTextView.setText(cityName);
                break;
            case REQUEST_CITY_WHEEL:
                province = (Province) data.getSerializableExtra(SELECTED_PROVINCE);
                city = (City) data.getSerializableExtra(SELECTED_CITY);
                cityTextView.setText(city.getName());
                break;
        }
    }


    private void initSlider() {
        HMSlideFragment slideFragment = (HMSlideFragment) getFragmentManager().findFragmentById(R.id.headlines_fragment);

        String jsonData = "{'code':1,'message':'success','result':[{'id':5,'picture_url':'http://file.youboy.com/a/142/67/57/6/660666.jpg','website_url':'http://baidu.com'},{'id':4,'picture_url':'http://img1.100ye.com/img1/4/1181/892/10772392/msgpic/61260332.jpg','website_url':'http://baidu.com'},{'id':3,'picture_url':'http://image5.huangye88.com/2013/01/08/db4ed2c6a01ec5ef.jpg','website_url':'http://baidu.com'},{'id':2,'picture_url':'http://file.youboy.com/a/149/94/17/5/669625.jpg','website_url':'http://baidu.com'}]}";

        Gson gson = new Gson();

        JSONObject jsonobject = null;
        String code = null;
        try {
            jsonobject = new JSONObject(jsonData);
            code = jsonobject.getString("code");
            int a =jsonobject.getInt("code");
            if(a== Config.CODE){
                String res =jsonobject.getString("result");
                ArrayList<PicEntity> myList = gson.fromJson(res, new TypeToken<List<PicEntity>>() {
                }.getType());

                slideFragment.feedData(myList);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

}
