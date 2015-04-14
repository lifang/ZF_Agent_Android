package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.entity.AdressEntity;
import com.posagent.MyApplication;
import com.posagent.activities.user.AdressList;
import com.posagent.utils.OnSwipeTouchListener;

import java.util.List;

public class AdressAdapter extends BaseAdapter {
    private Context context;
    private List<AdressEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;

    public AdressAdapter(Context context, List<AdressEntity> list) {
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
        final AdressEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adress_item, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_receiver);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_moblephone = (TextView) convertView.findViewById(R.id.tv_moblephone);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(entity.getReceiver());
        holder.tv_moblephone.setText(entity.getMoblephone());
        holder.tv_address.setText(entity.getAddress());
        if(MyApplication.getIsSelect()){
            holder.item_cb.setVisibility(View.VISIBLE);
        }else{
            holder.item_cb.setVisibility(View.GONE);
        }

        if (isDeleting()) {
            holder.tv_delete.setVisibility(View.VISIBLE);
        } else {
            holder.tv_delete.setVisibility(View.GONE);
        }

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete(entity);
            }
        });

        list.get(position).setIscheck(holder.item_cb.isChecked());
        holder.item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                entity.setIscheck(isChecked);
            }
        });

        convertView.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                holder.tv_delete.setVisibility(View.VISIBLE);

                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_title, tv_time, tv_moblephone, tv_address,
                tv_delete;
        public CheckBox item_cb;

    }

    //helper
    private boolean isDeleting() {

        AdressList adressList = adressList();
        if (adressList != null) {
            return adressList.isDeleting();
        }
        return false;
    }

    private void doDelete(AdressEntity entity) {
        AdressList adressList = adressList();
        if (adressList != null) {
            adressList.doDelete(entity);
        }
    }

    private AdressList adressList() {
        if (context instanceof AdressList) {
            AdressList adressList = (AdressList) context;
            return adressList;
        }
        return null;
    }

}
