package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.activity.us.UsMainActivity;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.DebugUtils;
import java.util.ArrayList;

/**
 * 培训积分
 */
public class TrainCreditActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_train_credits)
  TextView mTvCurrentCredit;
  @BindView(R.id.tv_train_system)
  TextView mTvTrainSystem;
  @BindView(R.id.tv_always_problems)
  TextView mTvAlwaysProblems;
  @BindView(R.id.tv_credit_list)
  TextView mTvCreditList;
  @BindView(R.id.activity_train_credit)
  LinearLayout mActivityTrainCredit;
  private String mType;
  @BindView(R.id.tv_back)
  TextView mTvBack;

  @BindView(R.id.iv_get)
  TextView mIvGet;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_credit;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      mType = intent.getStringExtra(Config.TRAIN_CREDIT_KEY);
      updatePage();
    }
  }

  private void updatePage() {

    mTvCurrentCredit.setText(mType);

  }


  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.tv_train_system, R.id.tv_always_problems, R.id.tv_credit_list,
      R.id.iv_get, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_train_system:
        startNewActivity(this, TrainSystemActivity.class);
        break;
      case R.id.tv_always_problems:
        Intent intent1 = new Intent(this, AlwaysProblemsActivity.class);
        intent1.putExtra("key", "train");
        startActivity(intent1);
        break;
      case R.id.tv_credit_list:
        Intent intent = new Intent(this, CreditRecordActivity.class);
        intent.putExtra("credit_current",
            DebugUtils.convert(mTvCurrentCredit.getText().toString().trim(), ""));
        startActivity(intent);
        break;
      case R.id.iv_get:
        showSelectDialog();
        break;
      default:
        break;
    }
  }

  private void showSelectDialog() {

    ArrayList<String> list = new ArrayList<>();
    list.add(getString(R.string.complete_exam));
    list.add(getString(R.string.apply_train));
    list.add(getString(R.string.upload_material));
    StytledDialog.showBottomItemDialog(this, list, getString(R.string.cancels), true, true,
        new MyItemDialogListener() {
          @Override
          public void onItemClick(String text, int position) {
            openActivityByPosition(position);
          }
        });
  }

  private void openActivityByPosition(int position) {
    if (0 == position) {
      Intent intent = new Intent(this, MainActivity.class);
      intent.setType(Config.TRAIN_CREDIT_EXAM_KEY);
      startActivity(intent);
      finish();
    } else if (2 == position) {
      Intent intent = new Intent(this, UsMainActivity.class);
      intent.setType(Config.TRAIN_CREDIT_UPLOAD_KEY);
      startActivity(intent);
      finish();
    } else if (1 == position) {
      Intent intent = new Intent(this, MainActivity.class);
      intent.setType(Config.TRAIN_CREDIT_TRAIN_KEY);
      startActivity(intent);
      finish();
    }
  }
}
