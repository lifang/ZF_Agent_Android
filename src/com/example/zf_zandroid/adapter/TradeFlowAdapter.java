package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeRecord;
import com.posagent.activities.trade.TradeDetailActivity;

import java.util.List;


public class TradeFlowAdapter extends BaseAdapter{
    private Context context;
    private List<TradeRecord> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public TradeFlowAdapter(Context context, List<TradeRecord> list) {
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
        final TradeRecord entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.trade_flow_item, null);
            convertView.setTag(holder);

            holder.trade_time = (TextView) convertView.findViewById(R.id.trade_time);
            holder.trade_account = (TextView) convertView.findViewById(R.id.trade_account);
            holder.trade_receive_account = (TextView) convertView.findViewById(R.id.trade_receive_account);
            holder.trade_client_number = (TextView) convertView.findViewById(R.id.trade_client_number);
            holder.trade_status = (TextView) convertView.findViewById(R.id.trade_status);
            holder.trade_amount = (TextView) convertView.findViewById(R.id.trade_amount);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        // fill data
        holder.trade_time.setText(entity.getTradedTimeStr());
        holder.trade_account.setText(entity.getPayFromAccount());
        holder.trade_receive_account.setText(entity.getPayIntoAccount());
        holder.trade_client_number.setText(entity.getTerminalNumber());

        String statusName = "未知";

        if (entity.getTradedStatus() == 1) {
            statusName = "交易成功";
        } else if (entity.getTradedStatus() == 2) {
            statusName = "交易失败";
        } else if (entity.getTradedStatus() == 3) {
            statusName = "交易结果待确认";
        }

        holder.trade_status.setText(statusName);
        holder.trade_amount.setText("￥" + StringUtil.priceShow(entity.getAmount()));

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, TradeDetailActivity.class);
                i.putExtra("id", entity.getId());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView trade_time, trade_account, trade_receive_account, trade_client_number,
                trade_status, trade_amount;
    }
}
