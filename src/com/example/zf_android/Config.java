package com.example.zf_android;

public class Config {

    //视频通话
    public static final String VIDEO_SERVER_IP = "121.40.84.2";
    public static final int VIDEO_SERVER_PORT = 8906;
    public static final String URL_NOTICE_VIDEO = "http://121.40.84.2:8180/zfmanager/notice/video";

    public final static String PATHS = "http://114.215.149.242:18080/ZFMerchant/api/";
    public final static String IMAGE_PATH = "";
	public static String checkVersion=PATHS+"";
	public static int ROWS=10;
	public static String getmes=PATHS+"message/receiver/getAll";
	public static String getsysmes=PATHS+"web/message/getAll";
	 
	public final static String LOGIN = PATHS+"user/studentLogin";
	public static final int CITY_ID = 1;
	public static int UserID=1;
	public static String goodcomment=PATHS+"comment/list";
	//user/userRegistration
	public final static String UserRegistration = PATHS+"user/userRegistration";
	public static final String SHARED = "zfagent";
	public static final String FINDPASS = null;
	public final static String RegistgetCode = PATHS+"user/sendPhoneVerificationCode/";
	public static final String GRTONE =PATHS+"customers/getOne/";
	// http://114.215.149.242:18080/ZFMerchant/api/message/receiver/getById
	public static final String getMSGById =PATHS+"message/receiver/getById";
	public static final String Car_edit =PATHS+"cart/update";
	public static final int CODE = 1;
	public static final String getMyOrderAll =PATHS+"order/getMyOrderAll";
	public static final String batchRead =PATHS+"message/receiver/batchRead";
	public static final String GOODDETAIL =PATHS+"good/goodinfo";
	public static final String ChooseAdress = PATHS+"customers/getAddressList/";
	public static final String goodadd=PATHS+"cart/add";
	//http://114.215.149.242:18080/ZFMerchant/api/order/shop
	public static final String SHOPORDER=PATHS+"order/shop";

    //商户PID
    public static final String PARTNER = "2088811347108355";
    //商户收款账号
    public static final String SELLER = "ebank007@epalmpay.cn";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALjI06X8hEw9LiLqTsqmjZAqwSq/VIGJKNQgIeCCr/oReR4OePe5i2u+89PpcFe6kF2v6gWulb4WNdHYw3Iiux56sm7jUQPC1hVYXG8tiaVEb3YhX2y0YGQUS18drBBGzHnlOQlrrmlBh9ugQFzLio2NwUWo0yfcXlLoKYyteDBVAgMBAAECgYBpjW441rHLyvbbwvQXFmSvAX0uKfTfubW01lYDpSNYuTpyTNoUx8w4U+98EVC3DD8DBUWs0TmAR7eeky+xtt0jZ1O8bpHUzRi02NOw2p1ZyAHN28rvUpultfInBpbqgJDvMoWIX4AeqWQcs4gbAbPyEaWvgYM53uW7eo9CtcFMgQJBAOHGVL8Xe9agkiGwYT8e9068+xjXiloAKgQjps8fxLfMCd34sI1tEjyz0jIZ+AK4pGvU1JJdtx7s70INnubqoY0CQQDRhbFcxqaz2c+S2WUQNduFah5EZt/vdWxo4+6EHrXNdAjT7nVyA8CzreRXcPEKQZ+RhuXyXGqSLDJGKYPGQIPpAkBSmqfjCoqKqlEM9mV+HKxLKKWOHz5FU44L2adsXKkyvfpWNmkSNXfYscoT/qBZDolJ0qK7soIPVIztU+JxhiL5AkAC037U9YkCHAoEvRHz6gYQAqJt4cVbgYX41Do/Zfqlzs7frPPAmfRbeBkAZPGbZc81M1CeuEhnuFjlQWIZpn0hAkEAu1Q+fNm01qqVJ0YCMeyUoLqin/rmRAsY93cDNk82ZxY+gc3YDlcvF5qqQqcqiSSHBZkAtQqFTzx3taybP2MKjw==";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    //异步通知接口
    public static final String NOTIFT_URL = "/app_notify_url.jsp";
    //支付成功跳转页面
    public static final String RETURN_URL = "/return_url.jsp";


}
