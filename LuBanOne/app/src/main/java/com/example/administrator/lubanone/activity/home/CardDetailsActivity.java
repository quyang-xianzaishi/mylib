package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_bank_name;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.BindCardResultBean.BankaccountlistBean;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.SPUtils;

/**
 * 银行卡详情
 */
public class CardDetailsActivity extends BaseActivity {


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.person)
  TextView mPerson;
  @BindView(tv_bank_name)
  TextView mTvBankName;
  @BindView(R.id.tv_bank_account)
  TextView mTvBankAccount;
  @BindView(R.id.bank_code)
  TextView mBankCode;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.delete)
  Button mDelete;
  private String mBankid;
  private String mPhone;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_card_details;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      Bundle bundle = intent.getBundleExtra("bundle");
      int position = bundle.getInt("position");
      BankaccountlistBean item = (BankaccountlistBean) bundle.getSerializable("item");
      updatePage(position, item);
    }
  }

  private void updatePage(int position, BankaccountlistBean item) {

    //持卡人
    mPerson.setText(SPUtils.getStringValue(this, Config.USER_INFO, Config.REAL_NAME, ""));

    //银行名称
    mTvBankName.setText(DebugUtils.convert(item.getBankname(), ""));

    //银行账号
    mTvBankAccount.setText(DebugUtils.convert(item.getBankaccount(), ""));

    //银行code
    mBankCode.setText(DebugUtils.convert(item.getBankcode(), ""));

    mBankid = item.getBankid();
    mPhone = item.getPhone();
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.tv_back) {
      finish();
    }
  }


  @OnClick({R.id.back, R.id.delete})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case R.id.delete:
        toCheckCodePage();
//        deleteCard();
        break;
    }
  }

  private void toCheckCodePage() {
    Intent intent = new Intent(this, CheckCodeActivity.class);
    intent.putExtra("bankid", DebugUtils.convert(mBankid, ""));
    intent.putExtra("phone", DebugUtils.convert(mPhone, ""));
    startActivity(intent);
  }


}
