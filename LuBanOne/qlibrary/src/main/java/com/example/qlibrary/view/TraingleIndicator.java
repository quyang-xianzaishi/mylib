package com.example.qlibrary.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Build.VERSION_CODES;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/8/27.
 */

public class TraingleIndicator extends LinearLayout {

  private Paint mPaint;

  private int startX;

  private int tagCount = 4;

  private ViewPager mViewPager;

  private int indicatorHeight = 5;

  private float RADIO = 1 / 5f;//三角形指示器的宽的一半，设置为每一个item的一半得五分之一

  public void setIndicatorCount(int count) {
    this.tagCount = count;
  }

  public void setRadio(float radio) {
    this.RADIO = radio;
  }

  public TraingleIndicator(Context context) {
    super(context);
    init();
  }

  public TraingleIndicator(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TraingleIndicator(Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public TraingleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public void bindIndicator(ViewPager viewPager) {
    this.mViewPager = viewPager;
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(Color.RED);
    mPaint.setStyle(Style.STROKE);
    mPaint.setStrokeWidth(5f);
    mPaint.setPathEffect(new CornerPathEffect(3f));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Path path = new Path();
    path.moveTo(startX, getHeight());
    path.lineTo(getTagWitdh() / 2 + startX - getTagWitdh() / 2 * RADIO, getHeight());
    path.lineTo(getTagWitdh() / 2 + startX, getTagHeight());
    path.lineTo(getTagWitdh() / 2 + startX + getTagWitdh() / 2 * RADIO, getHeight());
    path.lineTo(getTagWitdh() + startX, getHeight());
    canvas.drawPath(path, mPaint);
  }


  private int getTagWitdh() {
    int i = getWidth() / 4;
    return i;
  }

  private int getTagHeight() {
    int i = (int) (getHeight() - indicatorHeight*3);
//    int i = (int) (getHeight() - getHeight() * RADIO);
    return i;
  }


  //暴露给viewpage
  public void scroll(int position, float positionOffset) {
    int width = getWidth() / 4;
    startX = (int) (width * (position + positionOffset));
    invalidate();
  }


  public void setOnItemClick() {

    if (null == mViewPager) {
      throw new RuntimeException("not yet bind viewpager");
    }

    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final int m = i;
      View childAt = getChildAt(i);
      childAt.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mViewPager.setCurrentItem(m);
        }
      });
    }
  }
}
