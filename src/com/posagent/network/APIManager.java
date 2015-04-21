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
//    public static final String BaseUrl = "http://114.215.149.242:28080/ZFAgent/api";
    public static final String BaseUrl = "http://121.40.84.2:28080/ZFAgent/api";

    public static final String UrlLogin = BaseUrl + "/agent/agentLogin";
    public static final String UrlRegister = BaseUrl + "/agent/userRegistration";
    public static final String UrlSendEmailVerificationCode = BaseUrl + "/agent/sendEmailVerificationCode";
    public static final String UrlSendPhoneVerificationCode = BaseUrl + "/agent/sendPhoneVerificationCode";
    public static final String UrlUpdatePassword = BaseUrl + "/agent/updatePassword";
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
    public static final String UrlApplyDetail = BaseUrl + "/apply/getApplyDetails";
    public static final String UrlAddOpeningApply = BaseUrl + "/apply/addOpeningApply";
    public static final String UrlAgentDetail = BaseUrl + "/apply/getMerchant";
    public static final String UrlApplyChannelList = BaseUrl + "/apply/getChannels";
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
    public static final String UrlMessageList = BaseUrl + "/message/receiver/getAll";
    public static final String UrlMessageMarkRead = BaseUrl + "/message/receiver/batchRead";
    public static final String UrlMessageDelete = BaseUrl + "/message/receiver/batchDelete";
    public static final String UrlTradeClient = BaseUrl + "/trade/record/getTerminals";
    public static final String UrlTradeAgent = BaseUrl + "/trade/record/getAgents";
    public static final String UrlTradeList = BaseUrl + "/trade/record/getTradeRecords";
    public static final String UrlTradeDetail = BaseUrl + "/trade/getTradeRecord";
    public static final String UrlTradeStatistic = BaseUrl + "/trade/getTradeStatistics";
    public static final String UrlTerminalList = BaseUrl + "/terminal/getTerminalList";
    public static final String UrlTerminalBind = BaseUrl + "/terminal/bindingTerminals";
    public static final String UrlVerifyCode = BaseUrl + "/terminal/sendPhoneVerificationCodeReg";
    public static final String UrlCreateUser = BaseUrl + "/terminal/addCustomer";
    public static final String UrlBatchTerminalNumber = BaseUrl + "/terminal/batchTerminalNum";
    public static final String UrlBatchTerminalNumberPos = BaseUrl + "/terminal/screeningTerminalNum";
    public static final String UrlTerminalChoosePosList = BaseUrl + "/terminal/screeningPosName";
    public static final String UrlTerminalChooseChannelList = BaseUrl + "/terminal/getChannels";
    public static final String UrlCreateAfterSale = BaseUrl + "/terminal/submitAgent";
    public static final String UrlTerminalDetail = BaseUrl + "/terminal/getApplyDetails";
    public static final String UrlUserInfo = BaseUrl + "/agents/getOne";
    public static final String UrlUserVerifyCode = BaseUrl + "/agents/getUpdatePhoneDentcode";
    public static final String UrlUserChangePhone = BaseUrl + "/agents/updatePhone";
    public static final String UrlUserChangeEmail = BaseUrl + "/agents/updateEmail";
    public static final String UrlUserChangePassword = BaseUrl + "/agents/updatePassword";
    public static final String UrlStaffList = BaseUrl + "/customerManage/getList";
    public static final String UrlStaffDelete = BaseUrl + "/customerManage/deleteAll";
    public static final String UrlStaffCreate = BaseUrl + "/customerManage/insert";
    public static final String UrlStaffEdit = BaseUrl + "/customerManage/edit";
    public static final String UrlStaffInfo = BaseUrl + "/customerManage/getInfo";

    //sonagent
    public static final String UrlSonAgentList = BaseUrl + "/lowerAgent/list";
    public static final String UrlSonAgentInfo = BaseUrl + "/lowerAgent/info";
    public static final String UrlSonAgentCreate = BaseUrl + "/lowerAgent/createNew";
    public static final String UrlChangeProfit = BaseUrl + "/lowerAgent/changeProfit";
    public static final String UrlProfitList = BaseUrl + "/lowerAgent/getProfitlist";
    public static final String UrlSetProfit = BaseUrl + "/lowerAgent/saveOrEdit";
    public static final String UrlDeleteProfit = BaseUrl + "/lowerAgent/delChannel";
    public static final String UrlChannelList = BaseUrl + "/lowerAgent/getChannellist";

    //prepare
    public static final String UrlPrepareList = BaseUrl + "/preparegood/list";
    public static final String UrlPrepareInfo = BaseUrl + "/preparegood/info";
    public static final String UrlPrepareAdd = BaseUrl + "/preparegood/add";

    //exchange
    public static final String UrlExchangeList = BaseUrl + "/exchangegood/list";
    public static final String UrlExchangeInfo = BaseUrl + "/exchangegood/info";
    public static final String UrlExchangeAdd = BaseUrl + "/exchangegood/add";


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
                            Log.d("UnCatchException1", e.getMessage());
                            try {
                                completeEvent.setArrResult(json.getJSONArray("result"));
                            } catch (Exception ex) {
                                Log.d("UnCatchException2", ex.getMessage());
                            }
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

    public void onEventBackgroundThread(Events.MessageListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.MessageListCompleteEvent();
        CommonRequest(event, completeEvent, UrlMessageList);
    }

    public void onEventBackgroundThread(Events.MessageDeleteEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.MessageDeleteCompleteEvent();
        CommonRequest(event, completeEvent, UrlMessageDelete);
    }

    public void onEventBackgroundThread(Events.MessageMarkReadEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.MessageMarkReadCompleteEvent();
        CommonRequest(event, completeEvent, UrlMessageMarkRead);
    }

    public void onEventBackgroundThread(Events.TradeClientEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TradeClientCompleteEvent();
        CommonRequest(event, completeEvent, UrlTradeClient);
    }

    public void onEventBackgroundThread(Events.TradeAgentEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TradeAgentCompleteEvent();
        CommonRequest(event, completeEvent, UrlTradeAgent);
    }

    public void onEventBackgroundThread(Events.TradeListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TradeListCompleteEvent();
        CommonRequest(event, completeEvent, UrlTradeList);
    }

    public void onEventBackgroundThread(Events.TradeDetailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TradeDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlTradeDetail);
    }

    public void onEventBackgroundThread(Events.TradeStatisticEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TradeStatisticCompleteEvent();
        CommonRequest(event, completeEvent, UrlTradeStatistic);
    }

    public void onEventBackgroundThread(Events.TerminalListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalListCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalList);
    }

    public void onEventBackgroundThread(Events.TerminalBindEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalBindCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalBind);
    }

    public void onEventBackgroundThread(Events.VerifyCodeEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.VerifyCodeCompleteEvent();
        CommonRequest(event, completeEvent, UrlVerifyCode);
    }


    public void onEventBackgroundThread(Events.CreateUserEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CreateUserCompleteEvent();
        CommonRequest(event, completeEvent, UrlCreateUser);
    }

    public void onEventBackgroundThread(Events.BatchTerminalNumberEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.BatchTerminalNumberCompleteEvent();
        CommonRequest(event, completeEvent, UrlBatchTerminalNumber);
    }


    public void onEventBackgroundThread(Events.TerminalChoosePosListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalChoosePosListCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalChoosePosList);
    }

    public void onEventBackgroundThread(Events.TerminalChooseChannelListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalChooseChannelListCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalChooseChannelList);
    }

    public void onEventBackgroundThread(Events.BatchTerminalNumberPosEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.BatchTerminalNumberPosCompleteEvent();
        CommonRequest(event, completeEvent, UrlBatchTerminalNumberPos);
    }

    public void onEventBackgroundThread(Events.CreateAfterSaleEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.CreateAfterSaleCompleteEvent();
        CommonRequest(event, completeEvent, UrlCreateAfterSale);
    }

    public void onEventBackgroundThread(Events.TerminalDetailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.TerminalDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlTerminalDetail);
    }

    public void onEventBackgroundThread(Events.UserInfoEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UserInfoCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserInfo);
    }

    public void onEventBackgroundThread(Events.UserVerifyCodeEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UserVerifyCodeCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserVerifyCode);
    }

    public void onEventBackgroundThread(Events.ChangePhoneEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ChangePhoneCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserChangePhone);
    }

    public void onEventBackgroundThread(Events.ChangeEmailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ChangeEmailCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserChangeEmail);
    }

    public void onEventBackgroundThread(Events.ChangePasswordEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ChangePasswordCompleteEvent();
        CommonRequest(event, completeEvent, UrlUserChangePassword);
    }

    public void onEventBackgroundThread(Events.StaffListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StaffListCompleteEvent();
        CommonRequest(event, completeEvent, UrlStaffList);
    }

    public void onEventBackgroundThread(Events.StaffDeleteEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StaffDeleteCompleteEvent();
        CommonRequest(event, completeEvent, UrlStaffDelete);
    }

    //staff
    public void onEventBackgroundThread(Events.StaffCreateEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StaffCreateCompleteEvent();
        CommonRequest(event, completeEvent, UrlStaffCreate);
    }

    public void onEventBackgroundThread(Events.StaffEditEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StaffEditCompleteEvent();
        CommonRequest(event, completeEvent, UrlStaffEdit);
    }

    public void onEventBackgroundThread(Events.StaffInfoEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.StaffInfoCompleteEvent();
        CommonRequest(event, completeEvent, UrlStaffInfo);
    }

    //Son Agent
    public void onEventBackgroundThread(Events.SonAgentListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SonAgentListCompleteEvent();
        CommonRequest(event, completeEvent, UrlSonAgentList);
    }


    public void onEventBackgroundThread(Events.SonAgentInfoEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SonAgentInfoCompleteEvent();
        CommonRequest(event, completeEvent, UrlSonAgentInfo);
    }

    public void onEventBackgroundThread(Events.SonAgentCreateEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SonAgentCreateCompleteEvent();
        CommonRequest(event, completeEvent, UrlSonAgentCreate);
    }

    public void onEventBackgroundThread(Events.ChangeProfitEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ChangeProfitCompleteEvent();
        CommonRequest(event, completeEvent, UrlChangeProfit);
    }

    public void onEventBackgroundThread(Events.ProfitListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ProfitListCompleteEvent();
        CommonRequest(event, completeEvent, UrlProfitList);
    }

    public void onEventBackgroundThread(Events.SetProfitEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SetProfitCompleteEvent();
        CommonRequest(event, completeEvent, UrlSetProfit);
    }

    public void onEventBackgroundThread(Events.DeleteProfitEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.DeleteProfitCompleteEvent();
        CommonRequest(event, completeEvent, UrlDeleteProfit);
    }

    public void onEventBackgroundThread(Events.ChannelListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ChannelListCompleteEvent();
        CommonRequest(event, completeEvent, UrlChannelList);
    }

    //配货
    public void onEventBackgroundThread(Events.PrepareListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.PrepareListCompleteEvent();
        CommonRequest(event, completeEvent, UrlPrepareList);
    }

    public void onEventBackgroundThread(Events.PrepareInfoEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.PrepareInfoCompleteEvent();
        CommonRequest(event, completeEvent, UrlPrepareInfo);
    }

    public void onEventBackgroundThread(Events.PrepareAddEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.PrepareAddCompleteEvent();
        CommonRequest(event, completeEvent, UrlPrepareAdd);
    }

    //调货
    public void onEventBackgroundThread(Events.ExchangeListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ExchangeListCompleteEvent();
        CommonRequest(event, completeEvent, UrlExchangeList);
    }

    public void onEventBackgroundThread(Events.ExchangeInfoEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ExchangeInfoCompleteEvent();
        CommonRequest(event, completeEvent, UrlExchangeInfo);
    }

    public void onEventBackgroundThread(Events.ExchangeAddEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ExchangeAddCompleteEvent();
        CommonRequest(event, completeEvent, UrlExchangeAdd);
    }

    // find password
    public void onEventBackgroundThread(Events.SendEmailVerificationCodeEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SendEmailVerificationCodeCompleteEvent();
        CommonRequest(event, completeEvent, UrlSendEmailVerificationCode);
    }

    public void onEventBackgroundThread(Events.SendPhoneVerificationCodeEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.SendPhoneVerificationCodeCompleteEvent();
        CommonRequest(event, completeEvent, UrlSendPhoneVerificationCode);
    }

    public void onEventBackgroundThread(Events.UpdatePasswordEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.UpdatePasswordCompleteEvent();
        CommonRequest(event, completeEvent, UrlSendPhoneVerificationCode);
    }

    // terminal apply
    public void onEventBackgroundThread(Events.ApplyDetailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ApplyDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlApplyDetail);
    }

    public void onEventBackgroundThread(Events.AgentDetailEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AgentDetailCompleteEvent();
        CommonRequest(event, completeEvent, UrlAgentDetail);
    }

    public void onEventBackgroundThread(Events.ApplyChannelListEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.ApplyChannelListCompleteEvent();
        CommonRequest(event, completeEvent, UrlApplyChannelList);
    }

    public void onEventBackgroundThread(Events.AddOpeningApplyEvent event) {
        Events.CommonCompleteEvent completeEvent = new Events.AddOpeningApplyCompleteEvent();
        CommonRequest(event, completeEvent, UrlAddOpeningApply);
    }




}
