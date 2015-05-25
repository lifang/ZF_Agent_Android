package com.posagent.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.posagent.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

public class BaseListActivity extends ListActivity {

    protected int page = 1;
    protected int rows = Config.ROWS;

    protected List<Map<String, Object>> items;
    protected SimpleAdapter adapter;

    protected String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }
        
        selectedName = getIntent().getStringExtra(Constants.DefaultSelectedNameKey);

        items = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(
                this, items,
                R.layout.simple_list_item,
                new String[]{"name", "selected"},
                new int[]{R.id.item_name, R.id.item_selected});
        setListAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        //getRequests().cancelAll(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.toString());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.toString());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Map<String, Object> item = items.get(position);

        Intent intent = getIntent();
        intent.putExtra(Constants.DefaultSelectedNameKey, (String) item.get("name"));
        setResult(RESULT_OK, intent);
        finish();
    }



}
