package com.example.administrator.lubanone.activity.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.home.GuiZeActivity;
import com.example.administrator.lubanone.activity.home.ProtocolActivity;
import com.example.administrator.lubanone.bean.homepage.CountryResultBean;
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
import com.example.qlibrary.utils.PhoneUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {

  private EditText mUserNameInput;
  private EditText mUserPwdInput;
  private EditText mUserPhoneInput;
  private EditText mUserCodeInput;
  private Button mNewAccount;
  private ImageView mIvBack;
  private TextView mSendSMS;
  private TextView mTip;
  private TextView tv_back;
  private TextView tip_right;


  private int countryType;
  private TextView mTvCountryNumber;
  private boolean selected;


  private RequestListener mCountryListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CountryResultBean> beanResult = GsonUtil
            .processJson(jsonObject, CountryResultBean.class);
        updateCountry(beanResult);
      } catch (Exception e) {
        hideLandingDialog();
        showMsg(getInfo(R.string.get_country_list));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideLandingDialog();
      showMsg(getInfo(R.string.get_country_list));
    }
  };


  //注册获取验证码
  private RequestListener mSMSListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Log.e(TAG, "testSuccess=" + jsonObject);
        Result<String> beanResult = GsonUtil.processJson(jsonObject, String.class);
        updatePage(beanResult);
      } catch (Exception e) {
        showMsg(getInfo(R.string.send_sms_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getInfo(R.string.send_sms_fail));
    }
  };

  //注册
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> registerBeanResult = GsonUtil
            .processJson(jsonObject, Object.class);
        updatePageRegister(registerBeanResult);
      } catch (Exception e) {
        showMsg(getInfo(R.string.GET_DATE_FAIL));
        hideCommitDataDialog();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getInfo(R.string.GET_DATE_FAIL));
    }
  };
  private ImageView mIvIcon;

  private void updatePageRegister(Result<Object> registerBean) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(registerBean)) {
      SPUtils.putStringValue(this, Config.USER_INFO, "phone",
          mUserPhoneInput.getText().toString().trim());
      SPUtils.putStringValue(this, Config.USER_INFO, "account",
          mUserNameInput.getText().toString().trim());
      SPUtils.putStringValue(this, Config.USER_INFO, "password",
          mUserPwdInput.getText().toString().trim());
      startNewActivityThenFinish(RegisterActivity.this, RegisterSuccessActivity.class);
    } else {
      showMsg("".equals(registerBean.getMsg()) ? getString(R.string.register_fail) : registerBean.getMsg());
    }
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register;
  }

  @Override
  public void initView() {
    mIvIcon = (ImageView) findViewById(R.id.iv_icon);
    mIvIcon.setOnClickListener(this);

    tip_right = (TextView) findViewById(R.id.tip_right);
    mUserNameInput = (EditText) findViewById(R.id.user_name_input);
    mUserPwdInput = (EditText) findViewById(R.id.user_pwd_input);
    mUserPhoneInput = (EditText) findViewById(R.id.user_phone_input);
    mUserCodeInput = (EditText) findViewById(R.id.user_check_code_input);
    mNewAccount = (Button) findViewById(R.id.new_account);
    mIvBack = (ImageView) findViewById(R.id.iv_back);
    mSendSMS = (TextView) findViewById(R.id.send_sms);
    mTip = (TextView) findViewById(R.id.tip);
    tv_back = (TextView) findViewById(R.id.tv_back);
    mTip.setOnClickListener(this);
    tv_back.setOnClickListener(this);
    tip_right.setOnClickListener(this);

    mTvCountryNumber = (TextView) findViewById(R.id.tv_country_number);

    TextView tvSelectCountry = (TextView) findViewById(R.id.tv_select_country);
    tvSelectCountry.setOnClickListener(this);

    String tip = mTip.getText().toString().trim();
    SpannableString spannableString = new SpannableString(tip);
    spannableString
        .setSpan(new ForegroundColorSpan(Color.parseColor("#1C2164")), tip.indexOf("《"), spannableString.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    mTip.setText(spannableString);

    mIvBack.setOnClickListener(this);
    mSendSMS.setOnClickListener(this);
    mNewAccount.setOnClickListener(this);

  }

  @Override
  public void loadData() {

    //获取国家类型
//    getCountryList();
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.new_account:
          newAccount();
          break;
        case R.id.send_sms:
          sendSMS();
          break;
        case R.id.tv_select_country:
//          selectCountry();
          break;
        case R.id.tip:
          Intent intent = new Intent(this, ProtocolActivity.class);
          startActivityForResult(intent, Config.REGISTER_PROTOCAL);
          break;
        case R.id.iv_icon:
          selectIcon();
          break;
        case R.id.tip_right:
          startActivity(new Intent(this, GuiZeActivity.class));
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void selectIcon() {
    if (!selected) {
      ViewUtil.setBackground(mIvIcon, getMyDrawable(R.mipmap.xieyi_two));
      selected = !selected;
    } else {
      ViewUtil.setBackground(mIvIcon, getMyDrawable(R.mipmap.xieyi_one));
      selected = !selected;
    }
  }

  private void changeIcon() {
    if (selected) {
      ViewUtil.setBackground(mIvIcon, getMyDrawable(R.mipmap.xieyi_two));
    }
  }

  public void getCountryList() {
    judgeNet();
    showLandingDialog();
    RequestNet requestNet = new RequestNet(myApp, this, null, Urls.SELECT_COUNTRY,
        mCountryListener,
        RequestNet.POST);
  }

  private void sendSMS() {

    try {
      seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;

      if (TextUtils.isEmpty(mUserPhoneInput.getText().toString().trim())) {
        throw new DefineException(getString(R.string.please_input_phone_number));
      }
      if (!PhoneUtil.isPhone(mUserPhoneInput.getText().toString().trim())) {
        throw new DefineException(getString(R.string.please_input_phone_number_error));
      }

      String phone = mUserPhoneInput.getText().toString().trim();
      List<RequestParams> list = new ArrayList<>();
      RequestParams requestParams = new RequestParams("phone",
          phone);
      list.add(requestParams);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SEND_SMS, mSMSListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(),getString(R.string.send_sms_fail)));
    }

  }

  private void check() {
    String name = mUserNameInput.getText().toString().trim();
    String pwd = mUserPwdInput.getText().toString().trim();
    String phone = mUserPhoneInput.getText().toString().trim();
    String code = mUserCodeInput.getText().toString().trim();

    if (TextUtils.isEmpty(name)) {
      throw new DefineException(getString(R.string.user_name_empty));
    }

    if (pwd.length() <= 6) {
      throw new DefineException(getString(R.string.pwd_less_six));
    }

    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(phone) || TextUtils
        .isEmpty(code)) {
      throw new DefineException(getString(R.string.info_less));
    }

    if (StringUtil.hasSpace(name)) {
      throw new DefineException(getString(R.string.string_contains_space));
    }

    if (StringUtil.hasSpace(pwd)) {
      throw new DefineException(getString(R.string.pwd_contains_space));
    }

  }

  private void newAccount() {

    String userName = mUserNameInput.getText().toString().trim();
    String pwd = mUserPwdInput.getText().toString().trim();
    String phone = mUserPhoneInput.getText().toString().trim();
    String checkCode = mUserCodeInput.getText().toString().trim();

    if (TextUtils.isEmpty(userName)) {
      showMsg(getString(R.string.user_name_empty));
      return;
    }

    if (!StringUtil.rexCheckNickName(userName)) {
      showMsg(getString(R.string.user_name_geshi_error));
      return;
    }

    if (TextUtils.isEmpty(pwd) || !StringUtil.rexCheckPassword(pwd, 6, 20)) {
      showMsg(getString(R.string.pwd_format_error));
      return;
    }

    if (TextUtils.isEmpty(checkCode)) {
      showMsg(getString(R.string.check_code_empty));
      return;
    }

    if (!selected) {
      showMsg(getString(R.string.not_agree_protcal));
      return;
    }

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsUserName = new RequestParams();
    paramsUserName.setKey("account");
    paramsUserName.setValue(userName);

    RequestParams paramsPwd = new RequestParams();
    paramsPwd.setKey("password");
    paramsPwd.setValue(pwd);

    RequestParams paramsPhone = new RequestParams();
    paramsPhone.setKey("phone");
    paramsPhone.setValue(phone);

    RequestParams paramsPhoneCode = new RequestParams();
    paramsPhoneCode.setKey("phonecode");
    paramsPhoneCode.setValue(checkCode);

    RequestParams paramsAgree = new RequestParams();
    paramsAgree.setKey("lisence_agree");
    paramsAgree.setValue(1 + "");

    list.add(paramsAgree);
    list.add(paramsPhone);
    list.add(paramsPhoneCode);
    list.add(paramsPwd);
    list.add(paramsUserName);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.REGISTER, mListener,
        RequestNet.POST);
  }

  @Override
  public String setTip() {
    return getString(R.string.registering);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Config.REGISTER_PROTOCAL) {
      if (null != data) {
        boolean booleanExtra = data.getBooleanExtra(Config.I_AGREE, false);
        if (booleanExtra) {
          selected = true;
          changeIcon();
        }
      }
    }
  }

  private void updatePage(Result<String> beanResult) {
    if ("1".equals(beanResult.getType())) {
      showMsg(getInfo(R.string.send_sms_sucess));

      mSendSMS.setEnabled(false);
      Drawable drawable = DrableUtil.getDrawable(this, R.drawable.get_check_code_shape_no_select);
      ViewUtil.setBackground(mSendSMS, drawable);
      mSendSMS.setTextColor(ColorUtil.getColor(R.color.cb6b6b6, this));
      limitSeconds();
    } else {
      showMsg(
          "".equals(beanResult.getMsg()) ? getInfo(R.string.send_sms_fail) : beanResult.getMsg());
    }

  }


  private void updateCountry(Result<CountryResultBean> beanResult) {
    hideLandingDialog();
    if (null == beanResult) {
      showMsg(getInfo(R.string.GET_DATE_FAIL));
      return;
    }
  }

  private int seconds;

  private void limitSeconds() {

    CountDownTimer one = new CountDownTimer(62 * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {

        if (seconds == 0) {
          mSendSMS.setText(getString(R.string.reget_check_code));
          mSendSMS.setEnabled(true);
          Drawable drawable = DrableUtil
              .getDrawable(getApplicationContext(), R.drawable.get_check_code_shape);
          ViewUtil.setBackground(mSendSMS, drawable);
          mSendSMS
              .setTextColor(ColorUtil.getColor(R.color.cEA5412, getApplicationContext()));
          return;
        }
        if (null != mSendSMS && seconds >= 0) {
          mSendSMS.setText(seconds + getString(R.string.some_seconds));
        }
        seconds--;
      }

      @Override
      public void onFinish() {

      }
    };
    one.start();
  }

}
