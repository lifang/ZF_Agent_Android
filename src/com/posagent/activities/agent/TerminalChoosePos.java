package com.posagent.activities.agent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.TerminalChoosePosItem;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class TerminalChoosePos extends BaseListActivity {


    private List<TerminalChoosePosItem> myList;

    private int agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择POS机").show();
        agentId = getIntent().getIntExtra("agentId", 0);

        getData();

    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("agentId", agentId);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.TerminalChoosePosAgentListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.TerminalChoosePosAgentListCompleteEvent event) {
        myList = event.getList();
        for (TerminalChoosePosItem client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String _selectedName = client.getGoodname();
            item.put("name", _selectedName);
            item.put("selected", TextUtils.isEmpty(_selectedName)
                    || !_selectedName.equals(selectedName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        TerminalChoosePosItem item = myList.get(position);

        Intent intent = getIntent();

        String name = item.getGoodname();

        intent.putExtra(Constants.DefaultSelectedNameKey, name);
        intent.putExtra(Constants.DefaultSelectedIdKey, item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

}
