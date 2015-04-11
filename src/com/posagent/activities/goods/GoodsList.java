package com.posagent.activities.goods;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.activity.SearchFormActivity;
import com.example.zf_android.entity.PosEntity;
import com.example.zf_zandroid.adapter.PosAdapter;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
*
* 商品列表
*
*/
public class GoodsList extends BaseActivity implements XListView.IXListViewListener {

    private XListView Xlistview;
    private boolean onRefresh_number = true;

    private int page = 1;
    private int rows = Config.ROWS;
    private int orderType = 0;

    private LinearLayout eva_nodata;
    private PosAdapter myAdapter;
    List<PosEntity> myList = new ArrayList<PosEntity>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();
                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    }
                    onRefresh_number = true;
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_goods_list);

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("titleback_linear_back", AllProduct.class);
            put("serch_edit", SearchFormActivity.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();
        initView();
        initXListView();

	}

    private void initView() {
        //order type
        View v = findViewById(R.id.btn_ordertype_0);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 0;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_1);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 1;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_4);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 4;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_2_3);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderType == 2) {
                    orderType = 3;
                    TextView tv = (TextView) v;
                    tv.setText("价格升序");
                    ImageView iv = (ImageView) findViewById(R.id.icon_ordertype_2_3);
                    iv.setImageResource(R.drawable.ti_up);
                } else {
                    orderType = 2;
                    TextView tv = (TextView) v;
                    tv.setText("价格降序");
                    ImageView iv = (ImageView) findViewById(R.id.icon_ordertype_2_3);
                    iv.setImageResource(R.drawable.ti_down);
                }
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });
    }

    private void initXListView() {
        myAdapter = new PosAdapter(GoodsList.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);

        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent (GoodsList.this, GoodsDetail.class);
                i.putExtra("id", myList.get(position - 1).getId());
                startActivity(i);
            }
        });

        getData();
    }



    @Override
    public void onRefresh() {
        page = 1;
        myList.clear();
        getData();
    }


    @Override
    public void onLoadMore() {
        if (onRefresh_number) {
            page = page + 1;
            if (Tools.isConnect(getApplicationContext())) {
                onRefresh_number = false;
                getData();
            } else {
                onRefresh_number = true;
                EventBus.getDefault().post(new Events.NoConnectEvent());
            }
        } else {
            EventBus.getDefault().post(new Events.RefreshToMuch());
        }
    }

    private void onLoad() {
        Xlistview.stopRefresh();
        Xlistview.stopLoadMore();
        Xlistview.setRefreshTime(Tools.getHourAndMin());
    }

    public void buttonClick() {
        page = 1;
        myList.clear();
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("cityId", Tools.cityId());
        params.put("page", page);
        params.put("orderType", orderType);
        params.put("rows", rows);
        String strParams = params.toString();
        EventBus.getDefault().post(new Events.GoodsListEvent(strParams));
    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
	}

    // events
    public void onEventMainThread(Events.GoodsListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }


    // after reset order type
    private void afterResetOrderType(TextView tv) {
        String[] ids = {"btn_ordertype_0", "btn_ordertype_1", "btn_ordertype_2_3", "btn_ordertype_4"};
        for (String strId : ids) {
            int resourceId = resouceId(strId, "id");
            TextView _tv = (TextView)findViewById(resourceId);
            _tv.setTextColor(getResources().getColor(R.color.text292929));
        }
        tv.setTextColor(getResources().getColor(R.color.bg_item));

    }
	 
}
