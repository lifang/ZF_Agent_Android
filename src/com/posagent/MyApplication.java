package com.posagent;

 
 
import android.app.Activity;
import android.app.Application;

import com.example.zf_android.entity.ApplyneedEntity;
import com.example.zf_android.entity.GoodinfoEntity;
import com.example.zf_android.entity.PosSelectEntity;
import com.example.zf_android.entity.User;
import com.loopj.android.http.AsyncHttpClient;
import com.posagent.network.APIManager;

import java.util.LinkedList;
import java.util.List;
 
 
 

public class MyApplication extends Application{
	
	private static MyApplication  mInstance=null;
	//private ArrayList<Order> orderList = new ArrayList<Order>();
	/**
	 * ��֤��Ϣtoken
	 */
	private static  String versionCode="";
	private static int notifyId=0;
	private static Boolean isSelect=false;
	
	public static Boolean getIsSelect() {
		return isSelect;
	}
	public static void setIsSelect(Boolean isSelect) {
		MyApplication.isSelect = isSelect;
	}
	public static int getNotifyId() {
		return notifyId;
	}
	public static void setNotifyId(int notifyId) {
		MyApplication.notifyId = notifyId;
	}
	public static String getVersionCode() {
		return versionCode;
	}
	public static void setVersionCode(String versionCode) {
		MyApplication.versionCode = versionCode;
	}
	public static User getCurrentUser() {
		return currentUser;
	}
	public static void setCurrentUser(User currentUser) {
		MyApplication.currentUser = currentUser;
	}


	private static String token="";
	AsyncHttpClient client = new AsyncHttpClient(); //  
	
	public AsyncHttpClient getClient() {
		//client.setTimeout(6000);
		return client;
	}
	public void setClient(AsyncHttpClient client) {
		this.client = client;
	}
	public static String getToken() {
		return token;
	}
	public static void setToken(String token) {
		MyApplication.token = token;
	}


	public static PosSelectEntity pse = new PosSelectEntity();
	
	
	
	
	
	public static PosSelectEntity getPse() {
		return pse;
	}
	public static void setPse(PosSelectEntity pse) {
		MyApplication.pse = pse;
	}

	public static List<ApplyneedEntity> pub = new LinkedList<ApplyneedEntity>();   
	public static List<ApplyneedEntity> single = new LinkedList<ApplyneedEntity>();   
	  
	  
 

	public static List<ApplyneedEntity> getPub() {
		return pub;
	}
	public static void setPub(List<ApplyneedEntity> pub) {
		MyApplication.pub = pub;
	}
	public static List<ApplyneedEntity> getSingle() {
		return single;
	}
	public static void setSingle(List<ApplyneedEntity> single) {
		MyApplication.single = single;
	}


	public static User currentUser = new User();
	public static GoodinfoEntity gfe=new GoodinfoEntity();
	 
    public static GoodinfoEntity getGfe() {
		return gfe;
	}
	public static void setGfe(GoodinfoEntity gfe) {
		MyApplication.gfe = gfe;
	}


	private List<Activity> mList = new LinkedList<Activity>();   
    public void addActivity(Activity activity) {
        mList.add(activity);    
    }    
    public void exit() {
        try {    
            for (Activity activity:mList) {    
                if (activity != null)    
                    activity.finish();    
            }    
        } catch (Exception e) {    
            e.printStackTrace();    
        } 
        
    }  
 

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
        //setup APIManager EventBus
        APIManager.getDefault();
    }

	public static MyApplication getInstance() {
		return mInstance;
	}
	
 
}
