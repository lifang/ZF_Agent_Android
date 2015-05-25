package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.trade.ApplyDetailActivity;
import com.example.zf_android.trade.entity.TerminalApply;
import com.example.zf_android.trade.entity.TerminalComment;
import com.example.zf_android.trade.entity.TerminalDetail;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_android.trade.entity.TerminalOpen;
import com.example.zf_android.trade.entity.TerminalOpenInfo;
import com.example.zf_android.trade.entity.TerminalRate;
import com.example.zf_android.video.VideoActivity;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.ImageViewer;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.TerminalIntent.APPLY_STATUS;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_STATUS;

/***
 *
 * 终端详情
 *
 */
public class TerminalDetailActivity extends BaseActivity {


    private Button btn_apply_open, btn_sync;
    private TableLayout tl_rate;
    private LinearLayout ll_comments, ll_opening_info;

    private int terminalId;
    private int status;
    private int applyStatus;

    private TerminalDetail entity;
    private TerminalItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teminal_detail);
        new TitleMenuUtil(TerminalDetailActivity.this, "终端详情").show();
        item = gson.fromJson(getIntent().getStringExtra("json"), TerminalItem.class);
        if (null != item) {
            terminalId = item.getId();
        } else {
            terminalId = getIntent().getIntExtra("id", 0);
        }
        initView();
    }

    private void initView() {
        tl_rate = (TableLayout)findViewById(R.id.tl_rate);
        ll_comments = (LinearLayout)findViewById(R.id.ll_comments);
        ll_opening_info = (LinearLayout)findViewById(R.id.ll_opening_info);

        getData();
    }


    private void getData() {
        JsonParams params = new JsonParams();
        params.put("terminalsId", terminalId);

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
            case R.id.btn_apply_open:
                Intent i = new Intent(context, ApplyDetailActivity.class);
                i.putExtra("id", item.getId());
                i.putExtra(TERMINAL_ID, item.getId());
                i.putExtra(TERMINAL_STATUS, item.getStatus());
                context.startActivity(i);
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

    public void onEventMainThread(Events.FindPosPasswordCompleteEvent event) {
        toast(event.getStrResult());
    }

    public void onEventMainThread(Events.TerminalSyncCompleteEvent event) {
        toast(event.getStrResult());
        getData();
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
            setText("tv_order_number", apply.getOrderNumber());
            setText("tv_channel", apply.getChannelName());
            setText("tv_agent", apply.getTitle());
            setText("tv_create_time", apply.getCreatedAt());
            setText("tv_agent_tel", apply.getPhone());

            applyStatus = apply.getApplieStatus();

            //status
            status = apply.getStatus();
            state = statusName(status);
            setText("tv_status", state);


            hide("btn_1");
            hide("btn_2");
            hide("btn_3");
            hide("btn_4");

            Button btn;
            if (status == 1) {
                show("btn_1");
                show("btn_2");
                btn = (Button)findViewById(R.id.btn_1);
                btn.setText("找回POS密码");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findPosPassword();
                    }
                });

                if (apply.isNeedPreliminaryVerify()) {
                    show("btn_2");
                    btn = (Button)findViewById(R.id.btn_2);
                    btn.setText("视频认证");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            video_check();
                        }
                    });
                }


            } else if (status == 2) {
                show("btn_1");
                show("btn_2");
                show("btn_3");
                show("btn_4");
                btn = (Button)findViewById(R.id.btn_1);
                btn.setText("找回POS密码");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findPosPassword();
                    }
                });

                btn = (Button)findViewById(R.id.btn_2);
                btn.setText("视频认证");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        video_check();
                    }
                });

                btn = (Button)findViewById(R.id.btn_3);
                btn.setText("同步");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        terminalSync();
                    }
                });

                btn = (Button)findViewById(R.id.btn_4);
                btn.setText("重新申请开通");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reopen();
                    }
                });

            } else if (status == 3) {
                show("btn_1");
                show("btn_2");
                btn = (Button)findViewById(R.id.btn_1);
                btn.setText("开通申请");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        open();
                    }
                });

                if (apply.isNeedPreliminaryVerify()) {
                    show("btn_2");
                    btn = (Button)findViewById(R.id.btn_2);
                    btn.setText("视频认证");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            video_check();
                        }
                    });
                }
            }

        }

        if (null != info) {

        }

        if (rates.size() > 0) {
            updateRates(rates);
        }
        if (comments.size() > 0) {
            updateComments(comments);
        }
        if (opens.size() > 0) {
            updateOpeningInfos(opens);
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
            if (null == rate) {
                continue;
            }
            List<String> list = new ArrayList<String>();
            list.add(rate.getType());

            int intRate = rate.getServiceRate();
            if (null != rate.getTerminalRate()) {
                intRate += Integer.parseInt(rate.getTerminalRate());
            }

            list.add(StringUtil.rateShow(intRate) + "‰");
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
            if (null == comment) {
                continue;
            }

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

    private void updateOpeningInfos(List<TerminalOpen> opens) {
        ll_opening_info.removeAllViews();
        int len = opens.size();
        boolean isLast = false;

        // 文本
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                isLast = true;
            }

            TerminalOpen open = opens.get(i);
            if (null == open) {
                continue;
            }
            if (Constants.KeyValue.Picture == open.getTypes()) {
                continue;
            }
            updateOneOpenInfo(open);

        }

        // 图片
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                isLast = true;
            }

            TerminalOpen open = opens.get(i);
            if (null == open) {
                continue;
            }
            if (Constants.KeyValue.Picture != open.getTypes()) {
                continue;
            }
            updateOneOpenInfo(open);

        }
    }

    private void updateOneOpenInfo(final TerminalOpen open) {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10, 10, 10, 10);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(llp);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv_key = new TextView(this);
        LinearLayout.LayoutParams txtLlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_key.setLayoutParams(txtLlp);
        tv_key.setText(open.getKey());
        tv_key.setWidth(240);

        layout.addView(tv_key);

        LinearLayout.LayoutParams valueLlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        valueLlp.setMargins(60, 0, 0, 0);
        if (Constants.KeyValue.Picture == open.getTypes()) {
            ImageView iv_value = new ImageView(this);
            iv_value.setLayoutParams(valueLlp);
            iv_value.setImageResource(R.drawable.upload);
            iv_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TerminalDetailActivity.this, ImageViewer.class);
                    i.putExtra("url", open.getValue());
                    i.putExtra("justviewer", true);
                    startActivity(i);
                }
            });
            layout.addView(iv_value);
        } else {
            TextView tv_value = new TextView(this);
            tv_value.setLayoutParams(valueLlp);
            tv_value.setText(open.getValue());

            layout.addView(tv_value);
        }

        ll_opening_info.addView(layout);
    }

    private String rateStatusName(int status) {
        return  Constants.TerminalConstant.STATUS[status];
    }
    private String statusName(int status) {
        String[] arr = Constants.TerminalConstant.STATUS;
        if (status == 7) {
            return "已停用";
        }
        return  arr[status];
    }

    private void findPosPassword() {
        JsonParams params = new JsonParams();
        params.put("terminalsId", terminalId);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.FindPosPasswordEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void terminalSync() {
        JsonParams params = new JsonParams();
        params.put("terminalsId", terminalId);

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.TerminalSyncEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void open() {
        Intent i = new Intent(context, ApplyDetailActivity.class);
        i.putExtra("id", terminalId);
        i.putExtra(TERMINAL_ID, terminalId);
        i.putExtra(TERMINAL_STATUS, status);
        i.putExtra(APPLY_STATUS, applyStatus);
        context.startActivity(i);
    }

    private void reopen() {
        open();
    }

    private void video_check() {
        Intent i = new Intent(context, VideoActivity.class);
        i.putExtra(com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_ID, terminalId);
        context.startActivity(i);
    }

}
