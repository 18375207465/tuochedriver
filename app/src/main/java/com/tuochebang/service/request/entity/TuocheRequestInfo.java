package com.tuochebang.service.request.entity;

import java.io.Serializable;
import java.util.List;

public class TuocheRequestInfo implements Serializable {
    private String begin;
    private CarInfo car;
    private String corporate;
    private Customer customer;
    private String end;
    private String isReturn;
    private String mobile;
    private String money;
    private String name;
    private String orderNo;
    private String payName;
    private List<String> picture;
    private String remark;
    private String requestId;
    private String status;
    private String time;
    private String type;
    private String typeName;

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsReturn() {
        return this.isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPayName() {
        return this.payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public CarInfo getCar() {
        return this.car;
    }

    public void setCar(CarInfo car) {
        this.car = car;
    }

    public List<String> getPicture() {
        return this.picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public String toString() {
        return "TuocheRequestInfo{status='" + this.status + '\'' + ", corporate='" + this.corporate + '\'' + ", mobile='" + this.mobile + '\'' + ", time='" + this.time + '\'' + ", begin='" + this.begin + '\'' + ", end='" + this.end + '\'' + ", money='" + this.money + '\'' + ", typeName='" + this.typeName + '\'' + ", payName='" + this.payName + '\'' + ", isReturn=" + this.isReturn + ", car=" + this.car + ", picture=" + this.picture + ", remark='" + this.remark + '\'' + ", requestId='" + this.requestId + '\'' + ", customer=" + this.customer + ", orderNo='" + this.orderNo + '\'' + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + '}';
    }
}
