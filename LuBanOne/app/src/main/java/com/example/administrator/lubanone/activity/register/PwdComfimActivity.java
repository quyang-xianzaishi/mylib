package com.example.administrator.lubanone.activity.register;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.landregister.PayBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class PwdComfimActivity extends BaseActivity {


  private RequestListener mListener = new RequestListener() {

    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> stringResult = GsonUtil.processJson(jsonObject, Object.class);
        updatePage(stringResult);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.set_pay_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.set_pay_fail));
    }
  };


  private EditText mEtPwdOne;
  private EditText mEtPwdTwo;
  private EditText mEtPwdThree;
  private EditText mEtPwdFour;
  private EditText mEtPwdFive;
  private EditText mEtPwdSix;
  private String mOne;
  private String mTwo;
  private String mThree;
  private String mFour;
  private String mFive;
  private String mSix;
  private String mOne1;
  private String mTwo1;
  private String mThree1;
  private String mFour1;
  private String mFive1;
  private String mSix1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_pwd_comfim;
  }

  @Override
  public void finish() {
    super.finish();
  }


  @Override
  public void initView() {

    Button btnNext = (Button) findViewById(R.id.btn_next);
    TextView back = (TextView) this.findViewById(R.id.tv_back);

    back.setOnClickListener(this);
    btnNext.setOnClickListener(this);

    mEtPwdOne = (EditText) findViewById(R.id.et_pwd_one);
    mEtPwdTwo = (EditText) findViewById(R.id.et_pwd_two);
    mEtPwdThree = (EditText) findViewById(R.id.et_pwd_three);
    mEtPwdFour = (EditText) findViewById(R.id.et_pwd_four);
    mEtPwdFive = (EditText) findViewById(R.id.et_pwd_five);
    mEtPwdSix = (EditText) findViewById(R.id.et_pwd_six);

    mEtPwdOne.setTransformationMethod(new MyTransformationMethod());
    mEtPwdTwo.setTransformationMethod(new MyTransformationMethod());
    mEtPwdThree.setTransformationMethod(new MyTransformationMethod());
    mEtPwdFour.setTransformationMethod(new MyTransformationMethod());
    mEtPwdFive.setTransformationMethod(new MyTransformationMethod());
    mEtPwdSix.setTransformationMethod(new MyTransformationMethod());

    mEtPwdOne.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    mEtPwdOne.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    mEtPwdTwo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    mEtPwdThree.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    mEtPwdFour.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    mEtPwdFive.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    mEtPwdSix.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

    mEtPwdOne.addTextChangedListener(new OneTextWatcher());
    mEtPwdTwo.addTextChangedListener(new TwoTextWatcher());
    mEtPwdThree.addTextChangedListener(new ThreeTextWatcher());
    mEtPwdFour.addTextChangedListener(new ThourTextWatcher());
    mEtPwdFive.addTextChangedListener(new FiveTextWatcher());
    mEtPwdSix.addTextChangedListener(new SixTextWatcher());

  }

  private class MyTransformationMethod implements TransformationMethod {


    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
      return new SubCharSequence(source);
    }
  }


  private class SubCharSequence implements CharSequence {
    private CharSequence mSource;
    public SubCharSequence(CharSequence source) {
      mSource = source;
    }
    public char charAt(int index) {
      return '*';
    }
    public int length() {
      return mSource.length();
    }
    public CharSequence subSequence(int start, int end) {
      return mSource.subSequence(start, end);
    }
  }


  @Override
  public void loadData() {
    Intent intent = getIntent();
    if (null != intent) {
      Bundle bundle = intent.getBundleExtra("bundle");
      PayBean payBean = (PayBean) bundle.getSerializable("pwd");
      if (null != payBean) {
        mOne = payBean.getOne();
        mTwo = payBean.getTwo();
        mThree = payBean.getThree();
        mFour = payBean.getFour();
        mFive = payBean.getFive();
        mSix = payBean.getSix();

        System.out.println(
            "PwdComfimActivity.loadData=" + mOne + "=" + mTwo + "=" + mThree + "=" + mFour + "="
                + mFive + "=" + mSix);
      }
    }
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_next:
          comfirm();
          break;
        case R.id.tv_back:
          PwdComfimActivity.this.finish();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.set_paying);
  }

  private void comfirm() {
    try {
      judgeNet();
      check();

      StringBuilder builder = new StringBuilder();
      String pwdFirst = builder.append(mOne).append(mTwo).append(mThree).append(mFour).append(mFive)
          .append(mSix).toString();

      StringBuilder builderComfirm = new StringBuilder();
      String pwdComfirm = builderComfirm.append(mOne1).append(mTwo1).append(mThree1).append(mFour1)
          .append(mFive1)
          .append(mSix1).toString();

      if (!pwdComfirm.equals(pwdFirst)) {
        throw new DefineException(getString(R.string.two_pwd_no_same));
      }

      List<RequestParams> list = new ArrayList<>();
      RequestParams requestParams = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams requestParamsPwd = new RequestParams("paypwd", pwdFirst);
      RequestParams requestParamsPwdComfirm = new RequestParams("repaypwd", pwdComfirm);
      list.add(requestParams);
      list.add(requestParamsPwd);
      list.add(requestParamsPwdComfirm);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SET_PAY_PWD, mListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(e.getMessage());
    }

  }


  private void check() {
    try {
      mOne1 = mEtPwdOne.getText().toString().trim();
      mTwo1 = mEtPwdTwo.getText().toString().trim();
      mThree1 = mEtPwdThree.getText().toString().trim();
      mFour1 = mEtPwdFour.getText().toString().trim();
      mFive1 = mEtPwdFive.getText().toString().trim();
      mSix1 = mEtPwdSix.getText().toString().trim();
      if (TextUtils.isEmpty(mOne1) || TextUtils.isEmpty(mTwo1) || TextUtils.isEmpty(mThree1)
          || TextUtils.isEmpty(mFour1) || TextUtils.isEmpty(mFive1) || TextUtils.isEmpty(mSix1)) {
        throw new DefineException(getString(R.string.register_pwd_tip));
      }

      if (!isRight(mOne1) || !isRight(mTwo1) || !isRight(mThree1) || !isRight(mFour1) || !isRight(
          mFive1) || !isRight(mSix1)) {
        throw new DefineException(getString(R.string.pwd_only_number));
      }

      if (!isEqual(mOne, mOne1) || !isEqual(mTwo, mTwo1) || !isEqual(mThree, mThree1) || !isEqual(
          mFour, mFour1) || !isEqual(mFive, mFive1) || !isEqual(mSix, mSix1)) {
        throw new DefineException(getString(R.string.two_pwd_not_same));
      }
    } catch (Exception e) {
      showMsg(e.getMessage());
    }
  }


  public boolean isRight(String pwd) {
    if (TextUtils.isEmpty(pwd) || pwd.length() != 1 || !TextUtils.isDigitsOnly(pwd)) {
      return false;
    }
    return true;
  }

  public boolean isEqual(String a, String b) {
    if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b)) {
      return false;
    }
    if (a.trim().equals(b.trim())) {
      return true;
    }
    return false;
  }

  private class OneTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUitl.isNotEmpty(s.toString())) {
        mEtPwdTwo.requestFocus();
      }
    }
  }


  private class TwoTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUitl.isNotEmpty(s.toString())) {
        mEtPwdThree.requestFocus();
      }
    }
  }


  private class ThreeTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUitl.isNotEmpty(s.toString())) {
        mEtPwdFour.requestFocus();
      }
    }
  }


  private class ThourTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUitl.isNotEmpty(s.toString())) {
        mEtPwdFive.requestFocus();
      }
    }
  }


  private class FiveTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      if (TextUitl.isNotEmpty(s.toString())) {
        mEtPwdSix.requestFocus();
      }
    }
  }

  private class SixTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      comfirm();
    }
  }


  private void updatePage(Result<Object> stringResult) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(stringResult)) {
      showMsg(getString(R.string.set_pay_success));
      startNewActivity(PwdComfimActivity.this, RegisterThreeActivity.class);
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(stringResult), getString(R.string.set_pay_fail)));
    }
  }
}
