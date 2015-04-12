package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.StockAgentEntity;

import java.util.List;


public class StockAgentAdapter extends BaseAdapter{
    private Context context;
    private List<StockAgentEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public StockAgentAdapter(Context context, List<StockAgentEntity> list) {
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

        final StockAgentEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stock_agent_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
            holder.tv_historyCount = (TextView) convertView.findViewById(R.id.tv_historyCount);
            holder.tv_openCount = (TextView) convertView.findViewById(R.id.tv_openCount);
            holder.tv_lastPrepareTime = (TextView) convertView.findViewById(R.id.tv_lastPrepareTime);
            holder.tv_lastOpenTime = (TextView) convertView.findViewById(R.id.tv_lastOpenTime);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // fill data
        holder.tv_company_name.setText(entity.getCompany_name());
        holder.tv_historyCount.setText(String.valueOf(entity.getHoitoryCount()));
        holder.tv_openCount.setText(String.valueOf(entity.getOpenCount()));
        holder.tv_lastPrepareTime.setText("上次配送日期：" + String.valueOf(entity.getLastPrepareTime()));
        holder.tv_lastOpenTime.setText("上次开通日期：" + String.valueOf(entity.getLastOpenTime()));


        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_company_name, tv_historyCount, tv_openCount,
                tv_lastPrepareTime, tv_lastOpenTime;
    }
}
