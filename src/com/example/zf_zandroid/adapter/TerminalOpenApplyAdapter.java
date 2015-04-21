package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.TerminalApplyEntity;
import com.example.zf_android.trade.ApplyDetailActivity;
import com.posagent.activities.terminal.TerminalDetailActivity;
import com.posagent.utils.Constants;

import java.util.List;

import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_STATUS;


public class TerminalOpenApplyAdapter extends BaseAdapter{
    private Context context;
    private List<TerminalApplyEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public TerminalOpenApplyAdapter(Context context, List<TerminalApplyEntity> list) {
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

        final TerminalApplyEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.terminal_open_apply_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_terminal_number = (TextView) convertView.findViewById(R.id.tv_terminal_number);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.btn_apply_open = (Button) convertView.findViewById(R.id.btn_apply_open);
            holder.btn_apply_reopen = (Button) convertView.findViewById(R.id.btn_apply_reopen);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        // fill data
        holder.tv_terminal_number.setText(entity.getSerial_num());
        holder.tv_status.setText(Constants.TerminalConstant.STATUS[entity.getStatus()]);

        if (entity.getStatus() == 3) {
            holder.btn_apply_reopen.setVisibility(View.GONE);
            holder.btn_apply_open.setVisibility(View.VISIBLE);
        } else {
            holder.btn_apply_open.setVisibility(View.GONE);
            holder.btn_apply_reopen.setVisibility(View.VISIBLE);
        }


        holder.btn_apply_open.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, ApplyDetailActivity.class);
                i.putExtra("id", entity.getId());
                i.putExtra(TERMINAL_ID, entity.getId());
                i.putExtra(TERMINAL_STATUS, entity.getStatus());

                context.startActivity(i);
            }
        });

        holder.btn_apply_reopen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, ApplyDetailActivity.class);
                i.putExtra("id", entity.getId());
                i.putExtra(TERMINAL_ID, entity.getId());
                i.putExtra(TERMINAL_STATUS, entity.getStatus());
                context.startActivity(i);
            }
        });


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, TerminalDetailActivity.class);
                i.putExtra("id", entity.getId());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_terminal_number, tv_status;
        public Button btn_apply_open, btn_apply_reopen;
    }
}
