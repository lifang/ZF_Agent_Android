package com.posagent.activities.goods;

import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.TDateEntity;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.BaseActivity;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class TDateList extends BaseActivity implements OnClickListener {

    private TableLayout tl_tDates;

    private List<TDateEntity> tDates = new ArrayList<TDateEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdate_list);

        try {
            String json = getIntent().getStringExtra("json");
            tDates = gson.fromJson(json,
                    new TypeToken<List<TDateEntity>>() {}.getType());
        } catch (RuntimeException ex) {
            Log.d(TAG, ex.getMessage());
        }

        new TitleMenuUtil(TDateList.this, "交易费率").show();

        initView();


    }

    private void initView() {

        tl_tDates = (TableLayout)findViewById(R.id.tl_tDates);

        if (tDates != null) {
            tl_tDates.removeAllViews();
            int len = tDates.size();
            boolean isLast = false;
            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    isLast = true;
                }

                if (i == 0) {
                    List<String> list = new ArrayList<String>();
                    list.add("结算时间");
                    list.add("资金服务费(/天)");
                    TableRow tr = ViewHelper.tableRow(this, list, R.color.text292929, 12, isLast);
                    tl_tDates.addView(tr,
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                }

                TDateEntity rate = tDates.get(i);
                List<String> list = new ArrayList<String>();
                list.add(rate.getName());
                list.add("" + StringUtil.rateShow(rate.getService_rate()) + "‰");
                TableRow tr = ViewHelper.tableRow(this, list, R.color.tmc, 12, isLast);
                tl_tDates.addView(tr,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
