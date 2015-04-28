package com.posagent.activities.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.StockEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.StockAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.CommonInputer;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 库存管理
 */
public class StockList extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private StockAdapter myAdapter;
    List<StockEntity> myList = new ArrayList<StockEntity>();
    List<StockEntity> moreList = new ArrayList<StockEntity>();

    private StockEntity entity;
    private int changingNamePostion;

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
        setContentView(R.layout.activity_stock_list);

        new TitleMenuUtil(StockList.this, "库存管理").show();

        initView();
        getData();
    }

    private void initView() {

        myAdapter = new StockAdapter(StockList.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);

        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(StockList.this, StockDetail.class);
                StockEntity entity = myList.get(position - 1);
                i.putExtra("agentId", MyApplication.user().getAgentId());

                Gson gson = new Gson();
                String json = gson.toJson(entity);
                i.putExtra("json", json);

                startActivity(i);
            }
        });
        Xlistview.setAdapter(myAdapter);
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

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.StockListEvent();
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
    public void onEventMainThread(Events.StockListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.StockRenameCompleteEvent event) {
        if (event.getSuccess()) {
            toast(event.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CODE:
                String content = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);
                Log.d(TAG, content);
                StockEntity entity = myList.get(changingNamePostion);
                entity.setGoodname(content);

                changeName(entity);

                Xlistview.invalidateViews();
                break;
        }
    }

    public void goChangeName(StockEntity entity, int position) {
        entity = entity;
        changingNamePostion = position;
        Intent intent = new Intent(StockList.this, CommonInputer.class);

        intent.putExtra(Constants.CommonInputerConstant.TITLE_KEY, "商品更名");

        intent.putExtra(Constants.CommonInputerConstant.PLACEHOLDER_KEY, entity.getGoodname());

        startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CODE);

    }

    private void changeName(StockEntity entity) {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("goodId", entity.getGood_id());
        params.put("goodname", entity.getGoodname());
        String strParams = params.toString();
        Events.StockRenameEvent event = new Events.StockRenameEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

}
