package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017\6\28 0028.
 */

public class MyTextWatcher implements TextWatcher,RequestListener {

    private Context mContext;
    private String mUri;
    private List<RequestParams> params;
    private List resultList;

    public MyTextWatcher(Context context) {
        this.mContext = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //搜索框内容变化调后台接口查询结果
        if(s!=null&&s!=" "){
//            RequestNet requestNet = new RequestNet(params, mUri,this, RequestNet.GET);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        //

    }

    @Override
    public void testSuccess(String jsonObject) {

    }

    @Override
    public void onFail(String errorMsf) {

    }}
