package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.activity.TerminalDetail;
import com.example.zf_android.entity.OrderEntity;

import java.util.List;


public class TerminalAdapter extends BaseAdapter{
    private Context context;
    private List<OrderEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public TerminalAdapter(Context context, List<OrderEntity> list) {
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
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.terminal_item, null);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context, TerminalDetail.class);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_goodnum,tv_price,content,tv_ddbh,tv_time,tv_status,tv_sum,tv_psf,tv_pay,tv_gtd,content2,content_pp;
        private LinearLayout ll_ishow;
    }
}
