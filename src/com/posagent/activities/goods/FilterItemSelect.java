package com.posagent.activities.goods;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.GoodsCategoryEntity;
import com.example.zf_android.entity.IdValueEntity;
import com.google.gson.reflect.TypeToken;
import com.posagent.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/***
*
* 过滤表单
*
*/
public class FilterItemSelect extends BaseActivity {

    private LinearLayout ll_container;
    private CheckBox cb_all;
    private TextView next_sure;

    private LayoutInflater inflater;

    private List<IdValueEntity> list;
    private List<GoodsCategoryEntity> categories;

    private List<Integer> selectIds = new ArrayList<Integer>();



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_item_select);

        inflater = LayoutInflater.from(this);

        String json = getIntent().getStringExtra("json");
        if (null != json) {
            list = gson.fromJson(json, new TypeToken<List<IdValueEntity>>() {}.getType());
        }
        String categoryJson = getIntent().getStringExtra("categoryJson");
        if (null != categoryJson) {
            categories = gson.fromJson(categoryJson, new TypeToken<List<GoodsCategoryEntity>>() {}.getType());
        }
        String selectedIdsJson = getIntent().getStringExtra("selectedIdsJson");
        if (null != selectedIdsJson) {
            selectIds = gson.fromJson(selectedIdsJson, new TypeToken<List<Integer>>() {}.getType());
        }



        new TitleMenuUtil(FilterItemSelect.this, "筛选").show();


        initView();

	}

    private void initView() {

        next_sure = (TextView)findViewById(R.id.next_sure);
        next_sure.setVisibility(View.VISIBLE);
        next_sure.setOnClickListener(this);

        cb_all = (CheckBox)findViewById(R.id.cb_all);
        ll_container = (LinearLayout)findViewById(R.id.ll_container);

        if (null != list) {
            updateView();
        } else if (null != categories) {
            updateViewCategory();
        }

    }

    private void updateView() {
        View v;
        TextView tv;
        CheckBox cb;
        IdValueEntity item;
        int len = list.size();

        for (int i = 0; i < len; i++) {
            final int idx = i;
            item = list.get(i);
            v = inflater.inflate(R.layout.item_filter_item, null);
            tv = (TextView)v.findViewById(R.id.tv_name);
            tv.setText(item.getValue());

            cb = (CheckBox)v.findViewById(R.id.cb_item);
            if (selectIds.contains(item.getId())) {
                cb.setChecked(true);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    checkAtIndex(idx, cb.isChecked());
                }
            });

            ll_container.addView(v);
        }

    }

    private void updateViewCategory() {
        View v;
        TextView tv;
        CheckBox cb;
        GoodsCategoryEntity item;
        IdValueEntity sonItem;
        int len = categories.size();

        for (int i = 0; i < len; i++) {
            final int idx = i;
            item = categories.get(i);
            v = inflater.inflate(R.layout.item_filter_item, null);
            tv = (TextView)v.findViewById(R.id.tv_name);
            tv.setText(item.getValue());

            cb = (CheckBox)v.findViewById(R.id.cb_item);
            if (selectIds.contains(item.getId())) {
                cb.setChecked(true);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    checkAtIndex(idx, cb.isChecked());
                }
            });

            ll_container.addView(v);
            List<IdValueEntity> sons = item.getSon();
            if (null == sons) {
                continue;
            }
            int lenSon = sons.size();
            for (int j = 0; j < lenSon; j++) {
                final int idxSon = j;
                sonItem = sons.get(j);
                v = inflater.inflate(R.layout.item_filter_item, null);

                v.findViewById(R.id.ll_bg_line).setBackgroundResource(R.drawable.bg_item_gray);

                v.findViewById(R.id.v_left_padding).setVisibility(View.VISIBLE);
                tv = (TextView)v.findViewById(R.id.tv_name);
                tv.setText(sonItem.getValue());

                cb = (CheckBox)v.findViewById(R.id.cb_item);
                if (selectIds.contains(sonItem.getId())) {
                    cb.setChecked(true);
                }
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        checkSonAtIndex(idxSon, idx, cb.isChecked());
                    }
                });

                ll_container.addView(v);
            }
        }

    }


	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        switch (v.getId()) {
            case R.id.next_sure:
                //确认
                doSubmit();
                break;
            case R.id.ll_brands:

                break;
        }

        super.onClick(v);
	}

    private void checkAtIndex(int idx, boolean checked) {
        if (null != list) {
            IdValueEntity item = list.get(idx);
            item.setSelected(checked);
            if (checked) {
                selectIds.add(item.getId());
            } else {
                removeId(item.getId());
            }
        } else if (null != categories) {
            GoodsCategoryEntity item = categories.get(idx);
            item.setSelected(checked);
            if (checked) {
                selectIds.add(item.getId());
            } else {
                removeId(item.getId());
            }
        }

    }

    private void checkSonAtIndex(int sonIdx, int idx, boolean checked) {
        GoodsCategoryEntity item = categories.get(idx);
        List<IdValueEntity> sons = item.getSon();
        IdValueEntity son = sons.get(sonIdx);
        son.setSelected(checked);
        if (checked) {
            selectIds.add(son.getId());
        } else {
            removeId(son.getId());
        }
    }

    private void doSubmit() {
        Intent i = getIntent();

        if (cb_all.isChecked()) {
            i.putExtra("selectAll", true);
        } else {
            i.putExtra("selectedIdsJson", gson.toJson(selectIds));
        }
        setResult(RESULT_OK, i);
        finish();
    }

    private void removeId(int id) {
        List<Integer> list = new ArrayList<Integer>();
        for (Integer i: selectIds) {
            if (id != i.intValue()) {
                list.add(i);
            }
        }
        selectIds = list;
    }

	 
}
