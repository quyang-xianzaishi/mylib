package com.example.administrator.lubanone.bean;

import java.util.List;

/**
 * Created by hou on 2017/7/1.
 */

public class TaskCollectBean {

  private List<TaskCollectList> tasklist;

  public List<TaskCollectList> getTasklist() {
    return tasklist;
  }

  public void setTasklist(
      List<TaskCollectList> tasklist) {
    this.tasklist = tasklist;
  }

  @Override
  public String toString() {
    return "TaskCollectBean{" +
        "tasklist=" + tasklist +
        '}';
  }

  public class TaskCollectList {

    private String if_thumimg;//新闻缩略图
    private String if_id;//新闻id
    private String if_title;//新闻标题
    private String if_content;//新闻内容缩略
    private String collect_time;//收藏时间 日期格式

    public String getIf_thumimg() {
      return if_thumimg;
    }

    public void setIf_thumimg(String if_thumimg) {
      this.if_thumimg = if_thumimg;
    }

    public String getIf_id() {
      return if_id;
    }

    public void setIf_id(String if_id) {
      this.if_id = if_id;
    }

    public String getIf_title() {
      return if_title;
    }

    public void setIf_title(String if_title) {
      this.if_title = if_title;
    }

    public String getIf_content() {
      return if_content;
    }

    public void setIf_content(String if_content) {
      this.if_content = if_content;
    }

    public String getCollect_time() {
      return collect_time;
    }

    public void setCollect_time(String collect_time) {
      this.collect_time = collect_time;
    }

    @Override
    public String toString() {
      return "TaskCollectBean{" +
          "if_thumimg='" + if_thumimg + '\'' +
          ", if_id='" + if_id + '\'' +
          ", if_title='" + if_title + '\'' +
          ", if_content='" + if_content + '\'' +
          ", collect_time='" + collect_time + '\'' +
          '}';
    }
  }
}
