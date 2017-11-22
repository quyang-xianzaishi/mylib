package com.example.administrator.lubanone.activity.register;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.home.AccountCenterActivity;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ToastUtil;

/**
 * 账户激活
 */
public class AccountActiveActivity extends BaseActivity {

  private Button mSet;
  private Button mMainPage;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_account_active;
  }

  @Override
  public void initView() {

    mSet = (Button) findViewById(R.id.go_to_set);
    mMainPage = (Button) findViewById(R.id.go_to_main_page);
    mSet.setOnClickListener(this);
    mMainPage.setOnClickListener(this);

  }


  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.go_to_set:
          go2Set();
          break;
        case R.id.go_to_main_page:
          go2MainPage();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }

  }

  private void go2MainPage() {
    myApp.removeAllActivity();
    startNewActivityThenFinish(AccountActiveActivity.this, MainActivity.class);
  }


  private void go2Set() {
    myApp.removeAllActivity();
    Intent intent = new Intent(this, AccountCenterActivity.class);
    intent.putExtra("register", "register");
    startActivity(intent);
    finish();
  }


  @Override
  public void finish() {
    super.finish();
  }
}
