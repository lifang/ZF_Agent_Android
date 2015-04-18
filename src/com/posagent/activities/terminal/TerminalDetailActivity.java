package com.posagent.activities.terminal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.entity.TerminalApply;
import com.example.zf_android.trade.entity.TerminalComment;
import com.example.zf_android.trade.entity.TerminalDetail;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_android.trade.entity.TerminalOpen;
import com.example.zf_android.trade.entity.TerminalOpenInfo;
import com.example.zf_android.trade.entity.TerminalRate;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 *
 * 终端详情
 *
 */
public class TerminalDetailActivity extends BaseActivity {

    private Button btn_apply_open, btn_sync;
    private TableLayout tl_rate;
    private LinearLayout ll_comments;

    private int terminalId;

    private TerminalDetail entity;
    private TerminalItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teminal_detail);
        new TitleMenuUtil(TerminalDetailActivity.this, "终端详情").show();
        item = gson.fromJson(getIntent().getStringExtra("json"), TerminalItem.class);
        terminalId = item.getId();
        initView();
    }

    private void initView() {
        tl_rate = (TableLayout)findViewById(R.id.tl_rate);
        ll_comments = (LinearLayout)findViewById(R.id.ll_comments);

        btn_apply_open = (Button)findViewById(R.id.btn_apply_open);
        btn_apply_open.setOnClickListener(this);
        btn_sync = (Button)findViewById(R.id.btn_sync);
        btn_sync.setOnClickListener(this);

        getData();
    }


    private void getData() {
        JsonParams params = new JsonParams();
        //Fixme
        params.put("terminalsId", 1);

        String strParams = params.toString();
        Events.TerminalDetailEvent event = new Events.TerminalDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.btn_after_sale_apply:

                break;
            default:
                break;
        }

        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.TerminalDetailCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    private void updateView() {
        String state = "未知";
        TerminalApply apply = entity.getApplyDetails();
        TerminalOpenInfo info = entity.getOpeningInfos();
        List<TerminalOpen> opens = entity.getOpeningDetails();
        List<TerminalRate> rates = entity.getRates();
        List<TerminalComment> comments = entity.getTrackRecord();

        if (null != apply) {

            //applyDetails 账户信息
            setText("tv_terminal_number", apply.getTerminalNum());
            setText("tv_brand", apply.getBrandName());
            setText("tv_model", apply.getModelNumber());
            setText("tv_terminal_number", apply.getTerminalNum());
            setText("tv_terminal_number", apply.getTerminalNum());
            setText("tv_terminal_number", apply.getTerminalNum());
            setText("tv_order_number", apply.getOrderNumber());

        }

        if (null != info) {
            //status
            int status = info.getStatus();
            state = Constants.TerminalConstant.STATUS[status];
            setText("tv_status", state);

            //
            setText("tv_channel", info.getChannelname());
            setText("tv_agent", info.getName());
            setText("tv_agent2", info.getName());
            setText("tv_name2", info.getName());
            setText("tv_agent_tel", info.getPhone());
            setText("tv_agent_tel2", info.getPhone());
            setText("tv_create_time", info.getCreated_at());
            setText("tv_create_time", info.getCreated_at());


        }

        if (rates.size() > 0) {
            updateRates(rates);
        }
        if (comments.size() > 0) {
            updateComments(comments);
        }




    }


    private void updateRates(List<TerminalRate> rates) {
        tl_rate.removeAllViews();
        int len = rates.size();
        boolean isLast = false;
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                isLast = true;
            }

            if (i == 0) {
                List<String> list = new ArrayList<String>();
                list.add("交易类型");
                list.add("费率");
                list.add("开通状态");
                TableRow tr = ViewHelper.tableRow(this, list, R.color.text292929, 12, isLast);
                tl_rate.addView(tr,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }

            TerminalRate rate = rates.get(i);
            List<String> list = new ArrayList<String>();
            list.add(rate.getType());
            list.add(rate.getTerminalRate());
            list.add(rateStatusName(rate.getStatus()));
            TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
            tl_rate.addView(tr,
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }


    private void updateComments(List<TerminalComment> comments) {
        ll_comments.removeAllViews();
        int len = comments.size();
        boolean isLast = false;
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                isLast = true;
            }

            TerminalComment comment = comments.get(i);

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(10, 10, 10, 10);
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(llp);
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView tv_content = new TextView(this);
            LinearLayout.LayoutParams txtLlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv_content.setLayoutParams(txtLlp);
            tv_content.setText(comment.getContent());

            layout.addView(tv_content);


            TextView tv_user = new TextView(this);
            tv_user.setLayoutParams(txtLlp);
            tv_user.setTextSize(12);
            tv_user.setText(comment.getName().trim() + "   " + comment.getCreateAt());

            layout.addView(tv_user);

            View line = new View(this);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            line.setLayoutParams(lineParams);
            line.setBackgroundColor(getResources().getColor(R.color.Viewc2));

            layout.addView(line);

            ll_comments.addView(layout);


        }
    }

    private String rateStatusName(int status) {
        return  Constants.TerminalConstant.STATUS[status];
    }

}
