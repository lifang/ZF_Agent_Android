package com.posagent.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeStatistic;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.CLIENT_NUMBER;
import static com.example.zf_android.trade.Constants.TradeIntent.END_DATE;
import static com.example.zf_android.trade.Constants.TradeIntent.SON_AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.START_DATE;
import static com.example.zf_android.trade.Constants.TradeIntent.TRADE_TYPE;

public class TradeStatisticActivity extends BaseActivity {

    private int mAgentId;
    private int mSonAgentId;
    private int mTradeType;
    private String mStartDate;
    private String mEndDate;
    private String mClientNumber;

    private TextView statisticAmount;
    private TextView statisticCount;
    private TextView statisticTime;
    private TextView statisticClient;
    private TextView statisticChannel;

    private TradeStatistic entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mAgentId = intent.getIntExtra(AGENT_ID, 1);
        mSonAgentId = intent.getIntExtra(SON_AGENT_ID, 1);
        mTradeType = intent.getIntExtra(TRADE_TYPE, 1);
        mStartDate = intent.getStringExtra(START_DATE);
        mEndDate = intent.getStringExtra(END_DATE);
        mClientNumber = intent.getStringExtra(CLIENT_NUMBER);

        setContentView(R.layout.activity_trade_statistic);
        initViews();

    }

    private void initViews() {
        new TitleMenuUtil(this, getString(R.string.title_trade_statistic)).show();

        statisticAmount = (TextView) findViewById(R.id.trade_statistic_amount);
        statisticCount = (TextView) findViewById(R.id.trade_statistic_count);
        statisticTime = (TextView) findViewById(R.id.trade_statistic_time);
        statisticClient = (TextView) findViewById(R.id.trade_statistic_client);
        statisticChannel = (TextView) findViewById(R.id.trade_statistic_channel);

        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", mAgentId);
        params.put("sonagentId", mSonAgentId);

        params.put("tradeTypeId", mTradeType);
        params.put("terminalNumber", mClientNumber);
        params.put("startTime", mStartDate);
        params.put("endTime", mEndDate);


        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.TradeStatisticEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.TradeStatisticCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    private void updateView() {
        statisticAmount = (TextView) findViewById(R.id.trade_statistic_amount);
        statisticCount = (TextView) findViewById(R.id.trade_statistic_count);
        statisticTime = (TextView) findViewById(R.id.trade_statistic_time);
        statisticClient = (TextView) findViewById(R.id.trade_statistic_client);
        statisticChannel = (TextView) findViewById(R.id.trade_statistic_channel);

        statisticAmount.setText(entity.getAmountTotal());
        statisticCount.setText(entity.getTradeTotal());
        statisticTime.setText( mStartDate + " - " + mEndDate);
        statisticClient.setText(entity.getTerminalNumber());
        statisticChannel.setText(entity.getPayChannelName());

    }
}
