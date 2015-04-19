package com.posagent.events;

import com.example.zf_android.entity.AdressEntity;
import com.example.zf_android.entity.AgentTerminalEntity;
import com.example.zf_android.entity.ChanelEntitiy;
import com.example.zf_android.entity.MessageEntity;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.entity.PosEntity;
import com.example.zf_android.entity.SonAgent;
import com.example.zf_android.entity.SonAgentInfo;
import com.example.zf_android.entity.StaffEntity;
import com.example.zf_android.entity.StockAgentEntity;
import com.example.zf_android.entity.StockEntity;
import com.example.zf_android.entity.TerminalApplyEntity;
import com.example.zf_android.entity.TerminalChoosePosItem;
import com.example.zf_android.entity.User;
import com.example.zf_android.entity.UserTerminal;
import com.example.zf_android.trade.entity.AfterSaleRecord;
import com.example.zf_android.trade.entity.TerminalDetail;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_android.trade.entity.TradeAgent;
import com.example.zf_android.trade.entity.TradeClient;
import com.example.zf_android.trade.entity.TradeRecord;
import com.example.zf_android.trade.entity.UserInfo;
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
        public Boolean success() {
            return getSuccess();
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
    public static class AddressListCompleteEvent extends CommonCompleteEvent {
        private List<AdressEntity> list = new ArrayList<AdressEntity>();
        public List<AdressEntity> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<AdressEntity>>() {}.getType());

            }
            return list;
        }
        public void setList(List<AdressEntity> list) {
            this.list = list;
        }
    }

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

    // terminal
    public static class TerminalApplyListEvent extends CommonRequestEvent {}
    public static class TerminalApplyListCompleteEvent extends CommonCompleteEvent {
        private List<TerminalApplyEntity> list = new ArrayList<TerminalApplyEntity>();
        public List<TerminalApplyEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("applyList");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<TerminalApplyEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<TerminalApplyEntity> list) {
            this.list = list;
        }
    }

    //after sale
    public static class AfterSaleMaintainListEvent extends CommonRequestEvent {}
    public static class AfterSaleCancelListEvent extends CommonRequestEvent {}
    public static class AfterSaleUpdateListEvent extends CommonRequestEvent {}
    public static class AfterSaleListCompleteEvent extends CommonCompleteEvent {
        private List<AfterSaleRecord> list = new ArrayList<AfterSaleRecord>();
        public List<AfterSaleRecord> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<AfterSaleRecord>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<AfterSaleRecord> list) {
            this.list = list;
        }
    }

    public static class AfterSaleMaintainCancelEvent extends CommonRequestEvent {}
    public static class AfterSaleCancelCancelEvent extends CommonRequestEvent {}
    public static class AfterSaleUpdateCancelEvent extends CommonRequestEvent {}
    public static class AfterSaleCancelCompleteEvent extends CommonCompleteEvent {}

    public static class AfterSaleCancelResubmitEvent extends CommonRequestEvent {}
    public static class AfterSaleResubmitCompleteEvent extends CommonCompleteEvent {}

    //users
    public static class UserListEvent extends CommonRequestEvent {}
    public static class UserListCompleteEvent extends CommonCompleteEvent {
        private List<User> list = new ArrayList<User>();
        public List<User> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<User>>() {}.getType());

            }
            return list;
        }
        public void setList(List<User> list) {
            this.list = list;
        }
    }

    public static class UserDeleteEvent extends CommonRequestEvent {}
    public static class UserDeleteCompleteEvent extends CommonCompleteEvent {}
    public static class UserTerminalEvent extends CommonRequestEvent {}
    public static class UserTerminalCompleteEvent extends CommonCompleteEvent {
        private List<UserTerminal> list = new ArrayList<UserTerminal>();
        public List<UserTerminal> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<UserTerminal>>() {}.getType());

            }
            return list;
        }
        public void setList(List<UserTerminal> list) {
            this.list = list;
        }
    }



    public static class MessageListEvent extends CommonRequestEvent {}
    public static class MessageListCompleteEvent extends CommonCompleteEvent {
        private List<MessageEntity> list = new ArrayList<MessageEntity>();
        public List<MessageEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                        new TypeToken<List<MessageEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<MessageEntity> list) {
            this.list = list;
        }
    }

    public static class MessageDeleteEvent extends CommonRequestEvent {}
    public static class MessageDeleteCompleteEvent extends CommonCompleteEvent {}

    public static class MessageMarkReadEvent extends CommonRequestEvent {}
    public static class MessageMarkReadCompleteEvent extends CommonCompleteEvent {}


    //trade
    public static class TradeClientEvent extends CommonRequestEvent {}
    public static class TradeClientCompleteEvent extends CommonCompleteEvent {
        private List<TradeClient> list = new ArrayList<TradeClient>();
        public List<TradeClient> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TradeClient>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TradeClient> list) {
            this.list = list;
        }
    }


    public static class TradeAgentEvent extends CommonRequestEvent {}
    public static class TradeAgentCompleteEvent extends CommonCompleteEvent {
        private List<TradeAgent> list = new ArrayList<TradeAgent>();
        public List<TradeAgent> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TradeAgent>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TradeAgent> list) {
            this.list = list;
        }
    }

    public static class TradeListEvent extends CommonRequestEvent {}
    public static class TradeListCompleteEvent extends CommonCompleteEvent {
        private List<TradeRecord> list = new ArrayList<TradeRecord>();
        public List<TradeRecord> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<TradeRecord>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<TradeRecord> list) {
            this.list = list;
        }
    }

    // Terminal
    public static class TerminalListEvent extends CommonRequestEvent {}
    public static class TerminalListCompleteEvent extends CommonCompleteEvent {
        private List<TerminalItem> list = new ArrayList<TerminalItem>();
        public List<TerminalItem> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("applyList");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<TerminalItem>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<TerminalItem> list) {
            this.list = list;
        }
    }


    public static class TerminalBindEvent extends CommonRequestEvent {}
    public static class TerminalBindCompleteEvent extends CommonCompleteEvent{}

    public static class VerifyCodeEvent extends CommonRequestEvent {}
    public static class VerifyCodeCompleteEvent extends CommonCompleteEvent{}

    public static class CreateUserEvent extends CommonRequestEvent {}
    public static class CreateUserCompleteEvent extends CommonCompleteEvent{}
    public static class UserListReloadEvent extends CommonCompleteEvent{}


    public static class BatchTerminalNumberEvent extends CommonRequestEvent {}
    public static class BatchTerminalNumberCompleteEvent extends CommonCompleteEvent {
        private List<TerminalItem> list = new ArrayList<TerminalItem>();
        public List<TerminalItem> getList() {
            if (list.size() < 1) {
                String result = null;
                result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TerminalItem>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TerminalItem> list) {
            this.list = list;
        }
    }

    public static class TerminalChooseFinishEvent {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public TerminalChooseFinishEvent(List<String> _list) {
            list = _list;
        }
    }

    public static class TerminalChoosePosListEvent extends CommonRequestEvent {}
    public static class TerminalChoosePosListCompleteEvent extends CommonCompleteEvent {
        private List<TerminalChoosePosItem> list = new ArrayList<TerminalChoosePosItem>();
        public List<TerminalChoosePosItem> getList() {
            if (list.size() < 1) {
                String result = null;
                result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TerminalChoosePosItem>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TerminalChoosePosItem> list) {
            this.list = list;
        }
    }

    public static class TerminalChooseChannelListEvent extends CommonRequestEvent {}
    public static class TerminalChooseChannelListCompleteEvent extends CommonCompleteEvent {
        private List<ChanelEntitiy> list = new ArrayList<ChanelEntitiy>();
        public List<ChanelEntitiy> getList() {
            if (list.size() < 1) {
                String result = null;
                result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<ChanelEntitiy>>() {}.getType());

            }
            return list;
        }
        public void setList(List<ChanelEntitiy> list) {
            this.list = list;
        }
    }

    public static class BatchTerminalNumberPosEvent extends CommonRequestEvent {}
    public static class BatchTerminalNumberPosCompleteEvent extends CommonCompleteEvent {
        private List<TerminalItem> list = new ArrayList<TerminalItem>();
        public List<TerminalItem> getList() {
            if (list.size() < 1) {
                String result = null;
                result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TerminalItem>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TerminalItem> list) {
            this.list = list;
        }
    }

    public static class CreateAfterSaleEvent extends CommonRequestEvent {}
    public static class CreateAfterSaleCompleteEvent extends CommonCompleteEvent{}

    public static class TerminalDetailEvent extends CommonRequestEvent {}
    public static class TerminalDetailCompleteEvent extends CommonCompleteEvent {
        private TerminalDetail entity;

        public TerminalDetail getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, TerminalDetail.class);
            return entity;
        }

        public void setEntity(TerminalDetail entity) {
            this.entity = entity;
        }

    }

    //我的
    public static class UserInfoEvent extends CommonRequestEvent {}
    public static class UserInfoCompleteEvent extends CommonCompleteEvent {
        private UserInfo entity;

        public UserInfo getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, UserInfo.class);
            return entity;
        }

        public void setEntity(UserInfo entity) {
            this.entity = entity;
        }

    }

    public static class UserVerifyCodeEvent extends CommonRequestEvent {}
    public static class UserVerifyCodeCompleteEvent extends CommonCompleteEvent{}

    public static class ChangePhoneEvent extends CommonRequestEvent {}
    public static class ChangePhoneCompleteEvent extends CommonCompleteEvent{}

    public static class ChangeEmailEvent extends CommonRequestEvent {}
    public static class ChangeEmailCompleteEvent extends CommonCompleteEvent{}

    public static class ChangePasswordEvent extends CommonRequestEvent {}
    public static class ChangePasswordCompleteEvent extends CommonCompleteEvent{}

    public static class UserInfoReloadEvent extends CommonRequestEvent{}

    public static class StaffListEvent extends CommonRequestEvent {}
    public static class StaffListCompleteEvent extends CommonCompleteEvent {
        private List<StaffEntity> list = new ArrayList<StaffEntity>();
        public List<StaffEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<StaffEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<StaffEntity> list) {
            this.list = list;
        }
    }

    public static class StaffDeleteEvent extends CommonRequestEvent {}
    public static class StaffDeleteCompleteEvent extends CommonCompleteEvent {}

    public static class StaffCreateEvent extends CommonRequestEvent {}
    public static class StaffCreateCompleteEvent extends CommonCompleteEvent {}

    public static class StaffEditEvent extends CommonRequestEvent {}
    public static class StaffEditCompleteEvent extends CommonCompleteEvent {}

    public static class StaffInfoEvent extends CommonRequestEvent {}
    public static class StaffInfoCompleteEvent extends CommonCompleteEvent {
        private StaffEntity entity;

        public StaffEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, StaffEntity.class);
            return entity;
        }

        public void setEntity(StaffEntity entity) {
            this.entity = entity;
        }
    }

    public static class StaffListReloadEvent extends CommonRequestEvent{}


    //son agents
    public static class SonAgentListEvent extends CommonRequestEvent {}
    public static class SonAgentListCompleteEvent extends CommonCompleteEvent  {
        private List<SonAgent> list = new ArrayList<SonAgent>();
        public List<SonAgent> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<SonAgent>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<SonAgent> list) {
            this.list = list;
        }
    }


    public static class SonAgentListReloadEvent extends CommonRequestEvent {}

    public static class SonAgentInfoEvent extends CommonRequestEvent {}
    public static class SonAgentInfoCompleteEvent extends CommonCompleteEvent {
        private SonAgentInfo entity;

        public SonAgentInfo getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, SonAgentInfo.class);
            return entity;
        }

        public void setEntity(SonAgentInfo entity) {
            this.entity = entity;
        }
    }

    public static class SonAgentCreateEvent extends CommonRequestEvent {}
    public static class SonAgentCreateCompleteEvent extends CommonCompleteEvent {}

    public static class ChangeProfitEvent extends CommonRequestEvent {}
    public static class ChangeProfitCompleteEvent extends CommonCompleteEvent {}

    public static class ProfitListEvent extends CommonRequestEvent {}
    public static class ProfitListCompleteEvent extends CommonCompleteEvent {}

    public static class SetProfitEvent extends CommonRequestEvent {}
    public static class SetProfitCompleteEvent extends CommonCompleteEvent {}

    public static class DeleteProfitEvent extends CommonRequestEvent {}
    public static class DeleteProfitCompleteEvent extends CommonCompleteEvent {}

    public static class ChannelListEvent extends CommonRequestEvent {}
    public static class ChannelListCompleteEvent extends CommonCompleteEvent {}

}
