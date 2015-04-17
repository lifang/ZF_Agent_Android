package com.posagent.activities.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.AdressEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.activities.user.ChangeAdress;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 *
 *
 * 售后申请
 * @version
 *
 */
public class AfterSaleApply extends BaseActivity {

    private List<AdressEntity>  listAddress = new ArrayList<AdressEntity>();

    private LinearLayout click_choose_terminal, ll_choose_address;
    private TextView tv_receiver, tv_mobile, tv_address;
    private EditText et_aftersale_reson;
    private Button btn_after_sale_apply;

    private AdressEntity addressEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_apply);
        new TitleMenuUtil(AfterSaleApply.this, "售后申请").show();

        initView();
    }

    private void initView() {
        click_choose_terminal = (LinearLayout) findViewById(R.id.click_choose_terminal);
        click_choose_terminal.setOnClickListener(this);

        ll_choose_address = (LinearLayout) findViewById(R.id.ll_choose_address);
        ll_choose_address.setOnClickListener(this);

        tv_receiver = (TextView) findViewById(R.id.tv_receiver);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_address = (TextView) findViewById(R.id.tv_address);
        et_aftersale_reson = (EditText) findViewById(R.id.et_aftersale_reson);
        btn_after_sale_apply = (Button) findViewById(R.id.btn_after_sale_apply);
        btn_after_sale_apply.setOnClickListener(this);


        getData();
    }

    private void getData() {

        JsonParams params = new JsonParams();

        //Fixme
        params.put("customerId", 1);

        String strParams = params.toString();
        Events.AddressListEvent event = new Events.AddressListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    // events
    public void onEventMainThread(Events.AddressListCompleteEvent event) {
        if (event.getSuccess()) {
            listAddress = event.getList();

            for (AdressEntity entity: listAddress) {
                if (addressEntity == null) {
                    addressEntity = entity;
                }

                if (entity.getIs_default() == "1") {
                    addressEntity = entity;
                    break;
                }
            }

            updateAddress();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                int addressId = bundle.getInt("id");
                for (AdressEntity entity: listAddress) {
                    if (addressId == entity.getId()) {
                        addressEntity = entity;
                        updateAddress();
                        break;
                    }
                }
            }
        }
    } //onActivityResult



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_choose_terminal:
                Intent i = new Intent(AfterSaleApply.this, TerminalChooseForm.class);
                startActivity(i);
                break;
            case R.id.ll_choose_address:
                Intent i2 =new Intent(AfterSaleApply.this, ChangeAdress.class);
                startActivityForResult(i2, Constants.REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void updateAddress() {
        if (addressEntity != null) {
            tv_receiver.setText("收件人：" + addressEntity.getReceiver());
            tv_address.setText("收件地址：" + addressEntity.getAddress());
            tv_mobile.setText("" + addressEntity.getMoblephone());
        }
    }
}