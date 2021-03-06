package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.activity.SearchFormCommon;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.TerminalAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 终端管理
 */
public class Terminal extends BaseActivity implements IXListViewListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener
{
    private Button btn_bind_terminal, btn_after_sale_apply;
    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private TerminalAdapter myAdapter;
    List<TerminalItem> myList = new ArrayList<TerminalItem>();
    List<TerminalItem> moreList = new ArrayList<TerminalItem>();

    private String keys;

    private Spinner spinnerState;
    private String[] state= {"选择终端状态","已开通","部分开通","未开通","已注销","已停用"};
    private int status;

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
        setContentView(R.layout.activity_terminal);
        initView();
    }

    private void initView() {
        // set up click
        btn_after_sale_apply = (Button) findViewById(R.id.btn_after_sale_apply);
        btn_after_sale_apply.setOnClickListener(this);
        btn_bind_terminal = (Button) findViewById(R.id.btn_bind_terminal);
        btn_bind_terminal.setOnClickListener(this);

        show("iv_search_icon_terminal");
        findViewById(R.id.iv_search_icon_terminal).setOnClickListener(this);

        new TitleMenuUtil(Terminal.this, "终端管理").show();
        myAdapter = new TerminalAdapter(Terminal.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);


        spinnerState = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, state);
        spinnerState.setAdapter(adapter_state);
        spinnerState.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status = position;
        onRefresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        myList.clear();
        myAdapter.notifyDataSetChanged();
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
        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        if (status != 0) {
            params.put("status", status);
        }
        if (null != keys) {
            params.put("serialNum", keys);
        }
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.TerminalListEvent event = new Events.TerminalListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.btn_after_sale_apply:
                Intent i = new Intent(Terminal.this, AfterSaleApply.class);
                startActivity(i);
                break;
            case R.id.btn_bind_terminal:
                Intent i2 = new Intent(Terminal.this, TerminalUserBind.class);
                startActivity(i2);

                break;
            case R.id.iv_search_icon_terminal:
                Intent i3 = new Intent(context, SearchFormCommon.class);
                i3.putExtra("keys", keys);
                i3.putExtra("save_key", "TerminalKeyHistory");
                i3.putExtra("hint_text", "输入终端号");
                startActivityForResult(i3, Constants.REQUEST_CODE);
                break;
            default:
                break;
        }


        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.TerminalListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.GoodsDoSearchCompleteEvent event) {
        keys = event.getKeys();
        onRefresh();
    }

    public void onEventMainThread(Events.CreateAfterSaleCompleteEvent event) {
        onRefresh();
    }

}
