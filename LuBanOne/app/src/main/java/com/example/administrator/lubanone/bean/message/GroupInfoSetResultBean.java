package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\8\2 0002.
 */

public class GroupInfoSetResultBean {

  private List<GroupMemberInfoBean> data;
  private String groupmaster;
  private String groupname;
  private String groupnotice;
  private String groupimg;
  private String maillist;

  public List<GroupMemberInfoBean> getData() {
    return data;
  }

  public void setData(
      List<GroupMemberInfoBean> data) {
    this.data = data;
  }

  public String getGroupmaster() {
    return groupmaster;
  }

  public void setGroupmaster(String groupmaster) {
    this.groupmaster = groupmaster;
  }

  public String getGroupname() {
    return groupname;
  }

  public void setGroupname(String groupname) {
    this.groupname = groupname;
  }

  public String getGroupnotice() {
    return groupnotice;
  }

  public void setGroupnotice(String groupnotice) {
    this.groupnotice = groupnotice;
  }

  public String getGroupimg() {
    return groupimg;
  }

  public void setGroupimg(String groupimg) {
    this.groupimg = groupimg;
  }

  public String getMaillist() {
    return maillist;
  }

  public void setMaillist(String maillist) {
    this.maillist = maillist;
  }
}
