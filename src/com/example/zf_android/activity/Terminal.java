package com.example.zf_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.BaseActivity;
import com.example.zf_android.Config;
import com.example.zf_android.MyApplication;
import com.example.zf_android.R;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.example.zf_zandroid.adapter.TerminalAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ƣ�OrderList
 * ��������   �����б�
 * �����ˣ� ljp
 * ����ʱ�䣺2015-2-4 ����3:04:31
 */
public class Terminal extends BaseActivity implements IXListViewListener, View.OnClickListener {
    private Button btn_bind_terminal, btn_after_sale_apply;
    private XListView Xlistview;
    private MyTabWidget mTabWidget;
    private int page = 1;
    private int rows = Config.ROWS;
    private LinearLayout eva_nodata;
    private boolean onRefresh_number = true;
    private TerminalAdapter myAdapter;
    List<OrderEntity> myList = new ArrayList<OrderEntity>();
    List<OrderEntity> moreList = new ArrayList<OrderEntity>();
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
                case 2: // ����������
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
    //�������

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal);
        initView();
        getData();
    }

    private void initView() {
        // set up click
        btn_after_sale_apply = (Button) findViewById(R.id.btn_after_sale_apply);
        btn_after_sale_apply.setOnClickListener(this);
        btn_bind_terminal = (Button) findViewById(R.id.btn_bind_terminal);
        btn_bind_terminal.setOnClickListener(this);

        new TitleMenuUtil(Terminal.this, "终端管理").show();
        myAdapter = new TerminalAdapter(Terminal.this, myList);
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
        Xlistview = (XListView) findViewById(R.id.x_listview);
        Xlistview.setPullLoadEnable(true);
        Xlistview.setXListViewListener(this);
        Xlistview.setDivider(null);
        Xlistview.setAdapter(myAdapter);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        page = 1;
        System.out.println("onRefresh1");
        myList.clear();
        System.out.println("onRefresh2");
        getData();
    }


    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
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

    /*
     * �������
     */
    private void getData() {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
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
                                Toast.makeText(getApplicationContext(), code, 1000).show();
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
        switch (v.getId()) {
            case R.id.btn_after_sale_apply:
                Intent i = new Intent(getApplicationContext(), AfterSaleApply.class);
                startActivity(i);
                break;
            case R.id.btn_bind_terminal:

                break;
            default:
                break;
        }
    }
}
