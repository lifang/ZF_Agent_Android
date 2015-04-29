package com.posagent.activities.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.MessageEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.NewMessageAdapter;
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
 * 消息列表
 */
public class MessageList extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;

    private RelativeLayout rl_button_toolbar;
    private TextView tv_makeReadAll, tv_batchDelete;


    private boolean onRefresh_number = true;
    private boolean isDeleting = false;
    private NewMessageAdapter myAdapter;
    List<MessageEntity> myList = new ArrayList<MessageEntity>();

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
        setContentView(R.layout.activity_message_list);

        setupCommonViews();

        focusTabAtIndex(2);

        initView();
        getData();
    }

    private void initView() {
        new TitleMenuUtil(MessageList.this, "我的消息").show();
        rl_button_toolbar = (RelativeLayout) findViewById(R.id.rl_button_toolbar);
        if (!isDeleting) {
            rl_button_toolbar.setVisibility(View.GONE);
        }
        // delete icon show
        final TextView editIcon = (TextView) findViewById(R.id.tv_title_back_edit);
        editIcon.setVisibility(View.VISIBLE);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeleting = !isDeleting;
                updateListView();
                if (isDeleting) {
                    editIcon.setText("取消");
                    rl_button_toolbar.setVisibility(View.VISIBLE);
                } else {
                    editIcon.setText("编辑");
                    rl_button_toolbar.setVisibility(View.GONE);
                }
            }
        });

        tv_makeReadAll = (TextView) findViewById(R.id.tv_makeReadAll);
        tv_makeReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReadAll();
            }
        });
        tv_batchDelete = (TextView) findViewById(R.id.tv_batchDelete);
        tv_batchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

        myAdapter = new NewMessageAdapter(MessageList.this, myList);
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
        Events.MessageListEvent event = new Events.MessageListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void sendDelete() {
        JsonParams params = new JsonParams();
        params.put("customerId",  MyApplication.user().getId());
        params.put("ids", deleteIds);
        params.put("page", 1);
        params.put("rows", 10);
        String strParams = params.toString();
        Events.MessageDeleteEvent event = new Events.MessageDeleteEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    public void doDelete(MessageEntity entity) {
        entity.setSelected(true);
        deleteAll();
    }


    private void sendMakeReadAll() {
        JsonParams params = new JsonParams();
        params.put("customerId",  MyApplication.user().getId());
        params.put("ids", deleteIds);
        params.put("page", 1);
        params.put("rows", 10);
        String strParams = params.toString();
        Events.MessageMarkReadEvent event = new Events.MessageMarkReadEvent();
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
    public void onEventMainThread(Events.MessageListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.MessageDeleteCompleteEvent event) {
        if (event.success()) {
            myAdapter.notifyDataSetChanged();
        }
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.MessageMarkReadCompleteEvent event) {
        if (event.success()) {
            cancelBatchDelete();
            myAdapter.notifyDataSetChanged();
        }
        toast(event.getMessage());
    }



    // callback
    public void showMessageDetail(MessageEntity entity) {
        Intent i = new Intent(MessageList.this, MessageDetail.class);
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
                MessageEntity entity = gson.fromJson(bundle.getString("json"), MessageEntity.class);
                for (int i = 0; i < myList.size(); i++) {
                    MessageEntity item = myList.get(i);
                    if (item.getId().equals(entity.getId())) {
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
            for (MessageEntity entity : myList) {
                entity.setBatchEditing(true);
            }
        } else {
            for (MessageEntity entity : myList) {
                entity.setBatchEditing(false);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageList.this);
        builder.setMessage("确定删除这些消息吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<String>();

                for (int i = myList.size() - 1; i >= 0; i--) {
                    MessageEntity entity = myList.get(i);
                    if (entity.isSelected()) {
                        entity.setDeleted(true);
                        myList.remove(entity);
                        deleteIds.add(entity.getId());
                    } else {
                        entity.setDeleted(false);
                    }

                }
                if (deleteIds.size() > 0) {
                    sendDelete();

                    Toast.makeText(MessageList.this, "正在删除...", Toast.LENGTH_SHORT).show();
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

    private void makeReadAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageList.this);
        builder.setMessage("确定标记这些消息吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<String>();

                for (int i = myList.size() - 1; i >= 0; i--) {
                    MessageEntity entity = myList.get(i);
                    if (entity.isSelected()) {
                        entity.setStatus(true);
                        deleteIds.add(entity.getId());
                    }

                }
                if (deleteIds.size() > 0) {
                    sendMakeReadAll();

                    Toast.makeText(MessageList.this, "正在标记...", Toast.LENGTH_SHORT).show();
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
