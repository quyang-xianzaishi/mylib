package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
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
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * quyang 用户签名
 */
public class UserAssignActivity extends BaseActivity {


  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        uploadDate(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.update_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.update_fail));
      hideCommitDataDialog();
    }
  };


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.pre_info)
  TextView mPreInfo;
  @BindView(R.id.tv_complete)
  TextView mTvComplete;
  @BindView(R.id.et_assign)
  EditText mEtAssign;
  @BindView(R.id.activity_user_assign)
  LinearLayout mActivityUserAssign;
  private String mAssign;
  @BindView(R.id.tv_back)
  TextView tvBack;
  private String mStringExtra;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_assign;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      mStringExtra = intent.getStringExtra("assign");
      if (null != mStringExtra) {
        mEtAssign.setText(mStringExtra);
        mEtAssign.setSelection(TextUitl.isEmpty(mStringExtra) ? 0 : mStringExtra.length());
      }
    }

    mPreInfo.setText("");
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View view) {

  }

  @OnClick({R.id.back, R.id.pre_info, R.id.tv_complete, R.id.et_assign, R.id.tv_back})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.back:
        case R.id.tv_back:
        case R.id.pre_info:
          finish();
          break;
        case R.id.tv_complete:
          saveAssign();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }

  }

  private void saveAssign() {
    judgeNet();
    String assign = mEtAssign.getText().toString().trim();
    isEmptyWithException(assign, getInfo(R.string.assign_empty));
//    isFalseWithException(assign.length() > 100, getInfo(R.string.assign_too_long));
    if (TextUitl.isNotEmpty(mStringExtra) && mStringExtra.equals(assign)) {
      showMsg(getString(R.string.assign_no_update));
      return;
    }

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsAssign = new RequestParams("autograph", assign);
    list.add(paramsToken);
    list.add(paramsAssign);
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.ASSIGN_SET, mListener,
        RequestNet.POST);

  }

  @Override
  public String setTip() {
    return getString(R.string.updating);
  }

  private void uploadDate(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.update_success));
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.update_fail)));
    }

  }

}
