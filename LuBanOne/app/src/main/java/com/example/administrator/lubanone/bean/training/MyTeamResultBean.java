package com.example.administrator.lubanone.bean.training;

import com.example.administrator.lubanone.bean.Member;
import java.util.List;

/**
 * Created by Administrator on 2017\7\10 0010.
 */

public class MyTeamResultBean {

  private String teamsize;
  private String leader;
  private String level;
  private List<Member> list;


  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getTeamsize() {
    return teamsize;
  }

  public void setTeamsize(String teamsize) {
    this.teamsize = teamsize;
  }

  public String getLeader() {
    return leader;
  }

  public void setLeader(String leader) {
    this.leader = leader;
  }

  public List<Member> getList() {
    return list;
  }

  public void setList(List<Member> list) {
    this.list = list;
  }
}
