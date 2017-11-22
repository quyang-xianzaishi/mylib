package com.example.qlibrary.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.qlibrary.R;

/**
 *
 */
public class CustomListView extends ListView {

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

  public CustomListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initHeaderView();
  }

  public CustomListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initHeaderView();
  }

  public CustomListView(Context context) {
    super(context);
    mContext = context;
    initHeaderView();
  }

  private void initHeaderView() {
    headerView = LayoutInflater.from(mContext).inflate(R.layout.header_item_listview, this, false);
    pb = (ProgressBar) headerView.findViewById(R.id.head_pb);
    tvStatus = (TextView) headerView.findViewById(R.id.head_tip);

    headerView.measure(0, 0);

    headerHeight = headerView.getMeasuredHeight();

    headerView.setPadding(0, 0, 0, 0);

    this.addHeaderView(headerView);

  }


}
