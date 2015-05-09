package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.StringUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.Goodlist;

import java.util.List;




public class OrderDetail_PosAdapter extends BaseAdapter{
    private Context context;
    private List<Goodlist> list;
    private LayoutInflater inflater;
    private int state;
    private ViewHolder holder = null;
    public OrderDetail_PosAdapter(Context context, List<Goodlist> list ,int state) {
        this.context = context;
        this.list = list;
        this.state = state;
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
        final Goodlist entity = list.get(position);
        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_detail_positem, null);
            holder.content = (TextView) convertView.findViewById(R.id.content_pp);
            holder.evevt_img = (ImageView) convertView.findViewById(R.id.evevt_img);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_origin_price);
            holder.tv_x = (TextView) convertView.findViewById(R.id.tv_x);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.content.setText(entity.getGood_name());

        holder.tv_x.setText("X " + entity.getGood_num());
        holder.tv_price.setText("￥ "+ StringUtil.priceShow(entity.getGood_batch_price()));
        holder.tv_origin_price.setText("￥ "+ StringUtil.priceShow(entity.getGood_price()));
        ImageCacheUtil.IMAGE_CACHE.get(entity.getGood_logo(), holder.evevt_img);


        return convertView;
    }

    public final class ViewHolder {
        public TextView content,tv_price, tv_origin_price,tv_x;
        public ImageView evevt_img;
        public Button btn_ishow;
    }
}
