package com.posagent.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by holin on 4/11/15.
 */
public class JsonParams {
    private  Map<String, Object> params = new HashMap<String, Object>();

    public JsonParams() {}

    public void put(String key, Object value) {
        params.put(key, value);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(params);
    }

}
