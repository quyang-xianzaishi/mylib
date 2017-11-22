package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

public class GrowingActivity extends BaseActivity {

  @BindView(R.id.back)
  FrameLayout mBack;
  @BindView(R.id.tv_head)
  TextView mTvHead;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_growing;
  }

  @Override
  public void initView() {

  }

  @Override
  public void loadData() {
    try {




    } catch (Exception e) {

    }

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick(R.id.back)
  public void onViewClicked() {
    finish();
  }
}
