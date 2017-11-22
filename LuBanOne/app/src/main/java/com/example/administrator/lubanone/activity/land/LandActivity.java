package com.example.administrator.lubanone.activity.land;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.activity.register.RegisterActivity;
import com.example.administrator.lubanone.activity.register.RegisterFourActivity;
import com.example.administrator.lubanone.activity.register.RegisterOneActivity;
import com.example.administrator.lubanone.activity.register.RegisterThreeActivity;
import com.example.administrator.lubanone.activity.register.RegisterTwoActivity;
import com.example.administrator.lubanone.bean.homepage.LandResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.RongIMUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 登陆
 */
public class LandActivity extends BaseActivity {

  private EditText mName;
  private EditText mPwd;
  private Button mRegister;
  private Button mLand;
  private ImageView mEye;

  private boolean close;
  private LinearLayout mRlContainer;
  private ImageView mLanding;
  private ImageView mBack;
  private LinearLayout mLandPage;
  private boolean reLand;


  private RequestListener mLandLiscenter = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<LandResultBean> beanResult = GsonUtil.processJson(jsonObject, LandResultBean.class);
        updatePage(beanResult);
      } catch (Exception e) {
        hideCommitDialog();
        showMsg(getString(R.string.land_fail));
        mRlContainer.setVisibility(View.GONE);
        mLandPage.setVisibility(View.VISIBLE);
        mRegister.setClickable(true);
        mLand.setClickable(true);
        mBack.setClickable(true);
        hideCommitDataDialog();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDialog();
      showMsg(getString(R.string.land_fail));
      mLanding.clearAnimation();
      mRlContainer.setVisibility(View.GONE);
      mLandPage.setVisibility(View.VISIBLE);
      mRegister.setClickable(true);
      mLand.setClickable(true);
      mBack.setClickable(true);
      hideCommitDataDialog();
    }
  };

  private void updatePage(final Result<LandResultBean> beanResult) {
    if (null == beanResult || beanResult.getResult() == null) {
      if (null != beanResult) {
        showMsg(DebugUtils.convert(beanResult.getMsg(), getString(R.string.land_fail)));
      }
      mRlContainer.setVisibility(View.GONE);
      mLandPage.setVisibility(View.VISIBLE);
      mRegister.setClickable(true);
      mLand.setClickable(true);
      mBack.setClickable(true);
      return;
    }

    if ("1".equals(beanResult.getType())) {
//      mLanding.clearAnimation();
//      mLanding.setImageResource(R.mipmap.ok_2x);
      //记录token 用户名 密码 是否登录过
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
          beanResult.getResult().getToken());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.RONG_TOKEN,
          beanResult.getResult().getRongtoken());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_ID,
          beanResult.getResult().getUserid());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO,
          Config.LAND_NAME, mName.getText().toString().trim());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO,
          Config.USER_PWD, mPwd.getText().toString().trim());
      new RongIMUtil(getApplicationContext(), beanResult.getResult().getRongtoken()).initRong();

      startNewActivityThenFinish(LandActivity.this, MainActivity.class);
//      new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//          startNewActivityThenFinish(LandActivity.this, MainActivity.class);
//        }
//      }, 1000);
      return;
    }

    if ("0".equals(beanResult.getType())) {
      mRlContainer.setVisibility(View.GONE);
      mLandPage.setVisibility(View.VISIBLE);
      mRegister.setClickable(true);
      mLand.setClickable(true);
      mBack.setClickable(true);
      showMsg(beanResult.getMsg());
      return;
    }

    if ("2".equals(beanResult.getType())) {
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
          beanResult.getResult().getToken());
      mRlContainer.setVisibility(View.GONE);
      mLandPage.setVisibility(View.VISIBLE);
      mRegister.setClickable(true);
      mLand.setClickable(true);
      mBack.setClickable(true);
      showMsg(DebugUtils.convert(beanResult.getMsg(), getString(R.string.land_fail)));
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              jump(beanResult);
            }
          });
        }
      }, 1000);
    }
  }

  private void jump(Result<LandResultBean> beanResult) {
    String step = beanResult.getResult().getStep();

    // step1 激活步 step2 支付密码步 step3 真实姓名步 step4 银行账号步;
    if ("step1".equals(step)) {
      startNewActivity(LandActivity.this, RegisterOneActivity.class);
    }

    if ("step2".equals(step)) {
      startNewActivity(LandActivity.this, RegisterTwoActivity.class);
    }

    if ("step3".equals(step)) {
      startNewActivity(LandActivity.this, RegisterThreeActivity.class);
    }

    if ("step4".equals(step)) {
      startNewActivity(LandActivity.this, RegisterFourActivity.class);
    }


  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_land;
  }

  @Override
  public void initView() {

    Intent intent = getIntent();
    if (null != intent) {
      String type = intent.getType();
      if ("reland".equals(type)) {
        reLand = true;
      }
    }

    mName = (EditText) findViewById(R.id.user_name_input);
    mPwd = (EditText) findViewById(R.id.user_pwd_input);

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    tvBack.setOnClickListener(this);

    mRegister = (Button) findViewById(R.id.register);
    mLand = (Button) findViewById(R.id.btn_land);
    mBack = (ImageView) findViewById(R.id.iv_back);
    mRlContainer = (LinearLayout) findViewById(R.id.container);
    mRlContainer.setVisibility(View.GONE);

    mLandPage = (LinearLayout) findViewById(R.id.activity_register);
    mLanding = (ImageView) findViewById(R.id.landing);

    TextView tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
    tvForgetPwd.setOnClickListener(this);

    mBack.setOnClickListener(this);
    mRegister.setOnClickListener(this);
    mLand.setOnClickListener(this);

    mEye = (ImageView) findViewById(R.id.eye);
    mEye.setOnClickListener(this);

    mName.setText(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.LAND_NAME, ""));

    mName.setSelection(mName.getText().toString().trim().length());
    if (TextUitl.isNotEmpty(mName.getText().toString().trim())) {
      mPwd.requestFocus();
    }

  }

  @Override
  public void finish() {
    super.finish();

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    try {

      switch (v.getId()) {
        case R.id.register:
          land();
          break;
        case R.id.btn_land:
          register();
          break;
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.eye:
          clickPwd();
          break;
        case R.id.tv_forget_pwd:
          forgetPwd();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }

  }

  private void forgetPwd() {
    startNewActivity(LandActivity.this, UpdatePwdStepOneActivity.class);
  }

  private void clickPwd() {
    if (!close) {
      mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
      mEye.setImageResource(R.mipmap.eye_2x);
    } else {
      mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
      mEye.setImageResource(R.mipmap.eye_close_2x);
    }
    close = !close;
    mPwd.postInvalidate();
    mPwd.setSelection(mPwd.getText().toString().trim().length());
  }

  private void land() {
    check();
    String userName = mName.getText().toString().trim();
    String pwd = mPwd.getText().toString().trim();
    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
      showMsg(getString(R.string.account_or_pwd_empty));
      return;
    }

//    startLanding();

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsName = new RequestParams("account", userName);
    RequestParams paramsPwd = new RequestParams("password", pwd);
    RequestParams paramsRegId = new RequestParams("regid",
        JPushInterface.getRegistrationID(this.getApplicationContext()));
    list.add(paramsName);
    list.add(paramsPwd);
    list.add(paramsRegId);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.LAND, mLandLiscenter,
        RequestNet.POST);

  }

  @Override
  public String setTip() {
    return getString(R.string.landing_one);
  }

  private void startLanding() {

    mRlContainer.setVisibility(View.VISIBLE);
    mLandPage.setVisibility(View.GONE);

    Animation anim = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.landing);
    mLanding.startAnimation(anim);

    mRegister.setClickable(false);
    mLand.setClickable(false);
    mBack.setClickable(false);

  }

  private void check() {
    String inputName = mName.getText().toString().trim();
    String inputPwd = mPwd.getText().toString().trim();
    if (StringUtil.isContainChinese(inputName) || StringUtil.isContainChinese(inputPwd)) {
      throw new DefineException(getString(R.string.has_ch));
    }
  }

  private void register() {
    startNewActivity(LandActivity.this, RegisterActivity.class);
  }

}
