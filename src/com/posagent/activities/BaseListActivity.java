package com.posagent.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.zf_android.Config;
import com.example.zf_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class BaseListActivity extends ListActivity {

    protected int page = 1;
    protected int rows = Config.ROWS;

    protected List<Map<String, Object>> items;
    protected SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }

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



}
