package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.activity.LoginActivity;
import com.example.zf_android.activity.MenuMine;
import com.example.zf_android.activity.OrderList;
import com.example.zf_android.activity.StockList;
import com.example.zf_android.activity.SystemMessage;
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
import java.util.List;

import static com.example.zf_android.trade.Constants.CityIntent.CITY_ID;
import static com.example.zf_android.trade.Constants.CityIntent.CITY_NAME;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_PROVINCE;


public class Main extends BaseActivity implements OnClickListener{

    private RelativeLayout btn_user_list, click_after_sale, main_rl_pos,
            main_rl_renzhen, main_rl_zdgl,main_rl_jyls,main_rl_Forum,main_rl_wylc,
            main_rl_xtgg,click_apply_open,main_rl_my,main_rl_pos1,main_rl_gwc;
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
        setupCommonViews();

        focusTabAtIndex(0);
    }

    private void initView() {
        citySelect = findViewById(R.id.titleback_linear_back);
        cityTextView = (TextView) findViewById(R.id.tv_city);
        citySelect.setOnClickListener(this);
        main_rl_pos=(RelativeLayout) findViewById(R.id.main_rl_pos);
        main_rl_pos.setOnClickListener(this);
        main_rl_renzhen=(RelativeLayout) findViewById(R.id.main_rl_renzhen);
        main_rl_renzhen.setOnClickListener(this);
        main_rl_zdgl=(RelativeLayout) findViewById(R.id.main_rl_kcgl);
        main_rl_zdgl.setOnClickListener(this);
        main_rl_jyls=(RelativeLayout) findViewById(R.id.main_rl_jyls);
        main_rl_jyls.setOnClickListener(this);
        main_rl_Forum=(RelativeLayout) findViewById(R.id.main_rl_Forum);
        main_rl_Forum.setOnClickListener(this);

        click_apply_open=(RelativeLayout) findViewById(R.id.click_apply_open);
        click_apply_open.setOnClickListener(this);


        btn_user_list=(RelativeLayout) findViewById(R.id.btn_user_list);
        btn_user_list.setOnClickListener(this);
        click_after_sale=(RelativeLayout) findViewById(R.id.click_after_sale);
        click_after_sale.setOnClickListener(this);

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

            case R.id.main_rl_pos1:
                startActivity(new Intent(Main.this, SystemMessage.class));

                break;


            case R.id.main_rl_my:

                startActivity(new Intent(Main.this, MenuMine.class));

                break;

            case R.id.main_rl_pos:

                startActivity(new Intent(Main.this, AllProduct.class));

                break;


            case R.id.main_rl_renzhen:
                Intent i =new Intent(Main.this, OrderList.class);
                startActivity(i);

                break;
            case R.id.main_rl_kcgl:
                startActivity(new Intent(Main.this, StockList.class));
                break;
            case R.id.main_rl_jyls:

                startActivity(new Intent(Main.this, TradeFlowActivity.class));
                break;
            case R.id.main_rl_Forum: //锟斤拷要锟斤拷锟�
                startActivity(new Intent(Main.this, Terminal.class));
                break;

            case R.id.btn_user_list:
                startActivity(new Intent(Main.this, UserList.class));
                break;
            case R.id.click_apply_open: //��ϵ����
                startActivity(new Intent(Main.this, TerminalOpenApply.class));
                break;
            case R.id.main_rl_gwc:
                startActivity(new Intent(Main.this, AllProduct.class));
                break;
            case R.id.click_after_sale:
                startActivity(new Intent(Main.this, AfterSaleGridActivity.class));
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
