package menu.filedemo;

import java.io.Serializable;

/**
 * Created by zq on 2016/12/30.
 *
 * 手机本地文件操作类
 *
 */

public class AddFileInfo implements Serializable {

    private String name;
    private Long size;
    private String time;
    private boolean isCheck;
    private String path;
    public AddFileInfo(String name, Long size, String time, boolean isCheck, String path)
    {
        this.name=name;
        this.size=size;
        this.time=time;
        this.isCheck=isCheck;
        this.path=path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSize() {
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

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheck() {
        return isCheck;
    }
}
