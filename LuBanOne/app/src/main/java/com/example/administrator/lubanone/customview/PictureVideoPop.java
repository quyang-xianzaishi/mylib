package com.example.administrator.lubanone.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;

/**
 * Created by hou on 2017/7/10.
 */

public class PictureVideoPop extends PopupWindow {
  private Context mContext;

  private View view;

  private TextView btn_cancel;
  private TextView addPicture;
  private TextView addVideo;


  public PictureVideoPop(Context mContext, View.OnClickListener itemsOnClick) {

    this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_us_spread_bottom_layout, null);

    addPicture = (TextView) view.findViewById(R.id.us_spread_pop_add_picture);
    addVideo = (TextView) view.findViewById(R.id.us_spread_pop_add_video);
    btn_cancel = (TextView) view.findViewById(R.id.us_spread_pop_cancel);
    // 取消按钮
    btn_cancel.setOnClickListener(new View.OnClickListener() {

      public void onClick(View v) {
        // 销毁弹出框
        dismiss();
      }
    });
    // 设置按钮监听
    addVideo.setOnClickListener(itemsOnClick);
    addPicture.setOnClickListener(itemsOnClick);

    // 设置外部可点击
    this.setOutsideTouchable(true);
    // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
    this.view.setOnTouchListener(new View.OnTouchListener() {

      public boolean onTouch(View v, MotionEvent event) {

        int height = view.findViewById(R.id.us_spread_pop_ll).getTop();

        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
          if (y < height) {
            dismiss();
          }
        }
        return true;
      }
    });


    /* 设置弹出窗口特征 */
    // 设置视图
    this.setContentView(this.view);
    // 设置弹出窗体的宽和高
    this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
    this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

    // 设置弹出窗体可点击
    this.setFocusable(true);

//    // 实例化一个ColorDrawable颜色为半透明
//    ColorDrawable dw = new ColorDrawable(0xb0000000);
    // 设置弹出窗体的背景
//    this.setBackgroundDrawable(dw);

    // 设置弹出窗体显示时的动画，从底部向上弹出
    this.setAnimationStyle(R.style.pop_bottom_anim);

  }
}
