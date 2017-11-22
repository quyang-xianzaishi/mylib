package com.example.administrator.lubanone.activity.land;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.landregister.UpdateLandPwdOne;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 忘记密码step one
 */
public class UpdatePwdStepOneActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.icon)
  ImageView mIcon;
  @BindView(R.id.textView9)
  TextView mTextView9;
  @BindView(R.id.et_user_name)
  EditText mEtUserName;
  @BindView(R.id.btn_ok)
  Button mBtnOk;
  @BindView(R.id.tv_send)
  TextView tv_send;
  @BindView(R.id.et_user_phone)
  EditText et_user_phone;
  @BindView(R.id.et_check_code)
  EditText et_check_code;

  private int seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;

  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<UpdateLandPwdOne> result = GsonUtil.processJson(jsonObject, UpdateLandPwdOne.class);
        commitResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.find_pwd_fails));
        hideCommitDataDialog();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.find_pwd_fails));
      hideCommitDataDialog();
    }
  };


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_update_pwd_step_one;
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


  @OnClick({R.id.iv_back, R.id.tv_back, R.id.btn_ok, R.id.tv_send})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.btn_ok:
          commit();
          break;
        case R.id.tv_send:
          getSMS();
          break;
      }
    } catch (Exception e) {
      showMsg(e.getMessage());
    }
  }


  //获取短信
  RequestListener mListenr = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        getSMEByToken(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.send_sms_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getInfo(R.string.send_sms_fail));
    }
  };


  private void getSMEByToken(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.send_sms_sucess));

      tv_send.setEnabled(false);
      Drawable drawable = DrableUtil.getDrawable(this, R.drawable.get_check_code_shape_no_select);
      ViewUtil.setBackground(tv_send, drawable);
      tv_send.setTextColor(ColorUtil.getColor(R.color.cb6b6b6, this));
      limitSeconds();
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.send_sms_fail)));
    }
  }

  private void limitSeconds() {

    CountDownTimer one = new CountDownTimer(62 * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {

        if (seconds == 0) {
          tv_send.setText(getString(R.string.reget_check_code));
          tv_send.setEnabled(true);
          Drawable drawable = DrableUtil
              .getDrawable(getApplicationContext(), R.drawable.get_check_code_shape);
          ViewUtil.setBackground(tv_send, drawable);
          tv_send
              .setTextColor(ColorUtil.getColor(R.color.cEA5412, getApplicationContext()));
          return;
        }
        if (null != tv_send && seconds >= 0) {
          tv_send.setText(seconds + getString(R.string.some_seconds));
        }
        seconds--;
      }

      @Override
      public void onFinish() {

      }
    };
    one.start();
  }


  private void getSMS() {
    try {
      judgeNet();

      if (TextUtils.isEmpty(et_user_phone.getText().toString().trim())) {
        showMsg(getString(R.string.phone_number_empty));
        return;
      }
      seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsPhone = new RequestParams("phone",
          et_user_phone.getText().toString().trim());
      list.add(paramsPhone);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SMS_BY_PHONE, mListenr,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_sms_fail));
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  private void commit() {
    try {
      judgeNet();
      checkParams();
      List<RequestParams> paramList = getParamList();
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.FIND_PWD,
          mCommitListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(
          TextUitl.isEmpty(e.getMessage()) ? getString(R.string.find_pwd_fails) : e.getMessage());
    }
  }

  private void commitResult(Result<UpdateLandPwdOne> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      if (result.getResult() != null) {
        Intent intent = new Intent(this, ForgetPwdFourActivity.class);
        intent.putExtra(Config.TOKEN, DebugUtils.convert(result.getResult().getToken(), ""));
        startActivity(intent);
      }
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.find_pwd_fails)));
    }
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsUserName = new RequestParams("account",
        mEtUserName.getText().toString().trim());

    RequestParams paramsPhone = new RequestParams("phone",
        et_user_phone.getText().toString().trim());
    RequestParams paramsphonecode = new RequestParams("phonecode",
        et_check_code.getText().toString().trim());

    list.add(paramsUserName);
    list.add(paramsPhone);
    list.add(paramsphonecode);
    return list;
  }

  private void checkParams() {
    if (TextUtils.isEmpty(mEtUserName.getText().toString().trim())) {
      throw new DefineException(getString(R.string.user_name_empty));
    }
    isEmptyWithException(et_user_phone.getText().toString().trim(),
        getString(R.string.phone_number_empty));

    isEmptyWithException(et_check_code.getText().toString().trim(),
        getString(R.string.check_code_empty));
  }
}
