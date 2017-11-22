package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class HomeFragmentResultBean {


  /**
   * personalinfo : {"ue_account":"imclown","ue_accname":null,"ue_money":"0.000","tj_he":"0","levelname":"M6","peixunfen":"10086","pingjia":"4.9","pai":"0","jhm":"0","mxjjc":"1888888","ue_truename":"ajkfldsjf","nick":"Wwwww","autograph":"Eeeeeeewwq","country":"安圭拉","image":"http://42.51.40.5/Public/infothumimg/d433b95e12ee781.png"}
   * tgbzlist : 0
   * p_paylist : 0
   * p_confirmlist : 0
   * p_dpingjialist : 0
   * jsbzlist : 0
   * g_paylist : 0
   * g_confirmlist : 0
   * g_dpingjialist : 0
   * userjjlist : [{"orderid":"2","jb":"11颗(22000000)","date":"2017-07-01 15:15:04","date_hk":"2017-07-01 15:15:51","date_su":"2017-07-01 15:16:09","g_user":"test02","gpingjia":"0","ppingjia":"0","zt":"成长中(计息中)","user_jj_ts":"6天","user_jj_lx":"0.66颗(1320000)","isshouge":1},{"orderid":"2","jb":"11颗(22000000)","date":"2017-07-01 15:15:04","date_hk":"2017-07-01 15:15:51","date_su":"2017-07-01 15:16:09","g_user":"test02","gpingjia":"0","ppingjia":"0","zt":"成长中(计息中)","user_jj_ts":"6天","user_jj_lx":"0.66颗(1320000)","isshouge":1},{"orderid":"2","jb":"11颗(22000000)","date":"2017-07-01 15:15:04","date_hk":"2017-07-01 15:15:51","date_su":"2017-07-01 15:16:09","g_user":"test02","gpingjia":"0","ppingjia":"0","zt":"成长中(计息中)","user_jj_ts":"6天","user_jj_lx":"0.66颗(1320000)","isshouge":1},{"orderid":"2","jb":"11颗(22000000)","date":"2017-07-01 15:15:04","date_hk":"2017-07-01 15:15:51","date_su":"2017-07-01 15:16:09","g_user":"test02","gpingjia":"0","ppingjia":"0","zt":"成长中(计息中)","user_jj_ts":"6天","user_jj_lx":"0.66颗(1320000)","isshouge":1},{"orderid":"2","jb":"11颗(22000000)","date":"2017-07-01 15:15:04","date_hk":"2017-07-01 15:15:51","date_su":"2017-07-01 15:16:09","g_user":"test02","gpingjia":"0","ppingjia":"0","zt":"成长中(计息中)","user_jj_ts":"6天","user_jj_lx":"0.66颗(1320000)","isshouge":1}]
   */

  private PersonalinfoBean personalinfo;
  private String tgbzlist;
  private String p_paylist;
  private String p_confirmlist;
  private String p_dpingjialist;
  private String jsbzlist;
  private String g_paylist;
  private String g_confirmlist;
  private String g_dpingjialist;
  private List<UserjjlistBean> userjjlist;

  public PersonalinfoBean getPersonalinfo() {
    return personalinfo;
  }

  public void setPersonalinfo(PersonalinfoBean personalinfo) {
    this.personalinfo = personalinfo;
  }

  public String getTgbzlist() {
    return tgbzlist;
  }

  public void setTgbzlist(String tgbzlist) {
    this.tgbzlist = tgbzlist;
  }

  public String getP_paylist() {
    return p_paylist;
  }

  public void setP_paylist(String p_paylist) {
    this.p_paylist = p_paylist;
  }

  public String getP_confirmlist() {
    return p_confirmlist;
  }

  public void setP_confirmlist(String p_confirmlist) {
    this.p_confirmlist = p_confirmlist;
  }

  public String getP_dpingjialist() {
    return p_dpingjialist;
  }

  public void setP_dpingjialist(String p_dpingjialist) {
    this.p_dpingjialist = p_dpingjialist;
  }

  public String getJsbzlist() {
    return jsbzlist;
  }

  public void setJsbzlist(String jsbzlist) {
    this.jsbzlist = jsbzlist;
  }

  public String getG_paylist() {
    return g_paylist;
  }

  public void setG_paylist(String g_paylist) {
    this.g_paylist = g_paylist;
  }

  public String getG_confirmlist() {
    return g_confirmlist;
  }

  public void setG_confirmlist(String g_confirmlist) {
    this.g_confirmlist = g_confirmlist;
  }

  public String getG_dpingjialist() {
    return g_dpingjialist;
  }

  public void setG_dpingjialist(String g_dpingjialist) {
    this.g_dpingjialist = g_dpingjialist;
  }

  public List<UserjjlistBean> getUserjjlist() {
    return userjjlist;
  }

  public void setUserjjlist(List<UserjjlistBean> userjjlist) {
    this.userjjlist = userjjlist;
  }

  public static class PersonalinfoBean {

    /**
     * ue_account : imclown
     * ue_accname : null
     * ue_money : 0.000
     * tj_he : 0
     * levelname : M6
     * peixunfen : 10086
     * pingjia : 4.9
     * pai : 0
     * jhm : 0
     * mxjjc : 1888888
     * ue_truename : ajkfldsjf
     * nick : Wwwww
     * autograph : Eeeeeeewwq
     * country : 安圭拉
     * image : http://42.51.40.5/Public/infothumimg/d433b95e12ee781.png
     */

    private String ue_account;
    private Object ue_accname;
    private String ue_money;
    private String tj_he;
    private String levelname;
    private String peixunfen;
    private String pingjia;
    private String pai;
    private String jhm;
    private String mxjjc;
    private String ue_truename;
    private String nick;
    private String autograph;
    private String country;
    private String image;

    public String getUe_account() {
      return ue_account;
    }

    public void setUe_account(String ue_account) {
      this.ue_account = ue_account;
    }

    public Object getUe_accname() {
      return ue_accname;
    }

    public void setUe_accname(Object ue_accname) {
      this.ue_accname = ue_accname;
    }

    public String getUe_money() {
      return ue_money;
    }

    public void setUe_money(String ue_money) {
      this.ue_money = ue_money;
    }

    public String getTj_he() {
      return tj_he;
    }

    public void setTj_he(String tj_he) {
      this.tj_he = tj_he;
    }

    public String getLevelname() {
      return levelname;
    }

    public void setLevelname(String levelname) {
      this.levelname = levelname;
    }

    public String getPeixunfen() {
      return peixunfen;
    }

    public void setPeixunfen(String peixunfen) {
      this.peixunfen = peixunfen;
    }

    public String getPingjia() {
      return pingjia;
    }

    public void setPingjia(String pingjia) {
      this.pingjia = pingjia;
    }

    public String getPai() {
      return pai;
    }

    public void setPai(String pai) {
      this.pai = pai;
    }

    public String getJhm() {
      return jhm;
    }

    public void setJhm(String jhm) {
      this.jhm = jhm;
    }

    public String getMxjjc() {
      return mxjjc;
    }

    public void setMxjjc(String mxjjc) {
      this.mxjjc = mxjjc;
    }

    public String getUe_truename() {
      return ue_truename;
    }

    public void setUe_truename(String ue_truename) {
      this.ue_truename = ue_truename;
    }

    public String getNick() {
      return nick;
    }

    public void setNick(String nick) {
      this.nick = nick;
    }

    public String getAutograph() {
      return autograph;
    }

    public void setAutograph(String autograph) {
      this.autograph = autograph;
    }

    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }
  }

  public static class UserjjlistBean {

    /**
     * orderid : 2
     * jb : 11颗(22000000)
     * date : 2017-07-01 15:15:04
     * date_hk : 2017-07-01 15:15:51
     * date_su : 2017-07-01 15:16:09
     * g_user : test02
     * gpingjia : 0
     * ppingjia : 0
     * zt : 成长中(计息中)
     * user_jj_ts : 6天
     * user_jj_lx : 0.66颗(1320000)
     * isshouge : 1
     */

    private String orderid;
    private String jb;
    private String date;
    private String date_hk;
    private String date_su;
    private String g_user;
    private String gpingjia;
    private String ppingjia;
    private String zt;
    private String user_jj_ts;
    private String user_jj_lx;
    private int isshouge;

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getJb() {
      return jb;
    }

    public void setJb(String jb) {
      this.jb = jb;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getDate_hk() {
      return date_hk;
    }

    public void setDate_hk(String date_hk) {
      this.date_hk = date_hk;
    }

    public String getDate_su() {
      return date_su;
    }

    public void setDate_su(String date_su) {
      this.date_su = date_su;
    }

    public String getG_user() {
      return g_user;
    }

    public void setG_user(String g_user) {
      this.g_user = g_user;
    }

    public String getGpingjia() {
      return gpingjia;
    }

    public void setGpingjia(String gpingjia) {
      this.gpingjia = gpingjia;
    }

    public String getPpingjia() {
      return ppingjia;
    }

    public void setPpingjia(String ppingjia) {
      this.ppingjia = ppingjia;
    }

    public String getZt() {
      return zt;
    }

    public void setZt(String zt) {
      this.zt = zt;
    }

    public String getUser_jj_ts() {
      return user_jj_ts;
    }

    public void setUser_jj_ts(String user_jj_ts) {
      this.user_jj_ts = user_jj_ts;
    }

    public String getUser_jj_lx() {
      return user_jj_lx;
    }

    public void setUser_jj_lx(String user_jj_lx) {
      this.user_jj_lx = user_jj_lx;
    }

    public int getIsshouge() {
      return isshouge;
    }

    public void setIsshouge(int isshouge) {
      this.isshouge = isshouge;
    }
  }
}
