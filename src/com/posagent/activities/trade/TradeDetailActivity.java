package com.posagent.activities.trade;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeDetail;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

public class TradeDetailActivity extends BaseActivity {

    private int id;

    private TradeDetail entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
        new TitleMenuUtil(this, getString(R.string.title_trade_detail)).show();

        id = getIntent().getIntExtra("id", 0);

        initView();
    }

    private void initView() {
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("agentId", 1);
        params.put("id", id);
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

        Resources resources = getResources();
        String[] tradeStatuses = resources.getStringArray(R.array.trade_status);

        setText("trade_detail_status", tradeStatuses[entity.getTradedStatus()]);
        setText("trade_detail_amount", "" + entity.getAmount());
        setText("trade_detail_poundage", "" + entity.getPoundage());
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
        }

        for (int i = 0; i < commercialKeys.length; i++) {
            TextView value = new TextView(TradeDetailActivity.this);
            value.setGravity(Gravity.LEFT);
            value.setPadding(0, 5, 0, 5);
            value.setTextColor(getResources().getColor(R.color.text6c6c6c6));
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            value.setText(i == 0 ? entity.getMerchantNumber()
                    : i == 1 ? entity.getAgentId() + ""
                    : i == 2 ? entity.getMerchantName()
                    : "");
            mCommercialValueContainer.addView(value);
        }

        for (int i = 0; i < bankKeys.length; i++) {
            TextView value = new TextView(TradeDetailActivity.this);
            value.setGravity(Gravity.LEFT);
            value.setPadding(0, 5, 0, 5);
            value.setTextColor(resources.getColor(R.color.text6c6c6c6));
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            value.setText(i == 0 ? entity.getTerminalNumber()
                    : i == 1 ? entity.getPayFromAccount()
                    : i == 2 ? entity.getPayIntoAccount()
                    : i == 3 ? entity.getPayChannelName()
                    : i == 4 ? entity.getProfitPrice() + ""
                    : i == 5 ? entity.getAmount() + ""
                    : i == 6 ? entity.getTradedTimeStr()
                    : i == 7 ? tradeStatuses[entity.getTradedStatus()]
                    : i == 8 ? entity.getBatchNumber()
                    : i == 9 ? entity.getTradeNumber()
                    : "");
            mBankValueContainer.addView(value);
        }
    }

}
