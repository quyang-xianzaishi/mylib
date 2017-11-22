package com.example.administrator.lubanone.activity.register;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ToastUtil;

/**
 * 注册成功页面
 */
public class RegisterSuccessActivity extends BaseActivity {


  @Override
  protected void beforeSetContentView() {
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register_success;
  }

  @Override
  public void initView() {

    Button btnNext = (Button) findViewById(R.id.btn_next);
    TextView back = (TextView) this.findViewById(R.id.tv_back);

    btnNext.setOnClickListener(this);
    back.setOnClickListener(this);
  }

  @Override
  public void finish() {
    super.finish();
    MyApplication.getInstance().removeActivity(this);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_next:
          nextPage();
          break;
        case R.id.tv_back:
          RegisterSuccessActivity.this.finish();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void nextPage() {
    startNewActivityThenFinish(RegisterSuccessActivity.this, RegisterOneActivity.class);
  }
}
