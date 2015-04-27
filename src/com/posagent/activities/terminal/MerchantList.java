package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.MerchantEntity;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择商户").show();

        selectMerchantName = getIntent().getStringExtra(AGENT_NAME);
        terminalId = getIntent().getIntExtra("terminalId", 0);

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
        params.put("title", title);
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.MerchantListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
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

}
