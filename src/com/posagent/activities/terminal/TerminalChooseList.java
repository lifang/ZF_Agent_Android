package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.epalmpay.agentPhone.R;
import com.example.zf_android.activity.SearchFormCommon;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_zandroid.adapter.TerminalChooseListAdapter;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 * 选择终端
 *
 */
public class TerminalChooseList extends BaseActivity {


    private ListView lv_terminal_list;
    private TextView tv_filtered_number, tv_selected_tips, tv_submit;
    private CheckBox cb_checkall;

    private List<TerminalItem> list;
    private List<TerminalItem> originList;
    private TerminalChooseListAdapter adapter;

    private String keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_list);
        Intent i = getIntent();

        list = gson.fromJson(i.getStringExtra("json"), new TypeToken<List<TerminalItem>>() {}.getType());

        originList = new ArrayList<TerminalItem>(list);

        initView();

    }

    private void initView() {

        findViewById(R.id.titleback_linear_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.serch_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        tv_filtered_number = (TextView) findViewById(R.id.tv_filtered_number);
        tv_filtered_number.setText("" + list.size());

        tv_selected_tips = (TextView) findViewById(R.id.tv_selected_tips);

        tv_submit = (TextView) findViewById(R.id.tv_submit);
        cb_checkall = (CheckBox) findViewById(R.id.cb_checkall);

        tv_submit.setOnClickListener(this);
        cb_checkall.setOnClickListener(this);


        lv_terminal_list = (ListView) findViewById(R.id.lv_terminal_list);

        adapter = new TerminalChooseListAdapter(TerminalChooseList.this, list);
        lv_terminal_list.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                doSubmit();
                break;
            case R.id.cb_checkall:
                toggleCheckAll();
                break;
        }

        super.onClick(v);
    }

    public void toggleCheckedAtPosition(int position) {
        TerminalItem item = list.get(position);
        item.setSelected(!item.isSelected());
        updateSelectStatus();
    }

    public void updateSelectStatus() {
        int selectedCount = 0;
        for (TerminalItem item : list) {
            if (item.isSelected()) {
                selectedCount++;
            }
        }
        tv_selected_tips.setText("已选中" + selectedCount + "台");
    }

    private void doSubmit() {
        List<String> selectedList = new ArrayList<String>();
        for (TerminalItem item : list) {
            if (item.isSelected()) {
                selectedList.add(item.getTerminalNumber());
            }
        }
        finish();
        EventBus.getDefault().post(new Events.TerminalChooseFinishEvent(selectedList));
    }

    private void toggleCheckAll() {
        for (TerminalItem item : list) {
            item.setSelected(cb_checkall.isChecked());
        }
        adapter.notifyDataSetChanged();
        updateSelectStatus();
    }

    public void onEventMainThread(Events.GoodsDoSearchCompleteEvent event) {
        keys = event.getKeys();
        list = new ArrayList<TerminalItem>(originList);
        for (int i = list.size() - 1; i >= 0; i--) {
            TerminalItem entity = list.get(i);
            if (entity.getTerminalNumber().indexOf(keys) == -1) {
                list.remove(entity);
            }
        }
        adapter = new TerminalChooseListAdapter(TerminalChooseList.this, list);
        lv_terminal_list.setAdapter(adapter);

        updateView();
    }

    private void doSearch() {
        Intent i3 = new Intent(context, SearchFormCommon.class);
        i3.putExtra("keys", keys);
        i3.putExtra("save_key", "TerminalKeyHistory");
        i3.putExtra("hint_text", "输入终端号");
        startActivityForResult(i3, Constants.REQUEST_CODE);
    }

    private void updateView() {
        tv_filtered_number.setText("" + list.size());
        updateSelectStatus();
    }
}
