package com.example.administrator.lubanone.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.administrator.lubanone.adapter.homepage.CuiHuaJiAdapter;
import com.example.administrator.lubanone.bean.finance.CuiHuaJiResultBean;
import com.example.administrator.lubanone.bean.finance.CuiHuaJiResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
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

public class CuiHuaJiFragment extends BaseFragment {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.tv_get_cuihuaji)
  TextView mTvGetCuihuaji;
  @BindView(R.id.btn_into_channel)
  Button mBtnIntoChannel;
  @BindView(R.id.lv)
  ListView mLv;
  @BindView(R.id.cuihuaji_detail)
  LinearLayout cuihuajiDetail;
  @BindView(R.id.cuihuaji_obtain_record)
  LinearLayout cuihuajiRecord;
  @BindView(R.id.cuihuaji_detail_text)
  TextView cuihuajiDetailText;
  @BindView(R.id.cuihuaji_detail_line)
  TextView cuihuajiDetailLine;
  @BindView(R.id.cuihuaji_obtain_record_text)
  TextView cuihuajiObtainRecordText;
  @BindView(R.id.cuihuaji_obtain_record_line)
  TextView cuihuajiObtainRecordLine;
  @BindView(R.id.cuihuaji_obtain_list)
  LinearLayout cuihuajiObtainList;
  @BindView(R.id.cuihuaji_detail_info)
  LinearLayout cuihuajiDetailInfo;
  @BindView(R.id.cuihuaji_count)
  TextView cuihuajiCount;

  private Unbinder mBind;

  private PullToRefreshLayout pull;
  private List<DeallistBean> listData = new ArrayList<>();

  private int size = 5;
  private Boolean getMore;


  //init
  private RequestListener mCuiHuaJiListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CuiHuaJiResultBean> result = GsonUtil
            .processJson(jsonObject, CuiHuaJiResultBean.class);
        getCuiHuaJi(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(listData)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (CollectionUtils.isEmpty(listData)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  };
  private TextView mTvAllCount;
  private PullToRefreshLayout mEmptyLayout;
  private CuiHuaJiAdapter mAdapter;
  private int itemCount;
  private int currentPosition;

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
    View view = mInflater.inflate(R.layout.cuihuai_layout, null);
    mBind = ButterKnife.bind(this, view);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    mTvAllCount = (TextView) view.findViewById(R.id.tv_all_count);

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
      size = listData.size() + 5;
    } else {
      size = listData.size() == 0 ? size : listData.size() + 5;
    }

    RequestParams paramsPage = new RequestParams("number", size + "");
    list.add(paramsPage);
    list.add(paramsToken);
    RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
        list, Urls.CUIHUAJI, mCuiHuaJiListener,
        RequestNet.POST);
  }

  private void getCuiHuaJi(Result<CuiHuaJiResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(listData)) {
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
      if (CollectionUtils.isEmpty(listData)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
    }
  }

  private void updatePage(Result<CuiHuaJiResultBean> result) {

    CuiHuaJiResultBean resultResult = result.getResult();
    String catacount = resultResult.getCatacount();
    mTvAllCount.setText(new StringBuilder().append(getString(R.string.all_count)).append(catacount)
        .append(getString(R.string.ping)));
    cuihuajiCount.setText(catacount);
    listData.clear();
    if (!CollectionUtils.isEmpty(resultResult.getDeallist())) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      listData.addAll(resultResult.getDeallist());
    }
    if (!CollectionUtils.isEmpty(listData)) {
      showEmptyLayout(false);
      mLv.setAdapter(new CuiHuaJiAdapter(mActivity,
          listData));
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == listData.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      itemCount = listData.size();
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetEmptyRefresh(false);
    }
    setEmptyLayoutSuccess();
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

  @OnClick({R.id.iv_back, R.id.btn_into_channel, R.id.tv_back, R.id.cuihuaji_detail,
      R.id.cuihuaji_obtain_record})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
//        startActivity(new Intent(getActivity(), MainActivity.class));
        mActivity.finish();
        break;
      case R.id.btn_into_channel:
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setType(Config.MAIN_PAIN__KEY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        break;
      case R.id.cuihuaji_detail:
        //详情
        cuihuajiDetailText.setTextColor(getActivity().getResources().getColor(R.color.cEA5412));
        cuihuajiDetailLine.setVisibility(View.VISIBLE);
        cuihuajiDetailInfo.setVisibility(View.VISIBLE);

        cuihuajiObtainRecordText.setTextColor(getActivity().getResources().getColor(R.color.c333));
        cuihuajiObtainRecordLine.setVisibility(View.INVISIBLE);
        cuihuajiObtainList.setVisibility(View.GONE);
        break;
      case R.id.cuihuaji_obtain_record:
        //获得记录
        cuihuajiDetailText.setTextColor(getActivity().getResources().getColor(R.color.c333));
        cuihuajiDetailLine.setVisibility(View.INVISIBLE);
        cuihuajiDetailInfo.setVisibility(View.GONE);

        cuihuajiObtainRecordText
            .setTextColor(getActivity().getResources().getColor(R.color.cEA5412));
        cuihuajiObtainRecordLine.setVisibility(View.VISIBLE);
        cuihuajiObtainList.setVisibility(View.VISIBLE);
        break;
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
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }

  }
}
