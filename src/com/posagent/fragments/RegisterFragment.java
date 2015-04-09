package com.posagent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zf_android.R;
import com.posagent.activities.CommonInputer;
import com.posagent.utils.Constants;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;

/**
 * Created by Leo on 2015/2/6.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    static String TAG = "RegisterFragment";

    private int userKind;
    private LayoutInflater mInflater;


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
        switch (requestCode) {
            case Constants.COMMONINPUTER_REQUEST:
                Log.d(TAG, data.toString());
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

        String tag = getString(R.string.tag_destination_get);
        if (v.getTag().equals(tag)) {
            tag = getString(R.string.tag_destination_name);
            TextView tv = (TextView) v.findViewWithTag(tag);

            tag = getString(R.string.tag_destination);
            tv = (TextView) v.findViewWithTag(tag);

            Intent intent = new Intent(getActivity(), CommonInputer.class);
            startActivityForResult(intent, Constants.COMMONINPUTER_REQUEST);
        }
    }
}
