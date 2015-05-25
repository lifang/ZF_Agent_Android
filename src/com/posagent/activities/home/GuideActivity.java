package com.posagent.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.epalmpay.agentPhone.R;
import com.posagent.activities.BaseActivity;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;


/***
 *
 */
public class GuideActivity extends BaseActivity implements OnClickListener {

    private ViewPager mViewPager;
    private List<View> lists = new ArrayList<View>();
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        View item = getLayoutInflater().inflate(R.layout.item_for_guide, null);
        ImageView iv = (ImageView)item.findViewById(R.id.image);
        iv.setImageResource(R.drawable.guide1);
        lists.add(item);

        item = getLayoutInflater().inflate(R.layout.item_for_guide, null);
        iv = (ImageView)item.findViewById(R.id.image);
        iv.setImageResource(R.drawable.guide2);
        lists.add(item);

        item = getLayoutInflater().inflate(R.layout.item_for_guide, null);
        iv = (ImageView)item.findViewById(R.id.image);
        iv.setImageResource(R.drawable.guide3);
        lists.add(item);

        item = getLayoutInflater().inflate(R.layout.item_for_guide, null);
        iv = (ImageView)item.findViewById(R.id.image);
        iv.setImageResource(R.drawable.guide4);
        lists.add(item);

        adapter = new ViewPagerAdapter(lists);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // 当滑动式，顶部的imageView是通过animation缓慢的滑动
            @Override
            public void onPageSelected(int idx) {
                if (idx == lists.size() - 1) {
                    show("rl_actions");
                }
                changDots(idx);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        findViewById(R.id.tv_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Register.class);
                startActivity(i);
            }
        });

        findViewById(R.id.tv_use).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_linear_login:
                break;
            default:
                break;
        }
    }

    private void changDots(int idx) {
        List<View> views = ViewHelper.getViewsByTag((LinearLayout)findViewById(R.id.ll_dot_container), "dot");

        ImageView iv;
        for (int i = 0; i < views.size(); i++) {
            iv = (ImageView)views.get(i);
            if (i == idx) {
                iv.setImageResource(R.drawable.dot_press);
            } else {
                iv.setImageResource(R.drawable.dot);
            }
        }

    }




    public class ViewPagerAdapter extends PagerAdapter {

        List<View> viewLists;

        public ViewPagerAdapter(List<View> lists)
        {
            viewLists = lists;
        }

        //获得size
        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        //销毁Item
        @Override
        public void destroyItem(View view, int position, Object object)
        {
            ((ViewPager) view).removeView(viewLists.get(position));
        }

        //实例化Item
        @Override
        public Object instantiateItem(View view, int position)
        {
            ((ViewPager) view).addView(viewLists.get(position), 0);
            return viewLists.get(position);
        }
    }
}
