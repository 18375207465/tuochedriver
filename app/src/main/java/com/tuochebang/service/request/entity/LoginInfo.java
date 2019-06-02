package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private String token;
    private UserInfo user;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String toString() {
        return "LoginInfo{token='" + this.token + '\'' + ", user=" + this.user + '}';
    }
}
