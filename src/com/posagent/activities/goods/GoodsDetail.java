package com.posagent.activities.goods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.FactoryEntity;
import com.example.zf_android.entity.GoodinfoEntity;
import com.example.zf_android.entity.PicEntity;
import com.google.gson.Gson;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.fragments.HMSlideFragment;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class GoodsDetail extends BaseActivity implements OnClickListener {
    static final String TAG = "GoodsDetail";


    TextView tvTitle, tvSubtitle, tvQuantity, tvBrand, tv_origin_price,
                tv_model, tv_price, tv_terminal_kind, tv_sold_count,
                tv_comment_count, tv_brand2, tv_shell_material,
                tv_battery_info, tv_sign_order_way, tv_encrypt_card_way,
                tv_factory_url, tv_factory_desc, tv_goods_desc,
            tv_opening_requirement, tv_support_cancel;

    ImageView iv_factory_logo;

    LinearLayout ll_pay_channel, ll_support_areas, ll_orderType;

    TableLayout tl_standard_rates, tl_tDates, tl_other_rate;

    Button btn_confirm_order;

    JSONObject paychannelinfo;
    JSONArray payChannelList;
    FactoryEntity factory;
    GoodinfoEntity goodinfo;
    JSONArray goodPics;
    int commentCount;
    int orderType = Constants.Goods.OrderTypePigou;

    int goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        goodsId = getIntent().getIntExtra("id", 0);
        orderType = getIntent().getIntExtra("orderType", Constants.Goods.OrderTypePigou);

        initView();
        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{

        }};
        this.setClickableMap(clickableMap);
        new TitleMenuUtil(GoodsDetail.this, "商品详情").show();

    }

    private void initView() {
        //init id elements
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        tvQuantity = (TextView) findViewById(R.id.tv_quantity);
        tvBrand = (TextView) findViewById(R.id.tv_brand);
        tv_origin_price = (TextView) findViewById(R.id.tv_origin_price);
        tv_model = (TextView) findViewById(R.id.tv_model);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_terminal_kind = (TextView) findViewById(R.id.tv_terminal_kind);
        tv_sold_count = (TextView) findViewById(R.id.tv_sold_count);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        tv_brand2 = (TextView) findViewById(R.id.tv_brand2);
        tv_shell_material = (TextView) findViewById(R.id.tv_shell_material);
        tv_battery_info = (TextView) findViewById(R.id.tv_battery_info);
        tv_sign_order_way = (TextView) findViewById(R.id.tv_sign_order_way);
        tv_encrypt_card_way = (TextView) findViewById(R.id.tv_encrypt_card_way);
        iv_factory_logo = (ImageView) findViewById(R.id.iv_factory_logo);
        tv_factory_url = (TextView) findViewById(R.id.tv_factory_url);
        tv_factory_desc = (TextView) findViewById(R.id.tv_factory_desc);
        tv_goods_desc = (TextView) findViewById(R.id.tv_goods_desc);
        tv_opening_requirement = (TextView) findViewById(R.id.tv_opening_requirement);
        tv_support_cancel = (TextView) findViewById(R.id.tv_support_cancel);
        btn_confirm_order = (Button) findViewById(R.id.btn_confirm_order);

        ll_orderType = (LinearLayout) findViewById(R.id.ll_orderType);
        if (orderType != Constants.Goods.OrderTypePigou) {
            ll_orderType.setVisibility(View.VISIBLE);
            btn_confirm_order.setText("立即代购");
        } else {
            ll_orderType.setVisibility(View.GONE);
        }

        ll_pay_channel = (LinearLayout) findViewById(R.id.ll_pay_channel);
        ll_support_areas = (LinearLayout) findViewById(R.id.ll_support_areas);
        tl_standard_rates = (TableLayout) findViewById(R.id.tl_standard_rates);
        tl_tDates = (TableLayout) findViewById(R.id.tl_tDates);
        tl_other_rate = (TableLayout) findViewById(R.id.tl_other_rate);


        View btn_confirm_order = (View) findViewById(R.id.btn_confirm_order);
        btn_confirm_order.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_confirm_order) {
            Intent i2 =new Intent(GoodsDetail.this, GoodsConfirm.class);
            i2.putExtra("getTitle", goodinfo.getTitle());
            i2.putExtra("price", goodinfo.getPurchase_price());
            i2.putExtra("model", goodinfo.getModel_number());
            i2.putExtra("orderType", orderType);
            //Fixme
            i2.putExtra("paychannelId", 11);
            i2.putExtra("goodId", goodinfo.getId());
            startActivity(i2);

            return;
        }



        super.onClick(v);
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("goodId", goodsId);
        // Fixme
//        params.put("goodId", 1);
        String strParams = params.toString();
        Events.GoodsDetailEvent event = new Events.GoodsDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.GoodsDetailCompleteEvent event) {
        if (event.getSuccess()) {
            JSONObject result = event.getResult();

            Log.d(TAG, String.valueOf(result));

            Gson gson = new Gson();

            try {
                paychannelinfo = result.getJSONObject("paychannelinfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                commentCount = result.getInt("commentsCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                payChannelList = result.getJSONArray("payChannelList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                factory = gson.fromJson(result.getString("factory"), FactoryEntity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                goodinfo = gson.fromJson(result.getString("goodinfo"), GoodinfoEntity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                goodPics = result.getJSONArray("goodPics");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            updateInfo();
        }
    }

    private void updateInfo() {
        // slider
        ArrayList<PicEntity> list = new ArrayList<PicEntity>();
        int len = goodPics.length();
        for(int i=0;i < len; i++) {
            try {
                String url = goodPics.getString(i);
                PicEntity picEntity = new PicEntity();
                picEntity.setPicture_url(url);
                list.add(picEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initSlider(list);

        //goodinfo
        tvTitle.setText(goodinfo.getTitle());
        tvSubtitle.setText(goodinfo.getSecond_title());
        tvQuantity.setText("" + goodinfo.getQuantity() + "件");
        tvBrand.setText(goodinfo.getGood_brand());
        tv_model.setText(goodinfo.getModel_number());
        tv_origin_price.setText("原价 ￥" + goodinfo.getPrice());
        tv_sold_count.setText("已售" + goodinfo.getVolume_number());
        tv_price.setText("￥" + goodinfo.getPurchase_price());
        tv_brand2.setText(goodinfo.getGood_brand());
        tv_shell_material.setText(goodinfo.getShell_material());
        tv_battery_info.setText(goodinfo.getBattery_info());
        tv_sign_order_way.setText(goodinfo.getSign_order_way());
        tv_encrypt_card_way.setText(goodinfo.getEncrypt_card_way());
        tv_goods_desc.setText(goodinfo.getDescription());

        //Fixme
        tv_terminal_kind.setText("fixme");


        //pay channel list
        if (payChannelList != null) {
            ll_pay_channel.removeAllViews();
            len = payChannelList.length();
            for (int i = 0; i < len; i++) {
                try {
                    JSONObject channel = payChannelList.getJSONObject(i);
                    TextView tv = new TextView(this);
                    tv.setText(channel.getString("name"));
                    tv.setTag(channel);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 5, 0);
                    tv.setLayoutParams(lp);


                    tv.setTextSize(10);
                    ll_pay_channel.addView(tv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //pay channel info
        updateChannelInfo();

        // comment count
        tv_comment_count.setText("查看评论（" + commentCount + "）");

        //factory
        Picasso.with(this.getApplicationContext()).load(factory.getLogo_file_path())
                .into(iv_factory_logo);
        tv_factory_url.setText(factory.getWebsite_url());
        tv_factory_desc.setText(factory.getDescription());

    }

    private void updateChannelInfo() {

        if (paychannelinfo == null) {
            return;
        }

        try {
            boolean support_cancel_flag = paychannelinfo.getBoolean("support_cancel_flag");
            if (support_cancel_flag) {
                tv_support_cancel.setText("支持");
            } else {
                tv_support_cancel.setText("不支持");
            }

            String opening_requirement = paychannelinfo.getString("opening_requirement");
            if (opening_requirement != null) {
                tv_opening_requirement.setText(opening_requirement);
            }

            //area
            JSONArray areas = paychannelinfo.getJSONArray("supportArea");
            if (areas != null) {
                ll_support_areas.removeAllViews();
                int len = areas.length();
                for (int i = 0; i < len; i++) {
                    try {
                        String area = areas.getString(i);
                        TextView tv = new TextView(this);
                        tv.setText(area);
                        tv.setTextColor(getResources().getColor(R.color.tmc));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 5, 0);
                        tv.setLayoutParams(lp);


                        tv.setTextSize(12);
                        ll_support_areas.addView(tv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //standard rate
            JSONArray standard_rates = paychannelinfo.getJSONArray("standard_rates");
            if (standard_rates != null) {
                tl_standard_rates.removeAllViews();
                int len = standard_rates.length();
                boolean isLast = false;
                for (int i = 0; i < len; i++) {
                    try {

                        if (i == len - 1) {
                            isLast = true;
                        }

                        if (i == 0) {
                            List<String> list = new ArrayList<String>();
                            list.add("商户类型");
                            list.add("费率");
                            list.add("说明");
                            TableRow tr = ViewHelper.tableRow(this, list, R.color.text292929, 12, isLast);
                            tl_standard_rates.addView(tr,
                                    new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));
                        }

                        JSONObject rate = standard_rates.getJSONObject(i);
                        List<String> list = new ArrayList<String>();
                        list.add(rate.getString("name"));
                        list.add(rate.getString("standard_rate"));
                        list.add(rate.getString("description"));
                        TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                        tl_standard_rates.addView(tr,
                                new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //tl_tDates rate
            JSONArray tDates = paychannelinfo.getJSONArray("tDates");
            if (tDates != null) {
                tl_tDates.removeAllViews();
                int len = tDates.length();
                boolean isLast = false;
                for (int i = 0; i < len; i++) {
                    try {

                        if (i == len - 1) {
                            isLast = true;
                        }

                        if (i == 0) {
                            List<String> list = new ArrayList<String>();
                            list.add("结算周期");
                            list.add("费率");
                            list.add("说明");
                            TableRow tr = ViewHelper.tableRow(this, list, R.color.text292929, 12, isLast);
                            tl_tDates.addView(tr,
                                    new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));
                        }

                        JSONObject rate = tDates.getJSONObject(i);
                        List<String> list = new ArrayList<String>();
                        list.add(rate.getString("name"));
                        list.add(rate.getString("service_rate"));
                        list.add(rate.getString("description"));
                        TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                        tl_tDates.addView(tr,
                                new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            //tl_other_rate rate
            JSONArray other_rate = paychannelinfo.getJSONArray("other_rate");
            if (other_rate != null) {
                tl_other_rate.removeAllViews();
                int len = other_rate.length();
                boolean isLast = false;
                for (int i = 0; i < len; i++) {
                    try {

                        if (i == len - 1) {
                            isLast = true;
                        }

                        if (i == 0) {
                            List<String> list = new ArrayList<String>();
                            list.add("交易类型");
                            list.add("费率");
                            list.add("说明");
                            TableRow tr = ViewHelper.tableRow(this, list, R.color.text292929, 12, isLast);
                            tl_other_rate.addView(tr,
                                    new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));
                        }

                        JSONObject rate = other_rate.getJSONObject(i);
                        List<String> list = new ArrayList<String>();
                        list.add(rate.getString("trade_value"));
                        list.add(rate.getString("terminal_rate"));
                        list.add(rate.getString("description"));
                        TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                        tl_other_rate.addView(tr,
                                new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initSlider(ArrayList<PicEntity> list) {
        HMSlideFragment slideFragment = (HMSlideFragment) getFragmentManager().findFragmentById(R.id.headlines_fragment);
        slideFragment.feedData(list);
    }
}
