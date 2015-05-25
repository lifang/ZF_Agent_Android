package com.posagent.activities.agent;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.PrepareEntity;
import com.example.zf_zandroid.adapter.PrepareAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.trade.TradeAgentActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView Xlistview;

    private LinearLayout ll_choose_agent;

    private int page = 1;
    private int rows = Config.ROWS;
    private boolean onRefresh_number = true;
    private PrepareAdapter myAdapter;
    List<PrepareEntity> myList = new ArrayList<PrepareEntity>();
    List<PrepareEntity> moreList = new ArrayList<PrepareEntity>();

    private int sonAgentId;

    final static int REQUEST_TRADE_AGENT = 101;
    private String tradeAgentName;


    private View mTradeStart;
    private TextView mTradeStartDate;
    private View mTradeEnd;
    private TextView mTradeEndDate;

    private String tradeStartDate;
    private String tradeEndDate;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                    } else {
                        Xlistview.setVisibility(View.VISIBLE);
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

		setContentView(R.layout.activity_agent_cargo);
		new TitleMenuUtil(AgentCargoActivity.this, "配货管理").show();


        initXListView();

        initView();

	}

    private void initView() {
        //配置 配货 按钮
        TextView viewSetRate = (TextView)findViewById(R.id.next_sure);
        viewSetRate.setText("配货");
        viewSetRate.setVisibility(View.VISIBLE);
        viewSetRate.setOnClickListener(this);

        ll_choose_agent = (LinearLayout) findViewById(R.id.ll_choose_agent);
        ll_choose_agent.setOnClickListener(this);

        mTradeStart = findViewById(R.id.trade_start);
        mTradeStartDate = (TextView) findViewById(R.id.trade_start_date);
        mTradeEnd = findViewById(R.id.trade_end);
        mTradeEndDate = (TextView) findViewById(R.id.trade_end_date);

        findViewById(R.id.trade_search).setOnClickListener(this);

        mTradeStart.setOnClickListener(this);
        mTradeEnd.setOnClickListener(this);

//        getData();
    }

    private void initXListView() {
        myAdapter = new PrepareAdapter(AgentCargoActivity.this, myList);
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
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("sonAgentId", sonAgentId);
        params.put("startTime", tradeStartDate);
        params.put("endTime", tradeEndDate);
        params.put("page", page);
        params.put("rows", rows);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.PrepareListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.PrepareListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.next_sure) {
            startActivity(new Intent(this, AgentCargoCreateActivity.class));
            return;
        }

        if (v.getId() == R.id.trade_start) {
            showDatePicker(tradeStartDate, true);
            return;
        }

        if (v.getId() == R.id.trade_end) {
            showDatePicker(tradeEndDate, false);
            return;
        }

        if (v.getId() == R.id.trade_search) {
            onRefresh();
            return;
        }

        if (v.getId() == R.id.ll_choose_agent) {
            Intent iAgent = new Intent(AgentCargoActivity.this, AgentListActivity.class);
            iAgent.putExtra(AGENT_NAME, tradeAgentName);
            startActivityForResult(iAgent, REQUEST_TRADE_AGENT);
            return;
        }

        super.onClick(v);
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_TRADE_AGENT:
                String agentName = data.getStringExtra(AGENT_NAME);
                int agentId = data.getIntExtra(AGENT_ID, 0);
                tradeAgentName = agentName;
                setText("trade_agent_name", agentName);
                sonAgentId = agentId;
                break;
        }
    }


    /**
     * show the date picker
     *
     * @param date        the chosen date
     * @param isStartDate if true to choose the start date, else the end date
     * @return
     */
    private void showDatePicker(final String date, final boolean isStartDate) {

        final Calendar c = Calendar.getInstance();
        if (TextUtils.isEmpty(date)) {
            c.setTime(new Date());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                c.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINA);
                String dateStr = sdf.format(c.getTime());
                if (isStartDate) {
                    mTradeStartDate.setText(dateStr);
                    tradeStartDate = dateStr;
                } else {
                    mTradeEndDate.setText(dateStr);
                    tradeEndDate = dateStr;
                }
            }

        };

        new DatePickerDialog(AgentCargoActivity.this,
                datePicker,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

	 
}
