package com.posagent.activities.goods;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.activity.PayFromCar;
import com.example.zf_android.entity.AdressEntity;
import com.example.zf_android.entity.GoodinfoEntity;
import com.example.zf_android.entity.PayChannelEntity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.user.ChangeAdress;
import com.posagent.activities.user.UserList;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class GoodsConfirm extends BaseActivity implements OnClickListener, AdapterView.OnItemSelectedListener {
    List<AdressEntity>  listAddress = new ArrayList<AdressEntity>();
    private AdressEntity addressEntity;
    private String Url=Config.ChooseAdress;
    private TextView tv_sjr,tv_tel,tv_adress,tv_real_amount, tv_username, titleback_text_title;
    private LinearLayout ll_choose, ll_choose_users;
    private Spinner spinnerState;
    private CheckBox cb_zulin;
    private TextView title2,retail_price,showCountText,tv_pay,tv_count;
    private Button btn_pay;
    private String comment;
    private ImageView reduce, add, iv_face;
    private int price, originPrice, minQuantity;
    private int goodId,paychannelId,quantity,addressId,is_need_invoice=0;
    private EditText buyCountEdit,comment_et,et_titel;
    private CheckBox item_cb;
    private int invoice_type=1;//发票类型（0公司  1个人）
    private int buyType, userId;
    private String faceUrl, title;

    private GoodinfoEntity goodinfo;
    private PayChannelEntity paychannelinfo;

    private String[] state= {"公司","个人"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_good_confirm);
        new TitleMenuUtil(GoodsConfirm.this, "批购订单确认").show();

        String jsonGoodinfo = getIntent().getStringExtra("jsonGoodinfo");
        goodinfo = gson.fromJson(jsonGoodinfo, GoodinfoEntity.class);

        String jsonPayChannelInfo = getIntent().getStringExtra("jsonPayChannelInfo");
        paychannelinfo = gson.fromJson(jsonPayChannelInfo, PayChannelEntity.class);

        price =getIntent().getIntExtra("price", 0);
        originPrice=getIntent().getIntExtra("originPrice", 0);
        minQuantity=getIntent().getIntExtra("minQuantity", 0);
        goodId=getIntent().getIntExtra("goodId", 1);
        faceUrl = getIntent().getStringExtra("faceUrl");
        paychannelId=getIntent().getIntExtra("paychannelId", 1);
        buyType=getIntent().getIntExtra("buyType", Constants.Goods.OrderTypePigou);
        title = getIntent().getStringExtra("getTitle");

        initView();


    }

    private void initView() {
        add=(ImageView) findViewById(R.id.add);
        reduce=(ImageView) findViewById(R.id.reduce);
        reduce.setOnClickListener(this);
        add.setOnClickListener(this);

        findViewById(R.id.tv_zulin_xieyi).setOnClickListener(this);

        // 标题
        titleback_text_title=(TextView) findViewById(R.id.titleback_text_title);

        cb_zulin=(CheckBox) findViewById(R.id.cb_zulin);

        tv_username=(TextView) findViewById(R.id.tv_users);
        showCountText=(TextView) findViewById(R.id.showCountText);
        tv_sjr=(TextView) findViewById(R.id.tv_sjr);
        tv_count=(TextView) findViewById(R.id.tv_count);
        tv_tel=(TextView) findViewById(R.id.tv_tel);
        tv_real_amount=(TextView) findViewById(R.id.tv_real_amount);
        tv_adress=(TextView) findViewById(R.id.tv_adress);
        ll_choose=(LinearLayout) findViewById(R.id.ll_choose);
        ll_choose.setOnClickListener(this);
        title2=(TextView) findViewById(R.id.title2);
        retail_price=(TextView) findViewById(R.id.retail_price);
        btn_pay=(Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);
        tv_pay=(TextView) findViewById(R.id.tv_pay);
        et_titel=(EditText) findViewById(R.id.et_titel);
        buyCountEdit=(EditText) findViewById(R.id.buyCountEdit);
        comment_et=(EditText) findViewById(R.id.comment_et);

        setText("tv_origin_price", "原价 ￥" + StringUtil.priceShow(originPrice));
        setText("retail_price", "￥" + StringUtil.priceShow(price));


        item_cb=(CheckBox) findViewById(R.id.item_cb);
        item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1){
                    is_need_invoice=1;
                }else{
                    is_need_invoice=0;
                }
            }
        });
        buyCountEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                showCountText.setText(arg0.toString());
                tv_count.setText("共计:   "+arg0+"件");
                if( buyCountEdit.getText().toString().equals("")){
                    quantity=0;
                }else{
                    quantity= Integer.parseInt( buyCountEdit.getText().toString() );
                }
                String strTotalPrice = StringUtil.priceShow(price * quantity);
                String strTotalYajin = StringUtil.priceShow(goodinfo.getLease_deposit() * quantity);
                tv_real_amount.setText("实付：￥ "+ strTotalPrice);
                tv_pay.setText("实付：￥ "+ strTotalPrice);
                setText("tv_zulin_yajin", "￥ "+ strTotalYajin);
                setText("tv_zulin_heji", "合计：￥ "+ strTotalPrice);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        title2.setText(title);

        iv_face = (ImageView) findViewById(R.id.iv_face);
        ImageCacheUtil.IMAGE_CACHE.get(faceUrl, iv_face);


        tv_pay.setText("实付：￥ "+ price);
        tv_real_amount.setText("实付：￥ "+ price);

        ll_choose_users = (LinearLayout) findViewById(R.id.ll_choose_users);
        ll_choose_users.setOnClickListener(this);

        buyCountEdit.setText("1");
        if (buyType != Constants.Goods.OrderTypePigou) {
            hide("tv_origin_price");
            ll_choose_users.setVisibility(View.VISIBLE);
            if (buyType == Constants.Goods.OrderTypeDaigou) {
                setText("titleback_text_title", "代购买订单确认");
            } else if (buyType == Constants.Goods.OrderTypeDaizulin) {
                setText("tv_quantity_name", "租赁数量（件）");
                setText("titleback_text_title", "代租赁订单确认");
                setText("tv_max_month", goodinfo.getReturn_time() + "个月");
                setText("tv_min_month", goodinfo.getLease_time() + "个月");

                hide("tv_real_amount");
                hide("ll_fapiao");

                show("ll_zulin_extra");
                show("ll_zulin_xieyi");
            }
        } else {
            //批购
            ll_choose_users.setVisibility(View.GONE);
            //设置默认批购数量
            buyCountEdit.setText("" + minQuantity);
        }

        spinnerState = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, state);
        spinnerState.setAdapter(adapter_state);
        spinnerState.setOnItemSelectedListener(this);


        getData();


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        invoice_type = spinnerState.getSelectedItemPosition();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getData() {

        JsonParams params = new JsonParams();

        params.put("customerId", MyApplication.user().getId());

        String strParams = params.toString();
        Events.AddressListEvent event = new Events.AddressListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    // events
    public void onEventMainThread(Events.AddressListCompleteEvent event) {
        if (event.getSuccess()) {
            listAddress = event.getList();

            for (AdressEntity entity: listAddress) {
                if (addressEntity == null) {
                    addressEntity = entity;
                }

                if (entity.getIs_default() == "1") {
                    addressEntity = entity;
                    break;
                }
            }

            updateAddress();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case Constants.REQUEST_CODE:
                int addressId = bundle.getInt("id");
                for (AdressEntity entity: listAddress) {
                    if (addressId == entity.getId()) {
                        addressEntity = entity;
                        updateAddress();
                        break;
                    }
                }
                break;
            case Constants.REQUEST_CODE2:
                String username = bundle.getString("username");
                tv_username.setText(username);
                userId = bundle.getInt("userId", 0);
                break;
        }
    } //onActivityResult

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_choose_users:
                Intent i2 = new Intent(GoodsConfirm.this, UserList.class);
                i2.putExtra("forSelect", true);
                startActivityForResult(i2, Constants.REQUEST_CODE2);
                break;
            case R.id.tv_zulin_xieyi:
                Intent i3 = new Intent(GoodsConfirm.this, ZulinXieyi.class);
                i3.putExtra("json", gson.toJson(goodinfo));
                startActivity(i3);
                break;
            case R.id.ll_choose:
                Intent i =new Intent(GoodsConfirm.this, ChangeAdress.class);
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.btn_pay:
                confirmGood();
                break;
            case R.id.add:
                quantity= Integer.parseInt( buyCountEdit.getText().toString() )+1;
                buyCountEdit.setText(quantity+"");
                break;
            case R.id.reduce:
                if (buyType == Constants.Goods.OrderTypePigou) {
                    if(quantity <= minQuantity){
                        break;
                    }
                } else {
                    if(quantity <= 1){
                        break;
                    }
                }

                quantity= Integer.parseInt( buyCountEdit.getText().toString() )-1;
                buyCountEdit.setText(quantity+"");
                break;
            default:
                break;
        }

        super.onClick(arg0);
    }

    private boolean check() {
        if (buyType == Constants.Goods.OrderTypeDaizulin) {
            if (!cb_zulin.isChecked()) {
                toast("请同意租赁协议");
                return false;
            }
        }
        return true;
    }

    private void confirmGood() {

        if (!check()) {
            return;
        }

        quantity= Integer.parseInt( buyCountEdit.getText().toString() );
        comment=comment_et.getText().toString();
        JsonParams params = new JsonParams();

        if (userId > 0) {
            params.put("customerId", userId);
        } else {
            params.put("customerId", MyApplication.user().getAgentUserId());
        }
        params.put("creatid", MyApplication.user().getId());
        params.put("belongId", MyApplication.user().getAgentUserId());
        params.put("agentId", MyApplication.user().getAgentId());

        params.put("orderType", mapBuyType());
        params.put("goodId", goodId);
        params.put("paychannelId", paychannelId);
        params.put("addressId", addressEntity.getId());
        params.put("quantity", quantity);
        params.put("comment", comment);
        params.put("isNeedInvoice", is_need_invoice);
        params.put("invoiceType", invoice_type);
        params.put("invoiceInfo", et_titel.getText().toString());

        String strParams = params.toString();
        Events.CreateOrderEvent event = new Events.CreateOrderEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    // events
    public void onEventMainThread(Events.CreateOrderCompleteEvent event) {
        if (event.getSuccess()) {

        } else {
            Toast.makeText(getApplicationContext(), event.getMessage(), Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent (GoodsConfirm.this, PayFromCar.class);
        startActivity(i);
    }

    private void updateAddress() {
        if (addressEntity != null) {
            tv_sjr.setText("收件人：" + addressEntity.getReceiver());
            tv_adress.setText("收件地址：" + addressEntity.getAddress());
            tv_tel.setText("" + addressEntity.getMoblephone());
        }
    }

    private int mapBuyType() {
        int orderType = 0;

        switch (buyType) {
            case Constants.Goods.OrderTypePigou:
                orderType = 5;
                break;
            case Constants.Goods.OrderTypeDaigou:
                orderType = 3;
                break;
            case Constants.Goods.OrderTypeDaizulin:
                orderType = 4;
                break;
        }
        return orderType;
    }

    private void updatePrice() {
        int price = 0;
        int originPrice = goodinfo.getPrice() + paychannelinfo.getOpening_cost();
        if (buyType == Constants.Goods.OrderTypePigou) {
            price = goodinfo.getPurchase_price() + paychannelinfo.getOpening_cost();
        } else if (buyType == Constants.Goods.OrderTypeDaigou) {
            price = goodinfo.getRetail_price() + paychannelinfo.getOpening_cost();
        } else {
            price = goodinfo.getLease_price() + paychannelinfo.getOpening_cost();
        }
    }
}
