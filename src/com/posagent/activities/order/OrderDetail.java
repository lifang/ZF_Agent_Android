package com.posagent.activities.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.ScrollViewWithListView;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.Goodlist;
import com.example.zf_android.entity.MarkEntity;
import com.example.zf_android.entity.OrderDetailEntity;
import com.example.zf_zandroid.adapter.OrderDetail_PosAdapter;
import com.example.zf_zandroid.adapter.RecordAdapter;
import com.google.gson.Gson;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 *
 * 订单详情
 *
 */
public class OrderDetail extends BaseActivity implements OnClickListener{
    private  ScrollViewWithListView   pos_lv,his_lv;
    List<Goodlist>  goodlist = new ArrayList<Goodlist>();
    List<MarkEntity>  relist = new ArrayList<MarkEntity>();
    private OrderDetail_PosAdapter posAdapter;
    private RecordAdapter reAdapter;
    private LinearLayout ll_ishow;
    private Button btn_ishow, btn_cancel, btn_pay, btn_comment;
    private OrderDetailEntity  orderDetailEntity;
    private TextView tv_status,tv_sjps,tv_psf,tv_reperson,tv_tel,
            tv_adress,tv_ly,tv_fplx,fptt,
            tv_ddbh,tv_pay,tv_time,tv_gj,tv_money,
            tv_dingjin, tv_real_pay, tv_yifahuo, tv_dingjin_payed,
            tv_left;
    private int status, id, p;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    OrderDetailEntity entity = orderDetailEntity;
                    tv_real_pay.setText("实付金额 ：￥ " + entity.getOrder_totalPrice());
                    tv_dingjin.setText("定金总金额 ：￥ "+entity.getTotal_dingjin());
                    tv_dingjin_payed.setText("已支付定金 ：￥ "+entity.getZhifu_dingjin());
                    tv_yifahuo.setText("已发货数量 ："+entity.getShipped_quantity());
                    int left = Integer.parseInt(entity.getTotal_quantity()) - Integer.parseInt(entity.getShipped_quantity());
                    tv_left.setText("剩余货品数量 ："+ left);
                    tv_reperson.setText("收件人 ： "+entity.getOrder_receiver());
                    tv_tel.setText(entity.getOrder_receiver_phone());
                    tv_adress.setText("收货地址 ："+entity.getOrder_address());
                    tv_ly.setText("留言 ："+entity.getOrder_comment());
                    tv_fplx.setText(entity.getOrder_invoce_type().equals("1")?"发票类型 : 个人":"发票类型 : 公司");
                    fptt.setText("发票抬头："+entity.getOrder_invoce_info());
                    tv_ddbh.setText("订单编号 ："+entity.getOrder_number());
                    tv_pay.setText("支付方式 ："+entity.getOrder_payment_type());
                    tv_time.setText("实付金额 ：￥"+entity.getOrder_totalPrice());
                    tv_money.setText("订单日期 ："+entity.getOrder_createTime());
                    tv_gj.setText("共计 ："+entity.getTotal_quantity()+"件");

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        status=getIntent().getIntExtra("status", 0);
        id=getIntent().getIntExtra("id", 1);
        p=getIntent().getIntExtra("p", 1);
        if (p > 1) {
            new TitleMenuUtil(OrderDetail.this, "代购订单详情").show();
        } else {
            new TitleMenuUtil(OrderDetail.this, "批购订单详情").show();
        }

        initView();
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("id", id);

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
            orderDetailEntity = gson.fromJson(res, OrderDetailEntity.class);
            goodlist = orderDetailEntity.getOrder_goodsList() ;
            relist = orderDetailEntity.getComments().getList() ;

            posAdapter = new OrderDetail_PosAdapter(OrderDetail.this, goodlist,status);
            pos_lv.setAdapter(posAdapter);
            reAdapter=new RecordAdapter(OrderDetail.this, relist);
            his_lv.setAdapter(reAdapter);
            handler.sendEmptyMessage(0);

            setStatus();
        }

    }

    public void onEventMainThread(Events.CancelOrderCompleteEvent event) {
        if (event.getSuccess()) {
            getData();
        }

    }

    private void initView() {
        tv_money=(TextView) findViewById(R.id.tv_money);

        tv_gj=(TextView) findViewById(R.id.tv_gj);
        tv_real_pay=(TextView) findViewById(R.id.tv_real_pay);
        tv_dingjin=(TextView) findViewById(R.id.tv_dingjin);
        tv_dingjin_payed=(TextView) findViewById(R.id.tv_dingjin_payed);
        tv_yifahuo=(TextView) findViewById(R.id.tv_yifahuo);
        tv_left=(TextView) findViewById(R.id.tv_left);

        btn_ishow=(Button) findViewById(R.id.btn_ishow);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_pay=(Button) findViewById(R.id.btn_pay);
        btn_comment=(Button) findViewById(R.id.btn_comment);
        btn_ishow.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_comment.setOnClickListener(this);

        tv_ddbh=(TextView) findViewById(R.id.tv_ddbh);
        fptt=(TextView) findViewById(R.id.fptt);
        tv_pay=(TextView) findViewById(R.id.tv_pay);
        tv_fplx=(TextView) findViewById(R.id.tv_fplx);
        tv_ly=(TextView) findViewById(R.id.tv_ly);
        tv_adress=(TextView) findViewById(R.id.tv_adress);
        tv_reperson=(TextView) findViewById(R.id.tv_reperson);
        tv_tel=(TextView) findViewById(R.id.tv_tel);
        tv_time=(TextView) findViewById(R.id.tv_time);
        tv_psf=(TextView) findViewById(R.id.tv_psf);
        tv_status=(TextView) findViewById(R.id.tv_status);
        ll_ishow=(LinearLayout) findViewById(R.id.ll_ishow);
        ll_ishow.setVisibility(status==1 ? View.INVISIBLE
                : View.VISIBLE); // 只有状态是1 才有下面的按钮
        pos_lv=(ScrollViewWithListView) findViewById(R.id.pos_lv1);

        his_lv=(ScrollViewWithListView) findViewById(R.id.his_lv);
        setStatus();
    }

    private void setStatus() {
        if (orderDetailEntity != null) {
            status = orderDetailEntity.getOrder_status();
        }
        switch (status) {
            case 1:
                tv_status.setText("未付款");
                btn_ishow.setVisibility(View.GONE);
                break;
            case 2:
                tv_status.setText("已付款");
                btn_ishow.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                break;
            case 3:
                tv_status.setText("已发货");
                btn_ishow.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                break;
            case 4:
                tv_status.setText("已评价");
                btn_ishow.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);

                break;
            case 5:
                tv_status.setText("已取消");
                btn_ishow.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                break;
            case 6:
                tv_status.setText("交易关闭");
                btn_ishow.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ishow:
                Toast.makeText(getApplicationContext(), "请先付款···", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_pay:
                break;
            case R.id.btn_comment:
                break;
            case R.id.btn_cancel:
                doCancel();
                break;
            default:
                break;
        }

        super.onClick(v);
    }

    //helper
    private void doCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
        builder.setMessage("确定取消订单吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JsonParams params = new JsonParams();
                params.put("id", id);
                String strParams = params.toString();

                Events.CommonRequestEvent event;
                if (p > 1) {
                    event = new Events.CancelOrderDaigouEvent();
                } else {
                    event = new Events.CancelOrderPigouEvent();
                }
                event.setParams(strParams);
                EventBus.getDefault().post(event);

                Toast.makeText(OrderDetail.this, "正在取消...", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}
