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
import com.example.zf_android.entity.MessageEntity;
import com.posagent.activities.user.MessageList;
import com.posagent.utils.OnSwipeTouchListener;

import java.util.List;


public class NewMessageAdapter extends BaseAdapter {
    private Context context;
    private List<MessageEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private NewMessageAdapter _this;

    public NewMessageAdapter(Context context, List<MessageEntity> list) {
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
        final MessageEntity entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_message, null);

            holder.line_for_first_item = convertView.findViewById(R.id.line_for_first_item);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.cb_edit = (CheckBox) convertView.findViewById(R.id.cb_edit);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.tv_title.setText(entity.getTitle());
        if(entity.getStatus()) {
            holder.tv_title.setTextColor(context.getResources().getColor(R.color.text6c6c6c6));
        } else {
            holder.tv_title.setTextColor(context.getResources().getColor(R.color.text292929));
        }

        holder.tv_time.setText(entity.getCreate_at());

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

        if (position == 0) {
            holder.line_for_first_item.setVisibility(View.VISIBLE);
        }

        convertView.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void singleTapUp() {
                ((MessageList)context).showMessageDetail(entity);
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
                ((MessageList)context).doDelete(entity);
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
        private TextView tv_title, tv_delete, cb_edit, tv_time;
        private View line_for_first_item;
    }
}
