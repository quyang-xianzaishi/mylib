package com.example.administrator.lubanone.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by hou on 2017/9/1.
 */

public class ScollListView extends ListView {

  public ScollListView(Context context) {
    super(context);
  }

  public ScollListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScollListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
        , MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
