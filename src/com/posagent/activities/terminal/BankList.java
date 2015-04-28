package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.BankEntity;
import com.example.zf_android.trade.common.CommonUtil;
import com.posagent.activities.BaseListActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

public class BankList extends BaseListActivity {


    private String selectMerchantName;
    private String title = "";
    private int terminalId;
    private List<BankEntity> myList = new ArrayList<BankEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_search_list);
        new TitleMenuUtil(this, "选择银行").show();

        selectMerchantName = getIntent().getStringExtra(AGENT_NAME);
        terminalId = getIntent().getIntExtra("terminalId", 0);

        findViewById(R.id.iv_search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        BankEntity agent = myList.get(position);

        Intent intent = getIntent();
        intent.putExtra("bank_name", agent.getName());
        intent.putExtra("bank_no", agent.getNo());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getData() {
        //clear first
        items.clear();
        adapter.notifyDataSetChanged();

        CommonUtil.toastShort(this, "加载中...");

        JsonParams params = new JsonParams();
        //Fixme
        params.put("terminalId", 6);
//        params.put("terminalId", terminalId);

        String keyword = ((EditText)findViewById(R.id.et_keyword)).getText().toString();
        params.put("keyword", keyword);
        params.put("page", page);
        params.put("pageSize", 100);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.BankListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    // events
    public void onEventMainThread(Events.BankListCompleteEvent event) {
        myList = event.getList();
        for (BankEntity client : myList) {
            Map<String, Object> item = new HashMap<String, Object>();
            String agentName = client.getName();
            item.put("name", agentName);
            item.put("selected", TextUtils.isEmpty(agentName)
                    || !agentName.equals(selectMerchantName) ? null : R.drawable.checkbox);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

}
