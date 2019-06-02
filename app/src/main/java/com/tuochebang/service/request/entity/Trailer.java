package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class Trailer implements Serializable {
    private String mobile;
    private String nickName;
    private String picture;
    private String type;
    private int unfinish;
    private int userId;

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getUnfinish() {
        return this.unfinish;
    }

    public void setUnfinish(int unfinish) {
        this.unfinish = unfinish;
    }

    public String toString() {
        return "Trailer{userId=" + this.userId + ", type='" + this.type + '\'' + ", nickName='" + this.nickName + '\'' + ", mobile='" + this.mobile + '\'' + ", picture='" + this.picture + '\'' + ", unfinish=" + this.unfinish + '}';
    }
}
