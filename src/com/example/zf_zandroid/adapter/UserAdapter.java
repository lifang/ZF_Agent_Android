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
import com.example.zf_android.activity.StockDetail;
import com.example.zf_android.entity.OrderEntity;

import java.util.List;


public class UserAdapter extends BaseAdapter{
    private Context context;
    private List<OrderEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public UserAdapter(Context context, List<OrderEntity> list) {
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
            convertView = inflater.inflate(R.layout.list_item_user, null);

            holder.line_for_first_item = convertView.findViewById(R.id.line_for_first_item);
            holder.text_user_name = (TextView) convertView.findViewById(R.id.text_user_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context, StockDetail.class);
                context.startActivity(i);
            }
        });

        if (position == 0) {
            holder.line_for_first_item.setVisibility(View.VISIBLE);
        }
        holder.text_user_name.setText("用户 " + position);

        return convertView;
    }

    public final class ViewHolder {
        private TextView text_user_name;
        private LinearLayout ll_ishow;
        private View line_for_first_item;
    }
}
