package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class Auth implements Serializable {
    private int authId;
    private String mobile;
    private String name;
    private String timestamp;

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAuthId() {
        return this.authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String toString() {
        return "Auth{timestamp='" + this.timestamp + '\'' + ", name='" + this.name + '\'' + ", mobile='" + this.mobile + '\'' + ", authId=" + this.authId + '}';
    }
}
