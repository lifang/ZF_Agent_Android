package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.ChannelEntity;
import com.example.zf_android.entity.ChannelTradeEntity;
import com.example.zf_android.entity.ProfitEntity;
import com.example.zf_android.entity.ProfitTradeEntity;
import com.posagent.activities.agent.AgentProfitList;

import java.util.ArrayList;
import java.util.List;


public class ProfitAdapter extends BaseAdapter{
    private Context context;
    private List<ProfitEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public ProfitAdapter(Context context, List<ProfitEntity> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProfitEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if(true){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.profit_item, null);
            convertView.setTag(holder);

            //init

            ChannelEntity channel = entity.getChannel();
            holder.iv_delete = (ImageView)convertView.findViewById(R.id.iv_delete);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AgentProfitList)context).deleteProfit(entity);

                }
            });

            holder.tv_channel = (TextView)convertView.findViewById(R.id.tv_channel);
            holder.tv_channel.setText("支付通道：" + channel.getName());

            final LinearLayout container = holder.ll_trade_container = (LinearLayout)convertView.findViewById(R.id.ll_trade_container);
            LinearLayout llItem = null;

            List<ChannelTradeEntity> trades = channel.getTrades();
            if (null != trades) {
                for (ChannelTradeEntity trade: entity.getChannel().getTrades()) {
                    llItem = (LinearLayout)inflater.inflate(R.layout.profit_trade_item, null);
                    TextView tv_trade_naem = (TextView)llItem.findViewById(R.id.tv_trade_name);
                    tv_trade_naem.setText(trade.getTrade_value());

                    final TextView tv_percent = (TextView)llItem.findViewById(R.id.tv_percent);
                    final ProfitTradeEntity profitTradeEntity = getProfitTradeEntity(entity, trade);
                    tv_percent.setText("" + profitTradeEntity.getPercent() + "%");

                    llItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((AgentProfitList)context).changeProfit(tv_percent, entity, profitTradeEntity);

                        }
                    });
                    container.addView(llItem);
                }

                if (null != llItem) {
                    llItem.findViewById(R.id.v_bottom_line).setVisibility(View.GONE);
                }

            }

        } else {
            holder = (ViewHolder)convertView.getTag();
        }


        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_channel;
        public LinearLayout ll_trade_container;
        public ImageView iv_delete;
    }

    public ProfitTradeEntity getProfitTradeEntity(ProfitEntity profit, ChannelTradeEntity channelTradeEntity) {
        List<ProfitTradeEntity> trades = profit.getDetail();
        if (null != trades) {
            for (ProfitTradeEntity trade: trades) {
                if (trade.getTradeTypeId() == channelTradeEntity.getId()) {
                    return trade;
                }
            }

        } else {
            trades = new ArrayList<ProfitTradeEntity>();
            profit.setDetail(trades);
        }

        ProfitTradeEntity trade = new ProfitTradeEntity();
        trade.setPercent("0");
        trade.setTradeTypeId(channelTradeEntity.getId());
        trade.setTradeTypeName(channelTradeEntity.getName());


        profit.getDetail().add(trade);

        return trade;


    }
}
