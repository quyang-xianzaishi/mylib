package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\29 0029.
 */

public class ConversationInfoBean {

  private String groupNum;
  private List<User> mList;
  private String groupName;
  private String groupNotice;
  private String saveState;

  public ConversationInfoBean(String groupNum,
      List<User> mList, String groupName, String groupNotice, String saveState) {
    this.groupNum = groupNum;
    this.mList = mList;
    this.groupName = groupName;
    this.groupNotice = groupNotice;
    this.saveState = saveState;
  }

  public String getGroupNum() {
    return groupNum;
  }

  public void setGroupNum(String groupNum) {
    this.groupNum = groupNum;
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
