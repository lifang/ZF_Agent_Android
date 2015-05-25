package com.posagent.activities.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.MessageEntity;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 消息详情
 */
public class MessageDetail extends BaseActivity {
    private TextView msg_title, msg_content, msg_time;

    private List<String> deleteIds;

    private MessageEntity entity;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getStringExtra("msgId");
        if (null == id) {
            Gson gson = new Gson();
            entity = gson.fromJson(getIntent().getStringExtra("json"), MessageEntity.class);
        } else {
            getData();
        }

        setContentView(R.layout.activity_message_detail);
        initView();
    }

    private void initView() {
        new TitleMenuUtil(MessageDetail.this, "消息详情").show();

        // delete icon show
        ImageView deleteIcon = (ImageView) findViewById(R.id.search);
        deleteIcon.setVisibility(View.VISIBLE);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

        if (null != entity) {
            updateContent();
        }

    }

    private void updateContent() {
        msg_content = (TextView) findViewById(R.id.msg_content);
        msg_content.setText(entity.getContent());

        msg_title = (TextView) findViewById(R.id.msg_title);
        msg_title.setText(entity.getTitle());

        msg_time = (TextView) findViewById(R.id.msg_time);
        msg_time.setText(entity.getCreate_at());
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("customerId",  MyApplication.user().getId());
        params.put("id", id);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.MessageDetailEvent();
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

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.MessageDetailCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            if (null != entity) {
                updateContent();
            }
        }
    }

    public void onEventMainThread(Events.MessageDeleteCompleteEvent event) {
        toast(event.getMessage());
        if (event.success()) {
            Intent i = getIntent();
            Gson gson = new Gson();
            i.putExtra("json", gson.toJson(entity));
            setResult(RESULT_OK, i);
            finish();
        }
    }

    // helper

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetail.this);
        builder.setMessage("确定删除吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteIds = new ArrayList<String>();

                deleteIds.add(entity.getId());
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
