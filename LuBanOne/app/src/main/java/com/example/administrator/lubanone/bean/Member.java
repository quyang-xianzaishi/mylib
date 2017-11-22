package com.example.administrator.lubanone.bean;

import com.example.administrator.lubanone.utils.Node;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

/**
 * Created by HQOCSHheqing on 2016/8/2.
 *
 * @description 成员类（继承Node），此处的泛型Integer是因为ID和parentID都为int
 * ，如果为String传入泛型String即可
 */
public class Member extends Node<String> {

    private String user_id;//成员ID
    private String p_id;//父节点ID
    private String username;//成员名称
    private String usertruename;//成员真实名称
    private String grade;//成员等级
    private String state;//成员状态
    private String reason;//原因
    private String level;
    private String teamsize;

    public Member() {
    }

    public Member(String user_id, String p_id, String usertruename, String grade) {
        this.user_id = user_id;
        this.p_id = p_id;
        this.usertruename = usertruename;
        this.grade = grade;
    }

    /**
     * 此处返回节点ID
     * @return
     */
    @Override
    public String get_id() {
        return user_id;
    }

    /**
     * 此处返回父亲节点ID
     * @return
     */
    @Override
    public String get_parentId() {
        return p_id;
    }

    @Override
    public String get_label() {
        if(usertruename!=null&&usertruename.length()>0){
            return grade+"  "+usertruename;
        }else{
            return grade+"  "+username;
        }
    }

    @Override
    public boolean parent(Node dest) {
        if (user_id.equals(dest.get_parentId())){
            return true;
        }
        return false;
    }

    @Override
    public boolean child(Node dest) {
        if (p_id.equals(dest.get_id())){
            return true;
        }
        return false;
    }

    @Override
    public String get_grade() {
        return grade;
    }

    @Override
    public String get_state() {
        return state;
    }

    @Override
    public String get_reason() {
        return reason;
    }

    @Override
    public String get_team_size(){
        return teamsize;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertruename() {
        return usertruename;
    }

    public void setUsertruename(String usertruename) {
        this.usertruename = usertruename;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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
}
