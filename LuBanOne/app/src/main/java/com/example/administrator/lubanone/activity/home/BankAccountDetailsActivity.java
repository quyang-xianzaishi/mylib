package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.UserCardInfoBean;

/**
 * 银行账户详情
 */
public class BankAccountDetailsActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_open_account_banl)
  TextView mTvOpenAccountBanl;
  @BindView(R.id.tv_account)
  TextView mTvAccount;
  @BindView(R.id.tv_name)
  TextView mTvName;
  @BindView(R.id.activity_bank_account_details)
  LinearLayout mActivityBankAccountDetails;
  private UserCardInfoBean mItem;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_bank_account_details;
  }

  @Override
  public void initView() {

    Intent intent = getIntent();
    if (null != intent) {
      Bundle extras = intent.getExtras();
      mItem = (UserCardInfoBean) extras.getSerializable("item");
    }

    String account = mTvAccount.getText().toString().trim();
    String name = mTvName.getText().toString().trim();
    String bank = mTvOpenAccountBanl.getText().toString().trim();

    //设置信息
    mTvOpenAccountBanl.setText(bank + mItem.getCardBand());
    mTvName.setText(name + mItem.getUserName());
    mTvAccount.setText(account + mItem.getAccountNumber());

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }
}
