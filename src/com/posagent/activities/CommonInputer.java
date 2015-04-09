package com.posagent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.zf_android.R;


public class CommonInputer extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_inputer);
        initView();

    }

    private void initView() {
        View v = findViewById(R.id.click_common_inputer_confirm);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_common_inputer_confirm:
                Intent intent = getIntent();
                TextView tv = (TextView) findViewById(R.id.content);
                intent.putExtra("value", tv.getText());
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }

        super.onClick(v);

    }

}
