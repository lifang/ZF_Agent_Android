package com.posagent.utils;

/**
 * Created by holin on 4/8/15.
 */
public class Constants {
    public static final int SUCCESS_CODE = 1;

    public static final String USER_KIND = "user_kind";
    public static final String DefaultSelectedNameKey = "selectedName";
    public static final String DefaultSelectedIdKey = "selectedId";
    public static final int REQUEST_CODE = 99;
    public static final int REQUEST_CODE2 = 100;
    public static final int REQUEST_CODE3 = 101;
    public static final int REQUEST_CODE4 = 102;
    public static final int REQUEST_CODE5 = 103;


    public static class Goods {
        public static final int OrderTypeDaigou = 2;
        public static final int OrderTypeDaizulin = 3;
        public static final int OrderTypePigou = 1;
        public static final int BuyTypePigou = 5;
        public static final int BuyTypeDaigou = 3;
        public static final int BuyTypeDaizulin = 4;
    }

    public static class Roles {
        public static final int Pigou = 1;
        public static final int Daigou = 2;
        public static final int TerminalAndAfterSale = 3;
        public static final int TradeFlowAndProfit = 4;
        public static final int SonAgent = 5;
        public static final int ManUser = 6;
        public static final int ManStaff = 7;
        public static final int AgentInfo = 8;
        public static final int Stock = 9;
        public static final int Order = 100;
        public static final int AllProduct = 101;
        public static final int Message = 102;
        public static final int Mine = 103;
    }



    public static class CommonInputerConstant {
        public static final int REQUEST_CODE = 1;
        public static final int REQUEST_CITY_CODE = 2;
        public static final String TITLE_KEY = "title";
        public static final String PLACEHOLDER_KEY = "placeholder";
        public static final String VALUE_KEY = "value";
    }

    public static class UserConstant {
        public static final int USER_KIND_PESONAL = 1;
        public static final int USER_KIND_AGENT = 2;

    }

    public static class TerminalConstant {
        public static final String[] STATUS = {"未知",
                "已开通",
                "部分开通",
                "未开通",
                "已注销",
                "已停用"
        };



    }

    public static class KeyValue {
        public static final int Picture = 2;
    }

    public static class Trade {
        public static final String[] STATUS = {
                "未知",
                "转账",
                "消费",
                "还款",
                "生活充值",
                "话费充值"
        };

    }

}
