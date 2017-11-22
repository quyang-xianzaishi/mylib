package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/8/25.
 */

public class TaskModel {

  private List<TaskList> tasklist;

  public List<TaskList> getTasklist() {
    return tasklist;
  }

  public void setTasklist(
      List<TaskList> tasklist) {
    this.tasklist = tasklist;
  }

  @Override
  public String toString() {
    return "TaskModel{" +
        "tasklist=" + tasklist +
        '}';
  }

  public class TaskList {
    private String taskid;
    private String title;
    private String catalyst;
    private String thumimg;
    private String status;

    public String getTaskid() {
      return taskid;
    }

    public void setTaskid(String taskid) {
      this.taskid = taskid;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getCatalyst() {
      return catalyst;
    }

    public void setCatalyst(String catalyst) {
      this.catalyst = catalyst;
    }

    public String getThumimg() {
      return thumimg;
    }

    public void setThumimg(String thumimg) {
      this.thumimg = thumimg;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return "TaskModel{" +
          "taskid='" + taskid + '\'' +
          ", title='" + title + '\'' +
          ", catalyst='" + catalyst + '\'' +
          ", thumimg='" + thumimg + '\'' +
          ", status='" + status + '\'' +
          '}';
    }
  }
}
