package com.posagent.network;

import android.util.Log;

import com.example.zf_android.entity.PosEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by holin on 4/3/15.
 */
public class APIManager {
    /** Log tag, apps may override it. */
    public static String TAG = "APIManager";
    static volatile APIManager defaultInstance;

    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    // Host and api
    public static final String BaseUrl = "http://114.215.149.242:28080/ZFAgent/api";

    public static final String UrlLogin = BaseUrl + "/agent/agentLogin";
    public static final String UrlRegister = BaseUrl + "/agent/userRegistration";
    public static final String UrlGoodsList = BaseUrl + "/good/list";
    public static final String UrlGoodsDetail = BaseUrl + "/good/goodinfo";
    public static final String UrlCreateOrder = BaseUrl + "/order/agent";
    public static final String UrlEventList = BaseUrl + "/agents/getAddressList";
    public static final String UrlCreateAddress = BaseUrl + "/agents/insertAddress";
    public static final String UrlDeleteAddress = BaseUrl + "/agents/deleteAddress";
    public static final String UrlOrderList = BaseUrl + "/order/orderSearch";
    public static final String UrlOrderDetailPigou = BaseUrl + "/order/getWholesaleById";
    public static final String UrlOrderDetailDaigou = BaseUrl + "/order/getProxyById";
    public static final String UrlOrderCancelPigou = BaseUrl + "/order/cancelWholesale";
    public static final String UrlOrderCancelDaigou = BaseUrl + "/order/cancelProxy";
    public static final String UrlStockList = BaseUrl + "/stock/list";
    public static final String UrlStockRename = BaseUrl + "/stock/rename";
    public static final String UrlStockAgentList = BaseUrl + "/stock/info";
    public static final String UrlStockAgentTerminalList = BaseUrl + "/stock/terminallist";


    /** Convenience singleton for apps using a process-wide EventBus instance. */
    public static APIManager getDefault() {
        if (defaultInstance == null) {
            synchronized (APIManager.class) {
                if (defaultInstance == null) {
                    defaultInstance = new APIManager();
                    EventBus.getDefault().register(defaultInstance);
                }
            }
        }
        return defaultInstance;
    }

    // helper
    private void CommonRequest(Events.CommonRequestEvent event,
                               Events.CommonCompleteEvent completeEvent,
                               String url)
    {
        String params = event.getParams();
        RequestBody body = RequestBody.create(JSON, params);
        Log.d(TAG, params);

        Request request = this.request()
                .url(url)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, result);

            JSONObject json = null;
            String code = null;
            try {

                json = new JSONObject(result);
                int intCode = json.getInt("code");

                completeEvent.setSuccess(intCode == Constants.SUCCESS_CODE);
                completeEvent.setMessage(json.getString("message"));

                if(completeEvent.getSuccess()){
                    if (!json.getString("result").equals("null") &&
                            !json.getString("result").equals("") ) {
                        try {
                            completeEvent.setResult(json.getJSONObject("result"));
                        } catch (Exception e) {
                            completeEvent.setArrResult(json.getJSONArray("result"));
                        }
                    }
                }

                EventBus.getDefault().post(completeEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Request.Builder request() {
        return new Request.Builder()
                .addHeader("Content-Type", "application/json;text=utf-8");
    }

    public void onEventBackgroundThread(Events.DoLoginEvent event){
        String username = event.getUsername();
        String password = event.getPassword();
        RequestBody body = RequestBody.create(JSON, "{\"username\": \""+ username +"\", \"password\": \""+ password +"\"}");
        Log.d(TAG, body.toString());

        Request request = this.request()
                .url(UrlLogin)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, result);


            JSONObject json = null;
            String code = null;
            try {

                json = new JSONObject(result);
                int intCode = json.getInt("code");
                Events.LoginCompleteEvent loginEvent = new Events.LoginCompleteEvent("faceted");
                loginEvent.setSuccess(intCode == Constants.SUCCESS_CODE);
                loginEvent.setMessage(json.getString("message"));

                //TODO 登录成功后做一些事情
                if(loginEvent.getSuccess()){
                    String res =json.getString("result");
                }

                EventBus.getDefault().post(loginEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onEventBackgroundThread(Events.RegisterEvent event){
        String params = event.getParams();
        RequestBody body = RequestBody.create(JSON, params);
        Log.d(TAG, body.toString());

        Request request = this.request()
                .url(UrlRegister)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, result);


            JSONObject json = null;
            String code = null;
            try {

                json = new JSONObject(result);
                int intCode = json.getInt("code");
                Events.CompleteEvent completeEvent = new Events.CompleteEvent(intCode == Constants.SUCCESS_CODE);
                completeEvent.setMessage(json.getString("message"));

                //TODO 注册成功后做一些事情
                if(completeEvent.getSuccess()){
                    String res =json.getString("result");
                }

                EventBus.getDefault().post(completeEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onEventBackgroundThread(Events.GoodsListEvent event){
        String params = event.getParams();
        RequestBody body = RequestBody.create(JSON, params);
        Log.d(TAG, params);

        Request request = this.request()
                .url(UrlGoodsList)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, result);

            JSONObject json = null;
            String code = null;
            try {

                json = new JSONObject(result);
                int intCode = json.getInt("code");
                Events.GoodsListCompleteEvent completeEvent = new Events.GoodsListCompleteEvent(intCode == Constants.SUCCESS_CODE);
                completeEvent.setMessage(json.getString("message"));

                if(completeEvent.getSuccess()){
                    Gson gson = new Gson();

                    String res = json.getString("result");
                    json = new JSONObject(res);

                    List<PosEntity> list = gson.fromJson(json.getString("list"), new TypeToken<List<PosEntity>>() {
                    }.getType());

                    completeEvent.setList(list);

                }

                EventBus.getDefault().post(completeEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onEventBackgroundThread(Events.GoodsDetailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.GoodsDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlGoodsDetail);
    }

    public void onEventBackgroundThread(Events.CreateOrderEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CreateOrderCompleteEvent();
        CommonRequest(event, completeEvent, UrlCreateOrder);
    }

    public void onEventBackgroundThread(Events.AddressListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AddressListCompleteEvent();
        CommonRequest(event, completeEvent, UrlEventList);
    }

    public void onEventBackgroundThread(Events.CreateAddressEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CreateAddressCompleteEvent();
        CommonRequest(event, completeEvent, UrlCreateAddress);
    }

    public void onEventBackgroundThread(Events.DeleteAddressEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.DeleteAddressCompleteEvent();
        CommonRequest(event, completeEvent, UrlDeleteAddress);
    }

    public void onEventBackgroundThread(Events.OrderListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.OrderListCompleteEvent();
        CommonRequest(event, completeEvent, UrlOrderList);
    }

    public void onEventBackgroundThread(Events.OrderDetailPigouEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.OrderDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlOrderDetailPigou);
    }

    public void onEventBackgroundThread(Events.OrderDetailDaigouEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.OrderDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlOrderDetailDaigou);
    }

    public void onEventBackgroundThread(Events.CancelOrderDaigouEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CancelOrderCompleteEvent();
        CommonRequest(event, completeEvent, UrlOrderCancelDaigou);
    }

    public void onEventBackgroundThread(Events.CancelOrderPigouEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CancelOrderCompleteEvent();
        CommonRequest(event, completeEvent, UrlOrderCancelPigou);
    }

    public void onEventBackgroundThread(Events.StockListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StockListCompleteEvent();
        CommonRequest(event, completeEvent, UrlStockList);
    }

    public void onEventBackgroundThread(Events.StockRenameEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StockRenameCompleteEvent();
        CommonRequest(event, completeEvent, UrlStockRename);
    }

    public void onEventBackgroundThread(Events.StockAgentListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StockAgentListCompleteEvent();
        CommonRequest(event, completeEvent, UrlStockAgentList);
    }

    public void onEventBackgroundThread(Events.StockAgentTerminalListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StockAgentTerminalCompleteEvent();
        CommonRequest(event, completeEvent, UrlStockAgentTerminalList);
    }



}
