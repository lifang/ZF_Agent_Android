package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.example.zf_android.BaseActivity;
import com.example.zf_android.R;

/***
 * 
 * 
 * 所有商品
 * 
 * @version
 * 
 */
public class AllProduct extends BaseActivity implements OnClickListener {
    private RelativeLayout rl_sy,rl_gw,rl_xx,rl_wd;


    // �������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allproduct);
        initView();

	}

    private void initView() {
        // TODO Auto-generated method stub

        rl_wd=(RelativeLayout) findViewById(R.id.main_rl_my);
        rl_wd.setOnClickListener(this);
        rl_xx=(RelativeLayout) findViewById(R.id.main_rl_pos1);
        rl_xx.setOnClickListener(this);
        rl_sy =(RelativeLayout) findViewById(R.id.main_rl_sy);
        rl_sy.setOnClickListener(this);




    }


	@Override
	public void onClick(View v) { 
		switch (v.getId()) {

		case R.id.main_rl_pos1:  // ��POS����
			 startActivity(new Intent(AllProduct.this, SystemMessage.class));
			 finish();
			break;
		case R.id.main_rl_my:  // ��POS����
			 startActivity(new Intent(AllProduct.this, MenuMine.class));
			 finish();
			break;
		case R.id.main_rl_sy:  // ��POS����
			 startActivity(new Intent(AllProduct.this, Main.class));
			 finish();
			break;
		default:
			break;
		}
	}
}
