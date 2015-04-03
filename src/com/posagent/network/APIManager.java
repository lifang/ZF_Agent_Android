package com.posagent.network;

import android.util.Log;

import com.posagent.events.Events;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

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
    public static final String UrlLogin = BaseUrl + "/agent/agentLogin";


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
        RequestBody body = RequestBody.create(JSON, "username=" + username + "&password=" + password);

        Request request = new Request.Builder()
                .url(UrlLogin)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.d(TAG, response.body().string());
            EventBus.getDefault().post(new Events.LoginCompleteEvent("faceted"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
