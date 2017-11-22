package com.example.administrator.lubanone.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.example.administrator.lubanone.R;
import com.jingchen.pulltorefresh.PullableListView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class NewHomeFragmentListView extends PullableListView {

  private Context mContext;

  public NewHomeFragmentListView(Context context) {
    super(context);
    this.mContext = context;
  }

  public NewHomeFragmentListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
  }

  public NewHomeFragmentListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.mContext = context;
  }


  public void addHeader() {
    LayoutInflater inflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View headView = inflater.inflate(R.layout.listview_head, null);
    addHeaderView(headView);
  }
}
