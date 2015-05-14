package com.posagent.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeRecord;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_android.trade.widget.MyViewPager;
import com.example.zf_zandroid.adapter.TradeFlowAdapter;
import com.posagent.MyApplication;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.CLIENT_NUMBER;
import static com.example.zf_android.trade.Constants.TradeIntent.END_DATE;
import static com.example.zf_android.trade.Constants.TradeIntent.SON_AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.START_DATE;
import static com.example.zf_android.trade.Constants.TradeIntent.TRADE_TYPE;
import static com.example.zf_android.trade.Constants.TradeType.TRANSFER;

/**
 * Created by Leo on 2015/2/6.
 */
public class TradeFlowActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,
        XListView.IXListViewListener, MyTabWidget.OnTabSelectedListener {

    private MyTabWidget mTabWidget;
    private MyViewPager mViewPager;

    private List<TradeFlowFragment> mFragments;

    private XListView Xlistview;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private TradeFlowAdapter myAdapter;
    List<TradeRecord> myList = new ArrayList<TradeRecord>();
    List<TradeRecord> moreList = new ArrayList<TradeRecord>();

    Map<String, Object> searchMap;
    private int tradeTypeId = 1;

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

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }

        setContentView(R.layout.activity_trade_flow);
        initViews();

        Toast toast = Toast.makeText(getApplicationContext(),
                "手机端交易流水查询仅供单台终端查询，完整查询功能请登陆PC端合作伙伴平台",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        new TitleMenuUtil(this, getString(R.string.title_trade_flow)).show();

        mTabWidget = (MyTabWidget) findViewById(R.id.tab_widget);
        mTabWidget.setOnTabSelectedListener(this);

        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        mFragments = new ArrayList<TradeFlowFragment>();

        // add tabs to the TabWidget
        String[] tabs = getResources().getStringArray(R.array.trade_flow_tabs);
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }
        // add fragments according to the order
        TradeFlowFragment transferFragment = TradeFlowFragment.newInstance(TRANSFER);
        mFragments.add(transferFragment);

        mTabWidget.setViewPager(mViewPager);
        mViewPager.setAdapter(new TradeFlowPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(this);

        // init to select the first tab and fragment
        mTabWidget.updateTabs(0);
        mViewPager.setCurrentItem(0);

        myAdapter = new TradeFlowAdapter(TradeFlowActivity.this, myList);
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
        myAdapter.notifyDataSetChanged();
        getData(searchMap);
    }


    @Override
    public void onLoadMore() {
        if (onRefresh_number) {
            page = page + 1;
            if (Tools.isConnect(getApplicationContext())) {
                onRefresh_number = false;
                getData(searchMap);
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

    public void reGetData(Map<String, Object> map) {
        searchMap = map;
        onRefresh();
    }

    public void getData(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        searchMap = map;
        JsonParams params = new JsonParams();

        params.put("agentId", MyApplication.user().getAgentId());
        params.put("tradeTypeId", tradeTypeId);
        params.put("terminalNumber", map.get("terminalNumber"));
        params.put("sonagentId", map.get("sonagentId"));
        params.put("startTime", map.get("startTime"));
        params.put("endTime", map.get("endTime"));
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.TradeListEvent event = new Events.TradeListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    public void getStatistic(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        Intent intent = new Intent(TradeFlowActivity.this, TradeStatisticActivity.class);
        intent.putExtra(AGENT_ID, MyApplication.user().getAgentId());
        intent.putExtra(SON_AGENT_ID, MyApplication.user().getAgentUserId());
        intent.putExtra(TRADE_TYPE, tradeTypeId);
        intent.putExtra(CLIENT_NUMBER, map.get("terminalNumber").toString());
        intent.putExtra(START_DATE, map.get("startTime").toString());
        intent.putExtra(END_DATE, map.get("endTime").toString());
        startActivity(intent);
    }


    // events
    public void onEventMainThread(Events.TradeListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        mTabWidget.updateTabs(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("TradeFlow", "postion: " + position);
        tradeTypeId = position + 1;
        onRefresh();

    }

    public class TradeFlowPagerAdapter extends FragmentPagerAdapter {

        public TradeFlowPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}
