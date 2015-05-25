package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.StockEntity;
import com.posagent.activities.goods.GoodsDetail;
import com.posagent.activities.stock.StockList;
import com.posagent.utils.Constants;

import java.util.List;


public class StockAdapter extends BaseAdapter{
    private Context context;
    private List<StockEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    public StockAdapter(Context context, List<StockEntity> list) {
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
        final StockEntity entity = list.get(position);

        inflater = LayoutInflater.from(context);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stock_item, null);
            convertView.setTag(holder);

            //init
            holder.tv_brand = (TextView) convertView.findViewById(R.id.tv_brand);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_totalCount = (TextView) convertView.findViewById(R.id.tv_totalCount);
            holder.tv_agentCount = (TextView) convertView.findViewById(R.id.tv_agentCount);
            holder.tv_openCount = (TextView) convertView.findViewById(R.id.tv_openCount);
            holder.tv_historyCount = (TextView) convertView.findViewById(R.id.tv_historyCount);
            holder.tv_paychannel = (TextView) convertView.findViewById(R.id.tv_paychannel);

            holder.iv_face = (ImageView) convertView.findViewById(R.id.iv_face);

            holder.btn_change_name = (Button) convertView.findViewById(R.id.btn_change_name);
            holder.btn_go_pigou = (Button) convertView.findViewById(R.id.btn_go_pigou);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_brand.setText(entity.getGood_brand() + " " + entity.getModel_number());
        holder.tv_goods_name.setText(entity.getGoodname());
        holder.tv_paychannel.setText(entity.getPaychannel());
        holder.tv_totalCount.setText("总库存\n" + entity.getTotalCount() + "件");
        holder.tv_agentCount.setText("当前库存\n" + entity.getAgentCount() + "件");
        holder.tv_openCount.setText("已开通数量\n" + entity.getOpenCount() + "件");
        holder.tv_historyCount.setText("历史进货数量\n" + entity.getHoitoryCount() + "件");

        holder.iv_face = (ImageView) convertView.findViewById(R.id.iv_face);

        ImageCacheUtil.IMAGE_CACHE.get(entity.getPicurl(), holder.iv_face);


        holder.btn_change_name = (Button) convertView.findViewById(R.id.btn_change_name);
        holder.btn_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goChangeName(entity, position);
            }
        });

        holder.btn_go_pigou = (Button) convertView.findViewById(R.id.btn_go_pigou);
        holder.btn_go_pigou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, GoodsDetail.class);
                i.putExtra("id", entity.getGood_id());
                i.putExtra("buyType", Constants.Goods.OrderTypePigou);
                context.startActivity(i);
            }
        });


        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_brand, tv_paychannel, tv_goods_name, tv_totalCount,
                tv_agentCount, tv_openCount, tv_historyCount;
        public ImageView iv_face;
        public Button btn_change_name, btn_go_pigou;
    }

    private void goChangeName(StockEntity entity, int position) {
        if (context instanceof StockList) {
            ((StockList)context).goChangeName(entity, position);
        }
    }
}
