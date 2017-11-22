package com.example.administrator.lubanone.fragment;

import static com.example.qlibrary.utils.DebugUtils.convert;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedsMemberInfoActivity;
import com.example.administrator.lubanone.activity.home.SellNoMoneyDetailsActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessSellNoMoneyAdapter;
import com.example.administrator.lubanone.adapter.homepage.ProcessSellNoMoneyTimerAdapterNew;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.GPaylistBean;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
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

public class SellMoneyFragment extends BaseFragment implements OnMoneyPayListener<GPaylistBean>,
    OnBuyVIPListener, OnItemListener, OnItemClickListener {

  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<GPaylistBean> mPaylist = new ArrayList<>();
  private ProcessSellNoMoneyAdapter mMoneyAdapter;
  private int downPage = 0;
  private List<GPaylistBean> mPaylistBeanList;


  private int size = 5;
  private Boolean getMore;
  private int currentPosition;
  private int itemCount;
  private PullToRefreshLayout mEmptyLayout;


  private int longPosition;
  private Boolean longPayMoney;
  private Boolean getDownMore;


  //延长打款时间
  private RequestListener mLongPayTimeListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        longerResult(result);
      } catch (Exception e) {
        if (isAdded()) {
          showMsg(getString(R.string.long_pay_time_fail_one));
        }
        resetLongPayMoney(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (isAdded()) {

        showMsg(getString(R.string.long_pay_time_fail_one));
      }
      resetLongPayMoney(false);
    }
  };


  //投诉
  private RequestListener mComplaintListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        comliantResult(result);
      } catch (Exception e) {
        if (isAdded()) {
          showMsg(getString(R.string.tousu_fail));
        }
        resetTousu(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.tousu_fail));
      resetTousu(false);
    }
  };
  private int mPosition;
  private String mProlongtime;
  private String mPrice;

  public void resetTousu(boolean toTargetPosition) {
    if (tousu != null && tousu) {
      tousu = false;
      if (toTargetPosition) {
        mLv.setSelection(compliantPosition - 1 >= 0 ? compliantPosition - 1 : 0);
      }
    }
  }

  //催一催
  private RequestListener mCuiYiCuiListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        cuiResult(result);
      } catch (Exception e) {
        if (isAdded()) {
          showMsg(getString(R.string.cui_fail));
        }
        resetCuiYiCui(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (isAdded()) {
        showMsg(getString(R.string.cui_fail));
      }
      resetCuiYiCui(false);
    }
  };

  //listview
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        resetGetDownMore();
        Result<SellMoneyListResultBean> result = GsonUtil
            .processJson(jsonObject, SellMoneyListResultBean.class);
        getListInfo(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mPaylist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetCuiYiCui(false);
        resetLongPayMoney(false);
        resetTousu(false);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetDetails(false);
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (CollectionUtils.isEmpty(mPaylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      resetCuiYiCui(false);
      resetLongPayMoney(false);
      resetTousu(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetDetails(false);
      resetGetDownMore();
    }
  };


  private void updateTopInfo(SellMoneyListResultBean result) {
    if (null == result) {
      return;
    }

    //延长打款时间time
    mProlongtime = result.getProlongtime();

    //待匹配
    String jsbzlist = result.getJsbzlist();
    //待确认
    String g_confirmlist = result.getG_confirmlist();
    //待付款
    String listcount = result.getListcount();
    //待评价
    String g_dpingjialist = result.getG_dpingjialist();
    SellSeedsProcessActivity activity = (SellSeedsProcessActivity) mActivity;
    activity.onitem(jsbzlist, listcount, g_confirmlist, g_dpingjialist);
  }

  private void updatePage(SellMoneyListResultBean result) {

    //更新上面的四个按钮
    updateTopInfo(result);

    List<GPaylistBean> paylist = result.getG_paylist();
    mPaylist.clear();
    if (!CollectionUtils.isEmpty(paylist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mPaylist.addAll(paylist);
    }
    if (!CollectionUtils.isEmpty(mPaylist)) {
      showEmptyLayout(false);
      mPrice = result.getPrice();
      ProcessSellNoMoneyTimerAdapterNew moneyAdapter = new ProcessSellNoMoneyTimerAdapterNew(
          MyApplication.getContext(), mPaylist, this, this, this, mPrice);
      mLv.setAdapter(moneyAdapter);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mPaylist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      resetCuiYiCui(true);
      resetLongPayMoney(true);
      resetTousu(true);
      itemCount = mPaylist.size();
      resetDetails(true);
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetCuiYiCui(false);
      resetLongPayMoney(false);
      resetTousu(false);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      resetDetails(false);
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

  @Override
  public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
    final GPaylistBean itemAtPosition = (GPaylistBean) parent.getItemAtPosition(position);

    view.findViewById(R.id.ll_top).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_mid).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_down).setOnClickListener(new MyOnClickListener(itemAtPosition, position));

    View tvLianxi = view.findViewById(R.id.tv_lianxi);
    tvLianxi.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mActivity, BuySeedsMemberInfoActivity.class);
        intent.putExtra("userId", DebugUtils.convert(itemAtPosition.getBuymember(), ""));
        startActivity(intent);
      }
    });
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
    pull.setPullUpEnable(true);//设置不让上拉加载

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
        resetDetails(false);
        resetGetDownMore();
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(MyApplication.getContext(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mPaylist.size() + 5;
      } else {
        size = mPaylist.size() == 0 ? size : mPaylist.size() + 5;
      }

      RequestParams paramsPage = new RequestParams("number", size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_NO_MONEY, mListener, RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetDetails(false);
      resetGetDownMore();
    }
  }

  private void getListInfo(Result<SellMoneyListResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mPaylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetCuiYiCui(false);
      resetLongPayMoney(false);
      resetTousu(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetDetails(false);
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      if (CollectionUtils.isEmpty(mPaylist)) {
        showEmptyLayout(true);
      }
      resetCuiYiCui(false);
      resetLongPayMoney(false);
      resetTousu(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetDetails(false);
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

  //投诉和延长打款回调
  @Override
  public void onItem(Object object, int position, int type) {
    try {
      if (!NetUtil.isConnected(getActivity())) {
        throw new DefineException(getString(R.string.NET_ERROR));
      }
      if (type == 1) {
        showComplaintDialog((GPaylistBean) object, position);
      }
      if (type == 2) {

        allowDialog((GPaylistBean) object, position);
        //延长打款
      }
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(e.getMessage());
      }
    }
  }

  private void allowDialog(final GPaylistBean object, final int position) {
    StytledDialog.showIosAlert(getActivity(),
        MyApplication.getContext().getResources().getString(R.string.is_agree),
        getString(R.string.sell_long_tips) + DebugUtils.convert(mProlongtime, Config.long_time)
            + getString(R.string.hour_normal), getString(R.string.cancels),
        getString(R.string.confirm), "", true, true,
        new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
            longerPayTime((GPaylistBean) object, position);
          }
        });
  }

  private void showComplaintDialog(final GPaylistBean object, final int position) {

    StytledDialog.showIosAlert(mActivity, getString(R.string.complain),
        getString(R.string.complaint_dialog_tip), getString(R.string.cancel),
        getString(R.string.confim_no_space), "", true, true, new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
            compliantPosition = position;
            tousu = true;
            //投诉
            compliant((GPaylistBean) object, position);
          }
        });
  }

  public void resetLongPayMoney(boolean toTargetPosition) {
    if (null != longPayMoney && longPayMoney) {
      longPayMoney = false;
      if (toTargetPosition) {
        mLv.setSelection(longPosition);
      }
    }
  }

  //延长打款时间
  private void longerPayTime(GPaylistBean object, int position) {

    try {
      longPosition = position;
      longPayMoney = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          convert(object.getOrderid(), ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.AGREE_LONGER_PAY_TIME, mLongPayTimeListener,
          RequestNet.POST);
    } catch (Exception e) {
      resetLongPayMoney(false);
    }
  }

  private void longerResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.long_pay_time_success));
      getInitList();
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.long_pay_time_fail_one)));
      resetLongPayMoney(false);
    }
  }


  private void updateLongData() {
    if (null != mMoneyAdapter && !CollectionUtils.isEmpty(mPaylist) && longPosition != -1) {
      GPaylistBean paylistBean = mPaylist.get(longPosition);
      paylistBean.setProlongbutton(2 + "");
      mMoneyAdapter.notifyDataSetChanged();
    }
  }

  private int compliantPosition;
  private Boolean tousu;

  //投诉
  private void compliant(GPaylistBean object, int position) {

    try {
      compliantPosition = position;
      tousu = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          convert(object.getOrderid(), ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.COMPLAINT, mComplaintListener,
          RequestNet.POST);

    } catch (Exception e) {
      resetTousu(false);
      if (isAdded()) {
        showMsg(getString(R.string.tousu_fail));
      }
    }
  }


  private Boolean isDetails;

  private void resetDetails(boolean toPosition) {
    if (null != isDetails && isDetails) {
      isDetails = false;
      if (toPosition && -1 != mPosition) {
        mLv.setSelection(mPosition);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 0 && null != data) {
      isDetails = true;
      mPosition = data.getIntExtra("position", -1);
      getInitList();
    }
  }

  private void comliantResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.tousu_success));
      getInitList();
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.tousu_fail)));
      resetTousu(false);
    }
  }

  //卖方会员回调
  @Override
  public void onVIPItemClick(Object item, int position) {
    Intent intent = new Intent();
    intent.putExtra("userId", DebugUtils.convert(((GPaylistBean) item).getUserid(), ""));
    intent.setClass(mActivity, BuySeedsMemberInfoActivity.class);
    startActivity(intent);
  }


  private int cuiPosition = -1;
  private Boolean cuiYiCui;


  public void resetCuiYiCui(boolean toTargetPosition) {
    if (null != cuiYiCui && cuiYiCui) {
      cuiYiCui = false;
      if (toTargetPosition) {
        mLv.setSelection(cuiPosition);
      }
    }

  }

  //催一崔回调
  @Override
  public void onCuiPayClick(GPaylistBean item, int position) {
    try {
      cuiPosition = position;
      cuiYiCui = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderid = new RequestParams("orderid", item.getOrderid());
      list.add(paramsToken);
      list.add(paramsOrderid);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_CUI, mCuiYiCuiListener, RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.cui_fail));
      }
      resetCuiYiCui(false);
    }

  }

  private void cuiResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.cui_success));
      getInitList();
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.cui_fail)));
      resetCuiYiCui(false);
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
    System.out.println("SellMoneyFragment.onVisible");
    initData();
  }

  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }

  private class MyOnClickListener implements OnClickListener {


    private final GPaylistBean mItemAtPosition;
    private final int mPosition;

    public MyOnClickListener(GPaylistBean itemAtPosition, int position) {
      mItemAtPosition = itemAtPosition;
      mPosition = position;
    }

    @Override
    public void onClick(View v) {
      Intent intent = new Intent(mActivity, SellNoMoneyDetailsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", mItemAtPosition);
      bundle.putString("price", mPrice);
      intent.putExtra("item", bundle);
      intent.putExtra("position", mPosition);
      startActivityForResult(intent, 0);
    }
  }
}
