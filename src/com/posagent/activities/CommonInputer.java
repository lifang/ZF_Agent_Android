package com.posagent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.posagent.utils.Constants;


public class CommonInputer extends BaseActivity implements OnClickListener {

    private String pattern;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_inputer);

        initView();

    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(Constants.CommonInputerConstant.TITLE_KEY);
        String placeholder = bundle.getString(Constants.CommonInputerConstant.PLACEHOLDER_KEY);

        new TitleMenuUtil(CommonInputer.this, title).show();

        EditText et = (EditText) findViewById(R.id.content);
        et.setText(placeholder);
        //cursor
        et.setSelection(et.getText().length());

        View v = findViewById(R.id.click_common_inputer_confirm);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_common_inputer_confirm:
                TextView tv = (TextView) findViewById(R.id.content);
                String value = tv.getText().toString();

                Intent intent = getIntent();
                intent.putExtra(Constants.CommonInputerConstant.VALUE_KEY, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }

        super.onClick(v);

    }

}
