package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.AllScanBuySeedsRecordAdapter;
import com.example.administrator.lubanone.bean.homepage.BuyRecordResultBean;
import com.example.administrator.lubanone.bean.homepage.BuyRecordResultBean.ResultBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.SPUtils;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 买入种子记录
 */
public class BuySeedsRecordActivity extends BaseActivity implements OnListViewItemListener {


  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private List<ResultBean> mBuylist = new ArrayList<>();
  private int mPosition = -1;

  private int size = 30;
  private Boolean getMore;
  private Boolean emptyRefresh;

  private PullToRefreshLayout mEmptyLayout;
  private AllScanBuySeedsRecordAdapter mAdapter;
  private int currentPosition;
  private int itemCount;


  private RequestListener mGetBuyRecordListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        BuyRecordResultBean bean = gson
            .fromJson(jsonObject, BuyRecordResultBean.class);
        getBuyList(bean);
      } catch (Exception e) {
        if (CollectionUtils.isEmpty(mBuylist)) {
          showEmptyLayout(true);
        }
        fail();
        setEmptyLayoutFail();
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetEmptyRefresh(true);

      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (CollectionUtils.isEmpty(mBuylist)) {
        showEmptyLayout(true);
      }
      fail();
      setEmptyLayoutFail();
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      resetEmptyRefresh(true);
    }
  };

  private void resetEmptyRefresh(boolean a) {
    if (emptyRefresh != null && emptyRefresh) {
      emptyRefresh = false;
      if (a && CollectionUtils.isEmpty(mBuylist)) {
        showMsg(getString(R.string.no_more_message));
      }
    }
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_buy_seeds_record;
  }

  @Override
  public void initView() {

    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    TextView tvTitle = (TextView) findViewById(R.id.tv_title);
    tvTitle.setText(getString(R.string.buy_seed_process));
    mEmptyLayout = (PullToRefreshLayout) findViewById(R.id.empty_layout);

    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    ivBack.setOnClickListener(this);

    //下拉熟悉
    mLv = (PullableListView) findViewById(R.id.lv);
    pull = (PullToRefreshLayout) findViewById(R.id.pull);
    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);

    mLv.setOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        currentPosition = view.getLastVisiblePosition();
      }
    });
  }

  @Override
  public String setTip() {
    return getString(R.string.loading);
  }

  @Override
  public void loadData() {
    getInitLisst();
  }

  private void getInitLisst() {
    try {
      judgeNet();
      List<RequestParams> list = getParamList();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.BUY_SEEDS_RECORDS,
          mGetBuyRecordListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.get_buy_seeds_records_fail)));
      resetEmptyRefresh(true);
      showEmptyLayout(true);
      setEmptyLayoutFail();
    }
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));

    if (null != getMore && getMore) {
      size = mBuylist.size() + 5;
    } else {
      size = mBuylist.size() == 0 ? size : mBuylist.size();
    }

    RequestParams paramsPage = new RequestParams("number", size + "");
    list.add(paramsToken);
    list.add(paramsPage);
    return list;
  }

  private void getBuyList(BuyRecordResultBean result) {
    if (null == result || result.getResult() == null) {
      if (CollectionUtils.isEmpty(mBuylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      fail();
      setEmptyLayoutFail();
      resetEmptyRefresh(true);
      return;
    }
    if ("1".equals(result.getType())) {
      updatePage(result);
    } else if ("0".equals(result.getType())) {
      fail();
      setEmptyLayoutFail();
      if (CollectionUtils.isEmpty(mBuylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
    }
  }

  private void updatePage(BuyRecordResultBean result) {
    if (null == result) {
      setEmptyLayoutSuccess();
      resetEmptyRefresh(true);
      return;
    }
    List<ResultBean> buylist = result.getResult();
    mBuylist.clear();
    if (!CollectionUtils.isEmpty(buylist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mBuylist.addAll(buylist);
    }

    if (!CollectionUtils.isEmpty(mBuylist)) {
      showEmptyLayout(false);
      mLv.setAdapter(new AllScanBuySeedsRecordAdapter(this, mBuylist, this));
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mBuylist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mBuylist.size();
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_back:
        finish();
        break;
      default:
        break;
    }
  }

  //收割的回调
  @Override
  public void onItem(Object object, int position) {
    ResultBean bean = (ResultBean) object;
    Intent intent = new Intent(this, BuySeedsRecordDetailsActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("item", bean);
    intent.putExtra("item", bundle);
    startActivity(intent);
  }


  //更新列表
  private void updateListAdapter() {
    if (null != mAdapter) {
      mBuylist.remove(mPosition);
      mAdapter.notifyDataSetChanged();
    }
  }

  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      getInitLisst();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      getInitLisst();
    }
  }


  public class EmptyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      emptyRefresh = true;
      getInitLisst();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
}
