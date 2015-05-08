package com.example.zf_zandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.StringUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.PosEntity;
import com.posagent.activities.goods.GoodsList;
import com.posagent.utils.Constants;

import java.util.List;

public class PosAdapter extends BaseAdapter {
	private Context context;
	private List<PosEntity> list;
	private LayoutInflater inflater;
	private ViewHolder holder = null;

	public PosAdapter(Context context, List<PosEntity> list) {
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
		inflater = LayoutInflater.from(context);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(pos_item_layout(), null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
//            holder.img_type = (ImageView) convertView.findViewById(R.id.img_type);
            holder.img_face = (ImageView) convertView.findViewById(R.id.img_face);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_origin_price);
            holder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
			holder.ys = (TextView) convertView.findViewById(R.id.ys);
			holder.content1 = (TextView) convertView.findViewById(R.id.content1);
			holder.tv_td = (TextView) convertView.findViewById(R.id.tv_td);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        PosEntity entity = list.get(position);

        ImageCacheUtil.IMAGE_CACHE.get(entity.getUrl_path(), holder.img_face);

        holder.title.setText(entity.getTitle());

        if (buyType() == Constants.Goods.OrderTypePigou) {
            holder.tv_origin_price.setText("￥"+ StringUtil.priceShow(entity.getRetail_price()));
            holder.tv_price.setText("￥"+ StringUtil.priceShow(entity.getPurchase_price()));
        } else {
            holder.tv_price.setText("￥"+ StringUtil.priceShow(entity.getRetail_price()));
        }

        if (null != holder.tv_quantity) {
            holder.tv_quantity.setText("" + entity.getFloor_purchase_quantity() + "件");
        }
        if (null != holder.content1) {
            holder.content1.setText(entity.getModel_number());
        }
		holder.tv_td.setText(entity.getPay_channe());
		holder.ys.setText("已售"+entity.getVolume_number());


		return convertView;
	}

	public final class ViewHolder {
		public TextView title, ys, tv_origin_price, tv_price, content1, tv_td, tv_quantity;
		public CheckBox item_cb;
		public ImageView img_type, img_face;
	}

    private int pos_item_layout() {
        return ((GoodsList)context).pos_item_layout();
    }

    private int buyType() {
        return ((GoodsList)context).getBuyType();
    }
}
