package com.posagent.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.examlpe.zf_android.util.ScrollViewWithListView;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.AdressEntity;
import com.example.zf_zandroid.adapter.ChooseAdressAdapter;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 *
 * 类名称：ChanceAdress
 * 类描述：   选择地址
 * 创建人： ljp
 * 创建时间：2015-3-9 下午3:13:17
 * @version
 *
 */
public class ChangeAdress extends BaseActivity {
    private ScrollViewWithListView   lv;
    private String Url=Config.ChooseAdress;
    private int customerId;
    private int addressId;
    private ChooseAdressAdapter myAdapter;
    List<AdressEntity>  myList = new ArrayList<AdressEntity>();
    List<AdressEntity>  moreList = new ArrayList<AdressEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chance_adress);
        new TitleMenuUtil(ChangeAdress.this, "选择地址").show();
        addressId = getIntent().getIntExtra("addressId", 0);
        initView();
        getData();
    }

    private void initView() {
        hide("adresslist");

        show("next_sure");
        setText("next_sure", "管理地址");
        findViewById(R.id.next_sure).setOnClickListener(this);

        lv = (ScrollViewWithListView) findViewById(R.id.lv);
        myAdapter = new ChooseAdressAdapter(ChangeAdress.this, myList);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdressEntity entity = myList.get(position);
                Intent i = getIntent();
                i.putExtra("id", entity.getId());
                setResult(RESULT_OK, i);
                finish();
            }
        });
        findViewById(R.id.adresslist).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(ChangeAdress.this, AddressList.class);
                startActivity(i);
            }
        });
    }

    private void getData() {

        JsonParams params = new JsonParams();

        params.put("customerId",  MyApplication.user().getId());

        String strParams = params.toString();
        Events.AddressListEvent event = new Events.AddressListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.next_sure:
                Intent i = new Intent(ChangeAdress.this, AddressList.class);
                startActivity(i);
                break;
            default:

        }

        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.AddressListCompleteEvent event) {
        if (event.getSuccess()) {
            moreList = event.getList();
            myList.clear();
            myList.addAll(moreList);

            myAdapter.notifyDataSetChanged();
        }
    }

    public int addressId() {
        return addressId;
    }
}
