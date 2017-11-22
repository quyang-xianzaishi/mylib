package com.example.administrator.lubanone.activity.register;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.activity.QBaseActivity;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class RegisterThreeActivity extends BaseActivity {

  private EditText mRealName;

  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        updatePage(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.set_real_name_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getInfo(R.string.set_real_name_fail));
    }
  };


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register_three;
  }

  @Override
  public void initView() {

    Button btnNext = (Button) findViewById(R.id.btn_next);
    TextView back = (TextView) this.findViewById(R.id.tv_back);

    back.setOnClickListener(this);
    btnNext.setOnClickListener(this);
    mRealName = (EditText) findViewById(R.id.real_name);
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
        case R.id.btn_next:
          nextStep();
          break;
        case R.id.tv_back:
          RegisterThreeActivity.this.finish();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }

  }

  @Override
  public String setTip() {
    return getString(R.string.set_real_nameing);
  }

  private void nextStep() {
    judgeNet();
    check();
    RequestParams requestParamsToken = new RequestParams(Config.TOKEN,
        SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams requestParamsRealName = new RequestParams("truename",
        mRealName.getText().toString().trim());

    List<RequestParams> list = new ArrayList<>();
    list.add(requestParamsToken);
    list.add(requestParamsRealName);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SET_REAL_NAME, mListener,
        RequestNet.POST);

  }

  private void check() {
    String name = mRealName.getText().toString().trim();
    if (TextUtils.isEmpty(name)) {
      throw new DefineException(getString(R.string.real_name_empty));
    }
    if (StringUtil.hasNumber(name)) {
      throw new DefineException(getString(R.string.name_has_number));
    }
    if (StringUtil.hasSpecailSymbol(name)) {
      throw new DefineException(getString(R.string.name_has_other_char));
    }
  }

  private void updatePage(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME,
          mRealName.getText().toString().trim());
      showMsg(getString(R.string.set_real_name_success));
      startNewActivity(RegisterThreeActivity.this, RegisterFourActivity.class);
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.set_real_name_fail)));
    }
  }

}
