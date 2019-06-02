package com.tuochebang.service.widget.wxphotoselector;

public class ImageFloder {
    private int count;
    private String dir;
    private String firstImagePath;
    private String name;

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        this.name = this.dir.substring(this.dir.lastIndexOf("/") + 1);
    }

    public String getFirstImagePath() {
        return this.firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
