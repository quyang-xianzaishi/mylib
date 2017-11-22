package com.example.administrator.lubanone.activity.train;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * Created by hou on 2017/9/2.
 */

public class TrainRecordDetailsActivity extends BaseActivity {

  private LinearLayout backBtn;
  private TextView trainTheme;
  private TextView trainTime;
  private TextView trainNumber;
  private TextView trainStatus;
  private TextView trainScore;
  private RelativeLayout scoreLayout;

  private String mTheme;
  private String mTime;
  private String mNumber;
  private String mStatus;
  private String mScore;
  private String mReason;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_record_details;
  }

  @Override
  public void initView() {
    backBtn = (LinearLayout) findViewById(R.id.train_record_details_back_icon);
    trainTheme = (TextView) findViewById(R.id.train_record_details_theme);
    trainTime = (TextView) findViewById(R.id.train_record_details_time);
    trainNumber = (TextView) findViewById(R.id.train_record_details_number);
    trainStatus = (TextView) findViewById(R.id.train_record_details_status);
    trainScore = (TextView) findViewById(R.id.train_record_details_score);
    scoreLayout = (RelativeLayout) findViewById(R.id.train_record_details_score_layout);
    backBtn.setOnClickListener(this);
    Intent intent = getIntent();
    mTheme = intent.getStringExtra("theme");
    mTime = intent.getStringExtra("time");
    mNumber = intent.getStringExtra("number");
    mStatus = intent.getStringExtra("status");
    mScore = intent.getStringExtra("score");
    mReason = intent.getStringExtra("reason");

    trainTheme.setText(mTheme);
    trainTime.setText(mTime);
    trainNumber.setText(mNumber);
    switch (mStatus) {
      case "1":
        showPassDatas();
        break;
      case "2":
        showNoPassDatas();
        break;
    }

  }

  private void showPassDatas() {
    trainStatus.setText(getInfo(R.string.train_data_pass_apply));
    scoreLayout.setVisibility(View.VISIBLE);
    trainScore.setText(mScore + getInfo(R.string.score));
  }

  private void showNoPassDatas() {
    trainStatus.setTextColor(getResources().getColor(R.color.orange_ea5412));
    trainStatus.setText(getInfo(R.string.train_data_no_pass_apply1) + mReason + getInfo(
        R.string.train_data_no_pass_apply2));
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_record_details_back_icon:
        finish();
        break;
    }
  }
}
