package com.posagent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_android.R;
import com.example.zf_android.activity.AllProduct;
import com.posagent.activities.user.MenuMine;
import com.google.gson.Gson;
import com.posagent.activities.home.Main;
import com.posagent.activities.user.MessageList;
import com.posagent.events.Events;
import com.posagent.utils.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class BaseActivity extends Activity implements View.OnClickListener {

    protected String TAG = getClass().toString();

    protected Gson gson = new Gson();

    HashMap<String, Class> clickableMap = new HashMap<String, Class>();
    private static final ArrayList<String> tabImages = new ArrayList<String>() {{
        add("home");
        add("product");
        add("message");
        add("mine");
    }};

    public HashMap<String, Class> getClickableMap() {
        return clickableMap;
    }

    public void setClickableMap(HashMap<String, Class> clickableMap) {
        this.clickableMap.putAll(clickableMap);
    }

    protected void bindClickListener() {

        if (clickableMap == null) {
            return;
        }

        for (String strId : clickableMap.keySet()) {
            int resouceId = resouceId(strId, "id");
            Log.d("BaseActivity", "" + resouceId + "" + strId);
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
            int resouceId = resouceId(strId, "id");
            if (v.getId() == resouceId) {
                activity = entry.getValue();
                break;
            }

        }
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }
    }

    @Override
	protected void onDestroy() {
		//getRequests().cancelAll(this);
        EventBus.getDefault().unregister(this);
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

    protected int resouceId(String name, String kind) {
        return this.getResources().getIdentifier(name, kind, this.getPackageName());
    }

    protected void setText(String id, String value) {
        TextView tv = (TextView)findViewById(resouceId(id, "id"));
        tv.setText(value);
    }

    protected String getText(String id) {
        TextView tv = (TextView)findViewById(resouceId(id, "id"));
        return tv.getText().toString();
    }

    protected String getValue(String id) {
        EditText tv = (EditText)findViewById(resouceId(id, "id"));
        return tv.getText().toString();
    }

    protected void enterText(String id, String value) {
        EditText tv = (EditText)findViewById(resouceId(id, "id"));
        tv.setText(value);
    }

    protected void hide(String id) {
        View tv = findViewById(resouceId(id, "id"));
        tv.setVisibility(View.GONE);
    }

    protected void show(String id) {
        View tv = findViewById(resouceId(id, "id"));
        tv.setVisibility(View.VISIBLE);
    }

    protected void hideByTag(String tag) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        ArrayList<View> views = (ArrayList<View>) ViewHelper.getViewsByTag(viewGroup, tag);
        for (View _view: views) {
            _view.setVisibility(View.GONE);
        }
    }

    protected void showByTag(String tag) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        ArrayList<View> views = (ArrayList<View>) ViewHelper.getViewsByTag(viewGroup, tag);
        for (View _view: views) {
            _view.setVisibility(View.VISIBLE);
        }
    }

    protected void focusTabAtIndex(int index) {
        int bottomTabContainerId = resouceId("bottom_tab_container", "id");
        if (bottomTabContainerId > 0) {
            LinearLayout viewGroup = (LinearLayout)findViewById(R.id.bottom_tab_line);
            View view = viewGroup.getChildAt(index);
            view.setBackgroundResource(R.color.bgtitle);

            int tabId = resouceId("tab_index" + index, "id");
            viewGroup = (LinearLayout)findViewById(tabId);
            ImageView imageView = (ImageView)viewGroup.getChildAt(0);

            int resouceId;
            resouceId = resouceId(tabImages.get(index), "drawable");
            imageView.setBackgroundResource(resouceId);

            TextView tv = (TextView)viewGroup.getChildAt(1);
            tv.setTextColor(getResources().getColor(R.color.bgtitle));

        }
    }

    protected void setupCommonViews() {
        int bottomTabContainerId = resouceId("bottom_tab_container", "id");
        if (bottomTabContainerId > 0) {
            HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
                put("tab_index2", MessageList.class);
                put("tab_index3", MenuMine.class);
                put("tab_index0", Main.class);
                put("tab_index1", AllProduct.class);
            }};
            this.setClickableMap(clickableMap);
            this.bindClickListener();
        }
    }

    //event listener
    public void onEventMainThread(Events.NoConnectEvent event) {
        Toast.makeText(getApplicationContext(),
                "没有网络连接",
                Toast.LENGTH_SHORT).show();
    }
    public void onEventMainThread(Events.RefreshToMuch event) {
        Toast.makeText(getApplicationContext(),
                "刷新太频繁",
                Toast.LENGTH_SHORT).show();
    }

    //helper
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
