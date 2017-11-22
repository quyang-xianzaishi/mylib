package com.example.administrator.lubanone.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.example.administrator.lubanone.adapter.homepage.DreamPackgetLvAdapter;
import com.example.administrator.lubanone.bean.finance.DreamPackageResultBean;
import com.example.administrator.lubanone.bean.finance.DreamPackageResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
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
 * 梦想背包
 * Created by quyang on 2017/6/27.
 */

public class DreamPackageFragment extends BaseFragment {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.lv)
  PullableListView mLv;
  Unbinder unbinder;
  private Unbinder mUnbinder;
  private PullToRefreshLayout pull;
  private List<DeallistBean> mListData = new ArrayList<>();
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.tv_dream_package_seeds)
  TextView tv_dream_package_seeds;
  @BindView(R.id.sell_seeds_btn)
  Button sell_seeds_btn;

  private Boolean emptyRefresh;

  private int size = 5;
  private DreamPackgetLvAdapter mAdapter;
  private PullToRefreshLayout mEmptyLayout;
  private View mLlLayout;

  private Boolean getMore;


  //首次获取数据
  private RequestListener mmDramPackageListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        if (null != getMore && getMore) {
          getMore = false;
        }
        Result<DreamPackageResultBean> result = GsonUtil
            .processJson(jsonObject, DreamPackageResultBean.class);
        getDreamPackageInfo(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mListData)) {
          showEmptyLayout(true);
        }
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      if (CollectionUtils.isEmpty(mListData)) {
        showEmptyLayout(true);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  };


  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }


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
      getListInfo();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }
  }


  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.dream_package_layout, null);
    mUnbinder = ButterKnife.bind(this, view);

    mLlLayout = view.findViewById(R.id.ll_wanted_layout);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    //下拉熟悉
    mLv = (PullableListView) view.findViewById(R.id.lv);
    pull = (PullToRefreshLayout) view.findViewById(R.id.pull);
    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);

    return view;
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


  @Override
  public void initData() {
    getListInfo();
  }

  private void getListInfo() {
    try {
      if (!NetUtil.isConnected(getContext())) {
        showMsg(getString(R.string.NET_ERROR));
        showEmptyLayout(true);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        return;
      }
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mListData.size() + 5;
      } else {
        size = mListData.size() == 0 ? size : mListData.size() + 5;
      }

      RequestParams paramsPage = new RequestParams("number", size + "");

//    RequestParams paramsPage = new RequestParams("page", currentPage + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNe = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.DREAM_PACKAGE, mmDramPackageListener,
          RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
      resetEmptyRefresh(false);
      showEmptyLayout(true);
      setEmptyLayoutFail();
    }
  }

  private void getDreamPackageInfo(Result<DreamPackageResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mListData)) {
        showEmptyLayout(true);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result);
    } else {
      fail();
      if (CollectionUtils.isEmpty(mListData)) {
        showEmptyLayout(true);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  }

  private void updatePage(Result<DreamPackageResultBean> result) {

    DreamPackageResultBean resultResult = result.getResult();
    String seedcount = resultResult.getSeedcount();

    tv_dream_package_seeds.setText(DebugUtils.convert(seedcount, "0"));

    mListData.clear();
    if (!CollectionUtils.isEmpty(resultResult.getDeallist())) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mListData.addAll(resultResult.getDeallist());
    }
    if (!CollectionUtils.isEmpty(mListData)) {
      showEmptyLayout(false);
      DreamPackgetLvAdapter adapter = new DreamPackgetLvAdapter(mActivity,
          mListData);
      mLv.setAdapter(adapter);
    } else {
      resetEmptyRefresh(true);
      showEmptyLayout(true);
      setEmptyLayoutSuccess();
      fail();
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
    if (null != mUnbinder) {
      mUnbinder.unbind();
    }
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (null != unbinder) {
      unbinder.unbind();
    }
  }

  @OnClick({R.id.iv_back,
      R.id.tv_back, R.id.sell_seeds_btn})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        startActivity(new Intent(getActivity(), MainActivity.class));
        break;
      case R.id.sell_seeds_btn:
        sellSeed();
        break;
    }
  }

  private void sellSeed() {
    startActivity(new Intent(getActivity(), SellSeedsActivity.class));
  }

  public void setTitle(String dreamPackage) {
//    mTvDreamPackageSeeds
//        .setText(new StringBuilder()
//            .append(dreamPackage));
  }

  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      getListInfo();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      getListInfo();
    }
  }

  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      mLlLayout.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      mLlLayout.setVisibility(View.VISIBLE);
    }

  }

}
