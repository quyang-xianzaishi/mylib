package com.example.administrator.lubanone.fragment;

import android.content.Intent;
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
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.adapter.homepage.SellMatchSeedsProcessAdapter;
import com.example.administrator.lubanone.adapter.homepage.SellMatchSeedsProcessAdapterNew;
import com.example.administrator.lubanone.bean.homepage.SellMatchListResultBean;
import com.example.administrator.lubanone.bean.homepage.SellMatchListResultBean.JsbzlistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.DialogManager;
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

public class SellMatchFragment extends BaseFragment implements OnItemClickListener {

  private ListView mLv;
  private DialogManager mDialogManager;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<JsbzlistBean> mTgbzlist = new ArrayList<>();

  private int size = 5;
  private Boolean getMore;
  private int currentPosition;
  private int itemCount;


  //卖出带匹配
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        resetGetDownMore();
        Result<SellMatchListResultBean> result = GsonUtil
            .processJson(jsonObject, SellMatchListResultBean.class);
        getList(result);
      } catch (Exception e) {
        fail();
        resetEmptyRefresh(false);
        if (CollectionUtils.isEmpty(mTgbzlist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        setEmptyLayoutFail();
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      resetEmptyRefresh(false);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      if (CollectionUtils.isEmpty(mTgbzlist)) {
        showEmptyLayout(true);
      }
      setEmptyLayoutFail();
      resetGetDownMore();
    }
  };
  private SellMatchSeedsProcessAdapter mAdapter;
  private PullToRefreshLayout mEmptyLayout;
  private int lastPage = -1;
  private String mPrice;


  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }

  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }

  private Boolean emptyRefresh;
  private Boolean getDownMore;

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
//    final JsbzlistBean itemAtPosition = (JsbzlistBean) parent.getItemAtPosition(position);
//    view.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Intent intent = new Intent(mActivity, SellMatchFramentDEtailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", itemAtPosition);
//        bundle.putString("price",mPrice);
//        intent.putExtra("item", bundle);
//        startActivityForResult(intent, 0);
//      }
//    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
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
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_MATCH, mListener,
          RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.get_no_match_info_fail));
      }
    }
  }


  private void getList(Result<SellMatchListResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mTgbzlist)) {
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
      if (CollectionUtils.isEmpty(mTgbzlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  }


  private void updateTopInfo(SellMatchListResultBean result) {
    if (null == result) {
      return;
    }
    //待匹配
    String listcount = result.getListcount();
    //待确认
    String g_confirmlist = result.getG_confirmlist();
    //待付款
    String g_paylist = result.getG_paylist();
    //待评价
    String g_dpingjialist = result.getG_dpingjialist();
    SellSeedsProcessActivity activity = (SellSeedsProcessActivity) mActivity;
    activity.onitem(listcount, g_paylist, g_confirmlist, g_dpingjialist);
  }

  private void updatePage(SellMatchListResultBean result) {

    //更新上面的四个按钮
    updateTopInfo(result);

    List<JsbzlistBean> tgbzlist = result.getJsbzlist();
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
      SellMatchSeedsProcessAdapterNew adapter = new SellMatchSeedsProcessAdapterNew(
          mTgbzlist, mPrice);
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
      showEmptyLayout(true);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      fail();
      if (null != getMore && getMore) {
        getMore = false;
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
    System.out.println("SellMatchFragment.onVisible");
    initData();
  }
}
