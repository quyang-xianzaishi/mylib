package com.example.administrator.lubanone.activity.land;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.home.BuySeedsRecordActivity.RefreshListener;
import com.example.administrator.lubanone.bean.landregister.UpdateLandPwdThree;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.activity.QBaseActivity;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.PhoneUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 修改密码第三步
 */
public class ForgetPwdThreeActivity extends BaseActivity {


  private EditText mCheckCode;
  private TextView mPhone;


  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<UpdateLandPwdThree> result = GsonUtil
            .processJson(jsonObject, UpdateLandPwdThree.class);
        commitCodeResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.send_sms_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.send_sms_fail));
    }
  };
  private String mPhone1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_forget_pwd_three;
  }

  @Override
  public void initView() {


    ImageView back = (ImageView) findViewById(R.id.iv_back);
    TextView tv_back = (TextView) findViewById(R.id.tv_back);
    mCheckCode = (EditText) findViewById(R.id.user_pwd_input);
    Button btnNext = (Button) findViewById(R.id.btn_land);
    mPhone = (TextView) findViewById(R.id.phone);

    back.setOnClickListener(this);
    tv_back.setOnClickListener(this);
    btnNext.setOnClickListener(this);

    mPhone1 = SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_PHONE, "");
    mPhone.setText("+62" + mPhone1
    );

  }


  @Override
  public String setTip() {
    return getString(R.string.send_codeing);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_land:
          next();
          break;
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;

      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void next() {
    judgeNet();
    checkParams();
    List<RequestParams> paramList = getParamList();
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.CHECK_PHONE_CODE,
        mCommitListener,
        RequestNet.POST);
  }

  private void commitCodeResult(Result<UpdateLandPwdThree> result) {
    if (null == result.getResult()) {
      showMsg(getString(R.string.send_sms_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.send_sms_sucess));
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
          result.getResult().getToken());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_NAME,
          result.getResult().getAccount());
      startNewActivity(ForgetPwdThreeActivity.this, ForgetPwdFourActivity.class);
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.send_sms_fail)));
    }
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsPhone = new RequestParams("phone",
        mPhone1);
    RequestParams paramsCode = new RequestParams("phonecode",
        mCheckCode.getText().toString().trim());
    list.add(paramsPhone);
    list.add(paramsCode);
    return list;
  }

  private void checkParams() {
    if (TextUtils.isEmpty(mCheckCode.getText().toString().trim())) {
      throw new DefineException(getString(R.string.check_code_wrong));
    }
    if (TextUtils.isEmpty(mPhone.getText().toString().trim())) {
      throw new DefineException(getString(R.string.phone_number_empty));
    }
  }
}
