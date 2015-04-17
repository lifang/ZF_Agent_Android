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
    private ChooseAdressAdapter myAdapter;
    List<AdressEntity>  myList = new ArrayList<AdressEntity>();
    List<AdressEntity>  moreList = new ArrayList<AdressEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chance_adress);
        new TitleMenuUtil(ChangeAdress.this, "选择地址").show();

        initView();
        getData();
    }

    private void initView() {
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
                Intent i = new Intent(ChangeAdress.this, AdressList.class);
                startActivity(i);
            }
        });
    }

    private void getData() {

        JsonParams params = new JsonParams();

        //Fixme
        params.put("customerId", 1);

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
}
