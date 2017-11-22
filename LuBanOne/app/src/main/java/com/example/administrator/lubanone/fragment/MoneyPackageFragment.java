package com.example.administrator.lubanone.fragment;

import static com.example.administrator.lubanone.R.id.tv_dream_package_seeds;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.SellSeedsActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.adapter.homepage.MoneyPackageLvAdapter;
import com.example.administrator.lubanone.bean.finance.MoneyPackageResultBean;
import com.example.administrator.lubanone.bean.finance.MoneyPackageResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.PriceUtil;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by quyang on 2017/6/27.
 */

public class MoneyPackageFragment extends BaseFragment implements OnClickListener {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(tv_dream_package_seeds)
  TextView mTvDreamPackageSeeds;
  @BindView(R.id.sell_seeds_btn)
  Button mSellSeedsBtn;
  @BindView(R.id.tv_data_click)
  TextView mTvDataClick;
  @BindView(R.id.tv_order_id_click)
  TextView mTvOrderIdClick;
  @BindView(R.id.tv_trade_vip_click)
  TextView mTvTradeVipClick;
  @BindView(R.id.tv_trade_type_click)
  TextView mTvTradeTypeClick;
  @BindView(R.id.lv)
  ListView mLv;
  @BindView(R.id.money_package_count)
  TextView count;
  Unbinder unbinder;
  private Unbinder mBind;
  private PullToRefreshLayout pull;
  private List<DeallistBean> mDeallist = new ArrayList<>();
  private PullToRefreshLayout mEmptyLayout;
  private TextView mTvBack;


  private int size = 5;
  private Boolean getMore;
  private int currentPosition;
  private int itemCount;


  //init
  private RequestListener mMoneyPackageListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<MoneyPackageResultBean> result = GsonUtil
            .processJson(jsonObject, MoneyPackageResultBean.class);
        getMoneyPackageInfo(result);
      } catch (Exception e) {
        if (CollectionUtils.isEmpty(mDeallist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        fail();
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
      }

    }

    @Override
    public void onFail(String errorMsf) {
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      if (CollectionUtils.isEmpty(mDeallist)) {
        showEmptyLayout(true);
      }
      fail();
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  };

  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }


  private Boolean emptyRefresh;

  public void resetEmptyRefresh(boolean showMsg) {
    if (showMsg) {
      showEmptyMsg();
    }
    if (null != emptyRefresh && emptyRefresh) {
      emptyRefresh = false;
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


  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.money_package_layout, null);
    mBind = ButterKnife.bind(this, view);

    mTvDreamPackageSeeds.setText(getString(R.string.money_packge_seeds_count));

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);
    mTvBack = (TextView) view.findViewById(R.id.tv_back);
    mTvBack.setOnClickListener(this);

    //下拉熟悉
    mLv = (PullableListView) view.findViewById(R.id.lv);
    pull = (PullToRefreshLayout) view.findViewById(R.id.pull);
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

    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_back:
        getActivity().finish();
        break;
    }
  }

  public void setTitle(String moneyPackage) {
    mTvDreamPackageSeeds
        .setText(new StringBuilder().append(getString(R.string.money_packge_seeds_count))
            .append(moneyPackage));
  }

  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

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

  @Override
  public void initData() {
    getInitList();
  }

  private void getInitList() {
    if (!NetUtil.isConnected(getContext())) {
      showMsg(getInfo(R.string.NET_ERROR));
      showEmptyLayout(true);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      return;
    }
    ArrayList<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));

    if (null != getMore && getMore) {
      size = mDeallist.size() + 5;
    } else {
      size = mDeallist.size() == 0 ? size : mDeallist.size();
    }
    RequestParams paramsPage = new RequestParams("number", size + "");
    list.add(paramsPage);
    list.add(paramsToken);
    RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
        list, Urls.MONEY_PACKAGE, mMoneyPackageListener,
        RequestNet.POST);
  }

  private void getMoneyPackageInfo(Result<MoneyPackageResultBean> result) {

    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mDeallist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result);
    } else {
      if (CollectionUtils.isEmpty(mDeallist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      fail();
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  }

  public void fail() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }


  public void success() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }

  private void updatePage(Result<MoneyPackageResultBean> result) {

    String rewardbagcount = result.getResult().getRewardbagcount();
    mTvDreamPackageSeeds
        .setText(new StringBuilder().append(getString(R.string.money_packge_seeds_count))
            .append(PriceUtil
                .getPrice(rewardbagcount, result.getResult().getPrice(), getString(R.string.ke))));
    count.setText(rewardbagcount);
    List<DeallistBean> deallist = result.getResult().getDeallist();
    mDeallist.clear();
    if (!CollectionUtils.isEmpty(deallist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mDeallist.addAll(deallist);
    }
    if (!CollectionUtils.isEmpty(mDeallist)) {
      showEmptyLayout(false);
      mLv.setAdapter(new MoneyPackageLvAdapter(mActivity, mDeallist));
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mDeallist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mDeallist.size();
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(true);
      setEmptyLayoutSuccess();
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

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (null != mBind) {
      mBind.unbind();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (null != unbinder) {
      unbinder.unbind();
    }
  }

  @OnClick({R.id.iv_back, R.id.sell_seeds_btn})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
        startActivity(new Intent(getActivity(), MainActivity.class));
        break;
      case R.id.sell_seeds_btn:
        startActivity(new Intent(getActivity(), SellSeedsActivity.class));
        break;
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
}
