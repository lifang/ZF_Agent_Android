package com.posagent.activities.agent;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.SonAgent;
import com.example.zf_zandroid.adapter.AgentAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
*
* 代理商管理首页
*
*/
public class AgentManageActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView Xlistview;

    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private AgentAdapter myAdapter;
    List<SonAgent> myList = new ArrayList<SonAgent>();
    List<SonAgent> moreList = new ArrayList<SonAgent>();

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

		setContentView(R.layout.activity_agent_manage);
		new TitleMenuUtil(AgentManageActivity.this, "管理下级代理商").show();


        ((MyApplication)getApplication()).prepareChannelList();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("ll_create_agent", AgentNewActivity.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        findViewById(R.id.ll_profit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.TradeFlowAndProfit)) {return;}

                Intent i2 =new Intent(AgentManageActivity.this, AgentDefaultProfit.class);
                startActivityForResult(i2, Constants.REQUEST_CODE);
            }
        });

        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);


        initXListView();

	}

    private void initXListView() {
        myAdapter = new AgentAdapter(AgentManageActivity.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);
        getData();

        getDefaultProfit();
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
        params.put("agentsId", MyApplication.user().getAgentId());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SonAgentListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void getDefaultProfit() {
        JsonParams params = new JsonParams();
        params.put("agentsId", MyApplication.user().getAgentId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.GetDefaultProfitEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.ll_profit) {
            Intent i = new Intent(AgentManageActivity.this, AgentDefaultProfit.class);
            i.putExtra("profit", getValue("tv_profit"));
            startActivityForResult(i, Constants.REQUEST_CODE);

            return;
        }

        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.SonAgentListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.GetDefaultProfitCompleteEvent event) {
        setText("tv_profit", event.getDefautProfit() + "%");
    }


    public void onEventMainThread(Events.SonAgentListReloadEvent event) {
        onRefresh();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                setText("tv_profit", data.getStringExtra("value") + "%");
                break;
        }
    }
}
