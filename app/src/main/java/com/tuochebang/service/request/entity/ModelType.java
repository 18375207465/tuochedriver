package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class ModelType implements Serializable {
    private int typeId;
    private String typeName;

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return "ModelType{typeId=" + this.typeId + ", typeName='" + this.typeName + '\'' + '}';
    }
}
