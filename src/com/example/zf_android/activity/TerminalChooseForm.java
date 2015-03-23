package com.example.zf_android.activity;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.BaseActivity;
import com.example.zf_android.R;

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
public class TerminalChooseForm extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_choose_form);
        new TitleMenuUtil(TerminalChooseForm.this, "选择终端").show();
    }
}
