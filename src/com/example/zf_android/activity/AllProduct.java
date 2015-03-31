package com.example.zf_android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;
import com.posagent.activities.home.Main;

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


    // �������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allproduct);

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("main_rl_pos1", SystemMessage.class);
            put("main_rl_my", MenuMine.class);
            put("main_rl_sy", Main.class);
            put("btn_qpg", GoodsListActivity.class);
            put("btn_qdg", GoodsListActivity.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

	}


	@Override
	public void onClick(View v) {


        super.onClick(v);
	}
}
