package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class AdminInfo implements Serializable {
    private String account;
    private String address;
    private String businessLicense;
    private String corporate;
    private String name;
    private String password;
    private String picture0;
    private String picture1;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessLicense() {
        return this.businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getPicture0() {
        return this.picture0;
    }

    public void setPicture0(String picture0) {
        this.picture0 = picture0;
    }

    public String getPicture1() {
        return this.picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }
}
