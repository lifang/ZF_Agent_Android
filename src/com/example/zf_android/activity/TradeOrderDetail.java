package com.example.zf_android.activity;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
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
public class TradeOrderDetail extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_order_detail);
        new TitleMenuUtil(TradeOrderDetail.this, "交易详情").show();
    }
}
