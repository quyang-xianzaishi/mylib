package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.message.CustomMessageActivity;
import io.rong.imlib.model.CSCustomServiceInfo;

/**
 * 捐赠页面
 */
public class ThankActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.tv_servicer)
  ImageView mTvServicer;
  @BindView(R.id.activity_thank)
  LinearLayout mActivityThank;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_thank;
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


  @OnClick({R.id.iv_back, R.id.tv_back,R.id.tv_servicer})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_servicer:
        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
        CSCustomServiceInfo csInfo = csBuilder.nickName(getString(R.string.rongyun_nickname)).build();
        Intent intent4 = new Intent();
        intent4.putExtra("customServiceInfo",csInfo);
        intent4.setClass(this, CustomMessageActivity.class);
        startActivity(intent4);
        break;
      default:
        break;
    }
  }
}
