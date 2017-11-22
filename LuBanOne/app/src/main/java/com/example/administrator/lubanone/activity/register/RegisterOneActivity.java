package com.example.administrator.lubanone.activity.register;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.landregister.CheckActiveResultBean;
import com.example.administrator.lubanone.customview.progressdialog.BaseProgressDialog;
import com.example.administrator.lubanone.customview.progressdialog.MyProgressDialog;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.RongIMUtil;
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
 * 激活第一步
 */
public class RegisterOneActivity extends BaseActivity {


  private BaseProgressDialog dialog;
  private MyApplication mApplication;


  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CheckActiveResultBean> result = GsonUtil
            .processJson(jsonObject, CheckActiveResultBean.class);
        hideDialog();
        updatePage(result);
      } catch (Exception e) {
        hideDialog();
        showMsg(getInfo(R.string.active_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      hideDialog();
      showMsg(getInfo(R.string.active_fail));
    }
  };

  private void updatePage(Result<CheckActiveResultBean> result) {
    if (null == result) {
      showMsg(getInfo(R.string.active_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
          result.getResult().getToken());
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.RONG_TOKEN,
          result.getResult().getRongtoken());
      new RongIMUtil(this, result.getResult().getRongtoken()).initRong();
      startNewActivity(RegisterOneActivity.this, RegisterTwoActivity.class);
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.active_fail)));
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register_one;
  }

  @Override
  public void finish() {
    super.finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != mApplication) {
      mApplication.removeActivity(this);
    }
  }

  @Override
  public void initView() {

    dialog = new MyProgressDialog(this).setLabel(getString(R.string.activing));

    mApplication = (MyApplication) getApplication();
    mApplication.addActivity(this);

    Button btnNext = (Button) findViewById(R.id.btn_next);
    TextView back = (TextView) this.findViewById(R.id.tv_back);

    back.setOnClickListener(this);
    btnNext.setOnClickListener(this);
  }

  @Override
  public void loadData() {

  }

  public void showDialog() {
    if (null != dialog && !dialog.isShowing() && !isFinishing()) {
      dialog.show();
    }
  }


  public void hideDialog() {
    if (null != dialog && dialog.isShowing() && !isFinishing()) {
      dialog.dismiss();
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
          RegisterOneActivity.this.finish();
          break;
      }

    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void nextStep() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams requestParamsAccount = new RequestParams("account",
        SPUtils.getStringValue(this, Config.USER_INFO, "account", ""));
    RequestParams requestParamsPwd = new RequestParams("password",
        SPUtils.getStringValue(this, Config.USER_INFO, "password", ""));
    list.add(requestParamsAccount);
    list.add(requestParamsPwd);
    showDialog();
    RequestNet requestNet = new RequestNet(mApplication, this, list, Urls.CHECK_ACTIVE, mListener,
        RequestNet.POST);
  }


}
