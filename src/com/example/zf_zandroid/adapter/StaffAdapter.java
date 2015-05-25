package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.StaffEntity;
import com.posagent.activities.user.StaffList;
import com.posagent.utils.OnSwipeTouchListener;

import java.util.List;


public class StaffAdapter extends BaseAdapter {
    private Context context;
    private List<StaffEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private StaffAdapter _this;

    public StaffAdapter(Context context, List<StaffEntity> list) {
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
        final StaffEntity entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_staff, null);

            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.cb_edit = (CheckBox) convertView.findViewById(R.id.cb_edit);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_username.setText(entity.getUsername() + "("+ entity.getName() +")");
//        holder.tv_time.setText(entity.getCreate_at());

        // update editing item
        if (entity.isDeleting()) {
            holder.tv_delete.setVisibility(View.VISIBLE);
        } else {
            holder.tv_delete.setVisibility(View.GONE);
        }

        if (entity.isBatchEditing()) {
            holder.cb_edit.setVisibility(View.VISIBLE);
        } else {
            holder.cb_edit.setVisibility(View.GONE);
        }

        convertView.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void singleTapUp() {
                ((StaffList)context).showDetail(entity);
            }

            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                entity.setDeleting(false);
                _this.notifyDataSetChanged();
            }
            public void onSwipeLeft() {
                entity.setDeleting(true);
                _this.notifyDataSetChanged();
            }
            public void onSwipeBottom() {
            }
        });

        // bind actions
        holder.tv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StaffList)context).doDelete(entity);
            }
        });

        holder.cb_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                entity.setSelected(true);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        private TextView tv_username, tv_delete, cb_edit, tv_time;
    }
}
