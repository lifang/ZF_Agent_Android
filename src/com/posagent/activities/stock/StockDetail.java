package com.posagent.activities.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.StockAgentEntity;
import com.example.zf_android.entity.StockEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.StockAgentAdapter;
import com.google.gson.Gson;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.CommonInputer;
import com.posagent.activities.goods.GoodsDetail;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 库存详情
 */
public class StockDetail extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private StockAgentAdapter myAdapter;
    List<StockAgentEntity> myList = new ArrayList<StockAgentEntity>();
    List<StockAgentEntity> moreList = new ArrayList<StockAgentEntity>();

    private int agentId;
    private StockEntity entity;


    private TextView tv_brand, tv_paychannel, tv_goods_name, tv_totalCount,
            tv_agentCount, tv_openCount, tv_historyCount;
    private ImageView iv_face;
    private Button btn_change_name, btn_go_pigou;

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
        setContentView(R.layout.activity_stock_detail);

        agentId = getIntent().getIntExtra("agentId", 1);
        String json = getIntent().getStringExtra("json");
        Gson gson = new Gson();
        entity = gson.fromJson(json, StockEntity.class);


        initView();
        getData();
    }

    private void initView() {
        new TitleMenuUtil(StockDetail.this, "库存详情").show();


        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_totalCount = (TextView) findViewById(R.id.tv_totalCount);
        tv_agentCount = (TextView) findViewById(R.id.tv_agentCount);
        tv_openCount = (TextView) findViewById(R.id.tv_openCount);
        tv_historyCount = (TextView) findViewById(R.id.tv_historyCount);
        tv_paychannel = (TextView) findViewById(R.id.tv_paychannel);

        iv_face = (ImageView) findViewById(R.id.iv_face);

        btn_change_name = (Button) findViewById(R.id.btn_change_name);
        btn_go_pigou = (Button) findViewById(R.id.btn_go_pigou);

        //init data
        tv_brand.setText(entity.getGood_brand());
        tv_goods_name.setText(entity.getGoodname());
        tv_paychannel.setText(entity.getPaychannel());
        tv_totalCount.setText("总库存\n" + entity.getTotalCount());
        tv_agentCount.setText("代理商库存\n" + entity.getAgentCount());
        tv_openCount.setText("已开通数量\n" + entity.getOpenCount());
        tv_historyCount.setText("历史进货数量\n" + entity.getHoitoryCount());


        Picasso.with(this).load(entity.getPicurl()).into(iv_face);

        btn_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goChangeName();
            }
        });

        btn_go_pigou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (StockDetail.this, GoodsDetail.class);
                i.putExtra("id", entity.getGood_id());
                i.putExtra("orderType", Constants.Goods.OrderTypePigou);
                startActivity(i);
            }
        });



        myAdapter = new StockAgentAdapter(StockDetail.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);
        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(StockDetail.this, StockAgentDetail.class);
                Gson gson = new Gson();
                StockAgentEntity stockAgentEntity = myList.get(position);
                String json = gson.toJson(stockAgentEntity);
                i.putExtra("json", json);

                json = gson.toJson(entity);
                i.putExtra("stockJson", json);

                startActivity(i);
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
        //Fixme
        params.put("agentId", agentId);
        params.put("paychannelId", entity.getPaychannel_id());
        params.put("goodId", entity.getGood_id());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.StockAgentListEvent event = new Events.StockAgentListEvent();
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
    public void onEventMainThread(Events.StockAgentListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.StockRenameCompleteEvent event) {
        if (event.getSuccess()) {
            toast(this, event.getMessage());
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
                entity.setGoodname(content);
                tv_goods_name.setText(entity.getGoodname());
                changeName(entity);
                break;
        }
    }

    public void goChangeName() {
        Intent intent = new Intent(StockDetail.this, CommonInputer.class);

        intent.putExtra(Constants.CommonInputerConstant.TITLE_KEY, "商品更名");

        intent.putExtra(Constants.CommonInputerConstant.PLACEHOLDER_KEY, entity.getGoodname());

        startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CODE);

    }

    private void changeName(StockEntity entity) {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("agentId", 1);
        params.put("goodId", entity.getGood_id());
        params.put("goodname", entity.getGoodname());
        String strParams = params.toString();
        Events.StockRenameEvent event = new Events.StockRenameEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }
}
