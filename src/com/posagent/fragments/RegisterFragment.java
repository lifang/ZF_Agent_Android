package com.posagent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.trade.CitySelectActivity;
import com.google.gson.Gson;
import com.posagent.activities.CommonInputer;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Leo on 2015/2/6.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    static String TAG = "RegisterFragment";

    private int userKind;
    private LayoutInflater mInflater;
    private TextView tvContent;

    private String cityName;
    private TextView tvCityName;


    private int cityId;
    private String cardIdPhotoPath = "uploadFiles/tmp/1428715403214.jp";
    private String licenseNoPicPath = "uploadFiles/tmp/1428715403214.jp";
    private String taxNoPicPath = "uploadFiles/tmp/1428715403214.jp";


    public static RegisterFragment newInstance(int userKind) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.USER_KIND, userKind);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userKind = getArguments().getInt(Constants.USER_KIND);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

    }

    private void initViews(View view) {
        if (userKind == Constants.UserConstant.USER_KIND_PESONAL) {
            View company1 = view.findViewById(R.id.company_content1);
            company1.setVisibility(View.GONE);
            View company2 = view.findViewById(R.id.company_content2);
            company2.setVisibility(View.GONE);
        }

        String tag = getString(R.string.tag_destination_get);
        ArrayList<View> views = (ArrayList<View>) ViewHelper.getViewsByTag((ViewGroup) view, tag);
        for (View _view: views) {
            _view.setOnClickListener(this);
        }

        tvCityName = (TextView) view.findViewById(R.id.tv_city_name);
//        View v = view.findViewById(R.id.btn_submit);
//        v.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CODE:
                String content = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);
                tvContent.setText(content);
                Log.d(TAG, data.toString());
                break;
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:
                cityName = bundle.getString(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME);
                cityId = bundle.getInt(com.example.zf_android.trade.Constants.CityIntent.CITY_ID);
                tvCityName.setText(cityName);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.mi_select_city) {
            Intent intent = new Intent(getActivity(), CitySelectActivity.class);

            if (cityName != null) {
                intent.putExtra(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME, cityName);
            }
            startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CITY_CODE);
            return;
        }

        if (v.getId() == R.id.btn_submit) {
            this.doSubmit();
            return;
        }

        String tag = getString(R.string.tag_destination_get);
        if (v.getTag().equals(tag)) {
            Intent intent = new Intent(getActivity(), CommonInputer.class);

            tag = getString(R.string.tag_destination_name);
            TextView tv = (TextView) v.findViewWithTag(tag);
            intent.putExtra(Constants.CommonInputerConstant.TITLE_KEY, tv.getText());

            tag = getString(R.string.tag_destination);
            tvContent = tv = (TextView) v.findViewWithTag(tag);
            intent.putExtra(Constants.CommonInputerConstant.PLACEHOLDER_KEY, tv.getText());




            startActivityForResult(intent, Constants.CommonInputerConstant.REQUEST_CODE);
        }
    }

    private boolean check() {
        EditText etPassword = (EditText) getView().findViewById(R.id.et_password);
        EditText etPasswordConfirmation = (EditText) getView().findViewById(R.id.et_password_confirm);

        if (!etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }


    private void doSubmit() {
        // check first
        if (!this.check()) {
            return;
        }

        // prepare data
        String strParams = this.data();

        // submit
        EventBus.getDefault().post(new Events.RegisterEvent(strParams));
        Toast.makeText(getActivity().getApplicationContext(), "正在提交", Toast.LENGTH_SHORT).show();

    }

    private String data() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("cityId", cityId);
        data.put("cardIdPhotoPath", cardIdPhotoPath);
        data.put("types", 2);

        if (userKind == Constants.UserConstant.USER_KIND_AGENT) {
            data.put("licenseNoPicPath", licenseNoPicPath);
            data.put("taxNoPicPath", taxNoPicPath);
            data.put("types", 1);
        }

        String[] arr = {"et_username", "et_password",
                "tv_phone", "tv_idCard",
                "tv_email", "tv_name", "tv_taxRegisteredNo",
                "tv_companyName", "tv_address",
                "tv_businessLicense"};

        if (userKind == Constants.UserConstant.USER_KIND_PESONAL) {
            arr = new String[]{"et_username", "et_password",
                    "tv_phone", "tv_idCard",
                    "tv_email", "tv_name", "tv_address" };
        }

        for (String strId : arr) {
            int resouceId = resouceId(strId, "id");
            if (strId.startsWith("tv_")) {
                TextView tv = (TextView) getView().findViewById(resouceId);
                if (tv != null) {
                    data.put(strId.replace("tv_", ""), tv.getText().toString());
                }
            } else if (strId.startsWith("et_")) {
                EditText tv = (EditText) getView().findViewById(resouceId);
                if (tv != null) {
                    data.put(strId.replace("et_", ""), tv.getText().toString());
                }
            }
        }

        Gson gson = new Gson();
        String strJson = gson.toJson(data);
        Log.d(TAG, strJson);
        return strJson;
    }

    private int resouceId(String name, String kind) {
        return this.getResources().getIdentifier(name, kind, this.getActivity().getPackageName());
    }
}
