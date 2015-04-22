package com.posagent.activities.agent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.ProfitEntity;
import com.example.zf_android.entity.ProfitTradeEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.ProfitAdapter;
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
 * 设置分润
 */
public class AgentProfitList extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private ProfitAdapter myAdapter;
    List<ProfitEntity> myList = new ArrayList<ProfitEntity>();
    List<ProfitEntity> moreList = new ArrayList<ProfitEntity>();

    private TextView tvCurrent;
    private ProfitTradeEntity profitTradeEntity;
    private ProfitEntity profitEntity;

    private int sonAgentId;
    private String filtedIds;

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
        setContentView(R.layout.activity_common_list);

        new TitleMenuUtil(AgentProfitList.this, "设置分润").show();

        sonAgentId = getIntent().getIntExtra("id", 0);

        initView();
        getData();
    }

    private void initView() {

        // Add icon show
        ImageView addIcon = (ImageView) findViewById(R.id.iv_addIconEdge);

        addIcon.setVisibility(View.VISIBLE);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AgentProfitList.this, ChannelList.class);
                i.putExtra("filtedIds", filtedIds);
                startActivityForResult(i, Constants.REQUEST_CODE);

            }
        });

        myAdapter = new ProfitAdapter(AgentProfitList.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);

        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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
        //Fixme
        params.put("sonAgentsId", sonAgentId);
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ProfitListEvent();
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
    public void onEventMainThread(Events.ProfitListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                int selectChannelId = bundle.getInt("id", 0);
                Log.d(TAG, "id " + selectChannelId);
                ProfitEntity entity = new ProfitEntity();
                entity.setChannel(((MyApplication)getApplication()).getChannelEntityWithId(selectChannelId));
                myList.add(entity);
                handler.sendEmptyMessage(0);
                break;

            case Constants.REQUEST_CODE2:
                String percent = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);

                tvCurrent.setText(percent + "%");
                profitTradeEntity.setPercent(percent);

                //doSave
                doSaveOrEditProfit();
                break;
        }
    }


    public void changeProfit(TextView tv, ProfitEntity profit, ProfitTradeEntity trade) {

        tvCurrent = tv;
        profitEntity = profit;
        profitTradeEntity = trade;

        Intent i = new Intent(AgentProfitList.this, CommonInputer.class);
        i.putExtra(Constants.CommonInputerConstant.TITLE_KEY, "设置百分比");
        i.putExtra(Constants.CommonInputerConstant.PLACEHOLDER_KEY, trade.getPercent());

        startActivityForResult(i, Constants.REQUEST_CODE2);

    }

    private void doSaveOrEditProfit() {
        Log.d(TAG, "doSaveOrEditProfit " + profitEntity.getChannel().getName());

        String profitPercent;
        List<String> list = new ArrayList<String>();
        int sign = profitEntity.getId() == null ? 1 : 0;
        for (ProfitTradeEntity trade: profitEntity.getDetail()) {
            list.add(trade.getPercent() + "_" + trade.getTradeTypeId());
        }
        profitPercent = StringUtil.join(list, "|");


        //do save
        JsonParams params = new JsonParams();
        //Fixme
        params.put("agentsId", 1);

        params.put("sonAgentsId", sonAgentId);
        params.put("channelId", profitEntity.getChannel().getId());
        params.put("profitPercent", profitPercent);
        params.put("sign", sign);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.SetProfitEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);


    }


}
