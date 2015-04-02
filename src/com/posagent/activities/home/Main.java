package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.activity.LoginActivity;
import com.example.zf_android.activity.OrderList;
import com.example.zf_android.activity.StockList;
import com.example.zf_android.activity.Terminal;
import com.example.zf_android.activity.TerminalOpenApply;
import com.example.zf_android.activity.UserList;
import com.example.zf_android.trade.AfterSaleGridActivity;
import com.example.zf_android.trade.CitySelectActivity;
import com.example.zf_android.trade.TradeFlowActivity;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.posagent.activities.BaseActivity;

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
    private View item ;
    private LayoutInflater inflater;
    private ImageView image;
    private int  index_ima=0;
    private ArrayList<String> ma = new ArrayList<String>();
    List<View> list = new ArrayList<View>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2: // 网络有问题
                    Toast.makeText(getApplicationContext(), "网络未连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    break;
                case 4:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        testbutton=(ImageView) findViewById(R.id.testbutton);
        testbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, LoginActivity.class);
                startActivity(i);
            }
        });

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
    }

    private void initView() {
        citySelect = findViewById(R.id.titleback_linear_back);
        cityTextView = (TextView) findViewById(R.id.tv_city);
        citySelect.setOnClickListener(this);

        inflater = LayoutInflater.from(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
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

}
