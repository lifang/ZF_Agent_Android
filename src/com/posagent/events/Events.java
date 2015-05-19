package com.posagent.events;

import com.example.zf_android.entity.AdressEntity;
import com.example.zf_android.entity.AfterSaleCancel;
import com.example.zf_android.entity.AfterSaleMaintain;
import com.example.zf_android.entity.AfterSaleUpdate;
import com.example.zf_android.entity.AgentTerminalEntity;
import com.example.zf_android.entity.BankEntity;
import com.example.zf_android.entity.ChanelEntitiy;
import com.example.zf_android.entity.ChannelEntity;
import com.example.zf_android.entity.ChannelTradeEntity;
import com.example.zf_android.entity.ExchangeEntity;
import com.example.zf_android.entity.GoodCommentEntity;
import com.example.zf_android.entity.GoodsEntity;
import com.example.zf_android.entity.GoodsPictureEntity;
import com.example.zf_android.entity.GoodsSearchEntity;
import com.example.zf_android.entity.MerchantEntity;
import com.example.zf_android.entity.MessageEntity;
import com.example.zf_android.entity.OrderEntity;
import com.example.zf_android.entity.PayChannelInfoEntity;
import com.example.zf_android.entity.PayOrderInfo;
import com.example.zf_android.entity.PicEntity;
import com.example.zf_android.entity.PosEntity;
import com.example.zf_android.entity.PrepareEntity;
import com.example.zf_android.entity.ProfitEntity;
import com.example.zf_android.entity.ShopPayOrderInfo;
import com.example.zf_android.entity.SonAgent;
import com.example.zf_android.entity.SonAgentInfo;
import com.example.zf_android.entity.StaffEntity;
import com.example.zf_android.entity.StockAgentEntity;
import com.example.zf_android.entity.StockEntity;
import com.example.zf_android.entity.TerminalApplyEntity;
import com.example.zf_android.entity.TerminalChoosePosItem;
import com.example.zf_android.entity.User;
import com.example.zf_android.entity.UserInfoEntity;
import com.example.zf_android.entity.UserTerminal;
import com.example.zf_android.trade.entity.AfterSaleRecord;
import com.example.zf_android.trade.entity.ApplyDetail;
import com.example.zf_android.trade.entity.PrepareAgent;
import com.example.zf_android.trade.entity.TerminalDetail;
import com.example.zf_android.trade.entity.TerminalItem;
import com.example.zf_android.trade.entity.TradeAgent;
import com.example.zf_android.trade.entity.TradeClient;
import com.example.zf_android.trade.entity.TradeDetail;
import com.example.zf_android.trade.entity.TradeRecord;
import com.example.zf_android.trade.entity.TradeStatistic;
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
        private int intResult;
        private String strResult;

        public String getStrResult() {
            return strResult;
        }

        public void setStrResult(String strResult) {
            this.strResult = strResult;
        }

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

        public int getIntResult() {
            return intResult;
        }

        public void setIntResult(int intResult) {
            this.intResult = intResult;
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

    public static class NetworkLoading {
        public NetworkLoading() {}
    }
    public static class NoConnectEvent {
        public NoConnectEvent() {}
    }
    public static class RefreshToMuch {
        public RefreshToMuch() {}
    }
    public static class ServerError {
    }

    public static class DoLoginEvent extends CommonRequestEvent {
    }

    public static class LoginCompleteEvent extends CommonCompleteEvent {
        private UserInfoEntity entity;

        public UserInfoEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, UserInfoEntity.class);
            return entity;
        }

        public void setEntity(UserInfoEntity entity) {
            this.entity = entity;
        }
    }


    public static class RegisterEvent extends CommonRequestEvent {}
    public static class RegisterCompleteEvent extends CommonCompleteEvent {}

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

    public static class GoodsSearchItemEvent extends CommonRequestEvent {}
    public static class GoodsSearchItemCompleteEvent extends CommonCompleteEvent {
        private GoodsSearchEntity entity;

        public GoodsSearchEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, GoodsSearchEntity.class);
            return entity;
        }

        public void setEntity(GoodsSearchEntity entity) {
            this.entity = entity;
        }
    }

    public static class GoodsDoSearchCompleteEvent extends CompleteEvent {
        private String keys;

        public String getKeys() {
            return keys;
        }

        public void setKeys(String keys) {
            this.keys = keys;
        }
    }

    public static class GoodsDetailEvent extends CommonRequestEvent {}
    public static class GoodsDetailCompleteEvent extends CommonCompleteEvent {
        private GoodsEntity entity;

        public GoodsEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, GoodsEntity.class);
            return entity;
        }

        public void setEntity(GoodsEntity entity) {
            this.entity = entity;
        }
    }

    public static class PayChannelInfoEvent extends CommonRequestEvent {}
    public static class PayChannelInfoCompleteEvent extends CommonCompleteEvent {
        private PayChannelInfoEntity entity;

        public PayChannelInfoEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, PayChannelInfoEntity.class);
            return entity;
        }

        public void setEntity(PayChannelInfoEntity entity) {
            this.entity = entity;
        }
    }

    public static class GoodsCommentListEvent extends CommonRequestEvent {}
    public static class GoodsCommentListCompleteEvent extends CommonCompleteEvent {
        private List<GoodCommentEntity> list = new ArrayList<GoodCommentEntity>();
        public List<GoodCommentEntity> getList() {
            if (list.size() < 1) {
                try {
                    String result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<GoodCommentEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<GoodCommentEntity> list) {
            this.list = list;
        }
    }

    public static class GoodsPictureListEvent extends CommonRequestEvent {}
    public static class GoodsPictureListCompleteEvent extends CommonCompleteEvent {
        private List<GoodsPictureEntity> list = new ArrayList<GoodsPictureEntity>();
        public List<GoodsPictureEntity> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<GoodsPictureEntity>>() {}.getType());
            }
            return list;
        }
        public void setList(List<GoodsPictureEntity> list) {
            this.list = list;
        }
    }

    // find password
    public static class SendEmailVerificationCodeEvent extends CommonRequestEvent {}
    public static class SendEmailVerificationCodeCompleteEvent extends CommonCompleteEvent {}
    public static class SendPhoneVerificationCodeEvent extends CommonRequestEvent {}
    public static class SendPhoneVerificationCodeCompleteEvent extends CommonCompleteEvent {}
    public static class UpdatePasswordEvent extends CommonRequestEvent {}
    public static class UpdatePasswordCompleteEvent extends CommonCompleteEvent {}


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
    public static class UpdateAddressEvent extends CommonRequestEvent {}
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

    public static class PayOrderInfoEvent extends CommonRequestEvent {}
    public static class PayOrderInfoCompleteEvent extends CommonCompleteEvent {
        private PayOrderInfo entity;

        public PayOrderInfo getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, PayOrderInfo.class);
            return entity;
        }

        public void setEntity(PayOrderInfo entity) {
            this.entity = entity;
        }

    }

    public static class ShopPayOrderInfoEvent extends CommonRequestEvent {}
    public static class ShopPayOrderInfoCompleteEvent extends CommonCompleteEvent {
        private ShopPayOrderInfo entity;

        public ShopPayOrderInfo getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, ShopPayOrderInfo.class);
            return entity;
        }

        public void setEntity(ShopPayOrderInfo entity) {
            this.entity = entity;
        }

    }


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

    // terminal apply
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


    public static class ApplyChannelListEvent extends CommonRequestEvent {}
    public static class ApplyChannelListCompleteEvent extends CommonCompleteEvent {
        private List<ChannelEntity> list = new ArrayList<ChannelEntity>();
        public List<ChannelEntity> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<ChannelEntity>>() {}.getType());

            }
            return list;
        }
        public void setList(List<ChannelEntity> list) {
            this.list = list;
        }
    }

    public static class MerchantListEvent extends CommonRequestEvent {}
    public static class MerchantListCompleteEvent extends CommonCompleteEvent {
        private List<MerchantEntity> list = new ArrayList<MerchantEntity>();
        public List<MerchantEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("merchaneList");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<MerchantEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<MerchantEntity> list) {
            this.list = list;
        }
    }

    public static class BankListEvent extends CommonRequestEvent {}
    public static class BankListCompleteEvent extends CommonCompleteEvent {
        private List<BankEntity> list = new ArrayList<BankEntity>();
        public List<BankEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("content");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<BankEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<BankEntity> list) {
            this.list = list;
        }
    }

    public static class ApplyDetailEvent extends CommonRequestEvent {}
    public static class ApplyDetailCompleteEvent extends CommonCompleteEvent {
        private ApplyDetail entity;

        public ApplyDetail getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, ApplyDetail.class);
            return entity;
        }

        public void setEntity(ApplyDetail entity) {
            this.entity = entity;
        }
    }

    public static class MerchantDetailEvent extends CommonRequestEvent {}
    public static class MerchantDetailCompleteEvent extends CommonCompleteEvent {
        private MerchantEntity entity;

        public MerchantEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, MerchantEntity.class);
            return entity;
        }

        public void setEntity(MerchantEntity entity) {
            this.entity = entity;
        }
    }

    public static class AddOpeningApplyEvent extends CommonRequestEvent {}
    public static class AddOpeningApplyCompleteEvent extends CommonCompleteEvent {}


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

    public static class AfterSaleDetailMaintainEvent extends CommonRequestEvent {}
    public static class AfterSaleDetailMaintainCompleteEvent extends CommonCompleteEvent {
        private AfterSaleMaintain entity;

        public AfterSaleMaintain getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, AfterSaleMaintain.class);
            return entity;
        }

        public void setEntity(AfterSaleMaintain entity) {
            this.entity = entity;
        }
    }

    public static class AfterSaleDetailUpdateEvent extends CommonRequestEvent {}
    public static class AfterSaleDetailUpdateCompleteEvent extends CommonCompleteEvent {
        private AfterSaleUpdate entity;

        public AfterSaleUpdate getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, AfterSaleUpdate.class);
            return entity;
        }

        public void setEntity(AfterSaleUpdate entity) {
            this.entity = entity;
        }
    }
    public static class AfterSaleDetailCancelEvent extends CommonRequestEvent {}
    public static class AfterSaleDetailCancelCompleteEvent extends CommonCompleteEvent {
        private AfterSaleCancel entity;

        public AfterSaleCancel getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, AfterSaleCancel.class);
            return entity;
        }

        public void setEntity(AfterSaleCancel entity) {
            this.entity = entity;
        }
    }

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

    public static class MessageDetailEvent extends CommonRequestEvent {}
    public static class MessageDetailCompleteEvent extends CommonCompleteEvent {
        private MessageEntity entity;

        public MessageEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, MessageEntity.class);
            return entity;
        }

        public void setEntity(MessageEntity entity) {
            this.entity = entity;
        }
    }

    public static class GoToMessageDetailEvent extends CommonRequestEvent {
        String msgId;

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
    }


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

    // trade
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

    public static class TradeDetailEvent extends CommonRequestEvent {}
    public static class TradeDetailCompleteEvent extends CommonCompleteEvent {
        private TradeDetail entity;

        public TradeDetail getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, TradeDetail.class);
            return entity;
        }

        public void setEntity(TradeDetail entity) {
            this.entity = entity;
        }
    }


    public static class TradeStatisticEvent extends CommonRequestEvent {}
    public static class TradeStatisticCompleteEvent extends CommonCompleteEvent {
        private List<TradeStatistic> list = new ArrayList<TradeStatistic>();
        public List<TradeStatistic> getList() {
            if (list.size() < 1) {
                String result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<TradeStatistic>>() {}.getType());

            }
            return list;
        }
        public void setList(List<TradeStatistic> list) {
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

    // terminal actions
    public static class FindPosPasswordEvent extends CommonRequestEvent {}
    public static class FindPosPasswordCompleteEvent extends CommonCompleteEvent{}

    public static class TerminalSyncEvent extends CommonRequestEvent {}
    public static class TerminalSyncCompleteEvent extends CommonCompleteEvent{}

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

    public static class SearchAgentTerminalListEvent extends CommonRequestEvent {}
    public static class SearchAgentTerminalListCompleteEvent extends CommonCompleteEvent {
        private List<TerminalItem> list = new ArrayList<TerminalItem>();
        public List<TerminalItem> getList() {
            if (list.size() < 1) {
                String result = null;

                try {
                    result = getResult().getString("list");
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

    public static class TerminalChoosePosAgentListEvent extends CommonRequestEvent {}
    public static class TerminalChoosePosAgentListCompleteEvent extends CommonCompleteEvent {
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

    public static class PrepareGoodsChannelListEvent extends CommonRequestEvent {}
    public static class PrepareGoodsChannelListCompleteEvent extends CommonCompleteEvent {
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

    public static class PrepareAgentListEvent extends CommonRequestEvent {}
    public static class PrepareAgentListCompleteEvent extends CommonCompleteEvent {
        private List<PrepareAgent> list = new ArrayList<PrepareAgent>();
        public List<PrepareAgent> getList() {
            if (list.size() < 1) {
                String result = null;
                result = getArrResult().toString();
                list = (new Gson()).fromJson(result,
                        new TypeToken<List<PrepareAgent>>() {}.getType());

            }
            return list;
        }
        public void setList(List<PrepareAgent> list) {
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

    public static class UpdateEmailDentcodeEvent extends CommonRequestEvent {}
    public static class UpdateEmailDentcodeCompleteEvent extends CommonCompleteEvent{}


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

    public static class SetIsProfitEvent extends CommonRequestEvent {}
    public static class SetIsProfitCompleteEvent extends CommonCompleteEvent {}

    public static class GetDefaultProfitEvent extends CommonRequestEvent {}
    public static class GetDefaultProfitCompleteEvent extends CommonCompleteEvent {
        private String defautProfit;

        public String getDefautProfit() {
            String result = "" + getIntResult();
            return result;
        }

        public void setDefautProfit(String defautProfit) {
            this.defautProfit = defautProfit;
        }
    }

    public static class ChannelTradeListEvent extends CommonRequestEvent {}
    public static class ChannelTradeListCompleteEvent extends CommonCompleteEvent {

        private List<ChannelTradeEntity> list = new ArrayList<ChannelTradeEntity>();
        public List<ChannelTradeEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<ChannelTradeEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<ChannelTradeEntity> list) {
            this.list = list;
        }
    }

    public static class ProfitListEvent extends CommonRequestEvent {}
    public static class ProfitListCompleteEvent extends CommonCompleteEvent {
        private List<ProfitEntity> list = new ArrayList<ProfitEntity>();
        public List<ProfitEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<ProfitEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<ProfitEntity> list) {
            this.list = list;
        }
    }

    public static class SetProfitEvent extends CommonRequestEvent {}
    public static class SetProfitCompleteEvent extends CommonCompleteEvent {}

    public static class DeleteProfitEvent extends CommonRequestEvent {}
    public static class DeleteProfitCompleteEvent extends CommonCompleteEvent {}

    public static class ChannelListEvent extends CommonRequestEvent {}
    public static class ChannelListCompleteEvent extends CommonCompleteEvent {
        private List<ChannelEntity> list = new ArrayList<ChannelEntity>();
        public List<ChannelEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<ChannelEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<ChannelEntity> list) {
            this.list = list;
        }
    }

    //配货
    public static class PrepareListEvent extends CommonRequestEvent {}
    public static class PrepareListCompleteEvent extends CommonCompleteEvent {

        private List<PrepareEntity> list = new ArrayList<PrepareEntity>();
        public List<PrepareEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<PrepareEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<PrepareEntity> list) {
            this.list = list;
        }
    }

    public static class PrepareAddEvent extends CommonRequestEvent {}
    public static class PrepareAddCompleteEvent extends CommonCompleteEvent {}

    public static class PrepareInfoEvent extends CommonRequestEvent {}
    public static class PrepareInfoCompleteEvent extends CommonCompleteEvent {
        private PrepareEntity entity;

        public PrepareEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, PrepareEntity.class);
            return entity;
        }

        public void setEntity(PrepareEntity entity) {
            this.entity = entity;
        }
    }


    //调货
    public static class ExchangeListEvent extends CommonRequestEvent {}
    public static class ExchangeListCompleteEvent extends CommonCompleteEvent {

        private List<ExchangeEntity> list = new ArrayList<ExchangeEntity>();
        public List<ExchangeEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<ExchangeEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<ExchangeEntity> list) {
            this.list = list;
        }
    }

    public static class ExchangeAddEvent extends CommonRequestEvent {}
    public static class ExchangeAddCompleteEvent extends CommonCompleteEvent {}

    public static class ExchangeInfoEvent extends CommonRequestEvent {}
    public static class ExchangeInfoCompleteEvent extends CommonCompleteEvent {
        private ExchangeEntity entity;

        public ExchangeEntity getEntity() {
            String result = null;
            result = getResult().toString();
            entity = (new Gson()).fromJson(result, ExchangeEntity.class);
            return entity;
        }

        public void setEntity(ExchangeEntity entity) {
            this.entity = entity;
        }
    }

    //banner
    public static class BannerDataEvent extends CommonRequestEvent {}
    public static class BannerDataCompleteEvent extends CommonCompleteEvent {
        private List<PicEntity> list = new ArrayList<PicEntity>();
        public List<PicEntity> getList() {
            if (list.size() < 1) {
                String result = null;
                try {
                    result = getResult().getString("list");
                    list = (new Gson()).fromJson(result,
                            new TypeToken<List<PicEntity>>() {}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return list;
        }
        public void setList(List<PicEntity> list) {
            this.list = list;
        }
    }

    public static class SendDeviceCodeEvent extends CommonRequestEvent {}
    public static class SendDeviceCodeCompleteEvent extends CommonCompleteEvent{}

}
