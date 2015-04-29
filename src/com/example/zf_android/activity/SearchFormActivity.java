package com.example.zf_android.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zf_android.R;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/***
 *
 * 搜索表单
 *
 */
public class SearchFormActivity extends BaseListActivity {

    private static String TAG = "SearchFormActivity";

    private EditText search_edit;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_form);

        sharedPreferences = getSharedPreferences("SearchKeyHistroy", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initList();

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });


        final EditText search_edit = (EditText)findViewById(R.id.serch_edit);

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keys = search_edit.getText().toString();
                    searchKey(keys);
                    return false;
                }
                return false;
            }
        });


    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Map<String, Object> item = items.get(position);

        String key = (String) item.get("name");
        searchKey(key);
    }

    private void searchKey(String keys) {
        finish();

        Events.GoodsDoSearchCompleteEvent event1 = new Events.GoodsDoSearchCompleteEvent();
        event1.setKeys(keys);
        saveKey(keys);
        EventBus.getDefault().post(event1);
    }


    private void saveKey(String key) {
        Set<String> keySet = keySet();
        keySet.add(key);
        editor.putStringSet("keys", keySet);
        editor.commit();
    }

    private Set<String> keySet() {
        return sharedPreferences.getStringSet("keys", new HashSet<String>());
    }

    private void initList() {
        Set<String> keySet = keySet();
        items.clear();
        for (String name : keySet) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", name);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void clearHistory() {
        Set<String> keySet = new HashSet<String>();
        editor.putStringSet("keys", keySet);
        editor.commit();

        initList();
    }


}
