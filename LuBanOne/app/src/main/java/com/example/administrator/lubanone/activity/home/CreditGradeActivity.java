package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.UserCenterResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * 信用评分
 */
public class CreditGradeActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.tv_credit_grade)
  TextView mTvCreditGrade;
  @BindView(R.id.tv_credit_record)
  TextView mTvCreditRecord;
  @BindView(R.id.tv_credit_grade_system)
  TextView mTvCreditGradeSystem;
  @BindView(R.id.tv_always_problems)
  TextView mTvAlwaysProblems;
  @BindView(R.id.activity_user_level)
  LinearLayout mActivityUserLevel;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  private String mCredit;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_credit_grade;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      mCredit = intent.getStringExtra("credit");
      mTvCreditGrade.setText(mCredit);
    }
  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet lRequestNet = new RequestNet(myApp, this, list, Urls.USER_CENTER, mRequestListener,
          RequestNet.POST);
    } catch (DefineException e) {
      show(e.getMessage());
    }

  }

  RequestListener mRequestListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<UserCenterResultBean> result = GsonUtil
            .processJson(jsonObject, UserCenterResultBean.class);
        updataPage(result);
      } catch (Exception e) {
        show(getInfo(R.string.request_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      show(errorMsf);
    }
  };

  private void updataPage(Result<UserCenterResultBean> result) {
    if (null == result) {
      show(getInfo(R.string.request_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      adapterData(result);
    } else {
      show(ResultUtil.getErrorMsg(result));
    }
  }

  private void adapterData(Result<UserCenterResultBean> result) {

    //信用评分
    mTvCreditGrade.setText(StringUtil
        .getBufferString(DebugUtils.convert(result.getResult().getPingjia(), "0")));
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.iv_back, R.id.tv_credit_record, R.id.tv_credit_grade_system,
      R.id.tv_always_problems, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_credit_record:
        Intent intent = new Intent(this, CreditPointsActivity.class);//mCredit
        intent.putExtra("credit", DebugUtils.convert(mCredit, ""));
        startActivity(intent);
        break;
      case R.id.tv_credit_grade_system:
        startNewActivity(this, CreditGradeSystemActivity.class);
        break;
      case R.id.tv_always_problems:
        Intent intent1 = new Intent(this, AlwaysProblemsActivity.class);
        intent1.putExtra("key", "credit");
        startActivity(intent1);
        break;
      default:
        break;
    }
  }
}
