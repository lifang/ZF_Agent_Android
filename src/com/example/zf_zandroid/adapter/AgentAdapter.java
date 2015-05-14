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
import com.posagent.activities.agent.AgentDetailActivity;
import com.example.zf_android.entity.SonAgent;

import java.util.List;


public class AgentAdapter extends BaseAdapter{
    private Context context;
    private List<SonAgent> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public AgentAdapter(Context context, List<SonAgent> list) {
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
        final SonAgent entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.agent_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_types = (TextView) convertView.findViewById(R.id.tv_types);
            holder.tv_agent_name = (TextView) convertView.findViewById(R.id.tv_agent_name);
            holder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
            holder.tv_sold_count = (TextView) convertView.findViewById(R.id.tv_sold_count);
            holder.tv_left_count = (TextView) convertView.findViewById(R.id.tv_left_count);
            holder.tv_open_count = (TextView) convertView.findViewById(R.id.tv_open_count);


        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        String kind = entity.getTypes() == 1 ? "公司" : "个人";
        holder.tv_types.setText("代理商类型：" + kind);
        holder.tv_agent_name.setText("代理商名称：" + entity.getCompany_name());
        holder.tv_create_time.setText("加入时间：" + entity.getCreated_at());
        holder.tv_sold_count.setText("已售出：" + entity.getSoldNum());
        int left = (entity.getOpenNum() - entity.getSoldNum());
        if (left < 0) {
            left = 0;
        }
        holder.tv_left_count.setText("剩余库存：" + left);
        holder.tv_open_count.setText("终端开通量：" + entity.getOpenNum());


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, AgentDetailActivity.class);
                i.putExtra("name", entity.getCompany_name());
                i.putExtra("id", entity.getId());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_types, tv_agent_name, tv_create_time, tv_sold_count,
                tv_left_count, tv_open_count;
    }
}
