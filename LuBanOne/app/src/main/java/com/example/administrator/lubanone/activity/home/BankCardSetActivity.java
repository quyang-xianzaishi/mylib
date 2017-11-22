package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * quyang 修改银行卡 过时
 */
public class BankCardSetActivity extends BaseActivity {

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_bank_card_set;
  }

  @Override
  public void initView() {

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    tvBack.setOnClickListener(this);
    ivBack.setOnClickListener(this);

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
      case R.id.iv_back:
        finish();
        break;
      default:
        break;

    }
  }
}
