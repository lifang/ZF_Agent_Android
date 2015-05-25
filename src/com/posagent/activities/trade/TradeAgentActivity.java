package com.posagent.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.trade.entity.TradeAgent;
import com.posagent.MyApplication;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

public class TradeAgentActivity extends BaseListActivity {


    private String selectAgentName;
    private List<TradeAgent> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择代理商").show();

        selectAgentName = getIntent().getStringExtra(AGENT_NAME);

        getData();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);

        TradeAgent agent = myList.get(position);

        Intent intent = getIntent();
        intent.putExtra(AGENT_NAME, agent.getAgentName());
        intent.putExtra(AGENT_ID, agent.getAgentId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", MyApplication.user().getAgentId());
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.TradeAgentEvent event = new Events.TradeAgentEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.TradeAgentCompleteEvent event) {
        myList = event.getList();
        for (TradeAgent client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String agentName = client.getAgentName();
            item.put("name", agentName);
            item.put("selected", TextUtils.isEmpty(agentName)
                    || !agentName.equals(selectAgentName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
