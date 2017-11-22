package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
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
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 修改sex
 */
public class UserSexSetActivity extends BaseActivity {


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
        showMsg(getInfo(R.string.update_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.update_fail));
      hideCommitDataDialog();
    }
  };
  private String mSex;

  private void updatePage(Result<Object> result) {
    hideCommitDataDialog();
    if (null == result) {
      showMsg(getInfo(R.string.update_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.update_success));
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.update_fail)));
    }
  }

  private String getSex(int flag) {
    if (flag == 0) {
      return getString(R.string.man);
    } else if (flag == 1) {
      return getString(R.string.girl);
    }
    return getString(R.string.nuknow);
  }

  @BindView(R.id.rl_man)
  RelativeLayout mRlMan;
  @BindView(R.id.rl_girl)
  RelativeLayout mRlGirl;

  private int flag = -1;


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.pre_info)
  TextView mPreInfo;
  @BindView(R.id.info)
  TextView mInfo;
  @BindView(R.id.iv_delete)
  ImageView mIvDelete;
  @BindView(R.id.info_girl)
  TextView mInfoGirl;
  @BindView(R.id.iv_delete_girl)
  ImageView mIvDeleteGirl;
  @BindView(R.id.activity_user_info_set)
  LinearLayout mActivityUserInfoSet;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_sex_set;
  }

  @Override
  public void initView() {
    mPreInfo.setVisibility(View.INVISIBLE);
    mPreInfo.setText("");

    TextView tvSave = (TextView) findViewById(R.id.tv_save);
    tvSave.setOnClickListener(this);

    mRlMan.setOnClickListener(this);
    mRlGirl.setOnClickListener(this);
    mBack.setOnClickListener(this);
    mPreInfo.setOnClickListener(this);
    mBack.setVisibility(View.VISIBLE);

    mIvDeleteGirl.setVisibility(View.INVISIBLE);

    Intent intent = getIntent();
    if (null != intent) {
      mSex = intent.getStringExtra("sex");
      if (!TextUtils.isEmpty(mSex)) {
        updateSexIcon(Integer.parseInt(mSex));
      }
    }
  }

  private void updateSexIcon(int sex) {
    if (sex == 0) {
      //man
      mIvDeleteGirl.setVisibility(View.INVISIBLE);
      mIvDelete.setVisibility(View.VISIBLE);
      flag = 0;
    }
    if (sex == 1) {
      mIvDeleteGirl.setVisibility(View.VISIBLE);
      mIvDelete.setVisibility(View.INVISIBLE);
      flag = 1;
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  protected void onResume() {
    super.onResume();
    loadData();
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.rl_man:
//          updateSexIcon(0);
          saveAndFinish(0);
          break;
        case R.id.rl_girl:
//          updateSexIcon(1);
          saveAndFinish(1);
          break;
        case R.id.back:
        case R.id.pre_info:
          finish();
          break;
        case R.id.tv_save:
          saveAndFinish();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void saveAndFinish() {
    if (!NetUtil.isConnected(this)) {
      showMsg(getInfo(R.string.NET_ERROR));
      return;
    }
    if (flag == -1) {
      showMsg(getInfo(R.string.PLEASE_SELECT_SEX));
      return;
    }

    if (flag == Integer.parseInt(mSex)) {
      showMsg(getString(R.string.sex_no_update));
      return;
    }
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsSex = new RequestParams("sex", flag + "");
    list.add(paramsToken);
    list.add(paramsSex);
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SEX_SET, mListener,
        RequestNet.POST);
  }


  private void saveAndFinish(int tag) {
    try {
      if (!NetUtil.isConnected(this)) {
        showMsg(getInfo(R.string.NET_ERROR));
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsSex = new RequestParams("sex", tag + "");
      list.add(paramsToken);
      list.add(paramsSex);
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SEX_SET, mListener,
          RequestNet.POST);/**/
    } catch (Exception e) {
      showMsg(getString(R.string.save_fail));
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.updating);
  }
}
