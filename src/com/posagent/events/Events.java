package com.posagent.events;

/**
 * Created by holin on 4/3/15.
 */
public class Events {
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

        public String getToken() { return token; }

        public LoginCompleteEvent(String _token) {
            token = _token;
        }
    }
}
