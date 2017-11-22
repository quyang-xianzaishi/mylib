package com.example.administrator.lubanone.bean.message;


import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2016/1/8.
 */
public class Address {
    private String name;
    private String letter;
    private String userId;
    private String userimg;

    private int icon = R.mipmap.ic_launcher;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }
}
