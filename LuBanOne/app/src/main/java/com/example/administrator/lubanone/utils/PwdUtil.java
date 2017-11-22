package com.example.administrator.lubanone.utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017/6/27.
 */

public class PwdUtil {

    /**
     * 显示或者隐藏密码
     *
     * @param close
     * @param mEye
     * @param mPwd
     */
    public static void showOrHidePwdOfEt(boolean close, ImageView mEye, EditText mPwd) {
        if (!close) {
            mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEye.setImageResource(R.mipmap.eye_2x);
        } else {
            mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEye.setImageResource(R.mipmap.eye_close_2x);
        }
        close = !close;
        mPwd.postInvalidate();
    }


    /**
     * 显示或者隐藏密码
     *
     * @param close
     * @param mEye
     * @param mPwd
     */
    public static void showOrHidePwdOfTv(boolean close, ImageView mEye, TextView mPwd) {
        if (!close) {
            mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEye.setImageResource(R.mipmap.eye_2x);
        } else {
            mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEye.setImageResource(R.mipmap.eye_close_2x);
        }
        close = !close;
        mPwd.postInvalidate();
    }
}
