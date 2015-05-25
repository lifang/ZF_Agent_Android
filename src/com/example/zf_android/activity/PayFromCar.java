package com.example.zf_android.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.DialogUtil;
import com.examlpe.zf_android.util.DialogUtil.CallBackChange;
import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.alipay.PayActivity;
import com.example.zf_android.entity.PayOrderInfo;
import com.example.zf_android.entity.ShopPayOrderInfo;
import com.posagent.activities.order.OrderDetail;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;


public class PayFromCar extends PayActivity implements OnClickListener{
    private TextView tv_pay;
    private LinearLayout titleback_linear_back, ll_sh;
    private String orderId = "";
    private String outTradeNo;
    private String subject;
    private String body;
    private String price = "0.02";
    private int p;

    private PayOrderInfo entity;
    private ShopPayOrderInfo shopEntity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);

        orderId = getIntent().getExtras().getString("orderId", "");
        p=getIntent().getIntExtra("p", 1);

        new TitleMenuUtil(PayFromCar.this, "选择支付方式").show();

        if (orderId.equals("")) {
            Toast.makeText(this, "没有传订单id", Toast.LENGTH_SHORT).show();
        }

        tv_pay=(TextView) findViewById(R.id.tv_pay);

        titleback_linear_back = (LinearLayout) findViewById(R.id.titleback_linear_back);
        titleback_linear_back.setOnClickListener(this);
        ll_sh = (LinearLayout) findViewById(R.id.ll_sh);
        ll_sh.setOnClickListener(this);

        getData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleback_linear_back:
                dialogIntent();
                break;
            case R.id.ll_sh:
                pay(outTradeNo, subject, body, price);
                break;
            default:
                break;
        }
    }

    private void dialogIntent() {
        Dialog dialog = new DialogUtil(PayFromCar.this,
                "是否放弃付款？").getCheck(new CallBackChange() {

            @Override
            public void change() {
                Intent intent = new Intent(PayFromCar.this, OrderDetail.class);
                intent.putExtra("status",1);
                intent.putExtra("id", orderId);
                intent.putExtra("p", p);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            dialogIntent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("id", Integer.parseInt(orderId));

        String strParams = params.toString();

        Events.CommonRequestEvent event;
        if (1 == p) {
            event = new Events.PayOrderInfoEvent();
        } else {
            event = new Events.ShopPayOrderInfoEvent();
        }

        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.PayOrderInfoCompleteEvent event) {
        if (event.getSuccess()) {

            entity = event.getEntity();
            outTradeNo = entity.getOrder_number();

            price = "" + StringUtil.priceShow(entity.getShengyu_price());

            setText("tv_pay", "￥" + price);


        }

    }

    public void onEventMainThread(Events.ShopPayOrderInfoCompleteEvent event) {
        if (event.getSuccess()) {

            shopEntity = event.getEntity();
            outTradeNo = shopEntity.getOrder_number();

            price = "" + StringUtil.priceShow(shopEntity.getTotal_price());

            setText("tv_pay", "￥" + price);


        }

    }

    @Override
    public void success() {
        gotoDetail(2);
    }
    @Override
    public void handling() {
        gotoDetail(1);
    }
    @Override
    public void fail() {
        gotoDetail(1);
    }

    private void gotoDetail(int status) {
        Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
        intent.putExtra("status", status);
        intent.putExtra("id", orderId);
        intent.putExtra("p", p);
        startActivity(intent);
        finish();
    }
}
