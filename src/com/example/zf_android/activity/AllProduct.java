package com.example.zf_android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.zf_android.R;
import com.posagent.activities.BaseActivity;

import java.util.HashMap;

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

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("btn_qpg", GoodsListActivity.class);
            put("btn_qdg", GoodsListActivity.class);
        }};
        this.setClickableMap(clickableMap);

        setupCommonViews();

        focusTabAtIndex(1);

	}


	@Override
	public void onClick(View v) {


        super.onClick(v);
	}
}
