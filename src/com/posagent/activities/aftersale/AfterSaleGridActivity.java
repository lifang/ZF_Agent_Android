package com.posagent.activities.aftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;

import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_TYPE;
import static com.example.zf_android.trade.Constants.AfterSaleType.CANCEL;
import static com.example.zf_android.trade.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_android.trade.Constants.AfterSaleType.UPDATE;

/**
 * Created by Leo on 2015/2/26.
 */
public class AfterSaleGridActivity extends Activity implements View.OnClickListener {

    private TextView mMaintain;
    private TextView mCancel;
    private TextView mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_grid);
        new TitleMenuUtil(this, getString(R.string.title_after_sale)).show();
        initViews();
    }

    private void initViews() {
        mMaintain = (TextView) findViewById(R.id.after_sale_maintain);
        mCancel = (TextView) findViewById(R.id.after_sale_cancel);
        mUpdate = (TextView) findViewById(R.id.after_sale_update);
        mMaintain.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int recordType = 0;
        switch (v.getId()) {
            case R.id.after_sale_maintain:
                recordType = MAINTAIN;
                break;
            case R.id.after_sale_cancel:
                recordType = CANCEL;
                break;
            case R.id.after_sale_update:
                recordType = UPDATE;
                break;
        }
        Intent intent = new Intent(this, AfterSaleListActivity.class);
        intent.putExtra(RECORD_TYPE, recordType);
        startActivity(intent);
    }
}
