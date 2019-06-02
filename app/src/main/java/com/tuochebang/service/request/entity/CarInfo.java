package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class CarInfo implements Serializable {
    private String driveName;
    private String gearName;
    private String info;
    private String modelName;

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getGearName() {
        return this.gearName;
    }

    public void setGearName(String gearName) {
        this.gearName = gearName;
    }

    public String getDriveName() {
        return this.driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String toString() {
        return "CarInfo{modelName='" + this.modelName + '\'' + ", gearName='" + this.gearName + '\'' + ", driveName='" + this.driveName + '\'' + ", info='" + this.info + '\'' + '}';
    }
}
