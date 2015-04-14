package com.posagent.network;

import android.util.Log;

import com.example.zf_android.entity.PosEntity;
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
    public static final String UrlAddressList = BaseUrl + "/agents/getAddressList";
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
    public static final String UrlTerminalApplyList = BaseUrl + "/apply/getApplyList";
    public static final String UrlAfterSaleMaintainList = BaseUrl + "/cs/agents/search";
    public static final String UrlAfterSaleCancelList = BaseUrl + "/cs/cancels/search";
    public static final String UrlAfterSaleUpdateList = BaseUrl + "/update/info/search";
    public static final String UrlAfterSaleMaintainCancel = BaseUrl + "/cs/agents/cancelApply";
    public static final String UrlAfterSaleCancelCancel = BaseUrl + "/cs/cancels/cancelApply";
    public static final String UrlAfterSaleUpdateCancel = BaseUrl + "/update/info/cancelApply";
    public static final String UrlAfterSaleCancelResubmit = BaseUrl + "/cs/cancels/resubmitCancel";
    public static final String UrlUserList = BaseUrl + "/user/getUser";
    public static final String UrlUserDelete = BaseUrl + "/user/delectAgentUser";
    public static final String UrlUserTerminal = BaseUrl + "/user/getTerminals";


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

                //TODO 鐧诲綍鎴愬姛鍚庡仛涓�簺浜嬫儏
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

                //TODO 娉ㄥ唽鎴愬姛鍚庡仛涓�簺浜嬫儏
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
        CommonRequest(event, completeEvent, UrlAddressList);
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

    public void onEventBackgroundThread(Events.TerminalApplyListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalApplyListCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalApplyList);
    }

    public void onEventBackgroundThread(Events.AfterSaleMaintainListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleListCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleMaintainList);
    }

    public void onEventBackgroundThread(Events.AfterSaleCancelListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleListCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleCancelList);
    }

    public void onEventBackgroundThread(Events.AfterSaleUpdateListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleListCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleUpdateList);
    }

    public void onEventBackgroundThread(Events.AfterSaleMaintainCancelEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleCancelCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleUpdateCancel);
    }

    public void onEventBackgroundThread(Events.AfterSaleUpdateCancelEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleCancelCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleMaintainCancel);
    }

    public void onEventBackgroundThread(Events.AfterSaleCancelCancelEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleCancelCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleCancelCancel);
    }

    public void onEventBackgroundThread(Events.AfterSaleCancelResubmitEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AfterSaleResubmitCompleteEvent();
        CommonRequest(event, completeEvent, UrlAfterSaleCancelResubmit);
    }

    public void onEventBackgroundThread(Events.UserListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UserListCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserList);
    }

    public void onEventBackgroundThread(Events.UserDeleteEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UserDeleteCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserDelete);
    }
    public void onEventBackgroundThread(Events.UserTerminalEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UserTerminalCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserTerminal);
    }



}
