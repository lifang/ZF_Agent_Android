package com.posagent.events;

import com.example.zf_android.entity.AgentTerminalEntity;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.entity.PosEntity;
import com.example.zf_android.entity.StockAgentEntity;
import com.example.zf_android.entity.StockEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holin on 4/3/15.
 */
public class Events {
    public static class CompleteEvent {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private Boolean success;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public CompleteEvent() {
        }
        public CompleteEvent(Boolean _success) {
            success = _success;
        }
    }

    public static class CommonCompleteEvent extends CompleteEvent {
        private JSONObject result;
        private JSONArray arrResult;

        public JSONArray getArrResult() {
            return arrResult;
        }

        public void setArrResult(JSONArray arrResult) {
            this.arrResult = arrResult;
        }

        public JSONObject getResult() {
            return result;
        }

        public void setResult(JSONObject result) {
            this.result = result;
        }
    }

    public static class CommonRequestEvent {
        private String params;

        public void setParams(String params) {
            this.params = params;
        }

        public String getParams() {
            return params;
        }

        public CommonRequestEvent() {
        }

        public CommonRequestEvent(String _params) {
            params = _params;
        }
    }

    public static class NoConnectEvent {
        public NoConnectEvent() {}
    }
    public static class RefreshToMuch {
        public RefreshToMuch() {}
    }

    public static class DoLoginEvent {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public String getPassword() { return password; }

        public DoLoginEvent(String _username, String _password) {
            username = _username;
            password = _password;
        }
    }

    public static class LoginCompleteEvent {
        private String token;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private Boolean success;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getToken() { return token; }

        public LoginCompleteEvent(String _token) {
            token = _token;
        }
    }


    public static class RegisterEvent {
        private String params;

        public String getParams() {
            return params;
        }

        public RegisterEvent(String _params) {
            params = _params;
        }
    }

    public static class GoodsListEvent {
        private String params;

        public String getParams() {
            return params;
        }

        public GoodsListEvent(String _params) {
            params = _params;
        }
    }

    public static class GoodsListCompleteEvent extends CompleteEvent {
        private List<PosEntity> list = new ArrayList<PosEntity>();

        public List<PosEntity> getList() {
            return list;
        }

        public void setList(List<PosEntity> list) {
            this.list = list;
        }

        public GoodsListCompleteEvent(Boolean _success) {
            super(_success);
        }
    }

    public static class GoodsDetailEvent extends CommonRequestEvent {}
    public static class GoodsDetailCompleteEvent extends CommonCompleteEvent {}

    public static class CreateOrderEvent extends CommonRequestEvent {}
    public static class CreateOrderCompleteEvent extends CommonCompleteEvent {}

    public static class AddressListEvent extends CommonRequestEvent {}
    public static class AddressListReloadEvent extends CommonRequestEvent {}
    public static class AddressListCompleteEvent extends CommonCompleteEvent {}

    public static class CreateAddressEvent extends CommonRequestEvent {}
    public static class CreateAddressCompleteEvent extends CommonCompleteEvent {}

    public static class DeleteAddressEvent extends CommonRequestEvent {}
    public static class DeleteAddressCompleteEvent extends CommonCompleteEvent {}


    //order
    public static class OrderListEvent extends CommonRequestEvent {}
    public static class OrderListCompleteEvent extends CommonCompleteEvent {
        private List<OrderEntity> list = new ArrayList<OrderEntity>();
        public List<OrderEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<OrderEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<OrderEntity> list) {
            this.list = list;
        }
    }

    public static class OrderDetailPigouEvent extends CommonRequestEvent {}
    public static class OrderDetailDaigouEvent extends CommonRequestEvent {}
    public static class OrderDetailCompleteEvent extends CommonCompleteEvent {}

    public static class CancelOrderPigouEvent extends CommonRequestEvent {}
    public static class CancelOrderDaigouEvent extends CommonRequestEvent {}
    public static class CancelOrderCompleteEvent extends CommonCompleteEvent {}


    //stock
    public static class StockListEvent extends CommonRequestEvent {}
    public static class StockListCompleteEvent extends CommonCompleteEvent {
        private List<StockEntity> list = new ArrayList<StockEntity>();
        public List<StockEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<StockEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<StockEntity> list) {
            this.list = list;
        }
    }

    public static class StockRenameEvent extends CommonRequestEvent {}
    public static class StockRenameCompleteEvent extends CommonCompleteEvent {}

    public static class StockAgentListEvent extends CommonRequestEvent {}
    public static class StockAgentListCompleteEvent extends CommonCompleteEvent {
        private List<StockAgentEntity> list = new ArrayList<StockAgentEntity>();
        public List<StockAgentEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<StockAgentEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<StockAgentEntity> list) {
            this.list = list;
        }
    }

    public static class StockAgentTerminalListEvent extends CommonRequestEvent {}
    public static class StockAgentTerminalCompleteEvent extends CommonCompleteEvent {
        private List<AgentTerminalEntity> list = new ArrayList<AgentTerminalEntity>();
        public List<AgentTerminalEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<AgentTerminalEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<AgentTerminalEntity> list) {
            this.list = list;
        }
    }
}
