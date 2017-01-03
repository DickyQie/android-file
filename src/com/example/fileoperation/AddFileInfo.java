package com.example.fileoperation;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/30.
 *
 * 手机本地文件操作类
 *
 */

public class AddFileInfo implements Serializable {

    private String name;
    private String size;
    private String time;
    private String path;
    public AddFileInfo(String name, String size, String time,String path)
    {
        this.name=name;
        this.size=size;
        this.time=time;
        this.path=path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}
