package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class ReturnCarInfo implements Serializable {
    private double b_latitude;
    private double b_longitude;
    private String begin;
    private String corporate;
    private Customer customer;
    private double e_latitude;
    private double e_longitude;
    private String end;
    private String mobile;
    private double money;
    private String name;
    private String orderNo;
    private int returnId;
    private int status;
    private String time;
    private String type;

    public class Customer implements Serializable {
        String address;
        String corporate;
        String nickName;
        String phone;
        String picture;

        public String getPicture() {
            return this.picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickName() {
            return this.nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCorporate() {
            return this.corporate;
        }

        public void setCorporate(String corporate) {
            this.corporate = corporate;
        }
    }

    public double getB_longitude() {
        return this.b_longitude;
    }

    public void setB_longitude(double b_longitude) {
        this.b_longitude = b_longitude;
    }

    public double getB_latitude() {
        return this.b_latitude;
    }

    public void setB_latitude(double b_latitude) {
        this.b_latitude = b_latitude;
    }

    public double getE_longitude() {
        return this.e_longitude;
    }

    public void setE_longitude(double e_longitude) {
        this.e_longitude = e_longitude;
    }

    public double getE_latitude() {
        return this.e_latitude;
    }

    public void setE_latitude(double e_latitude) {
        this.e_latitude = e_latitude;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getReturnId() {
        return this.returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCorporate() {
        return this.corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBegin() {
        return this.begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
