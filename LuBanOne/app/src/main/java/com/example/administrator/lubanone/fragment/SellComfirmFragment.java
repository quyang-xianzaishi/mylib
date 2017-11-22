package com.example.administrator.lubanone.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedsMemberInfoActivity;
import com.example.administrator.lubanone.activity.home.SellConfirmDetailsActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessSellConfirmAdapter;
import com.example.administrator.lubanone.adapter.homepage.ProcessSellConfirmTimerAdapterNew;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.GConfirmlistBean;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
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
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/30.
 */

public class SellComfirmFragment extends BaseFragment implements
    OnMoneyPayListener<GConfirmlistBean>, OnListViewItemListener, OnItemClickListener {


  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<GConfirmlistBean> mConfirmlist = new ArrayList<>();
  private static final int REQUEST_ALBUM_OK = 123;
  private File mSaveFile;
  private int lastPage = -1;

  private int size = 5;
  private Boolean getMore;
  private Boolean getDownMore;

  private ProcessSellConfirmAdapter mConfirmAdapter;
  private PullToRefreshLayout mEmptyLayout;
  private int currentPosition;
  private int itemCount;
  private Integer type = null;


  //确认收款和投诉按钮
  private RequestListener mComfimListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        dealResult(result);
      } catch (Exception e) {
        resetTousu(false);
        resetReceiveMoney(false);
        if (isAdded()) {
          showMsg(getString(R.string.tousu_fail));
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      resetTousu(false);
      resetReceiveMoney(false);
      showMsg(getString(R.string.tousu_fail));
    }
  };


  //第一次请求数据列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        resetGetDownMore();
        Result<SellConfirmResultBean> result = GsonUtil
            .processJson(jsonObject, SellConfirmResultBean.class);
        getListInfo(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mConfirmlist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetTousu(false);
        resetReceiveMoney(false);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (CollectionUtils.isEmpty(mConfirmlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      resetTousu(false);
      resetReceiveMoney(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetGetDownMore();
    }
  };
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    initData();
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
  public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    final GConfirmlistBean itemAtPosition = (GConfirmlistBean) parent.getItemAtPosition(position);
    view.findViewById(R.id.ll_top).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_mid).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_down).setOnClickListener(new MyOnClickListener(itemAtPosition, position));

    view.findViewById(R.id.tv_lianxi).setOnClickListener(new OnClickListener() {
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
        size = mConfirmlist.size() + 5;
      } else {
        size = mConfirmlist.size() == 0 ? size : mConfirmlist.size() + 5;
      }

      RequestParams paramsPage = new RequestParams("number", size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_CONFRIM, mListener, RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.get_confirm_info_fail));
      }
      resetGetDownMore();
    }
  }


  private void getListInfo(Result<SellConfirmResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mConfirmlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetTousu(false);
      resetReceiveMoney(false);
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
      resetTousu(false);
      resetReceiveMoney(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      if (CollectionUtils.isEmpty(mConfirmlist)) {
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


  private void updateTopInfo(SellConfirmResultBean result) {
    if (null == result) {
      return;
    }
    String jsbzlist = result.getJsbzlist();
    String listcount = result.getListcount();
    String g_paylist = result.getG_paylist();
    String g_dpingjialist = result.getG_dpingjialist();
    SellSeedsProcessActivity activity = (SellSeedsProcessActivity) mActivity;
    activity.onitem(jsbzlist, g_paylist, listcount, g_dpingjialist);
  }

  //更新界面
  private void updatePage(SellConfirmResultBean result) {
    //更新首页卖出的4个按钮
    updateTopInfo(result);

    List<GConfirmlistBean> pConfirmlist = result.getG_confirmlist();
    mConfirmlist.clear();
    if (!CollectionUtils.isEmpty(pConfirmlist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      mConfirmlist.addAll(pConfirmlist);
    }
    if (!CollectionUtils.isEmpty(mConfirmlist)) {
      showEmptyLayout(false);
      mPrice = result.getPrice();
      ProcessSellConfirmTimerAdapterNew confirmAdapter = new ProcessSellConfirmTimerAdapterNew(
          MyApplication.getContext(), mConfirmlist,
          new MyOnItemListener(), this, this, mPrice);
      mLv.setAdapter(confirmAdapter);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mConfirmlist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      resetTousu(true);
      resetReceiveMoney(true);
      itemCount = mConfirmlist.size();
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetTousu(false);
      resetReceiveMoney(false);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
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


  private int tousuPosition;
  private Boolean tousu;


  public void resetTousu(boolean toTargetPosition) {
    if (null != tousu && tousu) {
      tousu = false;
      if (toTargetPosition) {
        mLv.setSelection(tousuPosition);
      }
    }
  }

  //投诉回调
  private class MyOnItemListener implements OnItemListener {

    @Override
    public void onItem(Object object, int position, int type) {

      tousuPosition = position;
      tousu = true;

      alertDialog((GConfirmlistBean) object, position);

    }
  }

  private void dialog() {
    final Dialog dialog = new Dialog(mActivity, R.style.MyDialog);
    View view = LayoutInflater.from(mActivity).inflate(R.layout.tousu_success_layout, null);
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(mActivity) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
      }
    }, 3500);
  }

  private void alertDialog(final GConfirmlistBean object, final int position) {

    StytledDialog
        .showAlert(getActivity(), getString(R.string.no_receiver_money),
            getString(R.string.complaint_dialog_tip),
            getString(R.string.cancel), getString(R.string.confirm),
            null, true, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                affirmReceiveMoney(object, position, 2);
              }
            }, false);
  }

  //会员信息回调
  @Override
  public void onCuiPayClick(GConfirmlistBean item, int position) {
    Intent intent = new Intent();
    intent.putExtra("userId", DebugUtils.convert(item.getUserid(), ""));
    intent.setClass(mActivity, BuySeedsMemberInfoActivity.class);
    startActivity(intent);

  }


  private int receiveMoneyPosition;
  private Boolean receiveMoney;


  public void resetReceiveMoney(boolean toTargetPosition) {
    if (null != receiveMoney && receiveMoney) {
      receiveMoney = false;
      if (toTargetPosition) {
        mLv.setSelection(receiveMoneyPosition - 1 >= 0 ? receiveMoneyPosition - 1 : 0);
      }
    }
  }

  //确认收款
  @Override
  public void onItem(final Object object, final int position) {
    StytledDialog
        .showAlert(getActivity(), null, getString(R.string.confirm_receive_money),
            getString(R.string.cancel), getString(R.string.choose_friend_commit),
            null, true, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                receiveMoneyPosition = position;
                receiveMoney = true;
                affirmReceiveMoney(object, position, 1);
              }
            }, true);

  }


  //确认收款 按钮 1   投诉2
  private void affirmReceiveMoney(Object object, int position, int type) {
    try {

      this.type = type;

      GConfirmlistBean bean = (GConfirmlistBean) object;
      String orderid = bean.getOrderid();

      //confirm 1为确认收款,2为投诉
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsType = new RequestParams("confirm", type + "");
      RequestParams paramsOrderId = new RequestParams("orderid", orderid);
      list.add(paramsToken);
      list.add(paramsType);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.SELL_COMFIRM_MONEY, mComfimListener,
          RequestNet.POST);
    } catch (Exception e) {
      resetTousu(false);
      resetReceiveMoney(false);
      if (isAdded()) {
        showMsg(getString(R.string.tousu_fail));
      }
    }
  }


  private void dealResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      if (type == 1) {
        showMsg(getString(R.string.receive_money_success));
      } else if (type == 2) {
//        showMsg(getString(R.string.complaint_succeess));
        dialog();
      }
      getInitList();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result),
          type == 1 ? getString(R.string.receive_money_success)
              : getString(R.string.complaint_succeess)));
      resetTousu(false);
      resetReceiveMoney(false);
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
    initData();
  }

  private class MyOnClickListener implements OnClickListener {


    private final GConfirmlistBean mItemAtPosition;
    private final int mPosition;

    public MyOnClickListener(GConfirmlistBean itemAtPosition, int position) {
      mItemAtPosition = itemAtPosition;
      mPosition = position;
    }

    @Override
    public void onClick(View v) {
      Intent intent = new Intent(mActivity, SellConfirmDetailsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", mItemAtPosition);
      bundle.putString("price", mPrice);
      intent.putExtra("item", bundle);
      intent.putExtra("position", mPosition);
      startActivityForResult(intent, 0);
    }
  }
}
