package com.posagent.activities.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.User;
import com.example.zf_android.entity.UserTerminal;
import com.example.zf_zandroid.adapter.UserTerminalAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 用户详情
 */
public class UserDetail extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;


    private boolean onRefresh_number = true;
    private boolean isDeleting = false;
    private UserTerminalAdapter myAdapter;
    List<UserTerminal> myList = new ArrayList<UserTerminal>();

    List<Integer> deleteIds;

    private User entity;

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

        Gson gson = new Gson();
        entity = gson.fromJson(getIntent().getStringExtra("json"), User.class);

        setContentView(R.layout.activity_user_terminal_list);
        initView();
        getData();
    }

    private void initView() {
        new TitleMenuUtil(UserDetail.this, "用户详情").show();

        // delete icon show
        ImageView deleteIcon = (ImageView) findViewById(R.id.search);
        deleteIcon.setVisibility(View.VISIBLE);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

        myAdapter = new UserTerminalAdapter(UserDetail.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
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
        params.put("customerId",  MyApplication.user().getId());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.UserTerminalEvent event = new Events.UserTerminalEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void sendDelete() {
        JsonParams params = new JsonParams();
        params.put("agentId",  MyApplication.user().getAgentId());
        params.put("customerArrayId", deleteIds);
        params.put("page", 1);
        params.put("rows", 10);
        String strParams = params.toString();
        Events.UserDeleteEvent event = new Events.UserDeleteEvent();
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
    public void onEventMainThread(Events.UserTerminalCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.UserDeleteCompleteEvent event) {
        if (event.success()) {
            myAdapter.notifyDataSetChanged();
        } else {
            toast(event.getMessage());
        }
    }

    // helper

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetail.this);
        builder.setMessage("确定删除这个用户吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<Integer>();

                deleteIds.add(entity.getCustomersId());
                if (deleteIds.size() > 0) {
                    sendDelete();

                    Toast.makeText(UserDetail.this, "正在删除...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

}
