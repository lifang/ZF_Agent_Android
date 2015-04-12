package com.example.zf_android.activity;

import android.os.Bundle;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;

/**
 * 订单支付
 *
 */
public class PayFromCar extends BaseActivity{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.pay);
			new TitleMenuUtil(PayFromCar.this, "订单支付").show();
		}
}
