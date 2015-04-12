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

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.user.ChangeAdress;
import com.example.zf_android.activity.PayFromCar;
import com.example.zf_android.entity.AdressEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class GoodsConfirm extends BaseActivity implements OnClickListener, AdapterView.OnItemSelectedListener {
    List<AdressEntity>  listAddress = new ArrayList<AdressEntity>();
    private AdressEntity addressEntity;
    private String Url=Config.ChooseAdress;
    private TextView tv_sjr,tv_tel,tv_adress;
    private LinearLayout ll_choose, ll_choose_users;
    private Spinner spinnerState;
    private TextView title2,retail_price,showCountText,tv_pay,tv_count;
    private Button btn_pay;
    private String comment;
    private ImageView reduce,add;
    private int pirce;
    private int goodId,paychannelId,quantity,addressId,is_need_invoice=0;
    private EditText buyCountEdit,comment_et,et_titel;
    private CheckBox item_cb;
    private int invoice_type=1;//发票类型（0公司  1个人）
    private int orderType;

    private String[] state= {"公司","个人"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_good_confirm);
        new TitleMenuUtil(GoodsConfirm.this, "批购订单确认").show();

        initView();

        title2.setText(getIntent().getStringExtra("getTitle"));
        pirce=getIntent().getIntExtra("price", 0);
        retail_price.setText("￥"+ pirce);
        goodId=getIntent().getIntExtra("goodId", 1);

        orderType=getIntent().getIntExtra("orderType", Constants.Goods.OrderTypePigou);

        paychannelId=getIntent().getIntExtra("paychannelId", 1);
        tv_pay.setText("实付：￥ "+pirce);
        getData();
    }

    private void initView() {
        add=(ImageView) findViewById(R.id.add);
        reduce=(ImageView) findViewById(R.id.reduce);
        reduce.setOnClickListener(this);
        add.setOnClickListener(this);

        showCountText=(TextView) findViewById(R.id.showCountText);
        tv_sjr=(TextView) findViewById(R.id.tv_sjr);
        tv_count=(TextView) findViewById(R.id.tv_count);
        tv_tel=(TextView) findViewById(R.id.tv_tel);
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


                tv_pay.setText("实付：￥ "+pirce*quantity);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        spinnerState = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, state);
        spinnerState.setAdapter(adapter_state);
        spinnerState.setOnItemSelectedListener(this);

        ll_choose_users = (LinearLayout) findViewById(R.id.ll_choose_users);
        if (orderType != Constants.Goods.OrderTypePigou) {
            ll_choose_users.setVisibility(View.VISIBLE);
        } else {
            ll_choose_users.setVisibility(View.GONE);
        }


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        invoice_type = spinnerState.getSelectedItemPosition();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getData() {

        JsonParams params = new JsonParams();

        //Fixme
        params.put("customerId", 1);

        String strParams = params.toString();
        Events.AddressListEvent event = new Events.AddressListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    // events
    public void onEventMainThread(Events.AddressListCompleteEvent event) {
        if (event.getSuccess()) {
            JSONArray items = event.getArrResult();
            String text = items.toString();
            Gson gson = new Gson();
            listAddress = gson.fromJson(text, new TypeToken<List<AdressEntity>>(){}.getType());

            for (AdressEntity entity: listAddress) {
                if (addressEntity == null) {
                    addressEntity = entity;
                }

                if (entity.getIs_default() == 1) {
                    addressEntity = entity;
                    break;
                }
            }

            updateAddress();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                int addressId = bundle.getInt("id");
                for (AdressEntity entity: listAddress) {
                    if (addressId == entity.getId()) {
                        addressEntity = entity;
                        updateAddress();
                        break;
                    }
                }
            }
        }
    } //onActivityResult

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
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
                if(quantity==0){
                    break;
                }
                quantity= Integer.parseInt( buyCountEdit.getText().toString() )-1;
                buyCountEdit.setText(quantity+"");
                break;
            default:
                break;
        }

        super.onClick(arg0);
    }

    private void confirmGood() {
        quantity= Integer.parseInt( buyCountEdit.getText().toString() );
        comment=comment_et.getText().toString();
        JsonParams params = new JsonParams();
        // Fixme
        params.put("customerId", 80);
        params.put("agentId", 42);
        params.put("orderType", 5);
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
}
