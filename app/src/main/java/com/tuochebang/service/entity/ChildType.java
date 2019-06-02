package com.tuochebang.service.entity;

import java.io.Serializable;

public class ChildType implements Serializable {
    private static final long serialVersionUID = -7129820307834657766L;
    private int id;
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "ChildType [id=" + this.id + ", name=" + this.name + "]";
    }
}
