package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zf_android.R;
import com.example.zf_android.entity.User;
import com.posagent.activities.user.UserList;
import com.posagent.utils.OnSwipeTouchListener;

import java.util.List;


public class UserAdapter extends BaseAdapter{
    private Context context;
    private List<User> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private UserAdapter _this;

    public UserAdapter(Context context, List<User> list) {
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
        final User entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_user, null);

            holder.line_for_first_item = convertView.findViewById(R.id.line_for_first_item);
            holder.text_user_name = (TextView) convertView.findViewById(R.id.text_user_name);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.cb_edit = (CheckBox) convertView.findViewById(R.id.cb_edit);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //fill data
        holder.text_user_name.setText(entity.getName());

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
                clickAtUser(entity);
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
                ((UserList)context).doDelete(entity);
            }
        });

        if (((UserList)context).isForSelect()) {
            convertView.findViewById(R.id.iv_right_arrow).setVisibility(View.GONE);
        }

        holder.cb_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                entity.setSelected(true);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        private TextView text_user_name, tv_delete, cb_edit;
        private View line_for_first_item;
    }

    private void clickAtUser(User entity) {
        ((UserList)context).clickAtUser(entity);
    }
}
