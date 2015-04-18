package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.ChanelEntitiy;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class TerminalChooseChannelList extends BaseListActivity {


    private List<ChanelEntitiy> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择支付通道").show();

        getData();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);

        ChanelEntitiy item = myList.get(position);

        Intent intent = getIntent();
        intent.putExtra(Constants.DefaultSelectedNameKey, item.getName());
        intent.putExtra(Constants.DefaultSelectedIdKey, item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("customerId", 80);
//        params.put("page", page);
//        params.put("rows", rows);
        String strParams = params.toString();
        Events.TerminalChooseChannelListEvent event = new Events.TerminalChooseChannelListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.TerminalChooseChannelListCompleteEvent event) {
        myList = event.getList();
        for (ChanelEntitiy client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String _selectedName = client.getName();
            item.put("name", _selectedName);
            item.put("selected", TextUtils.isEmpty(_selectedName)
                    || !_selectedName.equals(selectedName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
