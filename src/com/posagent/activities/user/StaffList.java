package com.posagent.activities.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.StaffEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.StaffAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 员工列表
 */
public class StaffList extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;

    private RelativeLayout rl_button_toolbar;
    private TextView tv_batchDelete, tv_cancel;


    private boolean onRefresh_number = true;
    private boolean isDeleting = false;
    private StaffAdapter myAdapter;
    List<StaffEntity> myList = new ArrayList<StaffEntity>();

    List<String> deleteIds;

    private Handler handler = new Handler() {
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        initView();
        getData();
    }

    private void initView() {
        new TitleMenuUtil(StaffList.this, "员工帐号").show();
        rl_button_toolbar = (RelativeLayout) findViewById(R.id.rl_button_toolbar);
        if (!isDeleting) {
            rl_button_toolbar.setVisibility(View.GONE);
        }
        // delete icon show
        final ImageView iv_delete = (ImageView) findViewById(R.id.search);
        iv_delete.setVisibility(View.VISIBLE);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeleting = !isDeleting;
                updateListView();
                if (isDeleting) {
                    rl_button_toolbar.setVisibility(View.VISIBLE);
                } else {
                    rl_button_toolbar.setVisibility(View.GONE);
                }
            }
        });

        // add icon show
        final ImageView iv_addIcon = (ImageView) findViewById(R.id.iv_addIcon);


        iv_addIcon.setVisibility(View.VISIBLE);
        iv_addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffList.this, StaffForm.class);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

        tv_batchDelete = (TextView) findViewById(R.id.tv_batchDelete);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_batchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeleting = false;
                rl_button_toolbar.setVisibility(View.GONE);
                cancelBatchDelete();
            }
        });

        myAdapter = new StaffAdapter(StaffList.this, myList);
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

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentsId",  MyApplication.user().getAgentId());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.StaffListEvent event = new Events.StaffListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void sendDelete() {
        JsonParams params = new JsonParams();
        params.put("agentsId",  MyApplication.user().getAgentId());
        params.put("loginId",  MyApplication.user().getId());
        params.put("customerIds", StringUtil.join(deleteIds, ","));

        String strParams = params.toString();
        Events.StaffDeleteEvent event = new Events.StaffDeleteEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    public void doDelete(StaffEntity entity) {
        entity.setSelected(true);
        deleteAll();
    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.StaffListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.StaffDeleteCompleteEvent event) {
        if (event.success()) {
            myAdapter.notifyDataSetChanged();
        }
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.StaffListReloadEvent event) {
        onRefresh();
    }


    // callback
    public void showDetail(StaffEntity entity) {
        Intent i = new Intent(StaffList.this, StaffForm.class);
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        i.putExtra("json", json);
        startActivityForResult(i, Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.REQUEST_CODE:
                Gson gson = new Gson();
                StaffEntity entity = gson.fromJson(bundle.getString("json"), StaffEntity.class);
                for (int i = 0; i < myList.size(); i++) {
                    StaffEntity item = myList.get(i);
                    if (item.getId() == entity.getId()) {
                        myList.remove(item);
                        return;
                    }
                }
                myList.remove(entity);
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    // helper
    private void cancelBatchDelete() {
        isDeleting = false;
        updateListView();
        rl_button_toolbar.setVisibility(View.GONE);
    }

    private void updateListView() {
        if (isDeleting) {
            // update list
            for (StaffEntity entity : myList) {
                entity.setBatchEditing(true);
            }
        } else {
            for (StaffEntity entity : myList) {
                entity.setBatchEditing(false);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaffList.this);
        builder.setMessage("确定删除这些员工吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<String>();

                for (int i = myList.size() - 1; i >= 0; i--) {
                    StaffEntity entity = myList.get(i);
                    if (entity.isSelected()) {
                        entity.setDeleted(true);
                        myList.remove(entity);
                        deleteIds.add("" + entity.getId());
                    } else {
                        entity.setDeleted(false);
                    }

                }
                if (deleteIds.size() > 0) {
                    sendDelete();

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
