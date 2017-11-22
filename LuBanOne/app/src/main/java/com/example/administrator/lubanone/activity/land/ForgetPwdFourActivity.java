package com.example.administrator.lubanone.activity.land;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 修改密码第四步
 */
public class ForgetPwdFourActivity extends BaseActivity {


  private EditText mInputPwd;
  private EditText mInputPwdAgain;

  //修改登录密码完成
  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        updateResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.update_pwd_fail));
        hideCommitDataDialog();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.update_pwd_fail));
    }
  };
  private String mTokne;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_forget_pwd_four;
  }

  @Override
  public void initView() {

    Intent intent = getIntent();
    if (null != intent) {
      mTokne = intent.getStringExtra(Config.TOKEN);
    }

    ImageView back = (ImageView) findViewById(R.id.iv_back);
    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    mInputPwd = (EditText) findViewById(R.id.user_pwd_input);
    mInputPwdAgain = (EditText) findViewById(R.id.user_phone_input);
    Button complete = (Button) findViewById(R.id.btn_land);

    back.setOnClickListener(this);
    tvBack.setOnClickListener(this);
    complete.setOnClickListener(this);

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
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.btn_land:
          complete();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }


  @Override
  public String setTip() {
    return getString(R.string.set_pwding);
  }

  private void complete() {
    try {
      judgeNet();
      checkParams();
      List<RequestParams> paramsList = getParamsList();
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, paramsList, Urls.RESET_PWD,
          mCommitListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.update_fail)));
      hideCommitDataDialog();
    }
  }

  private void updateResult(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {

      alertSuccessDialog();

    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.update_pwd_fail)));
    }
  }

  private void alertSuccessDialog() {

    StytledDialog.showBuySeedDialog(getApplicationContext(), getString(R.string.reset_forget_pwd),
        getString(R.string.reset_forget_pwd_success), null, getString(R.string.confirm), false,
        true,
        new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
            myApp.removeAllActivity();
            Intent intent = new Intent(ForgetPwdFourActivity.this, LandActivity.class);
            startActivity(intent);
            finish();

          }

          @Override
          public void onSecond(DialogInterface dialog) {

          }
        });
  }

  private List<RequestParams> getParamsList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN, mTokne);
    RequestParams paramsPwd = new RequestParams("password",
        mInputPwd.getText().toString().trim());
    RequestParams paramsPwdConfrim = new RequestParams("repassword",
        mInputPwdAgain.getText().toString().trim());
    list.add(paramsToken);
    list.add(paramsPwd);
    list.add(paramsPwdConfrim);
    return list;
  }

  private void checkParams() {
    String pwd = mInputPwd.getText().toString().trim();
    String pwdComfirm = mInputPwdAgain.getText().toString().trim();
    isEmptyWithException(pwd, getString(R.string.pwd_empty));
    isEmptyWithException(pwdComfirm, getString(R.string.pwd_not_equls));
    isFalseWithException(!pwdComfirm.equals(pwd),getString(R.string.pwd_not_equl));
  }
}
