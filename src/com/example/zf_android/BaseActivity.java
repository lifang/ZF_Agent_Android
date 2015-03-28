package com.example.zf_android;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends Activity implements View.OnClickListener {

    HashMap<String, Class> clickableMap;

    public HashMap<String, Class> getClickableMap() {
        return clickableMap;
    }

    public void setClickableMap(HashMap<String, Class> clickableMap) {
        this.clickableMap = clickableMap;
    }

    protected void bindClickListener() {

        if (clickableMap == null) {
            return;
        }

        for (String strId : clickableMap.keySet()) {
            int resouceId = this.getResources().getIdentifier(strId, "id", this.getPackageName());
            View view = (View) findViewById(resouceId);
            view.setOnClickListener(this);
        }

    }

    protected Class matchedClass(View v) {
        Class activity = null;
        if (null == clickableMap) {
            return activity;
        }
        for (Map.Entry<String, Class> entry : clickableMap.entrySet()) {
            String strId = entry.getKey();
            int resouceId = this.getResources().getIdentifier(strId, "id", this.getPackageName());
            if (v.getId() == resouceId) {
                activity = entry.getValue();
                break;
            }

        }
        return activity;
    }

    @Override
	protected void onDestroy() {
		//�����
		//getRequests().cancelAll(this);
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	//	StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	//	StatService.onResume(this);
	}

    @Override
    public void onClick(View v) {
        Class activity = this.matchedClass(v);
        if (activity != null) {
            startActivity(new Intent(this, activity));
//            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);

        }
    }
}
