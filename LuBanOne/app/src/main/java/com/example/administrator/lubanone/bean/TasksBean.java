package com.example.administrator.lubanone.bean;

import java.io.Serializable;

/**
 * Created by hou on 2017/6/22.
 * 任务对象
 */

public class TasksBean implements Serializable {
    private String status;//完成状态
    private String if_title;//新闻标题
    private String if_id;//新闻ID
    private String if_cataindex;//梦想催化剂数量
    private String if_thumimg;//新闻缩略图
    private String clicknum;//点击次数

    @Override
    public String toString() {
        return "TasksBean{" +
                "status='" + status + '\'' +
                ", if_title='" + if_title + '\'' +
                ", if_id='" + if_id + '\'' +
                ", if_cataindex='" + if_cataindex + '\'' +
                ", if_thumimg='" + if_thumimg + '\'' +
                ", clicknum='" + clicknum + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIf_title() {
        return if_title;
    }

    public void setIf_title(String if_title) {
        this.if_title = if_title;
    }

    public String getIf_id() {
        return if_id;
    }

    public void setIf_id(String if_id) {
        this.if_id = if_id;
    }

    public String getIf_cataindex() {
        return if_cataindex;
    }

    public void setIf_cataindex(String if_cataindex) {
        this.if_cataindex = if_cataindex;
    }

    public String getIf_thumimg() {
        return if_thumimg;
    }

    public void setIf_thumimg(String if_thumimg) {
        this.if_thumimg = if_thumimg;
    }

    public String getClicknum() {
        return clicknum;
    }

    public void setClicknum(String clicknum) {
        this.clicknum = clicknum;
    }
}
