package com.example.administrator.lubanone.utils;


import com.example.administrator.lubanone.R;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<T> {

    private int _level = -1;
    private List<Node> _childrenList = new ArrayList<>();
    private Node _parent;
    private int _icon;
    private boolean isExpand = false;
    //private String state;
    //private String reason;


    public abstract T get_id();
    public abstract T get_parentId();
    public abstract String get_label();
    public abstract boolean parent(Node dest);
    public abstract boolean child(Node dest);
    public abstract String get_grade();
    public abstract String get_state();
    public abstract String get_reason();
    public abstract String get_team_size();


    public int get_level() {
        if (_level == -1){
            int level = _parent == null ? 1 : _parent.get_level()+1;
            _level = level;
            return _level;
        }
        return _level;
    }

    public void set_level(int _level) {
        this._level = _level;
    }

    public List<Node> get_childrenList() {
        return _childrenList;
    }

    public void set_childrenList(List<Node> _childrenList) {
        this._childrenList = _childrenList;
    }

    public Node get_parent() {
        return _parent;
    }

    public void set_parent(Node _parent) {
        this._parent = _parent;
    }

    public int get_icon() {
        return _icon;
    }

    public void set_icon(int _icon) {
        this._icon = _icon;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (isExpand){
            _icon = R.mipmap.collapse;
        }else{
            _icon = R.mipmap.expand;
        }
    }

    public boolean isRoot(){
        return _parent == null;
    }

    public boolean isLeaf(){
        return _childrenList.size() <= 0;
    }

    /*public String getState() {
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
    }*/
}
