package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.entity.PicEntity;
import com.example.zf_android.trade.CitySelectActivity;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.aftersale.AfterSaleGridActivity;
import com.posagent.activities.order.OrderList;
import com.posagent.activities.stock.StockList;
import com.posagent.activities.terminal.Terminal;
import com.posagent.activities.terminal.TerminalOpenApply;
import com.posagent.activities.trade.TradeFlowActivity;
import com.posagent.activities.user.UserList;
import com.posagent.events.Events;
import com.posagent.fragments.HMSlideFragment;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.CityIntent.CITY_ID;
import static com.example.zf_android.trade.Constants.CityIntent.CITY_NAME;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_PROVINCE;


public class Main extends BaseActivity implements OnClickListener{

    private View citySelect;
    private TextView cityTextView;
    private int cityId;
    private String cityName;

    private Province province;
    private City city;
    public static final int REQUEST_CITY = 1;
    public static final int REQUEST_CITY_WHEEL = 2;


    private List<PicEntity> banners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("main_buy_pos", AllProduct.class);
            put("main_order_manage", OrderList.class);
        }};
        this.setClickableMap(clickableMap);

        setupCommonViews();

        focusTabAtIndex(0);

        if (null == MyApplication.getCurrentUser()) {
            Intent i = new Intent(Main.this, LoginActivity.class);
            startActivity(i);
        }
    }

    private void initView() {
        citySelect = findViewById(R.id.titleback_linear_back);
        cityTextView = (TextView) findViewById(R.id.tv_city);
        citySelect.setOnClickListener(this);



        findViewById(R.id.main_stock_manage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.Stock)) {return;}

                Intent i2 =new Intent(Main.this, StockList.class);
                startActivity(i2);
            }
        });
        findViewById(R.id.click_after_sale).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.TerminalAndAfterSale)) {return;}

                Intent i2 =new Intent(Main.this, AfterSaleGridActivity.class);
                startActivity(i2);
            }
        });
        findViewById(R.id.click_apply_open).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.TerminalAndAfterSale)) {return;}

                Intent i2 =new Intent(Main.this, TerminalOpenApply.class);
                startActivity(i2);
            }
        });
        findViewById(R.id.main_terminal_manage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.TerminalAndAfterSale)) {return;}

                Intent i2 =new Intent(Main.this, Terminal.class);
                startActivity(i2);
            }
        });
        findViewById(R.id.main_trade_list).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.TradeFlowAndProfit)) {return;}

                Intent i2 =new Intent(Main.this, TradeFlowActivity.class);
                startActivity(i2);
            }
        });

        findViewById(R.id.main_user_list).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.ManUser)) {return;}

                Intent i2 =new Intent(Main.this, UserList.class);
                startActivity(i2);
            }
        });

        getData();
    }

    private void getData() {
        //banner slider
        JsonParams params = new JsonParams();
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.BannerDataEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.BannerDataCompleteEvent event) {
        banners = event.getList();
        initSlider();
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
        slideFragment.feedData(banners);
    }

}
