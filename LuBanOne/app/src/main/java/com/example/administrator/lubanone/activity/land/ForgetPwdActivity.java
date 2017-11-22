package com.example.administrator.lubanone.activity.land;

import android.text.TextUtils;
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
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 忘记密码 第二步
 */
public class ForgetPwdActivity extends BaseActivity {

  private EditText mInputPhone;
  private TextView mPhone;
  private String mPhoneNumber;

  //send phone
  private RequestListener mSendSMSListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        sendSMSResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.get_check_code_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.get_check_code_fail));
    }
  };

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_forget_pwd;
  }

  @Override
  public String setTip() {
    return getString(R.string.sending_code);
  }

  @Override
  public void initView() {

    ImageView back = (ImageView) findViewById(R.id.iv_back);
    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    mInputPhone = (EditText) findViewById(R.id.user_pwd_input);
    Button sendCode = (Button) findViewById(R.id.btn_land);
    mPhone = (TextView) findViewById(R.id.phone);

    back.setOnClickListener(this);
    tvBack.setOnClickListener(this);
    sendCode.setOnClickListener(this);

    StringBuilder sb = new StringBuilder();
    sb.append("+62");

    mPhoneNumber = SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_PHONE, "");
    if (!TextUtils.isEmpty(mPhoneNumber)) {
      String substring = mPhoneNumber.substring(0, mPhoneNumber.length() - 2);
      String replace = mPhoneNumber.replace(substring, getStarts(substring));
      sb.append(replace);
      mPhone.setText(sb.toString());
    }
  }

  private CharSequence getStarts(String substring) {
    if (!TextUtils.isEmpty(substring)) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < substring.length(); i++) {
        sb.append("*");
      }
      return sb.toString();
    }
    return "*";
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_land:
          sendCode();
          break;
        case R.id.tv_back:
        case R.id.iv_back:
          finish();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void sendCode() {
    judgeNet();
    checkParams();
    List<RequestParams> paramList = getParamList();
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.SEND_PHONE,
        mSendSMSListener,
        RequestNet.POST);
  }

  private void sendSMSResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.get_check_code_success));
      startNewActivity(ForgetPwdActivity.this, ForgetPwdThreeActivity.class);
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.get_check_code_fail)));
    }
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> listParams = new ArrayList<>();
    RequestParams paramsToken = new RequestParams("phone", mPhoneNumber);
    RequestParams paramsPage = new RequestParams("phonesend",
        mInputPhone.getText().toString().trim());
    listParams.add(paramsToken);
    listParams.add(paramsPage);
    return listParams;
  }

  private void checkParams() {
    if (TextUtils.isEmpty(mInputPhone.getText().toString().trim())) {
      throw new DefineException(getString(R.string.phone_number_empty));
    }
    if (!mPhoneNumber.equals(mInputPhone.getText().toString().trim())) {
      throw new DefineException(getString(R.string.phone_not_equal));
    }
  }

  @Override
  public void finish() {
    super.finish();
  }


}
