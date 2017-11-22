package com.example.administrator.lubanone.widgets;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class AddFriendPopupWindow extends PopupWindow {

    private View mainView;
    private LinearLayout addFriend, groupChat;

    public AddFriendPopupWindow(final Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.add_friend_popup_window, null);
        //添加好友布局
        addFriend = ((LinearLayout)mainView.findViewById(R.id.group_chat));
        //发起群聊布局
        groupChat = (LinearLayout)mainView.findViewById(R.id.add_friend);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            addFriend.setOnClickListener(paramOnClickListener);
            groupChat.setOnClickListener(paramOnClickListener);
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
        WindowManager.LayoutParams params=paramActivity.getWindow().getAttributes();
        params.alpha=0.7f;
        paramActivity.getWindow().setAttributes(params);
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = paramActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                paramActivity.getWindow().setAttributes(lp);
            }
        });
    }


}
