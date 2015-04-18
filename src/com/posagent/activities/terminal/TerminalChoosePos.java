package com.posagent.activities.terminal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import de.greenrobot.event.EventBus;

/***
 * Pos筛选
 *
 */
public class TerminalChoosePos extends BaseActivity {

    private Button btn_submit;
    private EditText et_max_price, et_min_price;
    private LinearLayout ll_choose_pos, ll_choose_channel;
    private TextView tv_pos, tv_channel;


    private String posName, channelName;
    private int channelId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_pos);
        new TitleMenuUtil(TerminalChoosePos.this, "筛选").show();

        initView();

    }

    private void initView() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


        et_max_price = (EditText) findViewById(R.id.et_max_price);
        et_min_price = (EditText) findViewById(R.id.et_min_price);

        ll_choose_pos = (LinearLayout) findViewById(R.id.ll_choose_pos);
        ll_choose_channel = (LinearLayout) findViewById(R.id.ll_choose_channel);
        ll_choose_pos.setOnClickListener(this);
        ll_choose_channel.setOnClickListener(this);

        tv_pos = (TextView) findViewById(R.id.tv_pos);
        tv_channel = (TextView) findViewById(R.id.tv_channel);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.ll_choose_pos:
                Intent i = new Intent(TerminalChoosePos.this, TerminalChoosePosList.class);
                i.putExtra(Constants.DefaultSelectedNameKey, posName);
                startActivityForResult(i, Constants.REQUEST_CODE);
                break;
            case R.id.ll_choose_channel:
                Intent i2 = new Intent(TerminalChoosePos.this, TerminalChooseChannelList.class);
                i2.putExtra(Constants.DefaultSelectedNameKey, channelName);
                startActivityForResult(i2, Constants.REQUEST_CODE2);
                break;
        }

        super.onClick(v);
    }

    private void doSubmit() {
        JsonParams params = new JsonParams();

//        ": "泰山Pos旗舰版1",
//        "": 222,
//                "": 12435,
//                "": 1,
//                "":1
        // Fixme
        params.put("agentId", 1);
        params.put("title", posName);
        params.put("channelsId", channelId);
        params.put("minPrice", Integer.parseInt(et_min_price.getText().toString()));
        params.put("maxPrice", Integer.parseInt(et_max_price.getText().toString()));
        String strParams = params.toString();
        Events.BatchTerminalNumberPosEvent event = new Events.BatchTerminalNumberPosEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case Constants.REQUEST_CODE:
                posName = data.getStringExtra(Constants.DefaultSelectedNameKey);
                tv_pos.setText(posName);
                break;
            case Constants.REQUEST_CODE2:
                channelName = data.getStringExtra(Constants.DefaultSelectedNameKey);
                channelId = data.getIntExtra(Constants.DefaultSelectedIdKey, 0);
                tv_channel.setText(channelName);
                break;
        }


    }

    // events
    public void onEventMainThread(Events.BatchTerminalNumberPosCompleteEvent event) {
        Intent i = new Intent(TerminalChoosePos.this, TerminalChooseList.class);
        i.putExtra("json", gson.toJson(event.getList()));
        startActivity(i);
    }

    public void onEventMainThread(Events.TerminalChooseFinishEvent event) {
        finish();
    }
}
