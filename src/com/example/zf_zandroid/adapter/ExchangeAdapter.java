package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.ExchangeEntity;
import com.posagent.activities.agent.AgentCargoExchangeDetail;

import java.util.List;


public class ExchangeAdapter extends BaseAdapter{
    private Context context;
    private List<ExchangeEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public ExchangeAdapter(Context context, List<ExchangeEntity> list) {
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
        final ExchangeEntity entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.agent_cargo_exchange_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_company_from = (TextView) convertView.findViewById(R.id.tv_company_from);
            holder.tv_company_to = (TextView) convertView.findViewById(R.id.tv_company_to);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);


        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_company_from.setText("" + entity.getFromname());
        holder.tv_company_to.setText("" + entity.getToname());
        holder.tv_count.setText("" + entity.getQuantity());
        holder.tv_create_time.setText("" + entity.getCreated_at());


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, AgentCargoExchangeDetail.class);
                i.putExtra("id", entity.getId());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_company_from, tv_company_to, tv_create_time, tv_count;
    }
}
