package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

public class ReceiveMoneyInfoActivity extends BaseActivity {


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_receive_money_info;
  }

  @Override
  public void initView() {

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    tvBack.setOnClickListener(this);
    ivBack.setOnClickListener(this);

    Intent intent = getIntent();
    if (null != intent) {
      String vip = intent.getStringExtra("vip");
      getVIPInfo(vip);
    }
  }

  private void getVIPInfo(String vip) {

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }
  }
}
