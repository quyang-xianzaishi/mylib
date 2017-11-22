package com.example.administrator.lubanone.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.BuySeedsActivity;
import com.example.administrator.lubanone.activity.home.CreditGradeActivity;
import com.example.administrator.lubanone.activity.home.DreamFoundActivity;
import com.example.administrator.lubanone.activity.home.MyTeamActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.activity.home.TrainCreditActivity;
import com.example.administrator.lubanone.activity.home.UserCenterActivity;
import com.example.administrator.lubanone.activity.home.UserDreamsActivity;
import com.example.administrator.lubanone.activity.home.UserLevelActivity;
import com.example.administrator.lubanone.adapter.homepage.HomeFragmentLvAdapter;
import com.example.administrator.lubanone.bean.homepage.BuySellBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentLvBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentResultBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentResultBean.PersonalinfoBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentResultBean.UserjjlistBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmetListItemResultBean;
import com.example.administrator.lubanone.customview.NewHomeFragmentListView;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnViewClickListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.activity.BaseFragment;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.image.glide.GlideManager;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 首页 quyang
 */
public class HomeFragment extends BaseFragment implements OnClickListener, OnScrollListener {

  private View mView;
  private ImageView mHeadIcon;
  private TextView mHeadUserName;
  private ImageView mIvFinanceCenter;
  private ImageView mIvMyTeam;
  private TextView mDreamLeft;
  private TextView mDreamRight;
  private TextView mBonuxLeft;
  private TextView mBonuxRight;
  private TextView mActiveLeft;
  private TextView mActiveRight;
  private TextView mCatalystLeft;
  private TextView mCatalystRight;
  private LinearLayout mLlContainer;
  private Dialog mDialog;
  private LinearLayout mContainer;
  private int mPosition = -1;

  private NewHomeFragmentListView mLv;
//  private PullableListView mLv;

  private PullToRefreshLayout pull;
  private int currentPage = 1;

  private List<UserjjlistBean> mList;
  private List<HomeFragmentLvBean> mTargetList = new ArrayList<>();
  private String mImage;
  private BuySellBean mBuySellBean;


  private int size = 5;
  private Boolean getMore;


  private String mPeixunfen;
  private String mTraintCredit;
  private String mMxjjc;
  private String mDreamRight1;
  private String mMoneyPackage;
  private String mHomeActiveCode;
  private String personalinfoImage;

  private int currentPosition;//当前lv可视的最后一个item的posotion
  private int itemCount;
  private int mLastVisiblePosition;


  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      int what = msg.what;
      switch (what) {
        case 1:
          go2UserCenter();
          break;
        case 2:
          financeCenter();
          break;
      }

    }
  };

  private List<HomeFragmentLvBean> getDownTargetList(
      List<HomeFragmetListItemResultBean.UserjjlistBean> userjjlist) {
    List<HomeFragmentLvBean> arrayList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(userjjlist)) {
      for (HomeFragmetListItemResultBean.UserjjlistBean bean : userjjlist) {
        if (null == bean) {
          continue;
        }
        HomeFragmentLvBean lvBean = new HomeFragmentLvBean();
        lvBean.setDate(bean.getDate());
        lvBean.setDate_hk(bean.getDate_hk());
        lvBean.setDate_su(bean.getDate_su());
        lvBean.setG_user(bean.getG_user());
        lvBean.setGpingjia(bean.getGpingjia());
        lvBean.setIsshouge(bean.getIsshouge());
        lvBean.setJb(bean.getJb());
        lvBean.setOrderid(bean.getOrderid());
        lvBean.setUser_jj_lx(bean.getUser_jj_lx());
        lvBean.setUser_jj_ts(bean.getUser_jj_ts());
        lvBean.setZt(bean.getZt());
        lvBean.setPpingjia(bean.getPpingjia());
        arrayList.add(lvBean);
      }
      return arrayList;
    }
    return arrayList;

  }

  //收割
  private RequestListener mReapListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        reapResult(result);
      } catch (Exception e) {
        show(getString(R.string.reap_fail));
        resetReap(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      show(getString(R.string.reap_fail));
      resetReap(false);
    }
  };


  //请求首页数据
  private RequestListener mHomeFragmentListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        System.out.println("HomeFragment.testSuccess=" + jsonObject);
        Result<HomeFragmentResultBean> result = GsonUtil
            .processJson(jsonObject, HomeFragmentResultBean.class);
        updateHomePage(result);
      } catch (Exception e) {
        wrongAdapter();
        fail();
        resetGetMore(true);
        resetReap(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      wrongAdapter();
      fail();
      resetReap(false);
      resetGetMore(true);
    }
  };


  public void resetGetMore(boolean toTargetPosition) {
    if (null != getMore && getMore) {
      getMore = false;
      if (toTargetPosition) {
        mLv.setSelection(mLastVisiblePosition);
      }
    }
  }

  private void wrongAdapter() {
    String iconPath = SPUtils
        .getStringValue(getActivity(), Config.USER_INFO, Config.HEAD_ICON_PATH, "");
    File file = new File(iconPath);
    if (file.exists()) {
      GlideManager.glideWithRound(getContext(), file, mHeadIcon, 50);
    } else {
      mHeadIcon.setBackgroundResource(R.mipmap.head_2x);
    }

    BuySellBean bean = new BuySellBean();
    bean.setBuyMatch("");
    bean.setBuyNoMoney("");
    bean.setBuyConfirm("");
    bean.setBuyPingJia("");

    bean.setSellMatch("");
    bean.setSellNoMoney("");
    bean.setSellConfirm("");
    bean.setSellPingJia("");

    HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
        null, bean, new MyOnItemListener(),
        new MyViewClickListener());
    mLv.setAdapter(fragmentLvAdapter);
  }

  @Override
  public View initView() {
    mView = LayoutInflater.from(getActivity()).inflate(R.layout.home_page_new, null);

    ViewGroup headLayout = (ViewGroup) mView.findViewById(R.id.head_layout);
    mHeadIcon = (ImageView) headLayout.findViewById(R.id.icon);
    mHeadUserName = (TextView) headLayout.findViewById(R.id.user_name);
    mIvFinanceCenter = (ImageView) headLayout.findViewById(R.id.iv_finance_center);
    mIvMyTeam = (ImageView) headLayout.findViewById(R.id.iv_my_team);
    TextView mTvMyTeam = (TextView) headLayout.findViewById(R.id.tv_my_team);
    TextView mTvFinaceCenter = (TextView) headLayout.findViewById(R.id.tv_finance_center);
    View rlFinace = headLayout.findViewById(R.id.rl_finance);
    rlFinace.setOnClickListener(this);

    mHeadIcon.setOnClickListener(this);
    mIvFinanceCenter.setOnClickListener(this);
    mIvMyTeam.setOnClickListener(this);
    mTvMyTeam.setOnClickListener(this);
    mTvFinaceCenter.setOnClickListener(this);

    mContainer = (LinearLayout) mView.findViewById(R.id.container);

    //容器
    mLlContainer = (LinearLayout) mView.findViewById(R.id.ll_container);

    //梦想背包
    ViewGroup dreamPackageLayout = (ViewGroup) mView.findViewById(R.id.dream_package);
    mDreamLeft = (TextView) dreamPackageLayout.findViewById(R.id.tv_left);
    mDreamRight = (TextView) dreamPackageLayout.findViewById(R.id.tv_right);
    mDreamLeft.setOnClickListener(new MyOnClickListener(4));
    mDreamRight.setOnClickListener(new MyOnClickListener(5));

    //奖金背包
    ViewGroup bonuxPackageLayout = (ViewGroup) mView.findViewById(R.id.bonus_package);
    mBonuxLeft = (TextView) bonuxPackageLayout.findViewById(R.id.tv_left);
    mBonuxRight = (TextView) bonuxPackageLayout.findViewById(R.id.tv_right);
    mBonuxLeft.setOnClickListener(new MyOnClickListener(6));
    mBonuxRight.setOnClickListener(new MyOnClickListener(7));

    //激活码
    ViewGroup activeCodeLayout = (ViewGroup) mView.findViewById(R.id.active_code);
    mActiveLeft = (TextView) activeCodeLayout.findViewById(R.id.tv_left);
    mActiveRight = (TextView) activeCodeLayout.findViewById(R.id.tv_right);
    mActiveLeft.setOnClickListener(new MyOnClickListener(8));
    mActiveRight.setOnClickListener(new MyOnClickListener(9));

    //催化剂
    ViewGroup catalystLayout = (ViewGroup) mView.findViewById(R.id.catalyst);
    mCatalystLeft = (TextView) catalystLayout.findViewById(R.id.tv_left);
    mCatalystRight = (TextView) catalystLayout.findViewById(R.id.tv_right);
    mCatalystLeft.setOnClickListener(new MyOnClickListener(10));
    mCatalystRight.setOnClickListener(new MyOnClickListener(11));

    //买入 卖出
    ViewGroup buySellLayout = (ViewGroup) mView.findViewById(R.id.buy_sell_btn);
    Button btnBuy = (Button) buySellLayout.findViewById(R.id.buy_btn);
    Button btnSell = (Button) buySellLayout.findViewById(R.id.sell_btn);
    btnBuy.setOnClickListener(this);
    btnSell.setOnClickListener(this);

    //买入
    ViewGroup buyLayout = (ViewGroup) mView.findViewById(R.id.buy_layout);
    TextView tipBuy = (TextView) buyLayout.findViewById(R.id.tip_text);
    RadioButton rbBuyMatch = (RadioButton) buyLayout.findViewById(R.id.rb_match);
    TextView rbBuyDot = (TextView) buyLayout.findViewById(R.id.match_dot);
    RadioButton rbBuyNoMoney = (RadioButton) buyLayout.findViewById(R.id.rb_no_money);
    TextView rbBuyNoMoneyDot = (TextView) buyLayout.findViewById(R.id.no_money_dot);
    RadioButton rbBuyConfirm = (RadioButton) buyLayout.findViewById(R.id.rb_comfirm);
    TextView rbBuyConfirmDot = (TextView) buyLayout.findViewById(R.id.confirm_dot);
    RadioButton rbBuyrEvaluate = (RadioButton) buyLayout.findViewById(R.id.rb_evaluate);
    TextView rbBuyEvaluateDot = (TextView) buyLayout.findViewById(R.id.evaluate_dot);
    tipBuy.setText(getString(R.string.buy));
    rbBuyMatch.setOnClickListener(this);
    rbBuyNoMoney.setOnClickListener(this);
    rbBuyConfirm.setOnClickListener(this);
    rbBuyrEvaluate.setOnClickListener(this);

    //卖出
    ViewGroup sellLayout = (ViewGroup) mView.findViewById(R.id.sell_layout);
    TextView tipsell = (TextView) sellLayout.findViewById(R.id.tip_text);
    RadioButton rbsellMatch = (RadioButton) sellLayout.findViewById(R.id.rb_match);
    TextView rbsellDot = (TextView) sellLayout.findViewById(R.id.match_dot);
    RadioButton rbsellNoMoney = (RadioButton) sellLayout.findViewById(R.id.rb_no_money);
    TextView rbsellNoMoneyDot = (TextView) sellLayout.findViewById(R.id.no_money_dot);
    RadioButton rbsellConfirm = (RadioButton) sellLayout.findViewById(R.id.rb_comfirm);
    TextView rbsellConfirmDot = (TextView) sellLayout.findViewById(R.id.confirm_dot);
    RadioButton rbsellrEvaluate = (RadioButton) sellLayout.findViewById(R.id.rb_evaluate);
    TextView rbsellEvaluateDot = (TextView) sellLayout.findViewById(R.id.evaluate_dot);
    tipsell.setText(getString(R.string.sell));
    rbsellMatch.setOnClickListener(new MyOnClickListener(0));
    rbsellNoMoney.setOnClickListener(new MyOnClickListener(1));
    rbsellConfirm.setOnClickListener(new MyOnClickListener(2));
    rbsellrEvaluate.setOnClickListener(new MyOnClickListener(3));

    //下拉熟悉
    mLv = (NewHomeFragmentListView) mView.findViewById(R.id.lv);
    pull = (PullToRefreshLayout) mView.findViewById(R.id.pull);
    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);

    mDreamLeft.setText(getString(R.string.my_dream_package_one));
    mDreamRight
        .setText(new StringBuilder().append(getString(R.string.vip_level)).append("："));
    mBonuxLeft
        .setText(new StringBuilder().append(getString(R.string.my_money_package)).append("："));
    mBonuxRight.setText(new StringBuilder().append(getString(R.string.train_credit)).append("："));

    mActiveLeft.setText(new StringBuilder().append(getString(R.string.active_code)).append("："));
    mActiveRight.setText(new StringBuilder().append(getString(R.string.credit_grade)).append("："));

    mCatalystLeft
        .setText(new StringBuilder().append(getString(R.string.dream_cuihuaji)).append("："));
    mCatalystRight
        .setText(new StringBuilder().append(getString(R.string.dream_found_chi)).append(""));

    mHeadUserName.setText("");

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

    mLv.setOnScrollListener(this);

    return mView;
  }

  @Override
  public void initData() {
  }


  public void getInitList() {
    try {
      if (!NetUtil.isConnected(getContext())) {
        ToastUtil.showShort(getString(R.string.NET_ERROR), MyApplication.getContext());
        fail();
        wrongAdapter();
        resetReap(false);
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mTargetList.size() + 5;
      } else {
        size = mTargetList.size() == 0 ? size : mTargetList.size();
      }
      RequestParams paramsPage = new RequestParams("number",
          size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.HOME_FRAMENT, mHomeFragmentListener,
          RequestNet.POST);
    } catch (Exception e) {
      show(getString(R.string.GET_DATE_FAIL));
      resetReap(false);
      wrongAdapter();
      fail();
    }
  }


  public void updateHomePage(Result<HomeFragmentResultBean> result) {
    //显示空布局
    if (null == result.getResult() && CollectionUtils.isEmpty(mTargetList)) {
      HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
          null, mBuySellBean == null ? new BuySellBean() : mBuySellBean, new MyOnItemListener(),
          new MyViewClickListener());
      mLv.setAdapter(fragmentLvAdapter);
      fail();
      resetReap(false);
      resetGetMore(true);
      return;
    }

    //用户基本信息
    PersonalinfoBean personalinfo = result.getResult().getPersonalinfo();
    if (null != personalinfo) {
      SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.REAL_NAME,
          personalinfo.getUe_truename());
      SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.USER_ACCOUNT,
          personalinfo.getUe_account());
      personalinfoImage = personalinfo.getImage();
    }

    //列表信息
    if (ResultUtil.isSuccess(result) && !CollectionUtils
        .isEmpty(result.getResult().getUserjjlist())) {
      success();
      //列表有数据
      HomeFragmentResultBean fragmentResultBean = result.getResult();
      adapterPage(fragmentResultBean);
      resetReap(true);
      resetGetMore(true);
    } else if (ResultUtil.isSuccess(result) && CollectionUtils
        .isEmpty(result.getResult().getUserjjlist())) {
      success();

      //列表无数据
      HomeFragmentResultBean resultResult = result.getResult();

      //设置头部信息
      setTopInfos(resultResult);

      mBuySellBean = new BuySellBean();
      if (null != resultResult) {
        mBuySellBean.setBuyMatch(resultResult.getTgbzlist());
        mBuySellBean.setBuyNoMoney(resultResult.getP_paylist());
        mBuySellBean.setBuyConfirm(resultResult.getP_confirmlist());
        mBuySellBean.setBuyPingJia(resultResult.getP_dpingjialist());

        mBuySellBean.setSellMatch(resultResult.getJsbzlist());
        mBuySellBean.setSellNoMoney(resultResult.getG_paylist());
        mBuySellBean.setSellConfirm(resultResult.getG_confirmlist());
        mBuySellBean.setSellPingJia(resultResult.getG_dpingjialist());
      }

      if (CollectionUtils.isEmpty(mTargetList)) {
        HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
            null, mBuySellBean, new MyOnItemListener(),
            new MyViewClickListener());
        mLv.setAdapter(fragmentLvAdapter);
        resetReap(true);
        if (null != getMore && getMore) {
          getMore = false;
          show(getString(R.string.no_more_message));
        }
      } else {
        HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
            mTargetList, mBuySellBean, new MyOnItemListener(),
            new MyViewClickListener());
        mLv.setAdapter(fragmentLvAdapter);
        resetReap(false);
        resetGetMore(true);
      }
    }
  }

  private void adapterPage(HomeFragmentResultBean fragmentResultBean) {

    setTopInfos(fragmentResultBean);

    mList = fragmentResultBean.getUserjjlist();

    mBuySellBean = new BuySellBean();
    mBuySellBean.setBuyMatch(fragmentResultBean.getTgbzlist());
    mBuySellBean.setBuyNoMoney(fragmentResultBean.getP_paylist());
    mBuySellBean.setBuyConfirm(fragmentResultBean.getP_confirmlist());
    mBuySellBean.setBuyPingJia(fragmentResultBean.getP_dpingjialist());

    mBuySellBean.setSellMatch(fragmentResultBean.getJsbzlist());
    mBuySellBean.setSellNoMoney(fragmentResultBean.getG_paylist());
    mBuySellBean.setSellConfirm(fragmentResultBean.getG_confirmlist());
    mBuySellBean.setSellPingJia(fragmentResultBean.getG_dpingjialist());

    List<HomeFragmentLvBean> homeFragmentLvBeen = getTargetList(mList);
    mTargetList.clear();
    if (CollectionUtils.isNotEmpty(homeFragmentLvBeen)) {
      success();
      mTargetList.addAll(homeFragmentLvBeen);
    }
    if (CollectionUtils.isNotEmpty(mTargetList)) {
      HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
          mTargetList, mBuySellBean, new MyOnItemListener(),
          new MyViewClickListener());
      if (itemCount == mTargetList.size() && null != getMore && getMore) {
        show(getString(R.string.no_more_message));
      }
      itemCount = mTargetList.size();
      mLv.setAdapter(fragmentLvAdapter);
      resetReap(true);
    } else {
      HomeFragmentLvAdapter fragmentLvAdapter = new HomeFragmentLvAdapter(getActivity(),
          null, mBuySellBean, new MyOnItemListener(),
          new MyViewClickListener());
      mLv.setAdapter(fragmentLvAdapter);
    }
  }

  private void setTopInfos(HomeFragmentResultBean fragmentResultBean) {
    if (null != fragmentResultBean.getPersonalinfo()) {
      mDreamLeft.setText(
          new StringBuilder().append(getString(R.string.my_dream_package)).append(": ")
              .append(fragmentResultBean.getPersonalinfo()
                  .getUe_money()));

      mDreamRight1 = fragmentResultBean.getPersonalinfo()
          .getLevelname();
      mDreamRight.setText(
          new StringBuilder().append(getString(R.string.vip_level)).append(": ")
              .append(mDreamRight1));

      mMoneyPackage = fragmentResultBean.getPersonalinfo()
          .getTj_he();
      mBonuxLeft.setText(
          new StringBuilder().append(getString(R.string.my_money_package)).append(": ")
              .append(mMoneyPackage));

      mTraintCredit = fragmentResultBean.getPersonalinfo()
          .getPeixunfen();

      mBonuxRight.setText(new StringBuilder().append(getString(R.string.train_credit)).append(": ")
          + mTraintCredit);

      mHomeActiveCode = fragmentResultBean.getPersonalinfo().getJhm();
      mActiveLeft.setText(new StringBuilder().append(getString(R.string.active_code)).append(": ")
          .append(mHomeActiveCode));

      mPeixunfen = fragmentResultBean.getPersonalinfo()
          .getPingjia();
      mActiveRight.setText(new StringBuilder().append(getString(R.string.credit_grade)).append(": ")
          .append(DebugUtils.convert(mPeixunfen, "")));

      mCatalystLeft.setText(
          new StringBuilder().append(getString(R.string.dream_cuihuaji)).append(": ")
              .append(fragmentResultBean.getPersonalinfo().getPai()));

      mMxjjc = fragmentResultBean.getPersonalinfo()
          .getMxjjc();
      mCatalystRight.setText(
          new StringBuilder().append(getString(R.string.dream_found_chi)).append("")
              .append(DebugUtils.convert(StringUtil.getThreeString(mMxjjc), "")));

      mHeadUserName.setText(
          new StringBuilder().append(fragmentResultBean.getPersonalinfo().getUe_truename())
              .append("\n").append(fragmentResultBean
              .getPersonalinfo().getUe_account()));

      SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.REAL_NAME,
          fragmentResultBean.getPersonalinfo().getUe_truename());
      SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.USER_ACCOUNT,
          fragmentResultBean
              .getPersonalinfo().getUe_account());

      mImage = fragmentResultBean.getPersonalinfo().getImage();
      SPUtils.putStringValue(mActivity, Config.USER_INFO, Config.HEAD_ICON_PATH, mImage);
      System.out.println("image=" + mImage);
      if (TextUtils.isEmpty(mImage)) {
        mHeadIcon.setImageResource(R.mipmap.head_2x);
      } else {
        GlideManager
            .glideWithRound(getContext(), mImage, mHeadIcon,
                50);
      }

    }
  }


  private List<HomeFragmentLvBean> getTargetList(List<UserjjlistBean> list) {
    List<HomeFragmentLvBean> arrayList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(list)) {
      for (UserjjlistBean bean : list) {
        if (null == bean) {
          continue;
        }
        HomeFragmentLvBean lvBean = new HomeFragmentLvBean();
        lvBean.setDate(bean.getDate());
        lvBean.setDate_hk(bean.getDate_hk());
        lvBean.setDate_su(bean.getDate_su());
        lvBean.setG_user(bean.getG_user());
        lvBean.setGpingjia(bean.getGpingjia());
        lvBean.setIsshouge(bean.getIsshouge());
        lvBean.setJb(bean.getJb());
        lvBean.setOrderid(bean.getOrderid());
        lvBean.setUser_jj_lx(bean.getUser_jj_lx());
        lvBean.setUser_jj_ts(bean.getUser_jj_ts());
        lvBean.setZt(bean.getZt());
        lvBean.setPpingjia(bean.getPpingjia());
        arrayList.add(lvBean);
      }
      return arrayList;
    }
    return arrayList;

  }

  @Override
  public void onClick(View v) {
    try {
      Intent intent = null;
      Bundle bundle = new Bundle();
      switch (v.getId()) {
        case R.id.icon:
          mHandler.sendEmptyMessageDelayed(1, 1);
          break;
        case R.id.iv_finance_center:
        case R.id.tv_finance_center:
        case R.id.rl_finance:
          mHandler.sendEmptyMessageDelayed(2, 1);
          break;
        case R.id.iv_my_team:
        case R.id.tv_my_team:
          intoMyTeam();
          break;
        case R.id.rb_match://买入匹配
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
          break;
        case R.id.rb_no_money://买入代付款
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_money_from_home_fragment);
          break;
        case R.id.rb_comfirm://买入待确认
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_comfrim_from_home_fragment);
          break;
        case R.id.rb_evaluate://买入待评论
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_pingjia_from_home_fragment);
          break;
        case R.id.buy_btn://买入种子
          buySeeds();
          break;
        case R.id.sell_btn://卖出种子
          sellSeeds();
          break;
      }
      if (null != intent) {
        getActivity().startActivity(intent);
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), getContext());
    }


  }

  private void sellSeeds() {
    Intent intent = new Intent(getActivity(), SellSeedsActivity.class);
    startActivity(intent);
  }

  private void buySeeds() {
    Intent intent = new Intent(getActivity(), BuySeedsActivity.class);
    startActivity(intent);
  }

  private void intoMyTeam() {
    Intent intent = new Intent(getActivity(), MyTeamActivity.class);
    intent.putExtra("code_size", DebugUtils.convert(mHomeActiveCode, "0"));
    startActivity(intent);
  }


  private void financeCenter() {
    Intent intent = new Intent(getActivity(), UserDreamsActivity.class);
    intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.ALL_SCAN);
    startActivity(intent);
  }

  private void go2UserCenter() {
    Intent intent = new Intent(getActivity(), UserCenterActivity.class);
    intent.putExtra("icon_url", DebugUtils.convert(mImage, ""));
    intent.putExtra("code_size", DebugUtils.convert(mHomeActiveCode, "0"));
    startActivity(intent);
  }


  //listview滑动监听
  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    mLastVisiblePosition = view.getLastVisiblePosition();

  }

  private class MyOnClickListener implements View.OnClickListener {

    private int a;

    public MyOnClickListener(int a) {
      this.a = a;
    }

    @Override
    public void onClick(View v) {

      Intent intent = null;
      switch (a) {
        case 0:
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT);
          break;
        case 1:
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT);
          break;
        case 2:
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT);
          break;
        case 3:
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT);
          break;
        case 4://我的梦想背包
          intent = new Intent(getActivity(), UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.DREAM_PACKAGE);
          intent.putExtra(Config.HOME_FRAGMENT_DREAM_PACKAGE, DebugUtils.convert(mDreamRight1, ""));
          break;
        case 5://会员级别
          intent = new Intent(getActivity(), UserLevelActivity.class);
          intent.putExtra("code_size", DebugUtils.convert(mHomeActiveCode, "0"));
          break;
        case 6://我的奖金背包
          intent = new Intent(getActivity(), UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.MONEY_PACKAGE);
          break;
        case 7://培训积分
          intent = new Intent(getActivity(), TrainCreditActivity.class);
          intent.putExtra(Config.TRAIN_CREDIT_KEY, DebugUtils.convert(mTraintCredit, "0"));
          break;
        case 8://激活码
          intent = new Intent(getActivity(), UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.ACTIVE_CODE);
          break;
        case 9://信用评分
          intent = new Intent(getActivity(), CreditGradeActivity.class);
          intent.putExtra("credit", DebugUtils.convert(mPeixunfen, "0"));
          break;
        case 10://梦想催化剂
          intent = new Intent(getActivity(), UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.CUIHUAJI);
          break;
        case 11://梦想基金池
          intent = new Intent(getActivity(), DreamFoundActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, DebugUtils.convert(mMxjjc, ""));
          break;
      }
      if (null != intent) {
        startActivity(intent);
      }
    }
  }


  public void show(String msg) {
    ToastUtil.showShort(msg, MyApplication.getContext());
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

  public class MyViewClickListener implements OnViewClickListener {

    @Override
    public void onView(int position) {
      Intent intent = null;
      Bundle bundle = new Bundle();
      switch (position) {
        case 0://buy
          buySeeds();
          break;
        case 1://sell
          sellSeeds();
          break;
        case 2://买 带匹配
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
          break;
        case 3://买 待付款
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_money_from_home_fragment);
          break;
        case 4://买 待确认
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_comfrim_from_home_fragment);
          break;
        case 5://买 待评价
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          bundle.putSerializable("counts", mBuySellBean);
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_pingjia_from_home_fragment);
          break;
        case 6://卖 待匹配
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT);
          break;
        case 7://卖 待付款
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT);
          break;
        case 8://卖 待确认
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT);
          break;
        case 9://卖 待评价
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          bundle.putSerializable("counts", mBuySellBean);
          bundle.putString("active_code", DebugUtils.convert(mHomeActiveCode, ""));
          intent.putExtra("bundle", bundle);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT);
          break;
        case 10://收割
          break;
      }
      if (null != intent) {
        getActivity().startActivity(intent);
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    getInitList();
  }


  private int reapPosition;
  private Boolean reap;


  public void resetReap(boolean toTargetPosition) {
    if (null != reap && reap) {
      reap = false;
      if (toTargetPosition) {
        mLv.setSelection(reapPosition - 1 >= 0 ? reapPosition - 1 : 0);
      }
    }
  }

  //收割接口
  private class MyOnItemListener implements OnListViewItemListener {

    @Override
    public void onItem(Object object, int position) {
      reapSeeds(position);
    }
  }


  private void reapSeeds(int position) {
    try {
      mPosition = position - 1;
      reapPosition = mPosition;
      reap = true;

      if (!NetUtil.isConnected(getActivity())) {
        show(getString(R.string.NET_ERROR));
        resetReap(false);
        return;
      }

      HomeFragmentLvBean homeFragmentLvBean = mTargetList.get(mPosition);
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid", homeFragmentLvBean.getOrderid());
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.REAP_SEEDS, mReapListener, RequestNet.POST);
    } catch (Exception e) {
      show(getString(R.string.reap_fail));
      resetReap(false);
    }
  }


  private void reapResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      show(getString(R.string.reap_success));
      getInitList();
    } else {
      show(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.reap_fail)));
      resetReap(false);
    }
  }

  private class MyOnItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      show("position=" + position);
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

}
