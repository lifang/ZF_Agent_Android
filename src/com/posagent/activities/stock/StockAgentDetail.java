package com.posagent.activities.stock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.AgentTerminalEntity;
import com.example.zf_android.entity.StockAgentEntity;
import com.example.zf_android.entity.StockEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.StockAgentTerminalAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 终端代理商详情
 */
public class StockAgentDetail extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;

    private TextView tv_historyCount, tv_openCount, tv_lastPrepareTime, tv_lastOpenTime;

    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private StockAgentTerminalAdapter myAdapter;
    List<AgentTerminalEntity> myList = new ArrayList<AgentTerminalEntity>();
    List<AgentTerminalEntity> moreList = new ArrayList<AgentTerminalEntity>();

    private int agentId;
    private StockAgentEntity entity;
    private StockEntity stockEntity;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                    } else {
                        Xlistview.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.stock_agent_detail);

        agentId = getIntent().getIntExtra("agentId", 1);
        String json = getIntent().getStringExtra("json");
        Gson gson = new Gson();
        entity = gson.fromJson(json, StockAgentEntity.class);

        json = getIntent().getStringExtra("stockJson");
        stockEntity = gson.fromJson(json, StockEntity.class);



        initView();
        getData();
    }

    private void initView() {
        //init
        tv_historyCount = (TextView) findViewById(R.id.tv_historyCount);
        tv_openCount = (TextView) findViewById(R.id.tv_openCount);
        tv_lastPrepareTime = (TextView) findViewById(R.id.tv_lastPrepareTime);
        tv_lastOpenTime = (TextView) findViewById(R.id.tv_lastOpenTime);



        //fill data
        tv_historyCount.setText("配货总量：" + String.valueOf(entity.getHoitoryCount()));
        tv_openCount.setText("已开通量：" + String.valueOf(entity.getOpenCount()));
        tv_lastPrepareTime.setText("上次配送日期：" + String.valueOf(entity.getLastPrepareTime()));
        tv_lastOpenTime.setText("上次开通日期：" + StringUtil.clean(entity.getLastOpenTime()));



        new TitleMenuUtil(StockAgentDetail.this, entity.getCompany_name()).show();
        myAdapter = new StockAgentTerminalAdapter(StockAgentDetail.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
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
        params.put("paychannelId", stockEntity.getPaychannel_id());
        params.put("goodId", stockEntity.getGood_id());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.StockAgentTerminalListEvent event = new Events.StockAgentTerminalListEvent();
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
    public void onEventMainThread(Events.StockAgentTerminalCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }
}
