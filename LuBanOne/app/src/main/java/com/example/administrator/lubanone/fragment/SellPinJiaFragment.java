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
import com.example.administrator.lubanone.activity.home.PingLunActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessSellPingJiaAdapter;
import com.example.administrator.lubanone.bean.homepage.SellPingJiaResultBean;
import com.example.administrator.lubanone.bean.homepage.SellPingJiaResultBean.GDpingjialistBean;
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

public class SellPinJiaFragment extends BaseFragment implements OnItemClickListener {

  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<GDpingjialistBean> mPingjialist = new ArrayList<>();
  private ProcessSellPingJiaAdapter mAdapter;
  private int currentPosition;
  private int itemCount;
  private Boolean emptyRefresh;
  private Boolean getDownMore;


  //第一次请求数据列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        resetGetDownMore();
        System.out.println("SellPinJiaFragment.testSuccess11111=" + jsonObject);
        Result<SellPingJiaResultBean> result = GsonUtil
            .processJson(jsonObject, SellPingJiaResultBean.class);
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
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      resetEmptyRefresh(false);
      if (CollectionUtils.isEmpty(mPingjialist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      setEmptyLayoutFail();
      resetGetDownMore();
    }
  };
  private PullToRefreshLayout mEmptyLayout;
  private int size = 5;
  private Boolean getMore;
  private String mPrice;


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

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    final GDpingjialistBean itemAtPosition = (GDpingjialistBean) parent.getItemAtPosition(position);
//    view.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent intent = new Intent(getActivity(), PingLunActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("type", "sell");
//        bundle.putSerializable("orderId", itemAtPosition
//            .getOrderid());
//        intent.putExtra("bundle", bundle);
//        startActivityForResult(intent, 0);
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

      RequestParams paramsPage = new RequestParams("number", size + "");

      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_PINGJIA, mListener,
          RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.get_ping_lun_info_fail));
      }
      resetEmptyRefresh(false);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      resetGetDownMore();
    }
  }


  private void getList(Result<SellPingJiaResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mPingjialist)) {
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
      updatePage(result.getResult());
    } else {
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      if (CollectionUtils.isEmpty(mPingjialist)) {
        showEmptyLayout(true);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
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


  private void updateTopInfo(SellPingJiaResultBean result) {
    if (null == result) {
      return;
    }
    //待匹配
    String jsbzlist = result.getJsbzlist();
    //待确认
    String g_confirmlist = result.getG_confirmlist();
    //待付款
    String g_paylist = result.getG_paylist();
    //待评价
    String listcount = result.getListcount();
    SellSeedsProcessActivity activity = (SellSeedsProcessActivity) mActivity;
    activity.onitem(jsbzlist, g_paylist, g_confirmlist, listcount);
  }

  //更新界面
  private void updatePage(SellPingJiaResultBean bean) {
    updateTopInfo(bean);

    List<GDpingjialistBean> dpingjialist = bean.getG_dpingjialist();
    mPingjialist.clear();
    if (!CollectionUtils.isEmpty(dpingjialist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mPingjialist.addAll(dpingjialist);
    }
    if (!CollectionUtils.isEmpty(mPingjialist)) {
      showEmptyLayout(false);
      mPrice = bean.getPrice();
      ProcessSellPingJiaAdapter adapter = new ProcessSellPingJiaAdapter(
          MyApplication.getContext(), mPingjialist,
          new OnListViewItemListener() {
            @Override
            public void onItem(Object object, int position) {
              GDpingjialistBean dpingjialistBean = (GDpingjialistBean) object;
              Intent intent = new Intent(getActivity(), PingLunActivity.class);
              Bundle bundle = new Bundle();
              bundle.putString("type", "sell");
              bundle.putSerializable("orderId",
                  dpingjialistBean.getOrderid());
              intent.putExtra("bundle", bundle);
              startActivityForResult(intent, 4);
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
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
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
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    getInitList();
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
    System.out.println("SellPinJiaFragment.onVisible");
    initData();
  }


  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }
}
