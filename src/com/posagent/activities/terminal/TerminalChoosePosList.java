package com.posagent.activities.terminal;

import android.os.Bundle;
import android.text.TextUtils;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.TerminalChoosePosItem;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class TerminalChoosePosList extends BaseListActivity {


    private List<TerminalChoosePosItem> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, "选择POS机").show();

        getData();

    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("customerId", 80);
//        params.put("page", page);
//        params.put("rows", rows);
        String strParams = params.toString();
        Events.TerminalChoosePosListEvent event = new Events.TerminalChoosePosListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.TerminalChoosePosListCompleteEvent event) {
        myList = event.getList();
        for (TerminalChoosePosItem client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String _selectedName = client.getTitle();
            item.put("name", _selectedName);
            item.put("selected", TextUtils.isEmpty(_selectedName)
                    || !_selectedName.equals(selectedName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
