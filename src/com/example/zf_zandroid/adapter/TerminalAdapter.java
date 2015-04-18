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
import com.example.zf_android.trade.entity.TerminalItem;
import com.google.gson.Gson;
import com.posagent.activities.terminal.TerminalDetailActivity;

import java.util.List;


public class TerminalAdapter extends BaseAdapter{
    private Context context;
    private List<TerminalItem> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public TerminalAdapter(Context context, List<TerminalItem> list) {
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
        final TerminalItem entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.terminal_item, null);

            //init
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_terminal_number = (TextView) convertView.findViewById(R.id.tv_terminal_number);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_terminal_number.setText(entity.getTerminalNumber());
        holder.tv_status.setText(statusName(entity.getStatus()));

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, TerminalDetailActivity.class);

                Gson gson = new Gson();
                String json = gson.toJson(entity);
                i.putExtra("json", json);

                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_terminal_number, tv_status;
    }

    private String statusName(int status) {
        return com.posagent.utils.Constants.TerminalConstant.STATUS[status];
    }
}
