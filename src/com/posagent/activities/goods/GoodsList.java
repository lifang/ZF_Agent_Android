package com.posagent.activities.goods;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.activity.SearchFormActivity;
import com.example.zf_android.entity.PosEntity;
import com.example.zf_zandroid.adapter.PosAdapter;
import com.google.gson.reflect.TypeToken;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/***
*
* 商品列表
*
*/
public class GoodsList extends BaseActivity implements XListView.IXListViewListener {

    private XListView Xlistview;
    private boolean onRefresh_number = true;

    private int page = 1;
    private int rows = Config.ROWS;
    int buyType = Constants.Goods.OrderTypePigou;
    int orderType;

    private String keys;
    private String minPrice;
    private String maxPrice;
    private Map<String, Object> mapFilter;
    private List<Integer> selectedCategoryIds = new ArrayList<Integer>();
    private List<Integer> selectedBrandsIds = new ArrayList<Integer>();
    private List<Integer> selectedPayChannelsIds = new ArrayList<Integer>();
    private List<Integer> selectedPayCardIds = new ArrayList<Integer>();
    private List<Integer> selectedTradeTypeIds = new ArrayList<Integer>();
    private List<Integer> selectedSaleSlipIds = new ArrayList<Integer>();
    private List<Integer> selectedTDateIds = new ArrayList<Integer>();
    private Boolean hasLease = false;

    private LinearLayout eva_nodata;
    private PosAdapter myAdapter;
    List<PosEntity> myList = new ArrayList<PosEntity>();

    private Handler handler = new Handler() {
        @Override
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
                case 10:
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_goods_list);

        buyType = getIntent().getIntExtra("buyType", Constants.Goods.OrderTypePigou);

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();
        initView();
        initXListView();

	}

    private void initView() {

        findViewById(R.id.titleback_linear_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.serch_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GoodsList.this, SearchFormActivity.class);
                i.putExtra("keys", keys);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

        findViewById(R.id.iv_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GoodsList.this, FilterForm.class);
                i.putExtra("json", gson.toJson(mapFilter));
                startActivityForResult(i, Constants.REQUEST_CODE2);
            }
        });



        //order type
        View v = findViewById(R.id.btn_ordertype_0);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 0;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_1);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 1;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_4);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType = 4;
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });

        v = findViewById(R.id.btn_ordertype_2_3);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderType == 2) {
                    orderType = 3;
                    TextView tv = (TextView) v;
                    tv.setText("价格升序");
                    ImageView iv = (ImageView) findViewById(R.id.icon_ordertype_2_3);
                    iv.setImageResource(R.drawable.ti_up);
                } else {
                    orderType = 2;
                    TextView tv = (TextView) v;
                    tv.setText("价格降序");
                    ImageView iv = (ImageView) findViewById(R.id.icon_ordertype_2_3);
                    iv.setImageResource(R.drawable.ti_down);
                }
                afterResetOrderType((TextView)v);
                onRefresh();
            }
        });
    }

    private void initXListView() {
        myAdapter = new PosAdapter(GoodsList.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);

        Xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
           goDetail(myList.get(position - 1).getId());
            }
        });

        getData();
    }



    @Override
    public void onRefresh() {
        page = 1;
        myList.clear();
        handler.sendEmptyMessage(10);
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
        handler.sendEmptyMessage(0);
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("cityId", MyApplication.user().getAgentCityId());
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("orderType", orderType);
        params.put("type", buyType);

        if (keys != null) {
            params.put("keys", keys);
        }
        if (selectedBrandsIds != null && selectedBrandsIds.size() > 0) {
            params.put("brandsId", StringUtil.integerList(selectedBrandsIds));
        }
        if (selectedCategoryIds != null && selectedCategoryIds.size() > 0) {
            params.put("category", selectedCategoryIds.get(0));
        }
        if (selectedPayChannelsIds != null && selectedPayChannelsIds.size() > 0) {
            params.put("payChannelId", StringUtil.integerList(selectedPayChannelsIds));
        }
        if (selectedPayCardIds != null && selectedPayCardIds.size() > 0) {
            params.put("payCardId", StringUtil.integerList(selectedPayCardIds));
        }
        if (selectedTradeTypeIds != null && selectedTradeTypeIds.size() > 0) {
            params.put("tradeTypeId", StringUtil.integerList(selectedTradeTypeIds));
        }
        if (selectedSaleSlipIds != null && selectedSaleSlipIds.size() > 0) {
            params.put("saleSlipId", StringUtil.integerList(selectedSaleSlipIds));
        }
        if (selectedTDateIds != null && selectedTDateIds.size() > 0) {
            params.put("tDate", StringUtil.integerList(selectedTDateIds));
        }
        if (minPrice != null && !"".equals(minPrice)) {
            params.put("minPrice", Double.parseDouble(minPrice));
        }
        if (maxPrice != null && !"".equals(maxPrice)) {
            params.put("maxPrice", Double.parseDouble(maxPrice));
        }
        if (hasLease) {
            params.put("hasLease", 1);
        }



        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        EventBus.getDefault().post(new Events.GoodsListEvent(strParams));
    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                keys = data.getStringExtra("keys");
                onRefresh();
                break;
            case Constants.REQUEST_CODE2:
                String json = data.getStringExtra("json");
                if (null != json) {
                    mapFilter = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());

                }
                searchWithFilter();
                break;
        }
    }

    // events
    public void onEventMainThread(Events.GoodsListCompleteEvent event) {
        if (null != event.getList()) {
            myList.addAll(event.getList());
        }

        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }
    public void onEventMainThread(Events.GoodsDoSearchCompleteEvent event) {
        keys = event.getKeys();
        onRefresh();
    }

    public int pos_item_layout() {
        if (buyType == Constants.Goods.OrderTypePigou) {
            return R.layout.pos_item;
        }
        return R.layout.pos_item_daigou;
    }

    public int getBuyType() {
        return buyType;
    }


    // after reset order type
    private void afterResetOrderType(TextView tv) {
        String[] ids = {"btn_ordertype_0", "btn_ordertype_1", "btn_ordertype_2_3", "btn_ordertype_4"};
        for (String strId : ids) {
            int resourceId = resouceId(strId, "id");
            TextView _tv = (TextView)findViewById(resourceId);
            _tv.setTextColor(getResources().getColor(R.color.text292929));
        }
        tv.setTextColor(getResources().getColor(R.color.bg_item));

    }

    private void searchWithFilter() {

        selectedCategoryIds = (List<Integer>)mapFilter.get("selectedCategoryIds");
        selectedBrandsIds = (List<Integer>)mapFilter.get("selectedBrandsIds");
        selectedPayChannelsIds = (List<Integer>)mapFilter.get("selectedPayChannelsIds");
        selectedPayCardIds = (List<Integer>)mapFilter.get("selectedPayCardIds");
        selectedTradeTypeIds = (List<Integer>)mapFilter.get("selectedTradeTypeIds");
        selectedSaleSlipIds = (List<Integer>)mapFilter.get("selectedSaleSlipIds");
        selectedTDateIds = (List<Integer>)mapFilter.get("selectedTDateIds");
        hasLease = (Boolean)mapFilter.get("hasLease");

        minPrice = (String)mapFilter.get("minPrice");
        maxPrice = (String)mapFilter.get("maxPrice");


        onRefresh();
    }

    private void goDetail(int id) {
        Intent i = new Intent (GoodsList.this, GoodsDetail.class);
        i.putExtra("id", id);
        i.putExtra("buyType", buyType);
        startActivity(i);
    }
	 
}
