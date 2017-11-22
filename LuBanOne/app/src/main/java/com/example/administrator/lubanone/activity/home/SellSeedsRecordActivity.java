package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.AllScanSellSeedsRecordAdapter;
import com.example.administrator.lubanone.bean.homepage.SellSeedsRecordsresultBean;
import com.example.administrator.lubanone.bean.homepage.SellSeedsRecordsresultBean.ResultBean;
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
 * 卖出种子记录
 */
public class SellSeedsRecordActivity extends BaseActivity implements OnListViewItemListener {


  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private List<ResultBean> mSelllist = new ArrayList<>();
  private int mPosition = -1;

  private int size = 30;
  private Boolean getMore;
  private int currentPosition;
  private int itemCount;


  private PullToRefreshLayout mEmptyLayout;
  private AllScanSellSeedsRecordAdapter mAdapter;
  private Boolean emptyRefresh;


  private RequestListener mGetSellRecordListener = new RequestListener() {

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        SellSeedsRecordsresultBean bean = gson
            .fromJson(jsonObject, SellSeedsRecordsresultBean.class);
        getList(bean);
      } catch (Exception e) {
        fail();
        setEmptyLayoutSuccess();
        if (CollectionUtils.isEmpty(mSelllist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetEmptyRefresh(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (CollectionUtils.isEmpty(mSelllist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      fail();
      setEmptyLayoutFail();
      resetEmptyRefresh(false);
    }
  };


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_sell_seeds_record;
  }

  @Override
  public void initView() {

    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    TextView tvTitle = (TextView) findViewById(R.id.tv_title);
    tvTitle.setText(getString(R.string.sell_seed_record_one));
    TextView ivFinaceCenter = (TextView) findViewById(R.id.tv_finance_center);
    mEmptyLayout = (PullToRefreshLayout) findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    ivBack.setOnClickListener(this);
    ivFinaceCenter.setOnClickListener(this);

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
  public void loadData() {
    getInitList();
  }

  private void getInitList() {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mSelllist.size() + 5;
      } else {
        size = mSelllist.size() == 0 ? size : mSelllist.size();
      }

      RequestParams paramsPage = new RequestParams("number", size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SELL_SEEDS_RECORDS,
          mGetSellRecordListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.GET_DATE_FAIL)));
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      showEmptyLayout(true);
    }
  }


  private void getList(SellSeedsRecordsresultBean result) {
//    if (null == result || CollectionUtils.isEmpty(result.getResult())) {
//      if (CollectionUtils.isEmpty(mSelllist)) {
//        showEmptyLayout(true);
//      }
//      if (null != getMore && getMore) {
//        getMore = false;
//      }
//      fail();
//      setEmptyLayoutSuccess();
//      resetEmptyRefresh(true);
////      showMsg(DebugUtils
////          .convert(result.getMsg(), getString(R.string.sell_seed_record_empty)));
//      return;
//    }

    if ("1".equals(result.getType())) {
      updatePage(result);
    } else if ("0".equals(result.getType())) {
      fail();
      setEmptyLayoutSuccess();
      if (CollectionUtils.isEmpty(mSelllist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
//      showMsg(DebugUtils
//          .convert(result.getMsg(), getString(R.string.sell_seed_record_empty)));
    }
  }

  private void updatePage(SellSeedsRecordsresultBean result) {
    if (null == result) {
      setEmptyLayoutSuccess();
      resetEmptyRefresh(true);
      return;
    }
    List<ResultBean> list = result.getResult();
    mSelllist.clear();
    if (!CollectionUtils.isEmpty(list)) {
      success();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      mSelllist.addAll(list);
    }
    if (!CollectionUtils.isEmpty(mSelllist)) {
      showEmptyLayout(false);
      mLv.setAdapter(new AllScanSellSeedsRecordAdapter(this, mSelllist, this));
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mSelllist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mSelllist.size();
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
//      showMsg(getString(R.string.sell_seed_record_empty));
    }
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_back:
      case R.id.tv_finance_center:
        finish();
        break;
      default:
        break;
    }
  }

  //收割回调
  @Override
  public void onItem(Object object, int position) {

  }

  class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      getInitList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      getInitList();
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

  private void resetEmptyRefresh(boolean b) {
    if (emptyRefresh != null && emptyRefresh) {
      emptyRefresh = false;
      if (b && CollectionUtils.isEmpty(mSelllist)) {
        showMsg(getString(R.string.no_more_message));
      }
    }
  }

  public class EmptyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      emptyRefresh = true;
      getInitList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
