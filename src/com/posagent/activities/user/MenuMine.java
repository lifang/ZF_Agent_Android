package com.posagent.activities.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.example.zf_android.activity.MerchantList;
import com.example.zf_android.activity.MineSet;
import com.example.zf_android.activity.MyInfo;
import com.example.zf_android.activity.SystemMessage;
import com.example.zf_android.trade.ApplyOpenProgressActivity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.agent.AgentManageMainActivity;
import com.posagent.activities.home.LoginActivity;
import com.posagent.activities.home.Main;
import com.posagent.utils.Constants;


public class MenuMine extends BaseActivity {
    private ImageView search;
    private LinearLayout  ll_dd, ll_yg, ll_shjl,ll_wdxx,ll_sh,ll_request;
    private RelativeLayout  main_rl1, main_rl2, main_rl3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_mine);
        initView();
        setupCommonViews();
        focusTabAtIndex(3);
    }
    private void initView() {

        search=(ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        ll_dd=(LinearLayout) findViewById(R.id.ll_dd);
        ll_dd.setOnClickListener(this);
        ll_wdxx=(LinearLayout) findViewById(R.id.ll_wdxx);
        ll_wdxx.setOnClickListener(this);
        ll_yg=(LinearLayout) findViewById(R.id.ll_yg);
        ll_yg.setOnClickListener(this);

        findViewById(R.id.ll_400).setOnClickListener(this);

        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.setCurrentUser(null);
                startActivity(new Intent(context, LoginActivity.class));
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_request:
                startActivity(new Intent(this, ApplyOpenProgressActivity.class));
                break;
            case R.id.ll_sh:
                startActivity(new Intent(MenuMine.this,MerchantList.class));
                break;
            case R.id.search:
                startActivity(new Intent(MenuMine.this,MineSet.class));
                break;
            case R.id.ll_dd: //下级代理商
                if (checkRole(Constants.Roles.SonAgent)) {
                    startActivity(new Intent(MenuMine.this, AgentManageMainActivity.class));
                }
                break;
            case R.id.ll_wdxx:
                startActivity(new Intent(MenuMine.this, MyInfo.class));
                break;
            case R.id.ll_yg: //员工管理
                if (checkRole(Constants.Roles.ManStaff)) {
                    startActivity(new Intent(MenuMine.this, StaffList.class));
                }

                break;

            case R.id.main_rl_sy: //ϵ

                startActivity(new Intent(MenuMine.this,Main.class));

                break;

            case R.id.main_rl_gwc: //全部商品

                startActivity(new Intent(MenuMine.this, AllProduct.class));
                break;
            case R.id.main_rl_pos1:
                startActivity(new Intent(MenuMine.this, SystemMessage.class));
                break;
            case R.id.ll_400:
                Intent intent = new Intent(
                        Intent.ACTION_DIAL, Uri.parse("tel:4000908076"));
                startActivity(intent);
                break;

            default:
                break;
        }

        super.onClick(v);
    }
}
