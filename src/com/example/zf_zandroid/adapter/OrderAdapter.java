package com.example.zf_zandroid.adapter;

import android.content.Context;
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

import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.posagent.activities.order.OrderDetail;
import com.squareup.picasso.Picasso;

import java.util.List;



public class OrderAdapter extends BaseAdapter{
    private Context context;
    private List<OrderEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public OrderAdapter(Context context, List<OrderEntity> list) {
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

        OrderEntity entity = list.get(position);

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

            holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
            holder.tv_psf = (TextView) convertView.findViewById(R.id.tv_psf);
            holder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);

            holder.tv_gtd = (TextView) convertView.findViewById(R.id.tv_gtd);
            holder.content2 = (TextView) convertView.findViewById(R.id.content2);
            holder.content_pp = (TextView) convertView.findViewById(R.id.content_pp);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_goodnum = (TextView) convertView.findViewById(R.id.tv_goodnum);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        String face_url = entity.getOrder_goodsList().get(0).getGood_logo();
        try {
            Picasso.with(context).load(face_url).into(holder.iv_face);
        } catch (Exception ex) {
            Log.d("OrderAdapter", ex.getMessage());
        }


        holder.tv_price.setText("￥"+entity.getOrder_goodsList().get(0).getGood_price());
        holder.content2.setText(entity.getOrder_goodsList().get(0).getGood_brand());
        holder.tv_gtd.setText(entity.getOrder_goodsList().get(0).getGood_channel());
        holder.content_pp.setText(entity.getOrder_goodsList().get(0).getGood_name());

        holder.tv_goodnum.setText("X   "+entity.getOrder_goodsList().get(0).getGood_num());

        holder.tv_pay.setText("实付：￥"+entity.getOrder_totalPrice()/100);
        holder.tv_psf.setText("配送费：￥"+entity.getOrder_psf()	);
        holder.tv_ddbh.setText("订单编号: "+entity.getOrder_number()	);
        holder.tv_time.setText(entity.getOrder_createTime()	);
        holder.tv_sum.setText("共计:   "+entity.getOrder_totalNum()	+"件");
        switch (entity.getOrder_status()) {
            case 1:
                holder.tv_status.setText("未付款");
                holder.ll_ishow.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.tv_status.setText("已付款");
                holder.ll_ishow.setVisibility(View.GONE);
                break;
            case 3:
                holder.tv_status.setText("已发货");
                holder.ll_ishow.setVisibility(View.GONE);
                break;
            case 4:
                holder.tv_status.setText("已评价");
                holder.ll_ishow.setVisibility(View.GONE);
                break;
            case 5:
                holder.tv_status.setText("已取消");
                holder.ll_ishow.setVisibility(View.GONE);
                break;
            case 6:
                holder.tv_status.setText("交易关闭");
                holder.ll_ishow.setVisibility(View.GONE);
                break;
            default:

                holder.ll_ishow.setVisibility(View.GONE);
                break;
        }

        holder.tv_ddbh.setText("订单编号: "+entity.getOrder_number()	);
        holder.tv_ddbh.setText("订单编号: "+entity.getOrder_number()	);
        holder.tv_ddbh.setText("订单编号: "+entity.getOrder_number()	);


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context, OrderDetail.class);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_goodnum,tv_price,content,tv_ddbh,tv_time,tv_status,tv_sum,tv_psf,tv_pay,tv_gtd,content2,content_pp;
        private LinearLayout ll_ishow;
        public ImageView iv_face;
    }
}
