package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.trade.entity.TerminalItem;
import com.posagent.activities.terminal.TerminalChooseList;

import java.util.List;


public class TerminalChooseListAdapter extends BaseAdapter{
    private Context context;
    private List<TerminalItem> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public TerminalChooseListAdapter(Context context, List<TerminalItem> list) {
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
        final TerminalItem entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.terminal_choose_item, null);

            //init
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_terminal_number = (TextView) convertView.findViewById(R.id.tv_terminal_number);
            holder.cb_item = (CheckBox) convertView.findViewById(R.id.cb_item);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_terminal_number.setText(entity.getTerminalNumber());
        holder.tv_price.setText("￥" + StringUtil.priceShow(entity.getRetail_price()));
        holder.cb_item.setChecked(entity.isSelected());

        holder.cb_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckedAtPosition(position);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_terminal_number, tv_price;
        public CheckBox cb_item;

    }

    private void toggleCheckedAtPosition(int position) {
        ((TerminalChooseList)context).toggleCheckedAtPosition(position);
    }
}
