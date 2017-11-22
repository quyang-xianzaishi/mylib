package com.example.administrator.lubanone.activity.register;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.landregister.PayBean;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;

public class RegisterTwoActivity extends BaseActivity {


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

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register_two;
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

  @Override
  public void loadData() {

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
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_next:
          nextStep();
          break;
        case R.id.tv_back:
          RegisterTwoActivity.this.finish();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void nextStep() {

    check();

    Intent intent = new Intent(RegisterTwoActivity.this, PwdComfimActivity.class);
    Bundle bundle = new Bundle();

    PayBean payBean = new PayBean();
    payBean.setOne(mOne);
    payBean.setTwo(mTwo);
    payBean.setThree(mThree);
    payBean.setFour(mFour);
    payBean.setFive(mFive);
    payBean.setSix(mSix);

    bundle.putSerializable("pwd", payBean);
    intent.putExtra("bundle", bundle);
    startActivity(intent);
  }

  private void check() {
    mOne = mEtPwdOne.getText().toString().trim();
    mTwo = mEtPwdTwo.getText().toString().trim();
    mThree = mEtPwdThree.getText().toString().trim();
    mFour = mEtPwdFour.getText().toString().trim();
    mFive = mEtPwdFive.getText().toString().trim();
    mSix = mEtPwdSix.getText().toString().trim();
    if (TextUtils.isEmpty(mOne) || TextUtils.isEmpty(mTwo) || TextUtils.isEmpty(mThree) || TextUtils
        .isEmpty(mFour) || TextUtils.isEmpty(mFive) || TextUtils.isEmpty(mSix)) {
      throw new DefineException(getString(R.string.register_pwd_tip));
    }

    if (!isRight(mOne) || !isRight(mTwo) || !isRight(mThree) || !isRight(mFour) || !isRight(mFive)
        || !isRight(mSix)) {
      throw new DefineException(getString(R.string.pwe_only_number));
    }

    if (isNull(mOne) || isNull(mTwo) || isNull(mThree) || isNull(mFour) || isNull(mFive) || isNull(
        mSix)) {
      throw new DefineException(getString(R.string.pay_pwd_error));
    }
  }


  public boolean isRight(String pwd) {
    if (TextUtils.isEmpty(pwd) || pwd.length() != 1 || !TextUtils.isDigitsOnly(pwd)) {
      return false;
    }
    return true;
  }

  public boolean isNull(String str) {
    if (TextUtils.isEmpty(str)) {
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
      nextStep();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
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
}
