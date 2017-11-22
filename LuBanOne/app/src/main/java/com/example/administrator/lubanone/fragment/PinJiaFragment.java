package com.example.administrator.lubanone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.PingLunActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessPingJiaAdapter;
import com.example.administrator.lubanone.adapter.homepage.ProcessPingJiaAdapterNew;
import com.example.administrator.lubanone.bean.homepage.BuyPingJiaResultBean;
import com.example.administrator.lubanone.bean.homepage.BuyPingJiaResultBean.PDpingjialistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/30.
 */

public class PinJiaFragment extends BaseFragment implements OnItemClickListener {

  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<PDpingjialistBean> mPingjialist = new ArrayList<>();
  private ProcessPingJiaAdapter mAdapter;

  private int size = 5;
  private Boolean getMore;
  private Boolean getDownMore;
  private int currentPosition;
  private int itemCount;


  //第一次请求数据列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      dismissLoadingDialog();
      try {
        resetGetDownMore();
        Result<BuyPingJiaResultBean> result = GsonUtil
            .processJson(jsonObject, BuyPingJiaResultBean.class);
        getList(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mPingjialist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        setEmptyLayoutFail();
        resetEmptyRefresh(true);
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      if (CollectionUtils.isEmpty(mPingjialist)) {
        showEmptyLayout(true);
      }
      setEmptyLayoutFail();
      resetEmptyRefresh(true);
      resetGetDownMore();
    }
  };


  private PullToRefreshLayout mEmptyLayout;
  private String mPrice;

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

  public void showEmptyMsg() {
    if (isAdded() && null != emptyRefresh && emptyRefresh) {
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

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    PDpingjialistBean itemAtPosition = (PDpingjialistBean) parent.getItemAtPosition(position);
//    view.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//      }
//    });
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
    View view = mInflater.inflate(R.layout.list_view_only, null);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    mLv = (PullableListView) view.findViewById(R.id.lv);
    pull = (PullToRefreshLayout) view.findViewById(R.id.task_fragment_pullToRefreshLayout);

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
    return view;
  }

  @Override
  public void initData() {
    getInitList();
  }


  @Override
  public void setUserVisibleHint(boolean hidden) {
    super.setUserVisibleHint(hidden);
  }

  private void getInitList() {
    try {
      if (!NetUtil.isConnected(MyApplication.getContext())) {
        ToastUtil.showShort(MyApplication.getContext().getString(R.string.NET_ERROR),
            MyApplication.getContext());
        showEmptyLayout(true);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetGetDownMore();
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(MyApplication.getContext(), Config.USER_INFO, Config.TOKEN, ""));
      if (null != getMore && getMore) {
        size = mPingjialist.size() + 5;
      } else {
        size = mPingjialist.size() == 0 ? size : mPingjialist.size() + 5;
      }
      RequestParams paramsPage = new RequestParams("number",
          size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.BUY_PINGJIA_LIST, mListener, RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.get_ping_lun_info_fail));
      }
      resetGetDownMore();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    getInitList();
  }


  private void getList(Result<BuyPingJiaResultBean> result) {
    if (null == result) {
      fail();
      setEmptyLayoutFail();
      resetEmptyRefresh(true);
      if (CollectionUtils.isEmpty(mPingjialist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      fail();
      setEmptyLayoutFail();
      resetEmptyRefresh(true);
      if (null != getMore && getMore) {
        getMore = false;
      }
      if (CollectionUtils.isEmpty(mPingjialist)) {
        showEmptyLayout(true);
      }
    }
  }


  //下拉监听
  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      getDownMore = true;
      getInitList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      getInitList();
    }

  }

  private void updateTopInfo(BuyPingJiaResultBean result) {
    if (null == result) {
      return;
    }
    String tgbzlist = result.getTgbzlist();
    String p_paylist = result.getP_paylist();
    String p_confirmlist = result.getP_confirmlist();
    String listcount = result.getListcount();
    BuySeedProcessActivity activity = (BuySeedProcessActivity) mActivity;
    activity.onitem(tgbzlist, p_paylist, p_confirmlist, listcount);
  }

  //更新界面
  private void updatePage(BuyPingJiaResultBean bean) {
    updateTopInfo(bean);
    final List<PDpingjialistBean> dpingjialist = bean.getP_dpingjialist();
    mPingjialist.clear();
    if (!CollectionUtils.isEmpty(dpingjialist)) {
      success();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      mPingjialist.addAll(dpingjialist);
      size = dpingjialist.size();
    }
    if (!CollectionUtils.isEmpty(mPingjialist)) {
      showEmptyLayout(false);
      mPrice = bean.getPrice();
      ProcessPingJiaAdapterNew adapter = new ProcessPingJiaAdapterNew(
          MyApplication.getContext(), mPingjialist,
          new OnListViewItemListener() {
            @Override
            public void onItem(Object object, int position) {
              PDpingjialistBean dpingjialistBean = (PDpingjialistBean) object;
              Intent intent = new Intent(getActivity(), PingLunActivity.class);
              Bundle bundle = new Bundle();
              bundle.putString("type", "buy");
              bundle.putSerializable("orderId", dpingjialistBean
                  .getOrderid());
              intent.putExtra("bundle", bundle);
              startActivityForResult(intent, 3);
            }
          }, mPrice);
      mLv.setAdapter(adapter);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mPingjialist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mPingjialist.size();
    } else {
      fail();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      showEmptyLayout(true);
      if (null != getMore && getMore) {
        getMore = false;
      }
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


  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onVisible() {
    System.out.println("PinJiaFragment.onVisible");
    initData();
  }


  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }
}
