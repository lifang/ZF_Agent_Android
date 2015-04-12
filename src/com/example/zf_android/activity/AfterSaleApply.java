package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;
import com.posagent.activities.terminal.TerminalChooseForm;

/***
 *
 *
 * ����ƣ�OrderDetail
 * ��������   ��������
 * �����ˣ� ljp
 * ����ʱ�䣺2015-2-5 ����3:06:02
 * @version
 *
 */
public class AfterSaleApply extends BaseActivity implements View.OnClickListener {

    private LinearLayout click_choose_terminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_apply);
        new TitleMenuUtil(AfterSaleApply.this, "售后申请").show();

        click_choose_terminal = (LinearLayout) findViewById(R.id.click_choose_terminal);
        click_choose_terminal.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_choose_terminal:
                Intent i = new Intent(getApplicationContext(), TerminalChooseForm.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
