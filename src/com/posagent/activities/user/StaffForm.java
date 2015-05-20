package com.posagent.activities.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.StaffEntity;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 员工信息修改
 */
public class StaffForm extends BaseActivity
{

    private EditText et_name, et_username, et_password, et_password_confirm;
    private CheckBox cb_role_pigou, cb_role_daigou, cb_role_terminal, cb_role_stock,
            cb_role_fenrun, cb_role_xiaji, cb_role_user, cb_role_staff, cb_role_address;
    private Button btn_submit;

    private StaffEntity entity;
    private String[] roles = {"pigou", "daigou", "terminal", "fenrun",
            "xiaji", "user", "staff", "address", "stock"};

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_form);


        entity = gson.fromJson(getIntent().getStringExtra("json"), StaffEntity.class);

        if (null == entity) {
            new TitleMenuUtil(StaffForm.this, "创建员工帐号").show();
        } else {
            new TitleMenuUtil(StaffForm.this, "修改员工帐号").show();
            id = entity.getId();
        }



        initView();
    }

    private void initView() {

        et_name = (EditText) findViewById(R.id.et_name);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);

        cb_role_pigou = (CheckBox) findViewById(R.id.cb_role_pigou);
        cb_role_daigou = (CheckBox) findViewById(R.id.cb_role_daigou);
        cb_role_terminal = (CheckBox) findViewById(R.id.cb_role_terminal);
        cb_role_fenrun = (CheckBox) findViewById(R.id.cb_role_fenrun);
        cb_role_xiaji = (CheckBox) findViewById(R.id.cb_role_xiaji);
        cb_role_user = (CheckBox) findViewById(R.id.cb_role_user);
        cb_role_staff = (CheckBox) findViewById(R.id.cb_role_staff);
        cb_role_address = (CheckBox) findViewById(R.id.cb_role_address);
        cb_role_stock = (CheckBox) findViewById(R.id.cb_role_stock);

        if (null != entity) {
            et_name.setText(entity.getName());
            et_username.setText(entity.getUsername());

            getData();
        }


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }


    private void getData() {
        JsonParams params = new JsonParams();

        params.put("agentsId",  MyApplication.user().getAgentId());
        params.put("customerId", entity.getId());

        String strParams = params.toString();

        Events.CommonRequestEvent event = new Events.StaffInfoEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        switch (v.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
            default:
                break;
        }


        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }



    private boolean check() {

        if (null != entity && TextUtils.isEmpty(et_password.getText().toString())) {
            return true;
        }

        if (!et_password.getText().toString().equals(et_password_confirm.getText().toString())) {
            toast("两次密码不匹配");
            return false;
        }


        int len = et_password.getText().toString().length();
        if (len < 6 || len > 20) {
            toast("密码长度6-20位");
            return false;
        }
        return true;
    }

    private void doSubmit() {
        if(check()) {
            JsonParams params = new JsonParams();

            List<String> checkedRoles = new ArrayList<String>(roles.length);

            for (int i = 0; i < roles.length; i++) {
                String role = roles[i];
                int id = resouceId("cb_role_" + role, "id");
                CheckBox cb = (CheckBox) findViewById(id);
                if (cb.isChecked()) {
                    checkedRoles.add("" + i);
                }
            }
            if (null == entity) {
                params.put("agentsId",  MyApplication.user().getAgentId());
                params.put("loginId", et_username.getText().toString());
                params.put("userName", et_name.getText().toString());
                params.put("pwd1", StringUtil.Md5(et_password_confirm.getText().toString()));
            } else {
                params.put("customerId", id);
            }
            params.put("pwd", StringUtil.Md5(et_password.getText().toString()));
            params.put("roles", StringUtil.join(checkedRoles, ","));


            String strParams = params.toString();

            Events.CommonRequestEvent event = new Events.StaffCreateEvent();
            if (null != entity) {
                event = new Events.StaffEditEvent();
            }
            event.setParams(strParams);
            EventBus.getDefault().post(event);
        }
    }

    // events
    public void onEventMainThread(Events.StaffCreateCompleteEvent event) {
        toast(event.getMessage());
        if (event.success()) {
            EventBus.getDefault().post(new Events.StaffListReloadEvent());
            finish();
        }
    }

    public void onEventMainThread(Events.StaffEditCompleteEvent event) {
        toast(event.getMessage());
        if (event.success()) {
            EventBus.getDefault().post(new Events.StaffListReloadEvent());
            finish();
        }
    }

    public void onEventMainThread(Events.StaffInfoCompleteEvent event) {
        if (event.success()) {
            entity = event.getEntity();

            //update info
            updateInfo();
        }
    }

    private void updateInfo() {
        String strRoles = entity.getRolesStr();
        String[] arrRoles = strRoles.split(",");
        for (String idx : arrRoles) {
            String role = roles[Integer.parseInt(idx)];
            int id = resouceId("cb_role_" + role, "id");
            CheckBox cb = (CheckBox) findViewById(id);
            cb.setChecked(true);
        }

    }


}
