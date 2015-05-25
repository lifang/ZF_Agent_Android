package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.AgentTerminalEntity;
import com.posagent.utils.Constants;

import java.util.List;


public class StockAgentTerminalAdapter extends BaseAdapter{
    private Context context;
    private List<AgentTerminalEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public StockAgentTerminalAdapter(Context context, List<AgentTerminalEntity> list) {
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

        AgentTerminalEntity entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stock_terminal_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_terminal_number = (TextView) convertView.findViewById(R.id.tv_terminal_number);
            holder.tv_model = (TextView) convertView.findViewById(R.id.tv_model);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_terminal_number.setText(entity.getSerial_num());
        holder.tv_model.setText(entity.getModel_number());
        holder.tv_status.setText(Constants.TerminalConstant.STATUS[entity.getStatus()]);

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_terminal_number, tv_model, tv_status;
    }
}
