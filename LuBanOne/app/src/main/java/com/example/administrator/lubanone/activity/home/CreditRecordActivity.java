package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.CreditAdapter;
import com.example.administrator.lubanone.bean.homepage.CreditListResultBean;
import com.example.administrator.lubanone.bean.homepage.CreditListResultBean.CreditDetailsBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 评分记录
 */
public class CreditRecordActivity extends BaseActivity {

  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private List<CreditDetailsBean> mList = new ArrayList<>();

  private int itemCount;

  private int size = 5;
  private Boolean getMore;


  private String test_string;

  //第一次请求数据列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CreditListResultBean> result = GsonUtil
            .processJson(jsonObject, CreditListResultBean.class);
        updatePage(result);
      } catch (Exception e) {
        resetGetMore();
        showMsg(getString(R.string.get_credit_record_fails));
        fail();
        resetEmptyRefresh(true);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideLandingDialog();
      fail();
      showMsg(errorMsf);
      resetGetMore();
      resetEmptyRefresh(true);
    }
  };


  public void success() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void fail() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }

  private void updatePage(Result<CreditListResultBean> result) {

    if (ResultUtil.isSuccess(result)) {
      if (result == null || result.getResult() == null || CollectionUtils
          .isEmpty(result.getResult().getList())) {
        fail();
        resetGetMore();
        resetEmptyRefresh(false);
        showEmptyLayout(true);
        setEmptyLayoutSuccess();
        return;
      }
      adapterData(result.getResult());
    } else {
      resetGetMore();
      if (CollectionUtils.isEmpty(mList)) {
        resetEmptyRefresh(true);
      }
      fail();
    }

  }

  private void adapterData(CreditListResultBean result) {
    List<CreditDetailsBean> list = result.getList();

    mList.clear();
    if (CollectionUtils.isNotEmpty(list)) {
      success();
      showEmptyLayout(false);
      setEmptyLayoutSuccess();
      mList.addAll(list);
    }
    if (CollectionUtils.isNotEmpty(mList)) {
      mLv.setAdapter(new CreditAdapter(this, mList));
      if (null != getMore && getMore) {
        getMore = false;
        if (itemCount == mList.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mList.size();
    } else {
      fail();
      resetGetMore();
      showEmptyLayout(true);
      setEmptyLayoutSuccess();
      resetEmptyRefresh(true);
    }


  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_credit_record;
  }


  private PullToRefreshLayout mEmptyLayout;

  private Boolean emptyRefresh;


  private void resetEmptyRefresh(boolean b) {
    if (emptyRefresh != null && emptyRefresh) {
      emptyRefresh = false;
      if (b && CollectionUtils.isEmpty(mList)) {
        showMsg(getString(R.string.no_more_message));
      }
    }
  }


  public void setEmptyLayoutSuccess() {
    if (null != mEmptyLayout && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void setEmptyLayoutFail() {
    if (null != mEmptyLayout && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }


  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }


  public class EmptyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      emptyRefresh = true;
      loadData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }
  }

  @Override
  public void initView() {

    mEmptyLayout = (PullToRefreshLayout) findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    TextView tv_current_credit = (TextView) findViewById(R.id.tv_current_credit);
    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);

    tvBack.setOnClickListener(this);
    ivBack.setOnClickListener(this);

    mLv = (PullableListView) findViewById(R.id.lv);
    pull = (PullToRefreshLayout) findViewById(R.id.task_fragment_pullToRefreshLayout);

    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(false);//设置不让上拉加载

    Intent intent = getIntent();
    if (null != intent) {
      String creditCurrent = intent.getStringExtra("credit_current");
      tv_current_credit.setText(
          StringUtil.getBufferString(getString(R.string.current_train_credit), " ", creditCurrent));
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.loading);
  }

  @Override
  public void loadData() {
    request();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_back:
      case R.id.iv_back:
        finish();
        break;
      default:
        break;
    }
  }


  //下拉监听
  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      request();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      loadData();
    }

  }

  public void request() {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mList.size() + 5;
      } else {
        size = mList.size() == 0 ? size : mList.size();
      }

      RequestParams paramsPage = new RequestParams("number", size + "");

      list.add(paramsToken);
      list.add(paramsPage);

      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.CREDIT_RECORD_LIST, mListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.get_credit_record_fails)));
      resetGetMore();
      resetEmptyRefresh(true);
    }
  }


  private void resetGetMore() {
    if (null != getMore && getMore) {
      getMore = false;
    }
  }
}
