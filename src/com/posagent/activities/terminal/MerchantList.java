package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.activity.SearchFormCommon;
import com.example.zf_android.entity.MerchantEntity;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

public class MerchantList extends BaseListActivity {


    private String selectMerchantName;
    private String title = "";
    private int terminalId;
    private List<MerchantEntity> myList;
    private String keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择商户").show();

        selectMerchantName = getIntent().getStringExtra(AGENT_NAME);
        terminalId = getIntent().getIntExtra("terminalId", 0);

        //icons
        findViewById(R.id.iv_search_icon_terminal).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_search_icon_terminal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantList.this, SearchFormCommon.class);
                i.putExtra("save_key", "MerchantListHistory");
                i.putExtra("hint_text", "输入商家名称");
                i.putExtra("keys", keys);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

        getData();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);

        MerchantEntity agent = myList.get(position);

        Intent intent = getIntent();
        intent.putExtra(AGENT_NAME, agent.getTitle());
        intent.putExtra(AGENT_ID, agent.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("terminalId", terminalId);
        if (null != keys) {
            params.put("title", keys);
        }
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.MerchantListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void onRefresh() {
        page = 1;
        myList.clear();
        adapter.notifyDataSetChanged();
        getData();
    }


    // events
    public void onEventMainThread(Events.MerchantListCompleteEvent event) {
        myList = event.getList();
        for (MerchantEntity client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String agentName = client.getTitle();
            item.put("name", agentName);
            item.put("selected", TextUtils.isEmpty(agentName)
                    || !agentName.equals(selectMerchantName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    public void onEventMainThread(Events.GoodsDoSearchCompleteEvent event) {
        keys = event.getKeys();
        getData();
    }

}
