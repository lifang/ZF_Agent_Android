package com.posagent.activities.trade;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.trade.entity.TradeDetail;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

public class TradeDetailActivity extends BaseActivity {

    private int id;
    private int tradeType;

    private TradeDetail entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
        new TitleMenuUtil(this, getString(R.string.title_trade_detail)).show();

        id = getIntent().getIntExtra("id", 0);
        tradeType = getIntent().getIntExtra("tradeType", 1);

        initView();
    }

    private void initView() {
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("id", id);

        //Fixme
        params.put("isHaveProfit", 1);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.TradeDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.TradeDetailCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
    }

    private void updateView() {

        View k1 = null, k2 = null, v1 = null, v2 = null;

        Resources resources = getResources();
        String statusName = "未知";

        if (entity.getTradedStatus() == 1) {
            statusName = "交易成功";
        } else if (entity.getTradedStatus() == 2) {
            statusName = "交易失败";
        } else if (entity.getTradedStatus() == 3) {
            statusName = "交易结果待确认";
        }

        setText("trade_detail_status", statusName);
        setText("trade_detail_amount", "￥" + StringUtil.priceShow(entity.getAmount()));
        setText("trade_detail_poundage",  "￥" + StringUtil.priceShow(entity.getPoundage()));
        setText("trade_detail_time", "" + entity.getTradedTimeStr());

        LinearLayout mCommercialValueContainer = (LinearLayout) findViewById(R.id.trade_commercial_value_container);
        LinearLayout mBankValueContainer = (LinearLayout) findViewById(R.id.trade_bank_value_container);
        LinearLayout mBankKeyContainer = (LinearLayout) findViewById(R.id.trade_bank_key_container);
        LinearLayout mCommercialKeyContainer = (LinearLayout) findViewById(R.id.trade_commercial_key_container);

        String[] commercialKeys = resources.getStringArray(R.array.trade_item_commercial);
        String[] bankKeys = resources.getStringArray(R.array.trade_item_bank);

        for (int i = 0; i < commercialKeys.length; i++) {
            TextView key = new TextView(this);
            key.setGravity(Gravity.RIGHT);
            key.setPadding(0, 5, 0, 5);
            key.setTextColor(resources.getColor(R.color.text6c6c6c6));
            key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            key.setTag(commercialKeys[i]);
            key.setText(commercialKeys[i]);
            mCommercialKeyContainer.addView(key);
        }

        for (int i = 0; i < bankKeys.length; i++) {
            TextView key = new TextView(this);
            key.setGravity(Gravity.RIGHT);
            key.setPadding(0, 5, 0, 5);
            key.setTextColor(resources.getColor(R.color.text6c6c6c6));
            key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            key.setText(bankKeys[i]);
            mBankKeyContainer.addView(key);
            if (i == 1) {
                k1 = key;
            } else if (i == 2) {
                k2 = key;
            }
        }

        for (int i = 0; i < commercialKeys.length; i++) {
            TextView value = new TextView(TradeDetailActivity.this);
            value.setGravity(Gravity.LEFT);
            value.setPadding(0, 5, 0, 5);
            value.setTextColor(getResources().getColor(R.color.text6c6c6c6));
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            value.setText(i == 0 ? entity.getMerchant_number()
                    : i == 1 ? entity.getAgentName() + ""
                    : i == 2 ? entity.getMerchant_name()
                    : "");
            mCommercialValueContainer.addView(value);
        }

        for (int i = 0; i < bankKeys.length; i++) {
            TextView value = new TextView(TradeDetailActivity.this);
            value.setGravity(Gravity.LEFT);
            value.setPadding(0, 5, 0, 5);
            value.setTextColor(resources.getColor(R.color.text6c6c6c6));
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            if (i == 1) {
                v1 = value;
            } else if (i == 2) {
                v2 = value;
            }

            String text = "";
            switch (i) {
                case 0:
                    text = entity.getTerminalNumber();
                    break;
                case 1:
                    text = StringUtil.star(entity.getPayFromAccount(), 9, 12);
                    break;
                case 2:
                    text = StringUtil.star(entity.getPayIntoAccount(), 9, 12);

                    break;
                case 3:
                    text = entity.getPaychannel();
                    break;
                case 4:
                    text = "￥" + StringUtil.priceShow(entity.getProfitPrice());
                    break;
                case 5:
                    text = "￥" + StringUtil.priceShow(entity.getProfitPrice());
                    break;
                case 6:
                    text = "￥" + StringUtil.priceShow(entity.getAmount());
                    break;
                case 7:
                    text = entity.getTradedTimeStr();
                    break;
                case 8:
                    text = statusName;
                    break;
                case 9:
                    text = entity.getBatchNumber();
                    break;
                case 10:
                    text = entity.getTradeNumber();
                    break;
                default:

            }

            value.setText(text);
            mBankValueContainer.addView(value);



            if (tradeType == 4) {
                if (null != k1) {
                    k1.setVisibility(View.GONE);
                }
                if (null != v1) {
                    v1.setVisibility(View.GONE);
                }

                if (null != k2) {
                    ((TextView)k2).setText("手机号码");
                }
                if (null != v2) {
                    ((TextView)v2).setText(StringUtil.star(entity.getPhone(), 4, 8));
                }

            } else if (tradeType == 1) {
                if (null != k1) {
                    k1.setVisibility(View.GONE);
                }
                if (null != v1) {
                    v1.setVisibility(View.GONE);
                }

                if (null != k2) {
                    ((TextView)k2).setText("手续费");
                }
                if (null != v2) {
                    ((TextView)v2).setText("￥" + StringUtil.priceShow(entity.getPoundage()));
                }

            } else if (tradeType == 5) {
                if (null != k1) {
                    ((TextView)k1).setText("账户名");
                }
                if (null != v1) {
                    ((TextView)v1).setText(StringUtil.star(entity.getAccount_name(), 3, 4));
                }

                if (null != k2) {
                    ((TextView)k2).setText("账户帐号");
                }
                if (null != v2) {
                    ((TextView)v2).setText(StringUtil.star(entity.getAccount_number(), 11, 15));
                }

            }
        }


    }

}
