package com.posagent.network;

import android.util.Log;

import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

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
//    public static final String BaseUrl = "http://192.168.0.106:3000";
    public static final String UrlLogin = BaseUrl + "/agent/agentLogin";
//    public static final String UrlLogin = BaseUrl + "/";


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

                //TODO 登录成功后做一些事情
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



    // helper
    private Request.Builder request() {
       return new Request.Builder()
               .addHeader("Content-Type", "application/json;text=utf-8");
    }

}
