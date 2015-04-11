package com.posagent.activities.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_android.trade.widget.MyViewPager;
import com.posagent.events.Events;
import com.posagent.fragments.RegisterFragment;
import com.posagent.utils.Constants.UserConstant;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class Register extends FragmentActivity
        implements ViewPager.OnPageChangeListener,
                    View.OnClickListener {

    private MyTabWidget mTabWidget;
    private MyViewPager mViewPager;

    private List<RegisterFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        new TitleMenuUtil(Register.this, "申请成为代理商").show();
        initView();

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView() {
        mTabWidget = (MyTabWidget) findViewById(R.id.tab_widget);
        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        mFragments = new ArrayList<RegisterFragment>();

        // add tabs to the TabWidget
        String[] tabs = {"公司", "个人"};
        for (int i = 0; i < tabs.length; i++) {
            mTabWidget.addTab(tabs[i]);
        }


        mFragments.add(
                RegisterFragment.newInstance(UserConstant.USER_KIND_AGENT)
        );
        mFragments.add(
                RegisterFragment.newInstance(UserConstant.USER_KIND_PESONAL)
        );


        //setup
        mTabWidget.setViewPager(mViewPager);
        mViewPager.setAdapter(new RegisterPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(this);


        mTabWidget.updateTabs(0);
        mViewPager.setCurrentItem(0);



    }

    @Override
    public void onClick(View v) {

    }

    // evnets

    // 用户注册完成
    public void onEventMainThread(Events.CompleteEvent event) {
        Toast.makeText(getApplicationContext(),
                event.getMessage(),
                Toast.LENGTH_SHORT).show();

        if (event.getSuccess()) {
            finish();
        }
    }

    // implements
    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        mTabWidget.updateTabs(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    public class RegisterPagerAdapter extends FragmentPagerAdapter {

        public RegisterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}