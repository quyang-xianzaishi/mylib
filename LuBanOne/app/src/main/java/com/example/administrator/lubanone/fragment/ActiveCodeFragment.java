package com.example.administrator.lubanone.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.adapter.homepage.ActiveCodeAdapter;
import com.example.administrator.lubanone.adapter.homepage.ActiveCodeNewAdapter;
import com.example.administrator.lubanone.bean.finance.ActiveCodeResultBean;
import com.example.administrator.lubanone.bean.finance.ActiveCodeResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by quyang on 2017/6/27.
 */

public class ActiveCodeFragment extends BaseFragment implements OnClickListener, OnItemListener {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
//  @BindView(R.id.recyclerview)
//  RecyclerView mLv;
  @BindView(R.id.active_code_count)
  TextView codeCount;
  Unbinder unbinder;
  private Unbinder mBind;
  @BindView(R.id.ll_container)
  LinearLayout ll_container;

  private PullToRefreshLayout pull;
  private List<DeallistBean> codeList = new ArrayList<>();
  private TextView mTvAllCount;

  private Button mTransferBtn;
  private EditText mEtVIPId;
  private EditText mEtTranferCount;
  private EditText mEtSafePwd;
  private ActiveCodeAdapter mAdapter;

  private int size = 5;
  private Boolean getMore;


  private int currentPosition;//当前lv可视的最后一个item的posotion
  private int itemCount;

  private RequestListener mTranferListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> objectResult = GsonUtil.processJson(jsonObject, Object.class);
        tranferResult(objectResult);
      } catch (Exception e) {
        showMsg(getString(R.string.tranfer_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.tranfer_fail));
    }
  };

  //init
  private RequestListener mActiveCodeListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<ActiveCodeResultBean> result = GsonUtil
            .processJson(jsonObject, ActiveCodeResultBean.class);
        getActiveCodeInfo(result);
      } catch (Exception e) {
        if (CollectionUtils.isEmpty(codeList)) {
          showEmptyLayout(true);
        }
        fail();
        if (null != getMore && getMore) {
          getMore = false;
          //mLv.setSelection(currentPosition);
        }
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      if (CollectionUtils.isEmpty(codeList)) {
        showEmptyLayout(true);
      }
      fail();
      if (null != getMore && getMore) {
        getMore = false;
        //mLv.setSelection(currentPosition);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  };
  private PullToRefreshLayout mEmptyLayout;
  private String mActivecodecount;


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

  //查看更多/暂无数据
  @Override
  public void onItem(Object object, int position, int type) {
    if (type == 0) {
      getMore = true;
      getInitList();
    } else {
//      showMsg("暂无数据 后台功能为实现");
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
      getInitList();
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
    View view = mInflater.inflate(R.layout.active_layout, null);
    mBind = ButterKnife.bind(this, view);

    mTvBack.setOnClickListener(this);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(true);

    mTvAllCount = (TextView) view.findViewById(R.id.tv_all_count);
    mTransferBtn = (Button) view.findViewById(R.id.transfer_btn);
    mTransferBtn.setOnClickListener(this);
    mEtVIPId = (EditText) view.findViewById(R.id.et_vip_id);
    mEtTranferCount = (EditText) view.findViewById(R.id.et_active_code_size);
    mEtSafePwd = (EditText) view.findViewById(R.id.et_safe_pwd);

    //下拉熟悉
//    mLv = (RecyclerView) view.findViewById(R.id.recyclerview);
    pull = (PullToRefreshLayout) view.findViewById(R.id.pull);
    //滑动监听
//    RefreshListener listener = new RefreshListener();
//    pull.setOnPullListener(listener);
    pull.setPullDownEnable(false);
    pull.setPullUpEnable(false);

    return view;
  }

  @Override
  public void initData() {
    getInitList();
  }

  private void getInitList() {
    try {
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
        size = codeList.size() + 5;
      } else {
        size = codeList.size() == 0 ? size : codeList.size();
      }

      RequestParams paramsPage = new RequestParams("number", size + "");

      list.add(paramsPage);
      list.add(paramsToken);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.ACTIVE_CODE, mActiveCodeListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      resetEmptyRefresh(false);
      if (null != getMore && getMore) {
        getMore = false;
      }
      if (CollectionUtils.isEmpty(codeList)) {
        showEmptyLayout(true);
      }
      showEmptyLayout(true);
      setEmptyLayoutFail();
    }
  }


  private void getActiveCodeInfo(Result<ActiveCodeResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(codeList)) {
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
      fail();
      if (CollectionUtils.isEmpty(codeList)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  }

  private void updatePage(Result<ActiveCodeResultBean> result) {
    ActiveCodeResultBean resultResult = result.getResult();
    mActivecodecount = resultResult.getActivecodecount();
    mTvAllCount.setText(
        new StringBuilder().append(getString(R.string.all_count)).append(mActivecodecount)
            .append(getString(R.string.ge)));
    codeCount.setText(mActivecodecount);

    List<DeallistBean> tgbzlist = result.getResult().getDeallist();
    codeList.clear();
    if (!CollectionUtils.isEmpty(tgbzlist)) {
      success();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      codeList.addAll(tgbzlist);
    }
    if (!CollectionUtils.isEmpty(codeList)) {
      showEmptyLayout(false);
//      LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//      mLv.setLayoutManager(mLayoutManager);
//      //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//      mLv.setHasFixedSize(true);
//      //注意： more自动是bean里面手动初始化为true，方法测试
//      ActiveCodeNewAdapter adapter = new ActiveCodeNewAdapter(mActivity,
//          codeList, resultResult.getMore(), this);
//      mLv.setAdapter(adapter);
//

      //setListViewHeightBasedOnChildren(mLv);
      //mLv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,800));

      setAdapter(codeList,resultResult.getMore());

      if (null != getMore && getMore) {
        getMore = false;
        // mLv.setSelection(currentPosition);
        if (itemCount == codeList.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = codeList.size();
    } else {
      showEmptyLayout(true);
      setEmptyLayoutSuccess();
      fail();
      resetEmptyRefresh(false);
    }
  }

  private void setAdapter(List<DeallistBean> codeList, Boolean more) {
    more = true;

    ll_container.removeAllViews();

    for (DeallistBean bean:codeList) {
      if (null == bean) {
        continue;
      }

      int position = codeList.indexOf(bean);

      View inflate = LayoutInflater.from(mActivity).inflate(R.layout.active_code_item,null);

    View  root = (View) inflate.findViewById(R.id.root_normal);
     TextView tvDate = (TextView) inflate.findViewById(R.id.tv_date);
      TextView tvTradeObj = (TextView) inflate.findViewById(R.id.tv_trade_obj);
      TextView  tvTradeSize = (TextView) inflate.findViewById(R.id.tv_trade_size);

      DebugUtils.setStringValue(bean.getDate(), "", tvDate);
      DebugUtils.setStringValue(bean.getTradeobject(), "",tvTradeObj);
      DebugUtils.setStringValue(bean.getCount(), "", tvTradeSize);

      if ("0".equals(bean.getColor())) {
        tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mActivity));
      } else if ("1".equals(bean.getColor())) {
        tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mActivity));
      }

      if (position % 2 == 0) {
        root.setBackgroundColor(getResources().getColor(R.color.white));
      } else {
        root.setBackgroundColor(getResources().getColor(R.color.cf9f9f9));
      }
      ll_container.addView(inflate);
    }

    if (more != null && more) {
      View empty = LayoutInflater.from(mActivity).inflate(R.layout.active_more_layout, null);
      TextView tv = (TextView) empty.findViewById(R.id.tv);
      tv.setEnabled(true);
      tv.setText(getString(R.string.look_more));
      tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          getMore = true;
          getInitList();
        }
      });
      ll_container.addView(empty);
    } else {
      View empty = LayoutInflater.from(mActivity).inflate(R.layout.active_more_layout, null);
      TextView tv = (TextView) empty.findViewById(R.id.tv);
      tv.setEnabled(false);
      tv.setText(getString(R.string.no_more_message));
      ll_container.addView(empty);
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

  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    startActivity(new Intent(getActivity(), MainActivity.class));
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.transfer_btn:
          tranfer();
          break;
        case R.id.tv_back:
          startActivity(new Intent(getActivity(), MainActivity.class));
          break;
      }
    } catch (Exception e) {
      showMsg(e.getMessage());
    }

  }

  private void tranfer() {
    try {
      if (!NetUtil.isConnected(getActivity())) {
        ToastUtil.showShort(getString(R.string.NET_ERROR), MyApplication.getContext());
        return;
      }
      String vipId = mEtVIPId.getText().toString().trim();
      String count = mEtTranferCount.getText().toString().trim();
      String pwd = mEtSafePwd.getText().toString().trim();

      isEmptyWithExcetion(vipId, getString(R.string.vip_id_empty));
      isEmptyWithExcetion(count, getString(R.string.transfer_count_empty));
      isEmptyWithExcetion(pwd, getString(R.string.safe_pwd_empty));
      if (DemicalUtil.great(count, mActivecodecount)) {
        throw new DefineException(getString(R.string.tranfer_tips));
      }

      List<RequestParams> paramsList = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsPayPwd = new RequestParams("paypwd", pwd);
      RequestParams paramsCount = new RequestParams("count", count);
      RequestParams paramsId = new RequestParams("account", vipId);
      paramsList.add(paramsToken);
      paramsList.add(paramsPayPwd);
      paramsList.add(paramsCount);
      paramsList.add(paramsId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          paramsList, Urls.TRANSFER_CODE, mTranferListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.tranfer_fail)));
    }
  }

  private void tranferResult(Result<Object> objectResult) {
    if (ResultUtil.isSuccess(objectResult)) {
      showMsg(getString(R.string.tranfer_success));
      mEtVIPId.setText("");
      mEtTranferCount.setText("");
      mEtSafePwd.setText("");
      mEtVIPId.requestFocus();
      getInitList();
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(objectResult), getString(R.string.tranfer_fail)));
    }
  }


  public void isEmptyWithExcetion(String msg, String tip) {
    if (TextUtils.isEmpty(msg)) {
      throw new DefineException(tip);
    }
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


  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      //pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }

  /***
   * 动态设置listview的高度
   *
   * @param listView
   */
  public void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }
    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight
        + (listView.getDividerHeight() * (listAdapter.getCount() + 1));
    listView.setLayoutParams(params);
  }
}
