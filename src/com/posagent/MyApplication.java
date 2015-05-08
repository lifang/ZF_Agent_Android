package com.posagent;


import android.app.Activity;
import android.app.Application;
import android.support.v4.util.ArrayMap;

import com.example.zf_android.entity.ApplyneedEntity;
import com.example.zf_android.entity.ChannelEntity;
import com.example.zf_android.entity.ChannelTradeEntity;
import com.example.zf_android.entity.GoodinfoEntity;
import com.example.zf_android.entity.PosSelectEntity;
import com.example.zf_android.entity.UserInfoEntity;
import com.example.zf_android.entity.UserRole;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.loopj.android.http.AsyncHttpClient;
import com.posagent.events.Events;
import com.posagent.network.APIManager;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class MyApplication extends Application {
	
	private static MyApplication  mInstance=null;
	//private ArrayList<Order> orderList = new ArrayList<Order>();

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

    public static UserInfoEntity currentUser;

    public static UserInfoEntity getCurrentUser() {
        return currentUser;
    }
    public static UserInfoEntity user() {
        return currentUser;
    }

    public static void setCurrentUser(UserInfoEntity currentUser) {
        MyApplication.currentUser = currentUser;

        if (null == currentUser) {
            roles = null;
            return;
        }

        //update roles
        roles = new HashMap<String, String>();
        for (UserRole role: currentUser.getMachtigingen()) {
            roles.put(role.getRole_id(), "true");
        }
    }

    public static Map<Integer, String> mapCity;

    public static Map<String, String> roles;

    public static Map<String, String> getRoles() {
        return roles;
    }

    public static boolean hasRole(String roleId) {

        int intRoleId = Integer.parseInt(roleId);

        if (null == roles) {
            return false;
        }

        if (currentUser.getId() == currentUser.getAgentUserId()) {
            //是代理商
            if (currentUser.getParent_id() > 0) {
                //二级代理商
                switch (intRoleId) {
                    case Constants.Roles.AllProduct:
                    case Constants.Roles.Order:
                        return false;
                }
            }
            return true;

        } else {
            switch (intRoleId) {
                case Constants.Roles.AllProduct:
                    return roles.get("" + Constants.Roles.Daigou) != null ||
                            roles.get("" + Constants.Roles.Pigou) != null;
            }
            return roles.get(roleId) != null;
        }

    }



    public static List<ChannelEntity> channels = new LinkedList<ChannelEntity>();

    public static List<ChannelEntity> getChannels() {
        return channels;
    }

    public static void setChannels(List<ChannelEntity> channels) {
        MyApplication.channels = channels;
    }

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


//	public static User currentUser = new User();
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
            EventBus.getDefault().unregister(this);
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
        EventBus.getDefault().register(this);

        initMapCity();

    }

	public static MyApplication getInstance() {
		return mInstance;
	}


    //
    public void prepareChannelList() {
        if (getChannels().size() > 0) {
            return;
        }
        JsonParams params = new JsonParams();
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ChannelListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void getChannelTrade(ChannelEntity channel) {
        JsonParams params = new JsonParams();

        params.put("id", channel.getId());

        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ChannelTradeListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events
    public void onEventMainThread(Events.ChannelListCompleteEvent event) {
        setChannels(event.getList());
        for (ChannelEntity channel: event.getList()) {
            getChannelTrade(channel);
        }

    }

    public void onEventMainThread(Events.ChannelTradeListCompleteEvent event) {
        List<ChannelTradeEntity> list = event.getList();
        if (list.size() < 1) {
            return;
        }
        ChannelTradeEntity trade = list.get(0);
        for (ChannelEntity channel: getChannels()) {
            if (channel.getName().equals(trade.getName())) {
                channel.setTrades(list);
                break;
            }
        }

    }

    public ChannelEntity getChannelEntityWithId(int id) {
        for (ChannelEntity channel: getChannels()) {
            if (channel.getId() == id) {
                return channel;
            }
        }

        return null;
    }

    public ChannelEntity getChannelEntityWithName(String name) {
        for (ChannelEntity channel: getChannels()) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }

        return null;
    }

    private void initMapCity() {
        new Thread() {
            @Override
            public void run() {
                List<Province> provinces = CommonUtil.readProvincesAndCities(getApplicationContext());
                mapCity = new ArrayMap<Integer, String>();
                for (Province province: provinces) {
                    for (City city: province.getCities()) {
                        mapCity.put(city.getId(), city.getName());
                    }
                }
            }
        }.start();
    }

    public String cityNameForId(int cityId) {
        if (null == mapCity) {
            return "未知";
        }
        return mapCity.get(cityId);
    }






}
