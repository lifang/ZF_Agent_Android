package com.example.zf_android.trade;

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
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.MyApplication;
import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_android.trade.widget.MyViewPager;
import com.example.zf_zandroid.adapter.TradeFlowAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    List<OrderEntity> myList = new ArrayList<OrderEntity>();
    List<OrderEntity> moreList = new ArrayList<OrderEntity>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        //	norecord_text_to.setText("��û����ص���Ʒ");
                        Xlistview.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    }
                    onRefresh_number = true;
                    myAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 2: // ����������
                    Toast.makeText(getApplicationContext(), "no 3g or wifi content",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), " refresh too much",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trade_flow);
        initViews();
        getData();
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
        // TODO Auto-generated method stub
        page = 1;
        System.out.println("onRefresh1");
        myList.clear();
        System.out.println("onRefresh2");
        getData();
    }


    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        if (onRefresh_number) {
            page = page + 1;


            if (Tools.isConnect(getApplicationContext())) {
                onRefresh_number = false;
                getData();
            } else {
                onRefresh_number = true;
                handler.sendEmptyMessage(2);
            }
        } else {
            handler.sendEmptyMessage(3);
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

    /*
     * �������
     */
    private void getData() {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        String url = "http://114.215.149.242:18080/ZFMerchant/api/order/getMyOrderAll";
        RequestParams params = new RequestParams();
        params.put("customer_id", 80);
        params.put("page", page);
        params.put("pageSize", 2);

        params.setUseJsonStreamer(true);

        MyApplication.getInstance().getClient()
                .post(url, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        String responseMsg = new String(responseBody)
                                .toString();
                        Log.e("print", responseMsg);


                        Gson gson = new Gson();

                        JSONObject jsonobject = null;
                        String code = null;
                        try {
                            jsonobject = new JSONObject(responseMsg);
                            code = jsonobject.getString("code");
                            int a = jsonobject.getInt("code");
                            if (a == Config.CODE) {
                                String res = jsonobject.getString("result");
                                jsonobject = new JSONObject(res);
                                moreList.clear();
                                System.out.println("-jsonobject String()--" + jsonobject.getString("content").toString());
                                moreList = gson.fromJson(jsonobject.getString("content").toString(), new TypeToken<List<OrderEntity>>() {
                                }.getType());
                                System.out.println("-sendEmptyMessage String()--");
                                myList.addAll(moreList);
                                handler.sendEmptyMessage(0);


                            } else {
                                code = jsonobject.getString("message");
                                Toast.makeText(getApplicationContext(), code, 1000).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            ;
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        System.out.println("-onFailure---");
                        Log.e("print", "-onFailure---" + error);
                    }
                });


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
