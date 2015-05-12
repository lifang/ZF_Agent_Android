package com.posagent.activities.user;

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

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.User;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.UserAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 用户列表
 */
public class UserList extends BaseActivity implements IXListViewListener {

    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;

    private RelativeLayout rl_button_toolbar;
    private TextView tv_deleteAll, tv_cancelBatchDelete;


    private boolean onRefresh_number = true;
    private boolean isDeleting = false;
    private boolean forSelect = false;
    private UserAdapter myAdapter;
    List<User> myList = new ArrayList<User>();

    List<Integer> deleteIds;

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
        setContentView(R.layout.activity_user_list);

        forSelect = getIntent().getBooleanExtra("forSelect", false);

        initView();
        getData();
    }

    private void initView() {
        if (forSelect) {
            new TitleMenuUtil(UserList.this, "选择用户").show();

        } else {
            new TitleMenuUtil(UserList.this, "用户管理").show();

        }
        rl_button_toolbar = (RelativeLayout) findViewById(R.id.rl_button_toolbar);
        if (!isDeleting) {
            rl_button_toolbar.setVisibility(View.GONE);
        }

        if (forSelect) {
            //选择用户用
            // add icon show
            ImageView addIcon = (ImageView) findViewById(R.id.iv_addIconEdge);
            addIcon.setVisibility(View.VISIBLE);
            addIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(UserList.this, UserForm.class);
                    startActivity(i);

                }
            });
        } else {
            // delete icon show
            ImageView deleteIcon = (ImageView) findViewById(R.id.search);
//            deleteIcon.setVisibility(View.VISIBLE);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
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
        }


        tv_deleteAll = (TextView) findViewById(R.id.tv_deleteAll);
        tv_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });
        tv_cancelBatchDelete = (TextView) findViewById(R.id.tv_cancelBatchDelete);
        tv_cancelBatchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBatchDelete();
            }
        });

        myAdapter = new UserAdapter(UserList.this, myList);
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
        Events.UserListEvent event = new Events.UserListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void sendDelete() {
        JsonParams params = new JsonParams();
        params.put("agentId",  MyApplication.user().getAgentId());
        params.put("customerArrayId", deleteIds);
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.UserDeleteEvent event = new Events.UserDeleteEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    public void doDelete(User entity) {
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
    public void onEventMainThread(Events.UserListCompleteEvent event) {
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

    public void onEventMainThread(Events.UserListReloadEvent event) {
        myAdapter.notifyDataSetChanged();
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
            for (User entity : myList) {
                entity.setBatchEditing(true);
            }
        } else {
            for (User entity : myList) {
                entity.setBatchEditing(false);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this);
        builder.setMessage("确定删除这些用户吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<Integer>();

                for (int i = myList.size() - 1; i >= 0; i--) {
                    User entity = myList.get(i);
                    if (entity.isSelected()) {
                        entity.setDeleted(true);
                        myList.remove(entity);
                        deleteIds.add(entity.getCustomersId());

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

    public void clickAtUser(User entity) {
        if (forSelect) {
            Intent i = getIntent();
            i.putExtra("username", entity.getName());
            i.putExtra("userId", entity.getCustomersId());
            setResult(RESULT_OK, i);
            finish();
        } else {
            Intent i = new Intent(UserList.this, UserDetail.class);
            Gson gson = new Gson();
            String json = gson.toJson(entity);
            i.putExtra("json", json);
            startActivity(i);
        }
    }

    public boolean isForSelect() {
        return forSelect;
    }

}
