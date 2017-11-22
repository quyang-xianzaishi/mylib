package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * 密码管理
 */
public class ManagerPwdActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.up)
  TextView mUp;
  @BindView(R.id.down)
  TextView mDown;
  @BindView(R.id.activity_manager_pwd)
  RelativeLayout mActivityManagerPwd;
  @BindView(R.id.tv_back)
  TextView mTvBack;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_manager_pwd;
  }

  @Override
  public void initView() {
  }

  @Override
  public void loadData() {


  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.up, R.id.down, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.up:
        startNewActivity(ManagerPwdActivity.this, UpdateLandPwdActivity.class);
        break;
      case R.id.down:
        startNewActivity(ManagerPwdActivity.this, UpdatePayPwdActivity.class);
        break;
      default:
        break;
    }
  }
}
