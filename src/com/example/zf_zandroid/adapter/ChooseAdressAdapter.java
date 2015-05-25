package com.example.zf_zandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.AdressEntity;
import com.posagent.activities.user.ChangeAdress;

import java.util.List;

public class ChooseAdressAdapter extends BaseAdapter {
    private ChangeAdress context;
    private List<AdressEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;

    public ChooseAdressAdapter(ChangeAdress context, List<AdressEntity> list) {
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
        AdressEntity entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.choose_adress_item, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.adress_name);
            holder.tv_adress = (TextView) convertView.findViewById(R.id.tv_adress);
            holder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
            holder.ll_isshow = (LinearLayout) convertView.findViewById(R.id.ll_isshow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_tel.setText(list.get(position).getMoblephone());
        holder.tv_title.setText("收件人: " + list.get(position).getReceiver());

        boolean isDefault = entity.getIsDefault().equals("1");
        boolean isSelected = entity.getId() == context.addressId();


        if(isDefault || isSelected){
            holder.ll_isshow.setVisibility(View.VISIBLE);

            if (isSelected) {
                convertView.findViewById(R.id.item_cb).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.item_cb).setVisibility(View.GONE);
            }

            if (isDefault) {
                convertView.findViewById(R.id.tv_default_tips).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.tv_default_tips).setVisibility(View.GONE);
            }

        } else {
            holder.ll_isshow.setVisibility(View.INVISIBLE);
        }
        holder.tv_adress.setText("收件地址: " + list.get(position).getAddress());



        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_title, tv_time,tv_tel,tv_adress;
        public LinearLayout ll_isshow;

    }
}
