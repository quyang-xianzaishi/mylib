package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\8\1 0001.
 */

public class SearchResultBean {

  private List<SearchFriendBean> data;

  public List<SearchFriendBean> getList() {
    return data;
  }

  public void setList(
      List<SearchFriendBean> list) {
    data = list;
  }
}
