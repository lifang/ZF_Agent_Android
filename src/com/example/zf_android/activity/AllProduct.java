package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.goods.GoodsList;
import com.posagent.utils.Constants;

/***
 * 
 * 
 * 所有商品
 * 
 * @version
 * 
 */
public class AllProduct extends BaseActivity implements OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allproduct);

        setupCommonViews();

        focusTabAtIndex(1);

        initView();
	}

    private void initView() {
        findViewById(R.id.btn_pigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkRole(Constants.Roles.Pigou)) {return;}

                Intent i2 =new Intent(AllProduct.this, GoodsList.class);
                i2.putExtra("orderType", Constants.Goods.OrderTypePigou);
                startActivity(i2);
            }
        });
        findViewById(R.id.btn_daigou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRole(Constants.Roles.Daigou)) {return;}

                Intent i2 =new Intent(AllProduct.this, GoodsList.class);
                i2.putExtra("orderType", Constants.Goods.OrderTypeDaigou);
                startActivity(i2);
            }
        });
    }


	@Override
	public void onClick(View v) {


        super.onClick(v);
	}
}
