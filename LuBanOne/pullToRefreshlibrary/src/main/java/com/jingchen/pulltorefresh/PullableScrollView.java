package com.jingchen.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {

    private double scale = 0.4;               //速度因子
    private OnScrollListener onScrollListener;          //滑动监听

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight());
    }

    @Override
    public void fling(int velocityY) {
        velocityY *= scale;
        //此处改变速度，可根据需要变快或变慢。
        super.fling(velocityY);
    }

    public void setVelocityY(double scale) {
        this.scale = scale;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(l, t, oldl, oldt);
            //            Log.d("lius--", "onScrollChanged: " + l + "---" + t);
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.onScrollListener = listener;
    }

    public interface OnScrollListener {
        void onScroll(int x, int y, int oldX, int oldY);
    }
}
