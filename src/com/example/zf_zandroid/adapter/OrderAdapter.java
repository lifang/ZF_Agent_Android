package com.example.zf_zandroid.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.StringUtil;
import com.example.zf_android.R;
import com.example.zf_android.activity.PayFromCar;
import com.example.zf_android.entity.OrderEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.goods.GoodsDetail;
import com.posagent.activities.order.OrderDetail;
import com.posagent.activities.order.OrderList;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;

import java.util.List;

import de.greenrobot.event.EventBus;


public class OrderAdapter extends BaseAdapter{
    private BaseActivity context;
    private List<OrderEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public OrderAdapter(BaseActivity context, List<OrderEntity> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final OrderEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_item, null);
            holder.content = (TextView) convertView.findViewById(R.id.content_pp);
            holder.tv_ddbh = (TextView) convertView.findViewById(R.id.tv_ddbh);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.iv_face = (ImageView) convertView.findViewById(R.id.iv_face);

            holder.ll_ishow = (LinearLayout) convertView.findViewById(R.id.ll_ishow);


            holder.tv_gtd = (TextView) convertView.findViewById(R.id.tv_gtd);
            holder.content2 = (TextView) convertView.findViewById(R.id.content2);
            holder.content_pp = (TextView) convertView.findViewById(R.id.content_pp);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_origin_price);
            holder.tv_goodnum = (TextView) convertView.findViewById(R.id.tv_goodnum);
            holder.tv_guishu = (TextView) convertView.findViewById(R.id.tv_guishu);
            holder.tv_peisongfei = (TextView) convertView.findViewById(R.id.tv_peisongfei);
            holder.tv_shifu = (TextView) convertView.findViewById(R.id.tv_shifu);

            holder.tv_heji = (TextView) convertView.findViewById(R.id.tv_heji);
            holder.tv_dingjin_payed = (TextView) convertView.findViewById(R.id.tv_dingjin_payed);
            holder.tv_yifahuo = (TextView) convertView.findViewById(R.id.tv_yifahuo);
            holder.tv_left = (TextView) convertView.findViewById(R.id.tv_left);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        String face_url = entity.getOrder_goodsList().get(0).getGood_logo();
        try {
            ImageCacheUtil.IMAGE_CACHE.get(face_url, holder.iv_face);
        } catch (Exception ex) {
            Log.d("OrderAdapter", ex.getMessage());
        }


        holder.tv_origin_price.setText("原价：￥"+ StringUtil.priceShow(entity.getOrder_goodsList().get(0).getGood_price()));
        holder.tv_price.setText("￥"+ StringUtil.priceShow(entity.getOrder_goodsList().get(0).getGood_batch_price()));
        holder.content2.setText(entity.getOrder_goodsList().get(0).getGood_brand());
        holder.tv_gtd.setText(entity.getOrder_goodsList().get(0).getGood_channel());
        holder.content_pp.setText(entity.getOrder_goodsList().get(0).getGood_name());

        holder.tv_goodnum.setText("X   "+entity.getOrder_goodsList().get(0).getGood_num());

        holder.tv_ddbh.setText("订单编号: "+entity.getOrder_number()	);
        holder.tv_time.setText(entity.getOrder_createTime()	);

        holder.tv_heji.setText("合计：￥" + StringUtil.priceShow(entity.getActual_price()));
        holder.tv_shifu.setText("实付：￥" + StringUtil.priceShow(entity.getOrder_totalPrice()));
        holder.tv_dingjin_payed.setText("已付定金：￥" + StringUtil.priceShow(entity.getZhifu_dingjin()));
        holder.tv_yifahuo.setText("已发货数量：" + entity.getShipped_quantity());
        holder.tv_left.setText("剩余金额：￥" + StringUtil.priceShow(entity.getShengyu_price()));
        holder.tv_guishu.setText("归属用户：" + entity.getGuishu_user());
        holder.tv_peisongfei.setText("配送费：" + entity.getOrder_psf());

        switch (entity.getOrder_status()) {
            case 1:
                holder.tv_status.setText("未付款");
                break;
            case 2:
                holder.tv_status.setText("已付定金");
                break;
            case 3:
                holder.tv_status.setText("已完成");
                break;
            case 5:
                holder.tv_status.setText("已取消");
                break;
            default:
                holder.tv_status.setText("已取消" + entity.getOrder_status());

                break;
        }

        // 判断类别
        if(buyType() == Constants.Goods.BuyTypePigou) {
            //pigou
            convertView.findViewById(R.id.ll_daigou).setVisibility(View.GONE);
            convertView.findViewById(R.id.ll_pigou).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.ll_heji).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_origin_price).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.ll_daigou).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.ll_pigou).setVisibility(View.GONE);
            convertView.findViewById(R.id.ll_heji).setVisibility(View.GONE);
            convertView.findViewById(R.id.tv_origin_price).setVisibility(View.GONE);
        }

        // init actions click
        convertView.findViewById(R.id.btn_action_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancel(entity);
            }
        });

        convertView.findViewById(R.id.btn_action_pay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PayFromCar.class);
                i.putExtra("p", buyType());
                i.putExtra("orderId", "" + entity.getOrder_id());
                context.startActivity(i);
            }
        });

        convertView.findViewById(R.id.btn_action_dingjin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PayFromCar.class);
                i.putExtra("p", buyType());
                i.putExtra("orderId", "" + entity.getOrder_id());
                context.startActivity(i);
            }
        });

        int tmpGoodsId = 0;
        try {
            tmpGoodsId = Integer.parseInt(entity.getOrder_goodsList().get(0).getGood_id());
        } catch (Exception ex) {
            Log.d("UncatchException", ex.getMessage());
        }

        final int goodsId = tmpGoodsId;

        convertView.findViewById(R.id.btn_action_pigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, GoodsDetail.class);
                i.putExtra("id", goodsId);
                i.putExtra("buyType", mapBuyType());
                context.startActivity(i);
            }
        });

        convertView.findViewById(R.id.btn_action_daigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, GoodsDetail.class);
                i.putExtra("id", goodsId);
                i.putExtra("buyType", mapBuyType());
                context.startActivity(i);
            }
        });



        ViewHelper.initOrderActions(convertView, entity.getOrder_status(), buyType());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, OrderDetail.class);
                i.putExtra("status", entity.getOrder_status());
                i.putExtra("id", entity.getOrder_id());
                i.putExtra("p", buyType());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_goodnum,tv_price,content,content2,tv_ddbh,tv_time,tv_status,
                tv_heji,tv_dingjin_payed,tv_yifahuo,tv_left, tv_gtd,
                content_pp,tv_origin_price, tv_guishu, tv_peisongfei, tv_shifu;
        private LinearLayout ll_ishow;
        public ImageView iv_face;
    }

    private int buyType() {
        return ((OrderList)context).buyType();
    }

    //helper
    private void doCancel(final OrderEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定取消订单吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JsonParams params = new JsonParams();
                params.put("id", entity.getOrder_id());
                String strParams = params.toString();

                Events.CommonRequestEvent event;
                if (buyType() == Constants.Goods.BuyTypePigou) {
                    event = new Events.CancelOrderPigouEvent();
                } else {
                    event = new Events.CancelOrderDaigouEvent();
                }
                event.setParams(strParams);
                EventBus.getDefault().post(event);

                Toast.makeText(context, "正在取消...", Toast.LENGTH_SHORT).show();
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
        int buyType = buyType();

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
