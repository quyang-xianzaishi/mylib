package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.ActiveCodeBean;
import com.example.administrator.lubanone.customview.progressdialog.BaseProgressDialog;
import com.example.administrator.lubanone.customview.progressdialog.MyProgressDialog;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\19 0019.
 */

public class ActivateActivity extends BaseActivity implements View.OnClickListener {

  private TextView back;
  private TextView title;
  private TextView activationNumber;
  private EditText userNickName;
  private TextView activateBtn;
  private BaseProgressDialog uploadingDialog;


  private RequestListener mGetActiveCodeListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<ActiveCodeBean> result = GsonUtil
            .processJson(jsonObject, ActiveCodeBean.class);
        if (null != result && "1".equals(result.getType()) && result.getResult() != null) {
          activationNumber.setText(DebugUtils.convert(result.getResult().getActivecode(), ""));
        } else {
          showMsg(getString(R.string.get_active_code_fail));
        }
      } catch (Exception e) {
        showMsg(getString(R.string.get_active_code_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_active_code_fail));
    }
  };


  @Override
  protected void beforeSetContentView() {
    //透明状态栏
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_activate;
  }

  public void initView() {
    uploadingDialog = new MyProgressDialog(this).setLabel(getString(R.string.activing));

    back = (TextView) this.findViewById(R.id.activity_back);
    TextView tvBack = (TextView) this.findViewById(R.id.tv_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    activationNumber = (TextView) this.findViewById(R.id.activation_number);
    userNickName = (EditText) this.findViewById(R.id.user_nickname);
    activateBtn = (TextView) this.findViewById(R.id.activate_btn);

    title.setText(getString(R.string.active_vip));
    back.setOnClickListener(this);
    activateBtn.setOnClickListener(this);
    tvBack.setOnClickListener(this);
    Intent intent = getIntent();
    if (null != intent) {
      String codeSize = intent.getStringExtra("code_size");
      if (!TextUtils.isEmpty(codeSize)) {
//        activationNumber.setText(codeSize);
      }
    }
  }


  @Override
  public void loadData() {
    try {
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.GET_ACTIVE_CODE,
          mGetActiveCodeListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_active_code_fail));
    }
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.activity_back:
      case R.id.tv_back:
        ActivateActivity.this.finish();
        break;
      case R.id.activate_btn:
        activeUser();
        break;
      default:
        break;
    }
  }

  private void activeUser() {
    if (!TextUtils.isEmpty(userNickName.getText().toString().trim())) {
      activateAccount(userNickName.getText().toString().trim());
    } else {
      Toast.makeText(this, getString(R.string.activate_username_null),
          Toast.LENGTH_LONG).show();
    }
  }

  private void activateAccount(String accountName) {
    try {
      judgeNet();
      //激活账户
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsUserName = new RequestParams("account", accountName);
      list.add(paramsToken);
      list.add(paramsUserName);

      uploadingDialog.show();
      RequestNet requestNet = new RequestNet(myApp, this, list,
          Urls.ACTIVATE_ACCOUNT, mListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.active_fail)));
      uploadingDialog.dismiss();
    }
  }

  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Log.e("ActivateActivity", "testSuccess=" + jsonObject);
        uploadingDialog.dismiss();
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitResult(result);
      } catch (Exception e) {
        Toast.makeText(ActivateActivity.this, getString(R.string.active_user_fail),
            Toast.LENGTH_LONG).show();
        uploadingDialog.dismiss();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      uploadingDialog.dismiss();
      Toast
          .makeText(ActivateActivity.this, getString(R.string.active_user_fail), Toast.LENGTH_LONG)
          .show();
    }
  };

  private void commitResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      Toast.makeText(ActivateActivity.this, getString(R.string.active_user_success),
          Toast.LENGTH_LONG).show();
      finish();
    } else {
      Toast.makeText(ActivateActivity.this,
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.active_user_fail)),
          Toast.LENGTH_LONG).show();
    }
  }


}
