package com.example.administrator.lubanone.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.WindoswUtil;
import com.jingchen.pulltorefresh.PullableListView;

/**
 * Created by Administrator on 2017/7/21.
 */

public class EmptyListView extends PullableListView {


  private Context mContext;
  private View mHeadView;

  public EmptyListView(Context context) {
    super(context);
    this.mContext = context;
    addHeader();
  }

  public EmptyListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    addHeader();
  }

  public EmptyListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.mContext = context;
    addHeader();
  }


  public void addHeader() {
    LayoutInflater inflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mHeadView = inflater.inflate(R.layout.empty_layout, null);
    ViewGroup.LayoutParams layoutParams = mHeadView.getLayoutParams();
    layoutParams.width = WindoswUtil.getWindowWidth((Activity) getContext());
    layoutParams.height = WindoswUtil.getWindowHeight((Activity) getContext());
    mHeadView.setLayoutParams(layoutParams);
    addHeaderView(mHeadView);
  }

  public View getHeadView() {
    return mHeadView;
  }
}
