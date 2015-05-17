package com.example.zf_android.entity;

import java.util.List;

public class OrderDetailEntity {

    private String total_dingjin;
    private String zhifu_dingjin;
    private String guishu_user;
    private String terminals;
    private String logistics_name;
    private String logistics_number;

    public String getLogistics_name() {
        return logistics_name;
    }

    public void setLogistics_name(String logistics_name) {
        this.logistics_name = logistics_name;
    }

    public String getLogistics_number() {
        return logistics_number;
    }

    public void setLogistics_number(String logistics_number) {
        this.logistics_number = logistics_number;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getTotal_dingjin() {
        return total_dingjin;
    }

    public void setTotal_dingjin(String total_dingjin) {
        this.total_dingjin = total_dingjin;
    }

    public String getZhifu_dingjin() {
        return zhifu_dingjin;
    }

    public void setZhifu_dingjin(String zhifu_dingjin) {
        this.zhifu_dingjin = zhifu_dingjin;
    }

    public String getShipped_quantity() {
        return shipped_quantity;
    }

    public void setShipped_quantity(String shipped_quantity) {
        this.shipped_quantity = shipped_quantity;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    private String shipped_quantity;
    private String total_quantity;



    //    "order_number": "020150206104427420",
    private String order_number;
    //    "order_receiver_phone": "18352445051",\
    private String order_receiver_phone;
    //    "order_payment_type": "",
    private String order_payment_type;//支付方式
    //    "order_comment": "nihao",
    private String order_comment;//留言
    //    "order_address": "江苏省苏州市工业园区独墅湖高教区",
    private String order_address;//收件人地址
    //    "order_invoce_type": "个人",
    private String order_invoce_type;//发票类型
    //    "order_oldprice": "100",
    private String order_oldprice;
    //    "order_id": 1,
//    "order_totalNum": "5",
    private int order_id;
    private String order_totalNum;//订单总件数
    //    "order_status": 1,
    private int order_status;//订单状态
    //    "order_receiver": "李长福",
    private String order_receiver;//收件人
    //    "order_createTime": "2015-02-05 19:53:31",
    private String order_createTime;//订单日期
    //    "order_psf": "0",
    private String order_psf;//配送费
    //    "good_merchant": "",
    private String good_merchant;
    //    "order_invoce_info": "sad",
    private String order_invoce_info;

    public String getOrder_totalPrice() {
        return order_totalPrice;
    }

    public void setOrder_totalPrice(String order_totalPrice) {
        this.order_totalPrice = order_totalPrice;
    }

    //    "order_totalprice": "120",//实付金额
    private String order_totalPrice;
    private CommentG comments;
    private List <Goodlist> order_goodsList;




    public String getOrder_number() {
        return order_number;
    }




    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }




    public String getOrder_receiver_phone() {
        return order_receiver_phone;
    }




    public void setOrder_receiver_phone(String order_receiver_phone) {
        this.order_receiver_phone = order_receiver_phone;
    }




    public String getOrder_payment_type() {
        return order_payment_type;
    }




    public void setOrder_payment_type(String order_payment_type) {
        this.order_payment_type = order_payment_type;
    }




    public String getOrder_comment() {
        return order_comment;
    }




    public void setOrder_comment(String order_comment) {
        this.order_comment = order_comment;
    }




    public String getOrder_address() {
        return order_address;
    }




    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }




    public String getOrder_invoce_type() {
        return order_invoce_type;
    }




    public void setOrder_invoce_type(String order_invoce_type) {
        this.order_invoce_type = order_invoce_type;
    }




    public String getOrder_oldprice() {
        return order_oldprice;
    }




    public void setOrder_oldprice(String order_oldprice) {
        this.order_oldprice = order_oldprice;
    }




    public int getId() {
        return order_id;
    }




    public void setId(int id) {
        this.order_id = id;
    }




    public String getOrder_totalNum() {
        return order_totalNum;
    }




    public void setOrder_totalNum(String order_totalNum) {
        this.order_totalNum = order_totalNum;
    }




    public int getOrder_status() {
        return order_status;
    }




    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }




    public String getOrder_receiver() {
        return order_receiver;
    }




    public void setOrder_receiver(String order_receiver) {
        this.order_receiver = order_receiver;
    }




    public String getOrder_createTime() {
        return order_createTime;
    }




    public void setOrder_createTime(String order_createTime) {
        this.order_createTime = order_createTime;
    }




    public String getOrder_psf() {
        return "0";
    }




    public void setOrder_psf(String order_psf) {
        this.order_psf = order_psf;
    }




    public String getGood_merchant() {
        return good_merchant;
    }




    public void setGood_merchant(String good_merchant) {
        this.good_merchant = good_merchant;
    }




    public String getOrder_invoce_info() {
        return order_invoce_info;
    }




    public void setOrder_invoce_info(String order_invoce_info) {
        this.order_invoce_info = order_invoce_info;
    }




    public CommentG getComments() {
        return comments;
    }

    public void setComments(CommentG comments) {
        this.comments = comments;
    }




    public List<Goodlist> getOrder_goodsList() {
        return order_goodsList;
    }

    public void setOrder_goodsList(List<Goodlist> order_goodsList) {
        this.order_goodsList = order_goodsList;
    }




    public class CommentG {
        public List<MarkEntity> getList() {
            return list;
        }

        public void setList(List<MarkEntity> list) {
            this.list = list;
        }

        public  List<MarkEntity> list;
        public  int total;
        public int getTotal() {
            return total;
        }
        public void setTotal(int total) {
            this.total = total;
        }

    }

    public String getTerminals() {
        return terminals;
    }

    public void setTerminals(String terminals) {
        this.terminals = terminals;
    }

    public String getGuishu_user() {
        return guishu_user;
    }

    public void setGuishu_user(String guishu_user) {
        this.guishu_user = guishu_user;
    }
}
