package com.posagent.activities.goods;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.GoodsCategoryEntity;
import com.example.zf_android.entity.GoodsSearchEntity;
import com.example.zf_android.entity.IdValueEntity;
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
 * 过滤表单
 *
 */
public class FilterForm extends BaseActivity {

    private LinearLayout ll_brands, ll_category, ll_pay_channel,
            ll_pay_card, ll_trade_type, ll_sale_slip, ll_tdate;

    private TextView tv_brands, tv_category, tv_pay_channel,
            tv_pay_card, tv_trade_type, tv_sale_slip, tv_tdate;

    private EditText et_min_price, et_max_price;
    private ImageView img_on_off;


    private GoodsSearchEntity entity;
    private String currentKind;

    private Map<String, Object> mapFilter = new HashMap<String, Object>();

    private Boolean hasLease = false;
    private List<Integer> selectedCategoryIds = new ArrayList<Integer>();
    private List<Integer> selectedBrandsIds = new ArrayList<Integer>();
    private List<Integer> selectedPayChannelsIds = new ArrayList<Integer>();
    private List<Integer> selectedPayCardIds = new ArrayList<Integer>();
    private List<Integer> selectedTradeTypeIds = new ArrayList<Integer>();
    private List<Integer> selectedSaleSlipIds = new ArrayList<Integer>();
    private List<Integer> selectedTDateIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_form);

        String json = getIntent().getStringExtra("json");
        if (null != json) {
            mapFilter = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());

            if (null != mapFilter) {
                selectedCategoryIds = (List<Integer>)mapFilter.get("selectedCategoryIds");
                selectedBrandsIds = (List<Integer>)mapFilter.get("selectedBrandsIds");
                selectedPayChannelsIds = (List<Integer>)mapFilter.get("selectedPayChannelsIds");
                selectedPayCardIds = (List<Integer>)mapFilter.get("selectedPayCardIds");
                selectedTradeTypeIds = (List<Integer>)mapFilter.get("selectedTradeTypeIds");
                selectedSaleSlipIds = (List<Integer>)mapFilter.get("selectedSaleSlipIds");
                selectedTDateIds = (List<Integer>)mapFilter.get("selectedTDateIds");
                hasLease = (Boolean)mapFilter.get("hasLease");

                enterText("et_min_price", (String)mapFilter.get("minPrice"));
                enterText("et_max_price", (String)mapFilter.get("maxPrice"));

            }


        }

        new TitleMenuUtil(FilterForm.this, "筛选").show();


        initView();

    }

    private void initView() {
        ll_brands = (LinearLayout)findViewById(R.id.ll_brands);
        ll_brands.setOnClickListener(this);
        ll_category = (LinearLayout)findViewById(R.id.ll_category);
        ll_category.setOnClickListener(this);
        ll_pay_channel = (LinearLayout)findViewById(R.id.ll_pay_channel);
        ll_pay_channel.setOnClickListener(this);
        ll_pay_card = (LinearLayout)findViewById(R.id.ll_pay_card);
        ll_pay_card.setOnClickListener(this);
        ll_trade_type = (LinearLayout)findViewById(R.id.ll_trade_type);
        ll_trade_type.setOnClickListener(this);
        ll_sale_slip = (LinearLayout)findViewById(R.id.ll_sale_slip);
        ll_sale_slip.setOnClickListener(this);
        ll_tdate = (LinearLayout)findViewById(R.id.ll_tdate);
        ll_tdate.setOnClickListener(this);


        img_on_off = (ImageView)findViewById(R.id.img_on_off);
        img_on_off.setOnClickListener(this);




        tv_brands = (TextView)findViewById(R.id.tv_brands);
        tv_tdate = (TextView)findViewById(R.id.tv_tdate);
        tv_sale_slip = (TextView)findViewById(R.id.tv_sale_slip);
        tv_trade_type = (TextView)findViewById(R.id.tv_trade_type);
        tv_pay_card = (TextView)findViewById(R.id.tv_pay_card);
        tv_pay_channel = (TextView)findViewById(R.id.tv_pay_channel);
        tv_category = (TextView)findViewById(R.id.tv_category);

        et_min_price = (EditText)findViewById(R.id.et_min_price);
        et_max_price = (EditText)findViewById(R.id.et_max_price);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmit();
            }
        });

        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("cityId", MyApplication.user().getAgentCityId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.GoodsSearchItemEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void updateView() {

        updateHasRelease();

        if (null == entity) {
            return;
        }

        updateCategory();
        updateCommon(selectedBrandsIds, entity.getBrands(), tv_brands);
        updateCommon(selectedPayChannelsIds, entity.getPay_channel(), tv_pay_channel);
        updateCommon(selectedPayCardIds, entity.getPay_card(), tv_pay_card);
        updateCommon(selectedTradeTypeIds, entity.getTrade_type(), tv_trade_type);
        updateCommon(selectedSaleSlipIds, entity.getSale_slip(), tv_sale_slip);
        updateCommon(selectedTDateIds, entity.gettDate(), tv_tdate);
    }

    // events
    public void onEventMainThread(Events.GoodsSearchItemCompleteEvent event) {
        entity = event.getEntity();

        updateView();
    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        Intent i = new Intent(FilterForm.this, FilterItemSelect.class);
        switch (v.getId()) {
            case R.id.img_on_off:
                hasLease = !hasLease;
                updateHasRelease();
                break;
            case R.id.ll_brands:
                currentKind = "brands";
                i.putExtra("json", gson.toJson(entity.getBrands()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedBrandsIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_category:
                currentKind = "category";
                i.putExtra("categoryJson", gson.toJson(entity.getCategory()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedCategoryIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_pay_channel:
                currentKind = "pay_channel";
                i.putExtra("json", gson.toJson(entity.getPay_channel()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedPayChannelsIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_pay_card:
                currentKind = "pay_card";
                i.putExtra("json", gson.toJson(entity.getPay_card()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedPayCardIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_trade_type:
                currentKind = "trade_type";
                i.putExtra("json", gson.toJson(entity.getTrade_type()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedTradeTypeIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_sale_slip:
                currentKind = "sale_slip";
                i.putExtra("json", gson.toJson(entity.getSale_slip()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedSaleSlipIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_tdate:
                currentKind = "tdate";
                i.putExtra("json", gson.toJson(entity.gettDate()));
                i.putExtra("selectedIdsJson", gson.toJson(selectedTDateIds));
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
        }

        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                boolean selectAll = data.getBooleanExtra("selectAll", false);
                if (selectAll) {
                    if (currentKind.equals("category")) {
                        selectedCategoryIds.clear();
                        tv_category.setText("全部");
                    } else if (currentKind.equals("brands")) {
                        selectedBrandsIds.clear();
                        tv_brands.setText("全部");
                    } else if (currentKind.equals("pay_channel")) {
                        selectedPayChannelsIds.clear();
                        tv_pay_channel.setText("全部");
                    } else if (currentKind.equals("pay_card")) {
                        selectedPayCardIds.clear();
                        tv_pay_card.setText("全部");
                    } else if (currentKind.equals("trade_type")) {
                        selectedTradeTypeIds.clear();
                        tv_trade_type.setText("全部");
                    } else if (currentKind.equals("sale_slip")) {
                        selectedSaleSlipIds.clear();
                        tv_sale_slip.setText("全部");
                    } else if (currentKind.equals("tdate")) {
                        selectedTDateIds.clear();
                        tv_tdate.setText("全部");
                    }
                } else {
                    String selectIdsJson = data.getStringExtra("selectedIdsJson");
                    List<Integer> ids = gson.fromJson(selectIdsJson, new TypeToken<List<Integer>>() {}.getType());

                    if (currentKind.equals("category")) {
                        selectedCategoryIds = ids;
                        updateCategory();
                    } else if (currentKind.equals("brands")) {
                        selectedBrandsIds = ids;
                        updateCommon(selectedBrandsIds, entity.getBrands(), tv_brands);
                    } else if (currentKind.equals("pay_channel")) {
                        selectedPayChannelsIds = ids;
                        updateCommon(selectedPayChannelsIds, entity.getPay_channel(), tv_pay_channel);
                    } else if (currentKind.equals("pay_card")) {
                        selectedPayCardIds = ids;
                        updateCommon(selectedPayCardIds, entity.getPay_card(), tv_pay_card);
                    } else if (currentKind.equals("trade_type")) {
                        selectedTradeTypeIds = ids;
                        updateCommon(selectedTradeTypeIds, entity.getTrade_type(), tv_trade_type);
                    } else if (currentKind.equals("sale_slip")) {
                        selectedSaleSlipIds = ids;
                        updateCommon(selectedSaleSlipIds, entity.getSale_slip(), tv_sale_slip);
                    } else if (currentKind.equals("tdate")) {
                        selectedTDateIds = ids;
                        updateCommon(selectedTDateIds, entity.gettDate(), tv_tdate);
                    }
                }

                break;
        }
    }

    private void updateCommon(List<Integer> ids, List<IdValueEntity> entities, TextView tv) {
        List<Integer> newIds = new ArrayList<Integer>();
        for (Object id: ids) {
            if (id instanceof Double) {
                newIds.add(((Double)id).intValue());
            } else {
                newIds.add((Integer)id);
            }
        }
        if (newIds.size() > 0) {
            List<String> names = new ArrayList<String>();
            for (IdValueEntity item: entities) {
                if (newIds.contains(item.getId())) {
                    names.add(item.getValue());
                    continue;
                }
            }
            tv.setText(StringUtil.join(names, ","));
        } else {
            tv.setText("全部");
        }
    }

    private void updateCategory() {
        if (selectedCategoryIds.size() > 0) {

            List<Integer> newIds = new ArrayList<Integer>();
            for (Object id: selectedCategoryIds) {
                if (id instanceof Double) {
                    newIds.add(((Double)id).intValue());
                } else {
                    newIds.add((Integer)id);
                }
            }

            List<String> names = new ArrayList<String>();
            for (GoodsCategoryEntity category: entity.getCategory()) {
                if (newIds.contains(category.getId())) {
                    names.add(category.getValue());
                }

                List<IdValueEntity> sons = category.getSon();
                if (null != sons) {
                    for (IdValueEntity item: sons) {
                        if (newIds.contains(item.getId())) {
                            names.add(item.getValue());
                            continue;
                        }
                    }
                }
            }

            tv_category.setText(StringUtil.join(names, ","));
        } else {
            tv_category.setText("全部");
        }
    }

    private void doSubmit() {
        mapFilter = new HashMap<String, Object>();
        mapFilter.put("selectedCategoryIds", selectedCategoryIds);
        mapFilter.put("selectedBrandsIds", selectedBrandsIds);
        mapFilter.put("selectedPayChannelsIds", selectedPayChannelsIds);
        mapFilter.put("selectedPayCardIds", selectedPayCardIds);
        mapFilter.put("selectedTradeTypeIds", selectedTradeTypeIds);
        mapFilter.put("selectedSaleSlipIds", selectedSaleSlipIds);
        mapFilter.put("selectedTDateIds", selectedTDateIds);
        mapFilter.put("minPrice", getValue("et_min_price"));
        mapFilter.put("maxPrice", getValue("et_max_price"));
        mapFilter.put("hasLease", hasLease);


        Intent i = getIntent();
        i.putExtra("json", gson.toJson(mapFilter));
        setResult(RESULT_OK, i);
        finish();
    }

    private List<Integer> allList() {
        return new ArrayList<Integer>() {
            {
                add(0);
            }
        };
    }

    private void updateHasRelease() {
        if(hasLease){
            img_on_off.setBackgroundResource(R.drawable.pos_on);
        }else{
            img_on_off.setBackgroundResource(R.drawable.pos_off);
        }
    }


}
