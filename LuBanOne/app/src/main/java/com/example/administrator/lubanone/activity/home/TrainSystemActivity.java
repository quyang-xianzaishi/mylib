package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.activity.train.TrainActivity;
import com.example.administrator.lubanone.activity.us.UsMainActivity;

/**
 * 培训制度
 */
public class TrainSystemActivity extends BaseActivity {

  @BindView(R.id.tv_tip_one)
  TextView mTvTipOne;
  @BindView(R.id.tv_tip_two)
  TextView mTvTipTwo;
  @BindView(R.id.btn_exam)
  Button mBtnExam;
  @BindView(R.id.btn_train)
  Button mBtnTrain;
  @BindView(R.id.activity_train_system)
  LinearLayout mActivityTrainSystem;
  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView ivBack;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_system;
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

  @OnClick({R.id.btn_exam, R.id.btn_train, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_exam:
        //考试与培训（培训中心）
        /*Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("train_system", "train_system");
        startActivity(intent);*/
        Intent intent = new Intent(this, TrainActivity.class);
        startActivity(intent);
        finish();
        break;
      case R.id.btn_train:
        //推广资料
        /*Intent intent1 = new Intent(this, MainActivity.class);
        intent1.setType(Config.TRAIN_CREDIT_TRAIN_KEY);
        startActivity(intent1);*/
        Intent intent1 = new Intent(this, UsMainActivity.class);
        intent1.setType(Config.UPLOAD_SPREAD_INFO);
        startActivity(intent1);
        finish();
        break;
      case R.id.tv_back:
      case R.id.iv_back:
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
