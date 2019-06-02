package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String address;
    private String corporate;
    private String mobile;
    private String name;
    private String nickName;
    private int perfect;
    private String picture;
    private int unreadMsgCount;
    private int userId;
    private int userType;

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorporate() {
        return this.corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUnreadMsgCount() {
        return this.unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public int getPerfect() {
        return this.perfect;
    }

    public void setPerfect(int perfect) {
        this.perfect = perfect;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String toString() {
        return "UserInfo{userId=" + this.userId + ", picture='" + this.picture + '\'' + ", mobile='" + this.mobile + '\'' + ", name='" + this.name + '\'' + ", nickName='" + this.nickName + '\'' + ", userType='" + this.userType + '\'' + ", corporate='" + this.corporate + '\'' + ", address='" + this.address + '\'' + ", unreadMsgCount=" + this.unreadMsgCount + ", perfect=" + this.perfect + '}';
    }
}
