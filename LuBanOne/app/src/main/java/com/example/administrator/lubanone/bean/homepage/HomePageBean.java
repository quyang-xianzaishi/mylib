package com.example.administrator.lubanone.bean.homepage;

import org.json.JSONObject;

/**
 * Created by quyang on 2017/6/23.
 */

public class HomePageBean {

    private String name;
    private String pwd;
    private String sex;
    private String mone;


    public HomePageBean(JSONObject jsonObject) {
        name = jsonObject.optString("name", "");
        pwd = jsonObject.optString("pwd", "");
        sex = jsonObject.optString("sex", "");
        mone = jsonObject.optString("mone", "");
    }
}
