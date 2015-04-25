package com.example.zf_android.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.DialogUtil;
import com.examlpe.zf_android.util.DialogUtil.CallBackChange;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.alipay.PayActivity;
import com.example.zf_android.R;
import com.example.zf_android.entity.Goodlist;
import com.example.zf_android.entity.OrderDetailEntity;
import com.google.gson.Gson;
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
    private String price;
    private int p;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_pay.setText("￥  "+price);
                    break;
            }
        }
    };

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
                Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
                intent.putExtra("status",1);
                intent.putExtra("id", orderId);
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
        params.put("id", orderId);

        String strParams = params.toString();

        Events.CommonRequestEvent event;
        if (p == 1) {
            event = new Events.OrderDetailPigouEvent();
        } else {
            event = new Events.OrderDetailDaigouEvent();
        }

        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.OrderDetailCompleteEvent event) {
        if (event.getSuccess()) {
            Gson gson = new Gson();
            String res = event.getResult().toString();
            OrderDetailEntity orderDetail = gson.fromJson(res, OrderDetailEntity.class);
            List<Goodlist> goodlist= orderDetail.getOrder_goodsList();
            if (goodlist.size()>0) {
                subject = goodlist.get(0).getGood_name();
                body = subject;
            }
            outTradeNo = orderDetail.getOrder_number();
            price = orderDetail.getOrder_totalPrice();
            price = String.format("%.2f", Integer.parseInt(price)/100f);
            handler.sendEmptyMessage(0);

        }

    }

    @Override
    public void success() {
        Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
        intent.putExtra("status",2);
        intent.putExtra("id", orderId);
        intent.putExtra("p", p);
        startActivity(intent);
        finish();
    }
    @Override
    public void handling() {
        Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
        intent.putExtra("status",1);
        intent.putExtra("id", orderId);
        intent.putExtra("p", p);
        startActivity(intent);
        finish();
    }
    @Override
    public void fail() {
        Intent intent = new Intent(PayFromCar.this,OrderDetail.class);
        intent.putExtra("status",1);
        intent.putExtra("id", orderId);
        intent.putExtra("p", p);
        startActivity(intent);
        finish();
    }
}
