package com.example.administrator.lubanone.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017\6\22 0022.
 */

public class TrainingParger extends ViewPager {

    /**
     * 描述：复写子ViewPager，解决在低版本下ViewPager嵌套的问题
     */

    private float startX;
    private float startY;

    public TrainingParger(Context context) {
        super(context);
    }

    public TrainingParger(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是左右滑动还是上下滑动
                float endX = ev.getRawX();
                float endY = ev.getRawY();
                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {
                    //如果是左右滑动，请求父控件不要拦截自己的
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    //如果是上下滑动，拦截设置为false
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

}
