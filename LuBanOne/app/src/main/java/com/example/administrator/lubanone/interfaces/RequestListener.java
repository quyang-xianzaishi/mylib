package com.example.administrator.lubanone.interfaces;

import org.json.JSONObject;

/**
 * Created by quyang on 2017/6/20.
 */

public interface RequestListener {


    @Deprecated
    void onSuccess(JSONObject jsonObject);

    void testSuccess(String jsonObject);

    void onFail(String errorMsf);


}
