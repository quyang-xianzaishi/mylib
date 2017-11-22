package com.example.administrator.lubanone.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.adapter.homepage.NewBuySeedsProcessAdapter;
import com.example.administrator.lubanone.bean.homepage.BuyMatchListResultBean;
import com.example.administrator.lubanone.bean.homepage.BuyMatchListResultBean.TgbzlistBean;
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
 * Created by quyang on 2017/6/30.
 */

public class MatchFragment extends BaseFragment implements OnItemClickListener {

  private ListView mLv;
  private PullToRefreshLayout pull;
  private List<TgbzlistBean> mTgbzlist = new ArrayList<>();
  private PullToRefreshLayout mEmptyLayout;
  private int size = 5;
  private Boolean getMore;
  private Boolean getDownMore;
  private int currentPosition;
  private int itemCount;


  //买入带匹配
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      dismissLoadingDialog();
      try {
        resetGetDownMore();
        Log.e("TAG", "buy match=" + jsonObject);
        Result<BuyMatchListResultBean> result = GsonUtil
            .processJson(jsonObject, BuyMatchListResultBean.class);
        getList(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mTgbzlist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        setEmptyLayoutFail();
        resetEmptyRefresh(false);
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      setEmptyLayoutFail();
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      if (CollectionUtils.isEmpty(mTgbzlist)) {
        showEmptyLayout(true);
      }
      dismissLoadingDialog();
      resetEmptyRefresh(false);
      resetGetDownMore();
    }
  };

  private Boolean emptyRefresh;
  private String mPrice;

  public void resetEmptyRefresh(boolean showMsg) {
    if (showMsg) {
      showEmptyMsg();
    }
    if (null != emptyRefresh && emptyRefresh) {
      emptyRefresh = false;
    }
  }


  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    TgbzlistBean itemAtPosition = (TgbzlistBean) parent.getItemAtPosition(position);
//    if (null != itemAtPosition) {
//      Intent intent = new Intent(mActivity, MatchFramentDEtailsActivity.class);
//      Bundle bundle = new Bundle();
//      bundle.putSerializable("item", itemAtPosition);
//      bundle.putString("price", mPrice);
//      intent.putExtra("item", bundle);
//      startActivity(intent);
//    }
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


  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.list_view_only, null);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    mEmptyLayout.setVisibility(View.GONE);
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
      size = mTgbzlist.size() + 5;
    } else {
      size = mTgbzlist.size() == 0 ? size : mTgbzlist.size()+5;
    }

    RequestParams paramsPage = new RequestParams("number", size + "");
//    RequestParams paramsPage = new RequestParams("page", currentPage + "");
    list.add(paramsToken);
    list.add(paramsPage);
    if (null != mActivity && null != mListener) {
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.BUY_MATCH, mListener, RequestNet.POST);
    }
  }

  private void getList(Result<BuyMatchListResultBean> result) {
    if (null == result) {
      fail();
      setEmptyLayoutFail();
      resetEmptyRefresh(false);
      if (CollectionUtils.isEmpty(mTgbzlist)) {
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
      resetEmptyRefresh(false);
      if (CollectionUtils.isEmpty(mTgbzlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
    }
  }

  private void updatePage(BuyMatchListResultBean result) {
    //更新上面的四个按钮
    updateTopInfo(result);

    List<TgbzlistBean> tgbzlist = result.getTgbzlist();
    mTgbzlist.clear();
    if (!CollectionUtils.isEmpty(tgbzlist)) {
      success();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      mTgbzlist.addAll(tgbzlist);
    }
    if (!CollectionUtils.isEmpty(mTgbzlist)) {
      showEmptyLayout(false);
      mPrice = result.getPrice();
      NewBuySeedsProcessAdapter adapter = new NewBuySeedsProcessAdapter(mTgbzlist, mPrice);
      mLv.setAdapter(adapter);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mTgbzlist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = mTgbzlist.size();
    } else {
      fail();
      setEmptyLayoutSuccess();
      showEmptyLayout(true);
      resetEmptyRefresh(false);
      if (getMore != null && getMore) {
        getMore = false;
      }
    }
  }


  private void updateTopInfo(BuyMatchListResultBean result) {
    if (null == result) {
      return;
    }
    //待匹配
    List<TgbzlistBean> tgbzlist = result.getTgbzlist();
    String listcount = result.getListcount();
    //待确认
    String p_confirmlist = result.getP_confirmlist();
    //待付款
    String p_paylist = result.getP_paylist();
    //待评价
    String p_dpingjialist = result.getP_dpingjialist();
    BuySeedProcessActivity activity = (BuySeedProcessActivity) mActivity;
    activity.onitem(listcount, p_paylist, p_confirmlist, p_dpingjialist);
  }


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
    System.out.println("MatchFragment.onVisible");
    initData();
  }


  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }
}
