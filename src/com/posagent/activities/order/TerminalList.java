package com.posagent.activities.order;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseListActivity;

import java.util.HashMap;
import java.util.Map;

public class TerminalList extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "查看终端").show();

        getData();

    }

    private void getData() {
        String terminals = getIntent().getStringExtra("terminals");
        for (String terminal: terminals.split(" ")) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", terminal);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
