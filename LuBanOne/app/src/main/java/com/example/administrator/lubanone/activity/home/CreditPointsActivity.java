package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.CreditRecordAdapter;
import com.example.administrator.lubanone.bean.homepage.CreditRecordBean;
import com.example.administrator.lubanone.bean.homepage.CreditRecordBean.ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 信用评分记录
 */
public class CreditPointsActivity extends BaseActivity implements OnScrollListener {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.tv_my_credit)
  TextView mTvMyCredit;
  @BindView(R.id.lv)
  PullableListView mLv;
  private PullToRefreshLayout mEmptyLayout;
  @BindView(R.id.pull)
  PullToRefreshLayout pull;
  private Boolean getMore;
  private int size = 5;
  private List<ResultBean> mBeanList = new ArrayList<>();
  private int itemCount;
  private Boolean emptyRefresh;


  private RequestListener mCreditRecoredListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        System.out.println("recored=" + jsonObject);
        Gson gson = new Gson();
        CreditRecordBean recordBean = gson.fromJson(jsonObject, CreditRecordBean.class);
        getRecordList(recordBean);
      } catch (Exception e) {
        if (CollectionUtils.isEmpty(mBeanList)) {
          showEmptyLayout(true);
        }
        fail();
        resetGetMore(true);
        setEmptyLayoutFail();
        resetEmptyRefrsh(true);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (CollectionUtils.isEmpty(mBeanList)) {
        showEmptyLayout(true);
      }
      fail();
      resetGetMore(true);
      setEmptyLayoutFail();
      resetEmptyRefrsh(true);
    }
  };
  private int mLastVisiblePosition;


  @Override
  protected void beforeSetContentView() {

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

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }


  private void resetEmptyRefrsh(boolean b) {
    if (emptyRefresh != null && emptyRefresh) {
      emptyRefresh = false;
      if (b && CollectionUtils.isEmpty(mBeanList)) {
        showMsg(getString(R.string.no_more_message));
      }
    }
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    mLastVisiblePosition = view.getLastVisiblePosition();
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
  protected int getContentViewId() {
    return R.layout.activity_credit_points;
  }


  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      loadData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      loadData();
    }
  }

  public void resetGetMore(boolean toTargetPosition) {
    if (null != getMore && getMore) {
      getMore = false;
      if (toTargetPosition && CollectionUtils.isNotEmpty(mBeanList)) {
        mLv.setSelection(mLastVisiblePosition);
      }
    }
  }


  @Override
  public void initView() {

    mEmptyLayout = (PullToRefreshLayout) findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);

    Intent intent = getIntent();
    if (null != intent) {
      String stringExtra = intent.getStringExtra("credit");
      mTvMyCredit.setText(getString(R.string.current_credit_size) + stringExtra);
    }

    mLv.setOnScrollListener(this);
  }

  public void showEmptyLayout(boolean yes) {
    if (yes) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      mLv.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      mLv.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void loadData() {
    try {
      if (!NetUtil.isConnected(getApplicationContext())) {
        showMsg(getString(R.string.NET_ERROR));
        showEmptyLayout(true);
        resetGetMore(true);
        return;
      }
      checkData();
      List<RequestParams> paramList = getParamList();
      RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.CREDIT_RECORD,
          mCreditRecoredListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_credit_record_fail));
      resetGetMore(true);
      resetEmptyRefrsh(true);
    }
  }

  private void getRecordList(CreditRecordBean result) {
    if (null == result) {
      if (CollectionUtils.isEmpty(mBeanList)) {
        showEmptyLayout(true);
      }
      success();
      resetGetMore(true);
      setEmptyLayoutFail();
      resetEmptyRefrsh(true);
      return;
    }
    if ("1".equals(result.getType())) {
      updatePage(result.getResult());
    } else {
      if (CollectionUtils.isEmpty(mBeanList)) {
        showEmptyLayout(true);
      }
      fail();
      resetGetMore(true);
      setEmptyLayoutFail();
      resetEmptyRefrsh(true);
    }
  }

  private void updatePage(List<ResultBean> result) {
    if (null == result) {
      if (emptyRefresh != null && emptyRefresh) {
        emptyRefresh = false;
        success();
        if (CollectionUtils.isEmpty(mBeanList)) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      resetGetMore(true);
      return;
    }
    mBeanList.clear();
    if (CollectionUtils.isNotEmpty(result)) {
      success();
      mBeanList.addAll(result);
      setEmptyLayoutSuccess();
    }
    if (CollectionUtils.isNotEmpty(mBeanList)) {
      mLv.setAdapter(new CreditRecordAdapter(this, result));
      if (null != getMore && getMore) {
        getMore = false;
        if (itemCount == mBeanList.size()) {
          showMsg(getString(R.string.no_more_message));
        }
        if (CollectionUtils.isNotEmpty(mBeanList)) {
          mLv.setSelection(mLastVisiblePosition);
        }
      }
      itemCount = mBeanList.size();
    } else {
      success();
      showEmptyLayout(true);
      resetGetMore(false);
      setEmptyLayoutSuccess();
      resetEmptyRefrsh(true);
    }
  }

  private List<RequestParams> getParamList() {
    ArrayList<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));

    if (null != getMore && getMore) {
      size = mBeanList.size() + 5;
    } else {
      size = mBeanList.size() == 0 ? size : mBeanList.size();
    }

    RequestParams paramsPage = new RequestParams("number", size + "");

    list.add(paramsToken);
    list.add(paramsPage);
    return list;

  }

  private void checkData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }
  }


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

}
