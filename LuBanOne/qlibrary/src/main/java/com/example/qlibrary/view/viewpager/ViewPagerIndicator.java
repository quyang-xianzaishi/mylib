package com.example.qlibrary.view.viewpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;//画笔

    private int mTriangleWidth;//三角形宽度

    private int mTriangleHeight;//三角形高度

    private Path mPath;//3条线闭合

    private float raito = 1 / 6F;//三角形宽度位一个tab的六分之一

    private int mInitTraslationX;//三角形初始位置

    private int mTranslationX;//移动距离


    private int tabCount = 4;//tab个数


    private int cornerSize = 6;//三角形拐角大小


    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(new CornerPathEffect(cornerSize));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GRAY);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTraslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / tabCount * raito);
        mInitTraslationX = w / tabCount / 2 - mTriangleWidth / 2;
        setTrianle();
    }

    private void setTrianle() {
        if (mPath == null) {
            mPath = new Path();
        }
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleWidth / 2);
        mPath.close();
    }


    public float getRaito() {
        return raito;
    }

    public void setRaito(float raito) {
        this.raito = raito;
    }

    public int getTabCount() {
        return tabCount;
    }

    public void setTabCount(int tabCount) {
        this.tabCount = tabCount;
    }


    public void scroll(int position, float positionOffset) {
        int width = getWidth()/3;
        mTranslationX = (int) (width * positionOffset + position * width);
        invalidate();
    }
}
