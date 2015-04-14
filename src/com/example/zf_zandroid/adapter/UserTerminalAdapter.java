package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.UserTerminal;

import java.util.List;


public class UserTerminalAdapter extends BaseAdapter{
    private Context context;
    private List<UserTerminal> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private UserTerminalAdapter _this;

    public UserTerminalAdapter(Context context, List<UserTerminal> list) {
        this.context = context;
        this.list = list;
        _this = this;
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
        final UserTerminal entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_user_terminal, null);

            holder.text_user_name = (TextView) convertView.findViewById(R.id.text_user_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.text_user_name.setText(entity.getSerial_num());


        return convertView;
    }

    public final class ViewHolder {
        private TextView text_user_name, tv_delete, cb_edit;
        private View line_for_first_item;
    }
}
