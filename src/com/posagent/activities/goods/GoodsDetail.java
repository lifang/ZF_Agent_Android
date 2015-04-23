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
import com.example.zf_android.activity.GoodComment;
import com.example.zf_android.entity.FactoryEntity;
import com.example.zf_android.entity.GoodinfoEntity;
import com.example.zf_android.entity.GoodsEntity;
import com.example.zf_android.entity.OtherRateEntity;
import com.example.zf_android.entity.PayChannelEntity;
import com.example.zf_android.entity.PayChannelInfoEntity;
import com.example.zf_android.entity.PicEntity;
import com.example.zf_android.entity.StandardRateEntity;
import com.example.zf_android.entity.TDateEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.fragments.HMSlideFragment;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;
import com.squareup.picasso.Picasso;

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
            tv_opening_requirement, tv_support_cancel, tv_require_material;

    ImageView iv_factory_logo;

    LinearLayout ll_pay_channel, ll_support_areas, ll_orderType;

    TableLayout tl_standard_rates, tl_tDates, tl_other_rate;

    Button btn_confirm_order;


    FactoryEntity factory;
    GoodinfoEntity goodinfo;
    PayChannelInfoEntity paychannelinfo;
    int commentCount;
    int orderType = Constants.Goods.OrderTypePigou;
    String goodFaceUrl;

    private GoodsEntity entity;

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
        tv_require_material = (TextView) findViewById(R.id.tv_require_material);
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

            i2.putExtra("faceUrl", goodFaceUrl);
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
        //Fixme
        params.put("agentId", 1);
        params.put("goodId", 110);
//        params.put("goodId", goodsId);
        params.put("cityId", 0);
//        params.put("type", goodsId);

        String strParams = params.toString();
        Events.GoodsDetailEvent event = new Events.GoodsDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.GoodsDetailCompleteEvent event) {
        if (event.getSuccess()) {
            entity = event.getEntity();
            updateInfo();
        }
    }

    public void onEventMainThread(Events.PayChannelInfoCompleteEvent event) {
        if (event.getSuccess()) {
            paychannelinfo = event.getEntity();
            Log.d(TAG, "updateChannelInfo");
            updateChannelInfo();
        }
    }

    private void updateInfo() {
        // slider
        ArrayList<PicEntity> list = new ArrayList<PicEntity>();
        int len = entity.getGoodPics().size();
        for(int i=0;i < len; i++) {
            String url = entity.getGoodPics().get(i);
            if (goodFaceUrl == null) {
                goodFaceUrl = url;
            }
            PicEntity picEntity = new PicEntity();
            picEntity.setPicture_url(url);
            list.add(picEntity);
        }
        initSlider(list);

        goodinfo = entity.getGoodinfo();
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


        List<PayChannelEntity> payChannelList = entity.getPayChannelList();

        //pay channel list
        if (payChannelList != null) {
            ll_pay_channel.removeAllViews();

            for (final PayChannelEntity channel: payChannelList) {

                final PayChannelEntity mychannel = channel;

                TextView tv = new TextView(this);
                tv.setText(channel.getName());
                tv.setTag(channel);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 10, 0);
                tv.setLayoutParams(lp);
                tv.setBackgroundResource(R.drawable.bg_shape);
                tv.setPadding(5, 5, 5, 5);
                tv.setTextSize(10);
                ll_pay_channel.addView(tv);

                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePayChannel(channel);
                    }
                });

            }
        }


        //pay channel info
        updateChannelInfo();

        // comment count
        tv_comment_count.setText("查看评论（" + entity.getCommentsCount() + "）");
        tv_comment_count.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GoodsDetail.this, GoodComment.class);
                i.putExtra("commentsCount", entity.getCommentsCount());
                i.putExtra("goodId", goodsId);
                startActivity(i);
            }
        });

        factory = entity.getFactory();

        updateFactory();
    }

    private void updateFactory() {
        //factory
        Picasso.with(this.getApplicationContext()).load(factory.getLogo_file_path())
                .into(iv_factory_logo);
        tv_factory_url.setText(factory.getWebsite_url());
        tv_factory_desc.setText(factory.getDescription());
    }

    private void updateChannelInfo() {

        paychannelinfo = entity.getPaychannelinfo();

        if (paychannelinfo == null) {
            return;
        }

        boolean support_cancel_flag = paychannelinfo.isSupport_cancel_flag();
        if (support_cancel_flag) {
            tv_support_cancel.setText("支持");
        } else {
            tv_support_cancel.setText("不支持");
        }

        String opening_requirement = paychannelinfo.getOpening_requirement();
        if (opening_requirement != null) {
            tv_opening_requirement.setText(opening_requirement);
        }

        tv_require_material.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GoodsDetail.this, RequireMaterial.class);
                i.putExtra("json_pra", gson.toJson(paychannelinfo.getRequireMaterial_pra()));
                i.putExtra("json_pub", gson.toJson(paychannelinfo.getRequireMaterial_pub()));
                startActivity(i);
            }
        });

        //area
        List<String> areas = paychannelinfo.getSupportAreas();
        if (areas != null) {
            ll_support_areas.removeAllViews();
            for (String area: areas) {
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
            }
        }

        //standard rate
        List<StandardRateEntity> standard_rates = paychannelinfo.getStandard_rates();
        if (standard_rates != null) {
            tl_standard_rates.removeAllViews();
            int len = standard_rates.size();
            boolean isLast = false;
            for (int i = 0; i < len; i++) {
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

                StandardRateEntity rate = standard_rates.get(i);
                List<String> list = new ArrayList<String>();
                list.add(rate.getName());
                list.add("" + rate.getStandard_rate() + "‰");
                list.add(rate.getDescription());
                TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                tl_standard_rates.addView(tr,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

        //tl_tDates rate
        List<TDateEntity> tDates = paychannelinfo.gettDates();
        if (tDates != null) {
            tl_tDates.removeAllViews();
            int len = tDates.size();
            boolean isLast = false;
            for (int i = 0; i < len; i++) {
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

                TDateEntity rate = tDates.get(i);
                List<String> list = new ArrayList<String>();
                list.add(rate.getName());
                list.add("" + rate.getService_rate() + "‰");
                list.add(rate.getDescription());
                TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                tl_tDates.addView(tr,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }


        //tl_other_rate rate
        List<OtherRateEntity> other_rate = paychannelinfo.getOther_rates();
        if (other_rate != null) {
            tl_other_rate.removeAllViews();
            int len = other_rate.size();
            boolean isLast = false;
            for (int i = 0; i < len; i++) {
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

                OtherRateEntity rate = other_rate.get(i);
                List<String> list = new ArrayList<String>();
                list.add(rate.getTrade_value());
                list.add("" + rate.getTerminal_rate() + "‰");
                list.add(rate.getDescription());
                TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                tl_other_rate.addView(tr,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

        if (null != paychannelinfo.getPcfactory()) {
            factory = paychannelinfo.getPcfactory();
            updateFactory();
        }

    }


    private void initSlider(ArrayList<PicEntity> list) {
        HMSlideFragment slideFragment = (HMSlideFragment) getFragmentManager().findFragmentById(R.id.headlines_fragment);
        slideFragment.feedData(list);
    }

    private void changePayChannel(PayChannelEntity channel) {
        JsonParams params = new JsonParams();
        params.put("pcid", channel.getId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.PayChannelInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }
}
