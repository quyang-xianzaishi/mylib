package com.example.administrator.lubanone.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 *
 */
public class MyTeamCustomListView extends ListView {

  public static final int STATE_PULL_TO_REFRESH = 0;

  private int mCurrentState = STATE_PULL_TO_REFRESH;

  private int headerHeight;
  private float startY;
  private View headerView;
  private ImageView ivArrow;
  private ProgressBar pb;
  private TextView tvStatus;
  private TextView tvDate;
  private RotateAnimation animationUp;
  private RotateAnimation animationDown;
  private View mLoadLayout;

  private Context mContext;

  public MyTeamCustomListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initHeaderView();
  }

  public MyTeamCustomListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initHeaderView();
  }

  public MyTeamCustomListView(Context context) {
    super(context);
    mContext = context;
    initHeaderView();
  }

  private void initHeaderView() {
//    headerView = LayoutInflater.from(mContext).inflate(R.layout.my_team_head, this, false);
//    headerView.measure(0, 0);
//    headerHeight = headerView.getMeasuredHeight();
//    headerView.setPadding(0, 0, 0, 0);
//    this.addHeaderView(headerView);
  }

  public View getHeaderView() {
    return headerView;
  }



}
