package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.entity.TerminalApplyEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.TerminalOpenApplyAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 申请开通
 */
public class TerminalOpenApply extends BaseActivity implements IXListViewListener, View.OnClickListener {
    private Button btn_bind_terminal, btn_after_sale_apply;
    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private TerminalOpenApplyAdapter myAdapter;
    List<TerminalApplyEntity> myList = new ArrayList<TerminalApplyEntity>();
    List<OrderEntity> moreList = new ArrayList<OrderEntity>();


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
        setContentView(R.layout.activity_terminal_open_apply);
        initView();
        getData();
    }

    private void initView() {
        new TitleMenuUtil(TerminalOpenApply.this, "申请开通").show();
        myAdapter = new TerminalOpenApplyAdapter(TerminalOpenApply.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);

        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
        Events.TerminalApplyListEvent event = new Events.TerminalApplyListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.TerminalApplyListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_after_sale_apply:
                Intent i = new Intent(getApplicationContext(), AfterSaleApply.class);
                startActivity(i);
                break;
            case R.id.btn_bind_terminal:

                break;
            default:
                break;
        }
    }
}
