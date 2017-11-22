package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.UserAreaAdapter;
import com.example.administrator.lubanone.bean.homepage.AreaResultBean;
import com.example.administrator.lubanone.bean.homepage.AreaResultBean.CountrylistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
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
 * 地区
 */
public class AreaSetActivity extends BaseActivity implements OnListViewItemListener {


  //获取地区列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        dismissLoadingDialog();
        Log.e("AreaSetActivity", "testSuccess=" + jsonObject);
        Result<AreaResultBean> result = GsonUtil
            .processJson(jsonObject, AreaResultBean.class);
        udpatePage(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.get_area_info_fail));
        dismissLoadingDialog();
      }

    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_area_info_fail));
      dismissLoadingDialog();
    }
  };


  private void udpatePage(Result<AreaResultBean> result) {
    isResultNullWithException(result, getInfo(R.string.request_fail));
    if (ResultUtil.isSuccess(result)) {
      mUserAreaAdapter = new UserAreaAdapter(this, result.getResult().getCountrylist(), this, code);
      mLv.setAdapter(mUserAreaAdapter);
    } else {
      showMsg(ResultUtil.getErrorMsg(result));
    }
  }

  @BindView(R.id.lv)
  ListView mLv;


  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.pre_info)
  TextView mPreInfo;
  @BindView(R.id.activity_user_info_set)
  LinearLayout mActivityUserInfoSet;
  @BindView(R.id.tv_save)
  TextView mTvSave;
  @BindView(R.id.tv_back)
  TextView mTvBack;

  private UserAreaAdapter mUserAreaAdapter;

  private String mCountrycode;
  private String code;
  private Integer selectedPostioin;
  private String mCountryname;

  RequestListener mRequestListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        showCommitDataDialog();
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        saveResult(result);
      } catch (Exception e) {
        showCommitDataDialog();
        showMsg(getInfo(R.string.update_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showCommitDataDialog();
      showMsg(getString(R.string.update_fail));
    }
  };

  private void saveResult(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.update_success));
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_AREA,
          mCountrycode == null ? "" : mCountrycode);
      SPUtils.putStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_AREA_REAL,
          mCountryname);
      finish();
    } else {
      showMsg(ResultUtil.getErrorMsg(result));
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_area_set;
  }


  @Override
  public String setTip() {
    return getString(R.string.updating);
  }

  @Override
  public void initView() {
    mPreInfo.setVisibility(View.VISIBLE);
    Intent intent = getIntent();
    if (null != intent) {
      mCountrycode = intent.getStringExtra("area");
      code = mCountrycode;
    }
  }

  @Override
  public void loadData() {
    try {
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      showLoadingDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.AREA_LIST, mListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      dismissLoadingDialog();
    }
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.back, R.id.pre_info, R.id.tv_back})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.back:
        case R.id.tv_back:
        case R.id.pre_info:
          finish();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void saveAndFinish() {

    judgeNet();

    if (TextUitl.isNotEmpty(code) && code.equals(mCountrycode)) {
      showMsg(getString(R.string.area_no_update));
      return;
    }

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsCountry = new RequestParams("countrycode",
        mCountrycode == null ? "" : mCountrycode);
    Log.e("AreaSetActivity", "saveAndFinish=" + mCountrycode);
    list.add(paramsToken);
    list.add(paramsCountry);
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.AREA_SET, mRequestListener,
        RequestNet.POST);
  }


  private void saveAndFinish(String countryCode) {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsCountry = new RequestParams("countrycode",
          countryCode);
      list.add(paramsToken);
      list.add(paramsCountry);
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.AREA_SET, mRequestListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.save_fail)));
    }
  }


  @Override
  public void onItem(Object object, int position) {
    CountrylistBean countrylistBean = (CountrylistBean) object;
    mCountrycode = countrylistBean.getCountrycode();
    mCountryname = countrylistBean.getCountryname();
    selectedPostioin = position;
    mUserAreaAdapter.notifyDataSetChanged();

    if (TextUitl.isNotEmpty(mCountrycode)) {
      saveAndFinish(mCountrycode);
    }
  }


  public Integer getSelectedPosotion() {
    return selectedPostioin;
  }


  @OnClick(R.id.tv_save)
  public void onViewClicked() {
    try {
      saveAndFinish();
    } catch (Exception e) {
      showMsg(e.getMessage());
    }
  }

  public String getCountryType() {
    return mCountrycode;
  }
}
