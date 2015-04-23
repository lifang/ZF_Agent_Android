package com.posagent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.trade.CitySelectActivity;
import com.example.zf_android.trade.common.CommonUtil;
import com.posagent.activities.CommonInputer;
import com.posagent.activities.ImageViewer;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;
import com.posagent.utils.PhotoManager;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_TAKE_PHOTO;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_UPLOAD_IMAGE;

/**
 * Created by Leo on 2015/2/6.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    static String TAG = "RegisterFragment";

    private View thisview;

    private int userKind;
    private LayoutInflater mInflater;
    private TextView tvContent;

    private String cityName;
    private TextView tvCityName;


    private int cityId;
    private String cardIdPhotoPath;
    private String licenseNoPicPath;
    private String taxNoPicPath;

    private String photoPath;
    private String currentKind;

    private PhotoManager photoManager;


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
        photoManager = new PhotoManager(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        thisview = view;
        initViews(view);

    }

    private void initViews(View view) {
        if (userKind == Constants.UserConstant.USER_KIND_PESONAL) {
            View company1 = view.findViewById(R.id.company_content1);
            company1.setVisibility(View.GONE);
            View company2 = view.findViewById(R.id.company_content2);
            company2.setVisibility(View.GONE);
        }

        view.findViewById(R.id.mi_select_city).setOnClickListener(this);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        tvCityName = (TextView) view.findViewById(R.id.tv_city_name);

        //photo
        TextView tv_card_photo = (TextView) view.findViewById(R.id.tv_card_photo);
        tv_card_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "card";
                photoManager.prompt();
            }
        });

        TextView tv_license_photo = (TextView) view.findViewById(R.id.tv_license_photo);
        tv_license_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "license";
                photoManager.prompt();
            }
        });
        TextView tv_tax_photo = (TextView) view.findViewById(R.id.tv_tax_photo);
        tv_tax_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "tax";
                photoManager.prompt();
            }
        });

        ImageView iv_card_photo = (ImageView) view.findViewById(R.id.iv_card_photo);
        iv_card_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "card";
                Intent i = new Intent(getActivity(), ImageViewer.class);
                i.putExtra("url", cardIdPhotoPath);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

        ImageView iv_license_photo = (ImageView) view.findViewById(R.id.iv_license_photo);
        iv_license_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "license";
                Intent i = new Intent(getActivity(), ImageViewer.class);
                i.putExtra("url", licenseNoPicPath);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

        ImageView iv_tax_photo = (ImageView) view.findViewById(R.id.iv_tax_photo);
        iv_tax_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKind = "tax";
                Intent i = new Intent(getActivity(), ImageViewer.class);
                i.putExtra("url", taxNoPicPath);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

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
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case Constants.CommonInputerConstant.REQUEST_CODE:

                Bundle bundle = data.getExtras();
                String content = bundle.getString(Constants.CommonInputerConstant.VALUE_KEY);
                tvContent.setText(content);
                Log.d(TAG, data.toString());
                break;
            case Constants.CommonInputerConstant.REQUEST_CITY_CODE:

                Bundle bundle2 = data.getExtras();
                cityName = bundle2.getString(com.example.zf_android.trade.Constants.CityIntent.CITY_NAME);
                cityId = bundle2.getInt(com.example.zf_android.trade.Constants.CityIntent.CITY_ID);
                tvCityName.setText(cityName);
                break;
            case Constants.REQUEST_CODE:
                String url = data.getStringExtra("url");
                updatePhotoUrl(url);
                break;
            case REQUEST_UPLOAD_IMAGE:
            case REQUEST_TAKE_PHOTO: {

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            String url = (String) msg.obj;
                            updatePhotoUrl(url);
                        } else {
                            CommonUtil.toastShort(getActivity(), getString(R.string.toast_upload_failed));
                        }

                    }
                };
                photoManager.onActivityResult(requestCode, resultCode, data, handler);
                break;
            }
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

        if (v.getTag().equals("tag_man_photo")) {
            photoManager.prompt();
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
        JsonParams data = new JsonParams();
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

        return data.toString();
    }

    private int resouceId(String name, String kind) {
        return this.getResources().getIdentifier(name, kind, this.getActivity().getPackageName());
    }

    private void updatePhotoUrl(String url) {
        Log.d(TAG, url);
        if (currentKind.equals("card")) {
            cardIdPhotoPath = url;
            thisview.findViewById(R.id.iv_card_photo).setVisibility(View.VISIBLE);
            thisview.findViewById(R.id.tv_card_photo).setVisibility(View.GONE);
        } else if (currentKind.equals("license")) {
            licenseNoPicPath = url;
            thisview.findViewById(R.id.iv_license_photo).setVisibility(View.VISIBLE);
            thisview.findViewById(R.id.tv_license_photo).setVisibility(View.GONE);
        } else if (currentKind.equals("tax")) {
            taxNoPicPath = url;
            thisview.findViewById(R.id.iv_tax_photo).setVisibility(View.VISIBLE);
            thisview.findViewById(R.id.tv_tax_photo).setVisibility(View.GONE);
        }
    }



}
