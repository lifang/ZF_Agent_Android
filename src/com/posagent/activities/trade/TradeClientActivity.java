package com.posagent.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TradeClient;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.CLIENT_NUMBER;

public class TradeClientActivity extends BaseListActivity {


    private String selectedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        new TitleMenuUtil(this, getString(R.string.title_trade_client)).show();

        selectedNumber = getIntent().getStringExtra(CLIENT_NUMBER);

        getData();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView tv = (TextView) v.findViewById(R.id.item_name);
        Intent intent = new Intent();
        intent.putExtra(CLIENT_NUMBER, tv.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("agentId", 1);
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.TradeClientEvent event = new Events.TradeClientEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.TradeClientCompleteEvent event) {

        for (TradeClient client : event.getList()) {
            Map<String, Object> item = new HashMap<String, Object>();
            String clientNumber = client.getSerialNum();
            item.put("name", clientNumber);
            item.put("selected", TextUtils.isEmpty(clientNumber)
                    || !clientNumber.equals(selectedNumber) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
