package com.posagent.activities.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.examlpe.zf_android.util.ScrollViewWithListView;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.AdressEntity;
import com.example.zf_zandroid.adapter.AddressAdapter;
import com.google.gson.Gson;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class AddressList extends BaseActivity  {

    private AddressAdapter myAdapter;
    private ListView lv;
    private ImageView img_add, iv_delete;
    List<AdressEntity>  myList = new ArrayList<AdressEntity>();
    List<AdressEntity>  moreList = new ArrayList<AdressEntity>();

    private boolean deleting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_list);
        initView();
        getData();
    }

    private void initView() {
        iv_delete=(ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleting = !deleting;
                lv.invalidateViews();
            }
        });

        img_add=(ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i =new Intent(AddressList.this, AdressEdit.class);
                startActivity(i);

            }
        });

        new TitleMenuUtil(AddressList.this, "地址管理").show();

        lv = (ScrollViewWithListView) findViewById(R.id.lv);
        myAdapter = new AddressAdapter(AddressList.this, myList);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AdressEntity entity = myList.get(position);
//                Intent i = getIntent();
//                i.putExtra("id", entity.getId());
//                setResult(RESULT_OK, i);
//                finish();
            }
        });
    }

    public boolean isDeleting() {
        return deleting;
    }

    public void doDelete(final AdressEntity entity) {
        Log.d("TAG", entity.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
        builder.setMessage("确定删除地址[ " + entity.getAddress() + " ]吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JsonParams params = new JsonParams();

                params.put("id", entity.getId());

                String strParams = params.toString();
                Events.DeleteAddressEvent event = new Events.DeleteAddressEvent();
                event.setParams(strParams);
                EventBus.getDefault().post(event);

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void getData() {

        JsonParams params = new JsonParams();

        params.put("customerId", MyApplication.user().getId());

        String strParams = params.toString();
        Events.AddressListEvent event = new Events.AddressListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }


    // events
    public void onEventMainThread(Events.AddressListCompleteEvent event) {
        if (event.getSuccess()) {
            moreList = event.getList();
            myList.addAll(moreList);
            myAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(Events.AddressListReloadEvent event) {
        myList.clear();
        getData();
    }

    public void onEventMainThread(Events.DeleteAddressCompleteEvent event) {
        myList.clear();
        getData();
    }

    // callback
    public void showDetail(AdressEntity entity) {
        Intent i = new Intent(context, AdressEdit.class);
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        i.putExtra("json", json);
        startActivityForResult(i, Constants.REQUEST_CODE);
    }



}
