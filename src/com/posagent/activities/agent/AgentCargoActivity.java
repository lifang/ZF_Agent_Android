package com.posagent.activities.agent;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.example.zf_android.entity.SonAgent;
import com.posagent.activities.BaseActivity;
import com.example.zf_android.Config;
import com.posagent.MyApplication;
import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_zandroid.adapter.AgentAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
*
* 代理商配货管理
*
*/
public class AgentCargoActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView Xlistview;


    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private AgentAdapter myAdapter;
    List<SonAgent> myList = new ArrayList<SonAgent>();
    List<SonAgent> moreList = new ArrayList<SonAgent>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    }
                    onRefresh_number = true;
                    myAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "no 3g or wifi content",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), " refresh too much",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_agent_cargo);
		new TitleMenuUtil(AgentCargoActivity.this, "配货管理").show();

        // 准备需要监听Click的数据
        HashMap<String, Class> clickableMap = new HashMap<String, Class>(){{
//            put("ll_create_agent", AgentNewActivity.class);
//            put("ll_glph", AdressList.class);
//            put("ll_gltp", UserList.class);
        }};
        this.setClickableMap(clickableMap);
        this.bindClickListener();

        initXListView();

        //配置 配货 按钮
        TextView viewSetRate = (TextView)findViewById(R.id.next_sure);
        viewSetRate.setText("配货");
        viewSetRate.setVisibility(View.VISIBLE);
        viewSetRate.setOnClickListener(this);

	}

    private void initXListView() {
        myAdapter = new AgentAdapter(AgentCargoActivity.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);
        getData();
    }


    @Override
    public void onRefresh() {
        page = 1;
        myList.clear();
        getData();
    }


    @Override
    public void onLoadMore() {
        if (onRefresh_number) {
            page = page + 1;
            if (Tools.isConnect(getApplicationContext())) {
                onRefresh_number = false;
                getData();
            } else {
                onRefresh_number = true;
                handler.sendEmptyMessage(2);
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    private void onLoad() {
        Xlistview.stopRefresh();
        Xlistview.stopLoadMore();
        Xlistview.setRefreshTime(Tools.getHourAndMin());
    }

    public void buttonClick() {
        page = 1;
        myList.clear();
        getData();
    }

    private void getData() {
        String url = "http://114.215.149.242:18080/ZFMerchant/api/order/getMyOrderAll";
        RequestParams params = new RequestParams();
        params.put("customer_id", 80);
        params.put("page", page);
        params.put("pageSize", 4);

        params.setUseJsonStreamer(true);

        MyApplication.getInstance().getClient()
                .post(url, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        String responseMsg = new String(responseBody)
                                .toString();
                        Log.e("print", responseMsg);


                        Gson gson = new Gson();

                        JSONObject jsonobject = null;
                        String code = null;
                        try {
                            jsonobject = new JSONObject(responseMsg);
                            code = jsonobject.getString("code");
                            int a = jsonobject.getInt("code");
                            if (a == Config.CODE) {
                                String res = jsonobject.getString("result");
                                jsonobject = new JSONObject(res);
                                moreList.clear();
                                System.out.println("-jsonobject String()--" + jsonobject.getString("content").toString());
                                moreList = gson.fromJson(jsonobject.getString("content").toString(), new TypeToken<List<OrderEntity>>() {
                                }.getType());
                                System.out.println("-sendEmptyMessage String()--");
                                myList.addAll(moreList);
                                handler.sendEmptyMessage(0);


                            } else {
                                code = jsonobject.getString("message");
                                Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            ;
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        System.out.println("-onFailure---");
                        Log.e("print", "-onFailure---" + error);
                    }
                });


    }

	@Override
	public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        if (v.getId() == R.id.next_sure) {
            startActivity(new Intent(this, AgentCargoCreateActivity.class));
            return;
        }

        super.onClick(v);
	}

	 
}
