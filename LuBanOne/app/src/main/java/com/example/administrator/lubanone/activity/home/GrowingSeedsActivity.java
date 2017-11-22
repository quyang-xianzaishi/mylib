package com.example.administrator.lubanone.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.AllScanBuySeedsRecordAdapter;
import com.example.administrator.lubanone.adapter.homepage.NewOneHomeFragmentLvAdapter;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean.GrowseedslistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 成长中的种子
 */
public class GrowingSeedsActivity extends BaseActivity implements OnListViewItemListener,
    OnItemClickListener {


  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private List<GrowseedslistBean> mBuylist = new ArrayList<>();

  private int size = 10;
  private Boolean getMore;
  private Boolean emptyRefresh;

  private PullToRefreshLayout mEmptyLayout;
  private AllScanBuySeedsRecordAdapter mAdapter;
  private int currentPosition;
  private int itemCount;


  private RequestListener mReapListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        reapResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.reap_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(errorMsf);
    }
  };


  private RequestListener mGetBuyRecordListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<NewHomeFragmentResultBean> result = GsonUtil
            .processJson(jsonObject, NewHomeFragmentResultBean.class);
        getBuyList(result);
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
    return R.layout.activity_growing;
  }

  @Override
  public void initView() {

    FrameLayout ivBack = (FrameLayout) findViewById(R.id.iv_back);
    TextView tvTitle = (TextView) findViewById(R.id.tv_title);
    tvTitle.setText(getString(R.string.growing_seed));
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

    mLv.setOnItemClickListener(this);
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
    getInitLisst();
  }

  private void getInitLisst() {
    try {
      judgeNet();
      List<RequestParams> list = getParamList();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.GROWING_SEEDS_LSIT,
          mGetBuyRecordListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.get_growing_seeds)));
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

  private void getBuyList(Result<NewHomeFragmentResultBean> result) {
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
      updatePage(result.getResult());
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

  private void updatePage(NewHomeFragmentResultBean result) {
    if (null == result) {
      setEmptyLayoutSuccess();
      resetEmptyRefresh(true);
      return;
    }
    List<GrowseedslistBean> buylist = result.getGrowseedslist();
    mBuylist.clear();
    if (!CollectionUtils.isEmpty(buylist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mBuylist.addAll(buylist);
    }

    if (!CollectionUtils.isEmpty(mBuylist)) {
      showEmptyLayout(false);
      mLv.setAdapter(new NewOneHomeFragmentLvAdapter(this, mBuylist, this));
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
    alertConfimDialog((GrowseedslistBean)object);
  }

  private void reapSeeds(GrowseedslistBean position) {
    if (null == position) {
      return;
    }
    if (!NetUtil.isConnected(getApplicationContext())) {
      showMsg(getString(R.string.NET_ERROR));
      return;
    }
    ArrayList<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsOrderId = new RequestParams("orderid",
        DebugUtils.convert(position.getOrderid(), ""));
    list.add(paramsToken);
    list.add(paramsOrderId);
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.REAP_SEEDS, mReapListener,
        RequestNet.POST);
  }


  private void reapResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      show(getString(R.string.reap_success));
      updateListAdapter();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.reap_fail)));
    }
  }


  //更新列表
  private void updateListAdapter() {
    getInitLisst();
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
    final GrowseedslistBean itemAtPosition = (GrowseedslistBean) parent.getItemAtPosition(position);
    view.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(GrowingSeedsActivity.this, BuySeedsRecordDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("items", itemAtPosition);
        intent.putExtra("items", bundle);
        startActivity(intent);
      }
    });

    //收割
//    TextView reap = (TextView) view.findViewById(R.id.tv_right);
//    reap.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        reapSeeds(itemAtPosition);
//      }
//    });
  }

  private void alertConfimDialog(final GrowseedslistBean bean) {
    StytledDialog
        .showTwoBtnDialog(this, getString(R.string.confim_reap), getString(R.string.reap_tips),
            getString(R.string.cancels), getString(R.string.confim), false, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                reapSeeds(bean);
              }
            });
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
