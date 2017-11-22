package com.example.administrator.lubanone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by zheng on 2017\6\27 0027.
 */

public class UserPopupWindow  extends PopupWindow {

    private Context mContext;
    private View mView;
    private View.OnClickListener mOnClickListener;
    private int width;

    private Window dialogWindow;
    private WindowManager mindowManager;


    public UserPopupWindow(Activity mContext, XmlResourceParser resource){
        this.mContext = mContext;
        this.mView = LayoutInflater.from(mContext).inflate(resource, null);
        dialogWindow = mContext.getWindow();
        mindowManager = mContext.getWindowManager();
        initViews();
    }
    public UserPopupWindow(Activity mContext, XmlResourceParser resource,int width){
        this.mContext = mContext;
        this.mView = LayoutInflater.from(mContext).inflate(resource, null);
        this.width = width;
        dialogWindow = mContext.getWindow();
        mindowManager = mContext.getWindowManager();
        initViews();
    }

    private void initViews(){
        this.setContentView(this.mView);

        Display d = mindowManager.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(width!=0){
            this.setWidth((int) (width));
        }else {
            this.setWidth((int) (d.getWidth() * 0.8));
        }
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 设置外部可点击
        this.setOutsideTouchable(true);
    }

    public View getView() {
        return mView;
    }

    public void setOnClickListener(View view,View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        view.setOnClickListener(mOnClickListener);
    }

    public void showUserPopupWindow(View parent) {
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

}
