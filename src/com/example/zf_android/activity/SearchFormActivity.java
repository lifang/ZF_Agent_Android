package com.example.zf_android.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.posagent.activities.BaseActivity;
import com.example.zf_android.R;
import com.posagent.activities.goods.GoodsList;

import java.util.HashMap;

/***
*
* 搜索表单
*
*/
public class SearchFormActivity extends BaseActivity {

    private static String TAG = "SearchFormActivity";

    private EditText search_edit;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_form);

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
            put("tv_cancel", GoodsList.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        final EditText search_edit = (EditText)findViewById(R.id.serch_edit);

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, search_edit.getText().toString());
                    finish();
                    return false;
                }
                return false;
            }
        });


	}

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理

        super.onClick(v);
	}

	 
}
