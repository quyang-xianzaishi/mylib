package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\27 0027.
 */

public class ConversationInfoResultBean {

  private String groupId;
  private List<User> mList;
  private String groupName;
  private String groupNotice;
  private String saveState;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public List<User> getList() {
    return mList;
  }

  public void setList(List<User> list) {
    mList = list;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupNotice() {
    return groupNotice;
  }

  public void setGroupNotice(String groupNotice) {
    this.groupNotice = groupNotice;
  }

  public String getSaveState() {
    return saveState;
  }

  public void setSaveState(String saveState) {
    this.saveState = saveState;
  }

}
