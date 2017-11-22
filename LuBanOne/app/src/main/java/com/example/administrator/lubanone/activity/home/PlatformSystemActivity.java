package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * 平台制度
 */
public class PlatformSystemActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.activity_platform_system)
  LinearLayout mActivityPlatformSystem;
  @BindView(R.id.tv_back)
  TextView tvBack;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_platform_system;
  }

  @Override
  public void initView() {

    tvBack.setOnClickListener(this);

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }
  }


  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }
}
