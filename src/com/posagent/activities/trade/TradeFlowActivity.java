package com.posagent.activities.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeRecord;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_android.trade.widget.MyViewPager;
import com.example.zf_zandroid.adapter.TradeFlowAdapter;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeType.CONSUME;
import static com.example.zf_android.trade.Constants.TradeType.LIFE_PAY;
import static com.example.zf_android.trade.Constants.TradeType.PHONE_PAY;
import static com.example.zf_android.trade.Constants.TradeType.REPAYMENT;
import static com.example.zf_android.trade.Constants.TradeType.TRANSFER;

/**
 * Created by Leo on 2015/2/6.
 */
public class TradeFlowActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, XListView.IXListViewListener {

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

    private Handler handler = new Handler() {
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

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }

        setContentView(R.layout.activity_trade_flow);
        initViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        new TitleMenuUtil(this, getString(R.string.title_trade_flow)).show();

        mTabWidget = (MyTabWidget) findViewById(R.id.tab_widget);
        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        mFragments = new ArrayList<TradeFlowFragment>();

        // add tabs to the TabWidget
        String[] tabs = getResources().getStringArray(R.array.trade_flow_tabs);
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }
        // add fragments according to the order
        TradeFlowFragment transferFragment = TradeFlowFragment.newInstance(TRANSFER);
        TradeFlowFragment consumeFragment = TradeFlowFragment.newInstance(CONSUME);
        TradeFlowFragment repaymentFragment = TradeFlowFragment.newInstance(REPAYMENT);
        TradeFlowFragment lifePayFragment = TradeFlowFragment.newInstance(LIFE_PAY);
        TradeFlowFragment phonePayFragment = TradeFlowFragment.newInstance(PHONE_PAY);
        mFragments.add(transferFragment);
        mFragments.add(consumeFragment);
        mFragments.add(repaymentFragment);
        mFragments.add(lifePayFragment);
        mFragments.add(phonePayFragment);

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

    public void getData(Map<String, Object> map) {

        searchMap = map;

        JsonParams params = new JsonParams();

        //Fixme
        params.put("agentId", 1);
        params.put("tradeTypeId", map.get("tradeTypeId"));
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