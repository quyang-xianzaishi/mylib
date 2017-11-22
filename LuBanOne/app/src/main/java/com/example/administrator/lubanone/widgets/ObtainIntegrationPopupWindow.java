package com.example.administrator.lubanone.widgets;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class ObtainIntegrationPopupWindow extends PopupWindow {

    private View mainView;
    private LinearLayout completeExam,applyTrain, uploadMaterial;

    public ObtainIntegrationPopupWindow(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.obtain_integration_popup_window, null);
        completeExam = ((LinearLayout)mainView.findViewById(R.id.complete_exam));
        applyTrain = (LinearLayout)mainView.findViewById(R.id.apply_train);
        uploadMaterial = (LinearLayout)mainView.findViewById(R.id.upload_material);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            completeExam.setOnClickListener(paramOnClickListener);
            applyTrain.setOnClickListener(paramOnClickListener);
            uploadMaterial.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }
}
