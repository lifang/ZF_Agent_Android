package com.posagent.activities.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.OrderAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 */
public class OrderList extends BaseActivity implements IXListViewListener,
        AdapterView.OnItemSelectedListener,
        MyTabWidget.OnTabSelectedListener
{

    static final String TAG = "OrderList";

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private OrderAdapter myAdapter;
    List<OrderEntity> myList = new ArrayList<OrderEntity>();
    List<OrderEntity> moreList = new ArrayList<OrderEntity>();
    Spinner spinnerState;

    private String[] state= {"选择订单状态","全部","未付款","已付订金","已完成","已取消"};


    //params data
    private int p = 1;
    private int q = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    } else {
                        Xlistview.setVisibility(View.VISIBLE);
                        eva_nodata.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_order_list);
        initView();
        getData();
    }

    private void initView() {

        mTabWidget = (MyTabWidget) findViewById(R.id.tab_widget);
        mTabWidget.setOnTabSelectedListener(this);
        // add tabs to the TabWidget
        String[] tabs = getResources().getStringArray(R.array.order_kind_tabs);
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }
        mTabWidget.updateTabs(0);

        new TitleMenuUtil(OrderList.this, "订单管理").show();
        myAdapter = new OrderAdapter(OrderList.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);

        Xlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(OrderList.this, OrderDetail.class);
                OrderEntity orderEntity = myList.get(position - 1);
                i.putExtra("status", orderEntity.getOrder_status());
                i.putExtra("id", orderEntity.getOrder_id());
                i.putExtra("p", p);
                startActivity(i);
            }
        });
        Xlistview.setAdapter(myAdapter);

        spinnerState = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, state);
        spinnerState.setAdapter(adapter_state);
        spinnerState.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        q = position - 1;
        onRefresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "tab select: " + position);
        p = position + 1;
        onRefresh();
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
        params.put("customerId", MyApplication.user().getId());
        params.put("p", p);
        if (q > 0) {
           params.put("q", q);
        }
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.OrderListEvent event = new Events.OrderListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.OrderListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }
}
