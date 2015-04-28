package com.posagent.activities.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.activity.PayFromCar;
import com.example.zf_android.entity.Goodlist;
import com.example.zf_android.entity.MarkEntity;
import com.example.zf_android.entity.OrderDetailEntity;
import com.example.zf_zandroid.adapter.OrderDetail_PosAdapter;
import com.example.zf_zandroid.adapter.RecordAdapter;
import com.google.gson.Gson;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.goods.GoodsDetail;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 *
 * 订单详情
 *
 */
public class OrderDetail extends BaseActivity implements OnClickListener{

    OrderDetail context = this;

    private  ScrollViewWithListView   pos_lv,his_lv;
    List<Goodlist>  goodlist = new ArrayList<Goodlist>();
    List<MarkEntity>  relist = new ArrayList<MarkEntity>();
    private OrderDetail_PosAdapter posAdapter;
    private RecordAdapter reAdapter;
    private LinearLayout ll_ishow;
    private Button btn_view_terminals, btn_cancel, btn_pay, btn_comment;
    private OrderDetailEntity  orderDetailEntity;
    private TextView tv_status,tv_sjps,tv_psf,tv_reperson,tv_tel,
            tv_adress,tv_ly,tv_fplx,fptt, tv_order_type,
            tv_ddbh,tv_pay,tv_time,tv_gj,tv_money,
            tv_dingjin, tv_real_pay, tv_yifahuo, tv_dingjin_payed,
            tv_left;

    private TextView tv_status_daigou, tv_real_pay_daigou, tv_guishu;
    private int status, id, p;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    OrderDetailEntity entity = orderDetailEntity;

                    tv_real_pay.setText("实付金额 ：￥ " + StringUtil.priceShow(entity.getOrder_totalPrice()));
                    tv_real_pay_daigou.setText("实付金额 ：￥ " + StringUtil.priceShow(entity.getOrder_totalPrice()));
                    tv_ddbh.setText("订单编号 ：" + entity.getOrder_number());
                    tv_pay.setText("支付方式 ：" + entity.getOrder_payment_type());
                    tv_money.setText("实付金额 ：￥" + StringUtil.priceShow(entity.getOrder_totalPrice()));
                    tv_time.setText("订单日期 ：" + entity.getOrder_createTime());


                    tv_reperson.setText("收件人 ： " + entity.getOrder_receiver());
                    tv_tel.setText(entity.getOrder_receiver_phone());
                    tv_adress.setText("收货地址 ：" + entity.getOrder_address());
                    tv_ly.setText("留言 ：" + entity.getOrder_comment());
                    tv_fplx.setText(entity.getOrder_invoce_type().equals("1") ? "发票类型 : 个人" : "发票类型 : 公司");
                    fptt.setText("发票抬头：" + entity.getOrder_invoce_info());

                    if (p == Constants.Goods.BuyTypePigou) {
                        //show hide elements
                        hide("btn_view_terminals");
                        hide("ll_header_daigou");
                        show("ll_header_pigou");
                        setText("tv_order_type", "订购类型：批购");

                        tv_dingjin.setText("定金总金额 ：￥ " + StringUtil.priceShow(entity.getTotal_dingjin()));
                        tv_gj.setText("共计 ：" + entity.getTotal_quantity() + "件");
                        tv_dingjin_payed.setText("已支付定金 ：￥ " + StringUtil.priceShow(entity.getZhifu_dingjin()));
                        tv_yifahuo.setText("已发货数量 ：" + entity.getShipped_quantity());
                        int left = Integer.parseInt(entity.getTotal_quantity()) - Integer.parseInt(entity.getShipped_quantity());
                        tv_left.setText("剩余货品数量 ：" + left);

                    } else {
                        //show hide
                        show("btn_view_terminals");
                        show("ll_header_daigou");
                        hide("ll_header_pigou");
                        setText("tv_order_type", "订购类型：代购");
                        setText("tv_guishu", "归属用户：\n" + entity.getGuishu_user());


                        tv_gj.setText("共计 ：" + entity.getOrder_totalNum() + "件");


                    }


                    ViewHelper.initOrderActions(getWindow().getDecorView().findViewById(android.R.id.content), entity.getOrder_status(), p);

                    updateView();

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        status=getIntent().getIntExtra("status", 0);
        id=getIntent().getIntExtra("id", 0);
        if (id == 0) { //容错
            String strId = getIntent().getStringExtra("id");
            id = Integer.parseInt(strId);
        }
        p=getIntent().getIntExtra("p", 1);
        if (p != Constants.Goods.BuyTypePigou) {
            new TitleMenuUtil(OrderDetail.this, "代购订单详情").show();
        } else {
            new TitleMenuUtil(OrderDetail.this, "批购订单详情").show();
        }

        initView();
        getData();
    }

    private void initView() {
        tv_money=(TextView) findViewById(R.id.tv_money);
        tv_status_daigou=(TextView) findViewById(R.id.tv_status_daigou);
        tv_real_pay_daigou=(TextView) findViewById(R.id.tv_real_pay_daigou);
        tv_guishu=(TextView) findViewById(R.id.tv_guishu);


        tv_gj=(TextView) findViewById(R.id.tv_gj);
        tv_real_pay=(TextView) findViewById(R.id.tv_real_pay);
        tv_dingjin=(TextView) findViewById(R.id.tv_dingjin);
        tv_dingjin_payed=(TextView) findViewById(R.id.tv_dingjin_payed);
        tv_yifahuo=(TextView) findViewById(R.id.tv_yifahuo);
        tv_left=(TextView) findViewById(R.id.tv_left);

        tv_ddbh=(TextView) findViewById(R.id.tv_ddbh);
        fptt=(TextView) findViewById(R.id.fptt);
        tv_pay=(TextView) findViewById(R.id.tv_pay);
        tv_fplx=(TextView) findViewById(R.id.tv_fplx);
        tv_ly=(TextView) findViewById(R.id.tv_ly);
        tv_adress=(TextView) findViewById(R.id.tv_adress);
        tv_reperson=(TextView) findViewById(R.id.tv_reperson);
        tv_tel=(TextView) findViewById(R.id.tv_tel);
        tv_time=(TextView) findViewById(R.id.tv_time);
//        tv_psf=(TextView) findViewById(R.id.tv_psf);
        tv_status=(TextView) findViewById(R.id.tv_status);
        ll_ishow=(LinearLayout) findViewById(R.id.ll_ishow);
        pos_lv=(ScrollViewWithListView) findViewById(R.id.pos_lv1);

        his_lv=(ScrollViewWithListView) findViewById(R.id.his_lv);

    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("id", id);

        String strParams = params.toString();

        Events.CommonRequestEvent event;
        if (p == Constants.Goods.BuyTypePigou) {
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

            posAdapter = new OrderDetail_PosAdapter(OrderDetail.this, goodlist, status);
            pos_lv.setAdapter(posAdapter);
            reAdapter=new RecordAdapter(OrderDetail.this, relist);
            his_lv.setAdapter(reAdapter);
            handler.sendEmptyMessage(0);

            updateView();
        }

    }

    public void onEventMainThread(Events.CancelOrderCompleteEvent event) {
        if (event.getSuccess()) {
            getData();
        }

    }

    private void updateView() {
        if (status != Constants.Order.StatusUnpay) {
            hide("ll_ishow");
        }

        // init actions click
        findViewById(R.id.btn_action_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancel(orderDetailEntity);
            }
        });

        findViewById(R.id.btn_action_pay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PayFromCar.class);
                i.putExtra("p", p);
                i.putExtra("orderId", "" + orderDetailEntity.getOrder_id());
                context.startActivity(i);
            }
        });

        findViewById(R.id.btn_action_dingjin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PayFromCar.class);
                i.putExtra("p", p);
                i.putExtra("orderId", "" + orderDetailEntity.getOrder_id());
                context.startActivity(i);
            }
        });

        int tmpGoodsId = 0;
        try {
            tmpGoodsId = Integer.parseInt(orderDetailEntity.getOrder_goodsList().get(0).getGood_id());
        } catch (Exception ex) {
//            Log.d("UncatchException", ex.getMessage());
        }

        final int goodsId = tmpGoodsId;

        findViewById(R.id.btn_action_pigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, GoodsDetail.class);
                i.putExtra("id", goodsId);
                i.putExtra("buyType", mapBuyType());
                context.startActivity(i);
            }
        });

        findViewById(R.id.btn_action_daigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, GoodsDetail.class);
                i.putExtra("id", goodsId);
                i.putExtra("buyType", mapBuyType());
                context.startActivity(i);
            }
        });

        setStatus();
    }

    private void setStatus() {
        if (orderDetailEntity != null) {
            status = orderDetailEntity.getOrder_status();
        }
        String statusShow = "";
        switch (status) {
            case Constants.Order.StatusUnpay:
                statusShow = "未付款";
                break;
            case Constants.Order.StatusPayed:
                statusShow = "已付款";
                break;
            case Constants.Order.StatusSent:
                statusShow = "已发货";
                break;
            case Constants.Order.StatusComment:
                statusShow = "已评价";

                break;
            case Constants.Order.StatusCanceled:
                statusShow = "已取消";
                break;
            case Constants.Order.StatusClosed:
                statusShow = "交易关闭";
                break;

            default:
                break;
        }

        tv_status.setText(statusShow);
        tv_status_daigou.setText(statusShow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_terminals:
//                Toast.makeText(getApplicationContext(), "请先付款···", Toast.LENGTH_LONG).show();
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

    //helper
    private void doCancel(final OrderDetailEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
        builder.setMessage("确定取消订单吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JsonParams params = new JsonParams();
                params.put("id", entity.getOrder_id());
                String strParams = params.toString();

                Events.CommonRequestEvent event;
                if (p == Constants.Goods.BuyTypePigou) {
                    event = new Events.CancelOrderPigouEvent();
                } else {
                    event = new Events.CancelOrderDaigouEvent();
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

    private int mapBuyType() {
        int orderType = 0;
        int buyType = p;

        switch (buyType) {
            case Constants.Goods.BuyTypePigou:
                orderType = Constants.Goods.OrderTypePigou;
                break;
            case Constants.Goods.BuyTypeDaigou:
                orderType = Constants.Goods.OrderTypeDaigou;
                break;
            case Constants.Goods.BuyTypeDaizulin:
                orderType = Constants.Goods.OrderTypeDaizulin;
                break;
        }
        return orderType;
    }
}
