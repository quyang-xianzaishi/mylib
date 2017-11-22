package com.example.administrator.lubanone.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017/7/10.
 */

public class MyList extends ListView {


  public MyList(Context context) {
    super(context);
    init();
  }

  public MyList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyList(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public MyList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  int footerHeight;

  public void init() {
    View inflate = View.inflate(getContext(), R.layout.home_page_lv_item, null);

    inflate.measure(0, 0);// 手动测量
    footerHeight = inflate.getMeasuredHeight();

    inflate.setPadding(0, 0, 0, 0);
    addHeaderView(inflate);
  }
}
