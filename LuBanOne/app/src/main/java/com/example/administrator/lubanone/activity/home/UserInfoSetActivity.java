package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.util.Log;
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
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * 用户昵称
 */
public class UserInfoSetActivity extends BaseActivity {


  RequestListener mRequestListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Log.e("UserInfoSetActivity", "testSuccess=" + jsonObject);
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        updatePage(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.update_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.update_fail));
    }
  };
  private String mNickName;

  private void updatePage(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.update_success));
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.NICK_NAME,
          mInfo.getText().toString().trim());
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.update_fail)));
    }
  }


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.save)
  TextView mSave;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.info)
  EditText mInfo;//name
  @BindView(R.id.iv_delete)
  ImageView mIvDelete;
  @BindView(R.id.activity_user_info_set)
  LinearLayout mActivityUserInfoSet;
  @BindView(R.id.tv_back)
  TextView tvBack;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_info_set;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      mNickName = intent.getStringExtra("nick_name");
      mInfo.setText(mNickName);
      mInfo.setSelection(TextUitl.isEmpty(mNickName) ? 0 : mNickName.length());
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.save, R.id.back, R.id.iv_delete, R.id.tv_back})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.save:
          save();
          break;
        case R.id.back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.iv_delete:
          mInfo.setText("");
          break;
        default:
          break;
      }
    } catch (Exception e) {
      showMsg(e.getMessage());
    }
  }

  private void save() {
    judgeNet();
    String realNick = mInfo.getText().toString().trim();
    isEmptyWithException(realNick, getInfo(R.string.input_can_not_empty));
    if (realNick.startsWith(" ")) {
      showMsg(getString(R.string.nick_start_with_space));
      return;
    }

    if (TextUitl.isNotEmpty(mNickName) && mNickName.trim().equals(realNick)) {
      showMsg(getString(R.string.nick_no_update));
      return;
    }

    ArrayList<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsNick = new RequestParams("nick", realNick);
    list.add(paramsToken);
    list.add(paramsNick);
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.NICK_NAME_SET, mRequestListener,
        RequestNet.POST);
  }

  @Override
  public String setTip() {
    return getString(R.string.updating);
  }
}
