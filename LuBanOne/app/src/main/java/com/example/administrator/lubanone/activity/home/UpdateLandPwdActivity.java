package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_old_land_pwd;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
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
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 修改登陆密码
 */
public class UpdateLandPwdActivity extends BaseActivity {

  private int seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (seconds == 0) {
        mTvSend.setText(getString(R.string.send));
        mTvSend.setEnabled(true);
        mHandler.removeCallbacksAndMessages(null);
        return;
      }
      if (mTvSend != null && seconds >= 0) {
        mTvSend.setText(seconds + "s");
      }
      seconds--;
    }
  };

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


  //修改密码
  RequestListener mUpdatePwdListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitData(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.update_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getInfo(R.string.update_fail));
    }
  };


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(tv_old_land_pwd)
  EditText mTvOldLandPwd;//旧密码
  @BindView(R.id.iv_old_eye)
  ImageView mIvOldEye;//eye 旧密码的
  @BindView(R.id.tv_new_land_pwd)
  EditText mTvNewLandPwd;//et 新密码
  @BindView(R.id.iv_new_eye)
  ImageView mIvNewEye;//eye 新密码
  @BindView(R.id.et_check_code)
  EditText mEtCheckCode;//et 验证吗
  @BindView(R.id.tv_send)
  TextView mTvSend;//发送
  @BindView(R.id.btn_confirm)
  Button mBtnConfirm;//btn 确认
  @BindView(R.id.tv_back)
  TextView tvBack;
  @BindView(R.id.tv_new_land_pwd_second)
  EditText tv_new_land_pwd_second;
  @BindView(R.id.rl_new_eye)
  RelativeLayout rl_new_eye;
  @BindView(R.id.rl_old_eye)
  RelativeLayout rl_old_eye;
  @BindView(R.id.iv_new_second_eye)
  ImageView iv_new_second_eye;
  @BindView(R.id.rl_new_second_eye)
  RelativeLayout rl_new_second_eye;


  private String oldPwd;//旧密码


  private boolean close;
  private boolean close1;
  private boolean confrim;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_update_land_pwd;
  }

  @Override
  public void initView() {
    mEtCheckCode.addTextChangedListener(new MyTextWatcher());
  }


  //监听et内容变化
  private class MyTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (mEtCheckCode.hasFocus() && TextUitl
          .isNotEmpty(mEtCheckCode.getText().toString().trim())) {
        ViewUtil.setBackground(mBtnConfirm,
            DrableUtil.getDrawable(getApplicationContext(), R.drawable.blue_bg));
      } else {
        ViewUtil.setBackground(mBtnConfirm,
            DrableUtil.getDrawable(getApplicationContext(), R.drawable.gray_bg));
      }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.iv_back, tv_old_land_pwd, R.id.iv_old_eye, R.id.tv_new_land_pwd,
      R.id.iv_new_eye, R.id.et_check_code, R.id.tv_send, R.id.btn_confirm, R.id.tv_back,
      R.id.rl_new_eye, R.id.rl_old_eye, R.id.rl_new_second_eye, R.id.iv_new_second_eye})
  public void onViewClicked(View view) {

    try {
      switch (view.getId()) {
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case tv_old_land_pwd:
          break;
        case R.id.iv_old_eye://tv_old_land_pwd
        case R.id.rl_old_eye://tv_old_land_pwd
          showOrHideOldPwd();
          break;
        case R.id.tv_new_land_pwd:
          break;
        case R.id.iv_new_eye://tv_new_land_pwd
        case R.id.rl_new_eye://tv_new_land_pwd
          showOrHideNewPwd();
          break;
        case R.id.et_check_code:
          break;
        case R.id.tv_send:
          getSMS();
          break;
        case R.id.btn_confirm:
          confirm();
          break;
        case R.id.iv_new_second_eye:
        case R.id.rl_new_second_eye:
          showOrHideConfirmPwd();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void showOrHideOldPwd() {
    if (!close) {
      mTvOldLandPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      mIvOldEye.setImageResource(R.mipmap.eye_2x);
    } else {
      mTvOldLandPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
      mIvOldEye.setImageResource(R.mipmap.eye_close_2x);
    }
    close = !close;
    mTvOldLandPwd.postInvalidate();//tv_old_land_pwd
    mTvOldLandPwd.setSelection(mTvOldLandPwd.getText().toString().trim().length());
  }

  private void showOrHideNewPwd() {
    if (!close1) {
      mTvNewLandPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      mIvNewEye.setImageResource(R.mipmap.eye_2x);
    } else {
      mTvNewLandPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
      mIvNewEye.setImageResource(R.mipmap.eye_close_2x);
    }
    close1 = !close1;
    mTvNewLandPwd.postInvalidate();
    mTvNewLandPwd.setSelection(mTvNewLandPwd.getText().toString().trim().length());
  }


  private void showOrHideConfirmPwd() {
    if (!confrim) {
      tv_new_land_pwd_second.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      iv_new_second_eye.setImageResource(R.mipmap.eye_2x);
    } else {
      tv_new_land_pwd_second.setTransformationMethod(PasswordTransformationMethod.getInstance());
      iv_new_second_eye.setImageResource(R.mipmap.eye_close_2x);
    }
    confrim = !confrim;
    tv_new_land_pwd_second.postInvalidate();
    tv_new_land_pwd_second
        .setSelection(tv_new_land_pwd_second.getText().toString().trim().length());
  }


  private void confirm() {
    judgeNet();

    String newPwd = mTvNewLandPwd.getText().toString().trim();
    String comfrimPwd = tv_new_land_pwd_second.getText().toString().trim();
    String checkCode = mEtCheckCode.getText().toString().trim();
    String oldPwd = mTvOldLandPwd.getText().toString().trim();

    isEmptyWithException(oldPwd, getString(R.string.old_pwd_empty));
    isEmptyWithException(comfrimPwd, getString(R.string.comfim_pwd_empty));
    isEmptyWithException(newPwd, getString(R.string.pwd_empty));
    isEmptyWithException(oldPwd, getString(R.string.old_pwd_empty));
    isFalseWithException(newPwd.length() < 8, getString(R.string.pwd_8_less));
    isFalseWithException(newPwd.equals(this.oldPwd), getString(R.string.old_new_pwd_equal));
    isEmptyWithException(checkCode, getString(R.string.check_code_empty));
    if (!StringUtil.justNumber(checkCode)) {
      showMsg(getString(R.string.check_code_only_number));
      return;
    }

    ArrayList<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPwd = new RequestParams("newpassword", newPwd);
    RequestParams paramsCheckCode = new RequestParams("phonecode", checkCode);
    RequestParams paramsOldPwd = new RequestParams("password", oldPwd);
    RequestParams paramsNewPwd = new RequestParams("renewpassword", comfrimPwd);
    list.add(paramsToken);
    list.add(paramsPwd);
    list.add(paramsNewPwd);
    list.add(paramsCheckCode);
    list.add(paramsOldPwd);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.UPDATE_LAND_PWD,
        mUpdatePwdListener, RequestNet.POST);

  }

  @Override
  public String setTip() {
    return getString(R.string.updating);
  }

  private void getSMS() {
    try {
      seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SMS_BY_TOKNE, mListenr,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_sms_fail));
    }
  }


  private void getSMEByToken(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.send_sms_sucess));

      mTvSend.setEnabled(false);
      Drawable drawable = DrableUtil.getDrawable(this, R.drawable.get_check_code_shape_no_select);
      ViewUtil.setBackground(mTvSend, drawable);
      mTvSend.setTextColor(ColorUtil.getColor(R.color.cb6b6b6, this));
      limitSeconds();
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.send_sms_fail)));
    }
  }

//  private void limitSeconds() {
//    CountDownTimer one = new CountDownTimer(62 * 1000, 1000) {
//      @Override
//      public void onTick(long millisUntilFinished) {
//        if (seconds == 0) {
//          mTvSend.setText(getString(R.string.send));
//          mTvSend.setEnabled(true);
//          mHandler.removeCallbacksAndMessages(null);
//          return;
//        }
//        if (mTvSend != null && seconds >= 0) {
//          mTvSend.setText(seconds + "s");
//        }
//        seconds--;
//      }
//
//      @Override
//      public void onFinish() {
//
//      }
//    };
//    one.start();
//  }

  private void limitSeconds() {

    CountDownTimer one = new CountDownTimer(62 * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {

        if (seconds == 0) {
          mTvSend.setText(getString(R.string.reget_check_code));
          mTvSend.setEnabled(true);
          Drawable drawable = DrableUtil
              .getDrawable(getApplicationContext(), R.drawable.get_check_code_shape);
          ViewUtil.setBackground(mTvSend, drawable);
          mTvSend
              .setTextColor(ColorUtil.getColor(R.color.cEA5412, getApplicationContext()));
          return;
        }
        if (null != mTvSend && seconds >= 0) {
          mTvSend.setText(seconds + getString(R.string.some_seconds));
        }
        seconds--;
      }

      @Override
      public void onFinish() {

      }
    };
    one.start();
  }


  private void commitData(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.update_success));
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.update_fail)));
    }
  }

}
