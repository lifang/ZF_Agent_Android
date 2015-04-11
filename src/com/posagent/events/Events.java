package com.posagent.events;

import com.example.zf_android.entity.PosEntity;

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

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public CompleteEvent(Boolean _success) {
            success = _success;
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

    public static class GoodsDetailEvent {
        private String params;

        public String getParams() {
            return params;
        }

        public GoodsDetailEvent(String _params) {
            params = _params;
        }
    }

    public static class GoodsDetailCompleteEvent extends CompleteEvent {

        private JSONObject result;

        public JSONObject getResult() {
            return result;
        }

        public void setResult(JSONObject result) {
            this.result = result;
        }

        public GoodsDetailCompleteEvent(Boolean _success) {
            super(_success);
        }
    }
}
