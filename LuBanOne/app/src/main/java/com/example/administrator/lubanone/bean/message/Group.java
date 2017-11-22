package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class Group {

    private String groupid;
    private String groupname;
    private String groupimg;

    public Group(String groupid, String groupname, String groupimg) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.groupimg = groupimg;
    }

    public Group() {
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupimg() {
        return groupimg;
    }

    public void setGroupimg(String groupimg) {
        this.groupimg = groupimg;
    }
}
