package com.example.administrator.lubanone.fragment;

import static com.example.administrator.lubanone.R.id.pull;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.BuySeedsActivity;
import com.example.administrator.lubanone.activity.home.DreamFoundActivity;
import com.example.administrator.lubanone.activity.home.GrowingSeedsActivity;
import com.example.administrator.lubanone.activity.home.MyTeamActivityNew;
import com.example.administrator.lubanone.activity.home.SellSeedsActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.activity.home.UserCenterActivity;
import com.example.administrator.lubanone.activity.home.UserDreamsActivity;
import com.example.administrator.lubanone.activity.home.UserLevelActivity;
import com.example.administrator.lubanone.activity.message.MessageActivity;
import com.example.administrator.lubanone.adapter.homepage.RecyclerAdapter;
import com.example.administrator.lubanone.adapter.homepage.RecyclerHomeTopAdapter;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.BannerBean;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.BuylistBean;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.FootbannerBean;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.GrowseedsBean;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.ReapseedbuttonType;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.SelllistBean;
import com.example.administrator.lubanone.bean.homepage.BuySellResultBean;
import com.example.administrator.lubanone.bean.homepage.BuySellResultBean.ListBean;
import com.example.administrator.lubanone.event.HomeEvent;
import com.example.administrator.lubanone.event.HomeOneEvent;
import com.example.administrator.lubanone.interfaces.OnRecyclerItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.image.glide.GlideManager;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.TimeUtil;
import com.example.qlibrary.utils.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by admistrator on 2017/8/15.
 */

public class HomeNewFragment extends BaseFragment implements OnRecyclerItemListener {

  @BindView(R.id.iv_heand_icon)
  CircleImageView mIvHeandIcon;
  @BindView(R.id.tv_head)
  TextView mTvHead;
  @BindView(R.id.iv_msg)
  ImageView mIvMsg;
  @BindView(R.id.tv_member_level)
  TextView mTvMemberLevel;
  @BindView(R.id.tv_all_asserts)
  TextView mTvAllAsserts;
  @BindView(R.id.tv_info)
  TextView mTvInfo;
  @BindView(R.id.tv_asserts)
  RelativeLayout mTvAsserts;
  @BindView(R.id.rb_match)
  ImageView mRbMatch;
  @BindView(R.id.tv_one)
  TextView mTvOne;
  @BindView(R.id.rb_no_money)
  ImageView mRbNoMoney;
  @BindView(R.id.tv_two)
  TextView mTvTwo;
  @BindView(R.id.rb_comfirm)
  ImageView mRbComfirm;
  @BindView(R.id.tv_three)
  TextView mTvThree;
  @BindView(R.id.rb_evaluate)
  ImageView mRbEvaluate;
  @BindView(R.id.tv_four)
  TextView mTvFour;
  @BindView(R.id.rg)
  LinearLayout mRg;
  @BindView(R.id.ll_container)
  LinearLayout mLlContainer;
  @BindView(R.id.lv)
  PullableScrollView mLv;
  @BindView(pull)
  PullToRefreshLayout mPull;
  Unbinder unbinder;
  private Banner mViewPager;

  @BindView(R.id.tv_dream)
  TextView mTvDream;
  @BindView(R.id.tv_money_package)
  TextView tv_money_package;
  @BindView(R.id.tv_found_chi)
  TextView tv_found_chi;
  @BindView(R.id.iv_train)
  Banner iv_train;
  @BindView(R.id.tv_hours)
  TextView tv_hours;
  @BindView(R.id.tv_minus)
  TextView tv_minus;
  @BindView(R.id.tv_seconds)
  TextView tv_seconds;


  @BindView(R.id.rl_finance_center)
  RelativeLayout rl_finance_center;

  @BindView(R.id.rl_my_team)
  RelativeLayout rl_my_team;


  @BindView(R.id.rl_buy_seeds)
  RelativeLayout rl_buy_seeds;
  @BindView(R.id.rl_dream)
  RelativeLayout rl_dream;
  @BindView(R.id.rl_money_package)
  RelativeLayout rl_money_package;
  @BindView(R.id.rl_found_chi)
  RelativeLayout rl_found_chi;
  @BindView(R.id.match_dot)
  TextView match_dot;


  @BindView(R.id.rl_sell_seeds)
  RelativeLayout rl_sell_seeds;
  @BindView(R.id.iv_more_grow_seeds)
  View iv_more_grow_seeds;
  @BindView(R.id.frameLayout_one)
  FrameLayout frameLayout_one;
  @BindView(R.id.iv_level)
  TextView iv_level;

  private String mImage;

  private RecyclerView mBuyRecyclerView;
  private RecyclerView mSellRecyclerView;

  private int tag = -1;

  private TextView mTvBuyNoMatch;
  private TextView mTvBuyNoPay;
  private TextView mTvBuyNoConfrim;
  private TextView mTvBuyNopingjia;
  private TextView mTvSellNoMatch;
  private TextView mTvSellNoPay;
  private TextView mTvSellNoConfrim;
  private TextView mTvSellNopingjia;


  public void onInvisible() {
  }

  public void onVisible() {
    initData();
    changeBuyTag(-1);
    changeSellTag(-1);
  }

  private RequestListener buySellListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<BuySellResultBean> result = GsonUtil
            .processJson(jsonObject, BuySellResultBean.class);
        getResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.GET_DATE_FAIL));
    }
  };


  private void getResult(Result<BuySellResultBean> result) {
    if (result == null) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      adapterBuySellData(result);
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    }
  }

  private void adapterBuySellData(Result<BuySellResultBean> result) {
    if (result.getResult() == null) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      return;
    }
    BuySellResultBean resultResult = result.getResult();
    List<ListBean> list = resultResult.getList();

    if (tag == 0) {
      //买入
      LinearLayoutManager twolinearLayoutManager = new LinearLayoutManager(mActivity,
          LinearLayoutManager.HORIZONTAL, false);
      mBuyRecyclerView.setLayoutManager(twolinearLayoutManager);
      RecyclerHomeTopAdapter twoAdapter = new RecyclerHomeTopAdapter(getSellListOne(list),
          mActivity,
          new MyBuyRecyclerItemListener());
      mBuyRecyclerView.setAdapter(twoAdapter);
    }

    if (tag == 1) {
      //卖出
      LinearLayoutManager twolinearLayoutManager = new LinearLayoutManager(mActivity,
          LinearLayoutManager.HORIZONTAL, false);
      mSellRecyclerView.setLayoutManager(twolinearLayoutManager);
      RecyclerAdapter twoAdapter = new RecyclerAdapter(getSellListOne(list),
          mActivity,
          new MyTwoOnRecyclerItemListener());
      mSellRecyclerView.setAdapter(twoAdapter);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
//    initData();
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
        showMsg(getString(R.string.reap_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.reap_fail));
    }
  };
  private String mFund;


  private void reapResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.reap_success));
      initData();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.reap_fail)));
    }
  }


  private RequestListener mlistener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {

      try {
        Result<BewHomeResultBean> result = GsonUtil
            .processJson(jsonObject, BewHomeResultBean.class);
        updateHomePage(result);
      } catch (Exception e) {
        showMsg(getString(R.string.GET_DATE_FAIL));
        if (null != mPull) {
          mPull.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      if (null != mPull) {
        mPull.refreshFinish(PullToRefreshLayout.SUCCEED);
      }
    }
  };

  private void updateHomePage(Result<BewHomeResultBean> result) {
    if (null == result) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      adapterData(result.getResult());
    } else {
      showMsg(DebugUtils.convert(result.getMsg(), getString(R.string.GET_DATE_FAIL)));
    }

  }

  private void adapterData(BewHomeResultBean result) {
    if (result == null) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      return;
    }

    //设置基本信息
    setBasicInfo(result);

    //中间的2个list
    setTwoList(result);

    //成长中的种子
    setGrowingSeeds(result);

    //上面的banner
    topBanner(result);

    //下面的banner
    dowmBanner(result);

    if (null != mPull) {
      mPull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }

  private void dowmBanner(BewHomeResultBean result) {
    //设置图片加载器
    iv_train.setImageLoader(new GlideImageLoader());
    //设置图片集合
    iv_train.setImages(getFootTargetBannerList(result.getFootbanner()));

    //设置自动轮播，默认为true
    iv_train.isAutoPlay(true);
    //设置轮播时间
    iv_train.setDelayTime(2000);
    //设置指示器位置（当banner模式中有指示器时）
    iv_train.setIndicatorGravity(BannerConfig.CENTER);

    //设置banner动画效果
//    mViewPager.setBannerAnimation(Transformer.DepthPage);

    //banner设置方法全部调用完毕时最后调用
    iv_train.start();
  }

  private void topBanner(BewHomeResultBean result) {
    //设置图片加载器
    mViewPager.setImageLoader(new GlideImageLoader());
    //设置图片集合
    mViewPager.setImages(getTargetBannerList(result.getBanner()));

    //设置自动轮播，默认为true
    mViewPager.isAutoPlay(true);
    //设置轮播时间
    mViewPager.setDelayTime(2000);
    //设置指示器位置（当banner模式中有指示器时）
    mViewPager.setIndicatorGravity(BannerConfig.CENTER);

    //设置banner动画效果
//    mViewPager.setBannerAnimation(Transformer.DepthPage);

    //banner设置方法全部调用完毕时最后调用
    mViewPager.start();
  }


  private List<String> getFootTargetBannerList(List<FootbannerBean> banner) {

    ArrayList<String> list = new ArrayList<>();

    for (FootbannerBean bean : banner) {
      if (bean == null || TextUitl.isEmpty(bean.getLink())) {
        continue;
      }
      list.add(bean.getLink());
    }
    return list;
  }


  private List<String> getTargetBannerList(List<BannerBean> banner) {

    ArrayList<String> list = new ArrayList<>();

    for (BannerBean bean : banner) {
      if (bean == null || TextUitl.isEmpty(bean.getLink())) {
        continue;
      }
      list.add(bean.getLink());
    }
    return list;
  }

  private void setGrowingSeeds(BewHomeResultBean result) {

    mLlContainer.removeAllViews();

    List<GrowseedsBean> growseeds = result.getGrowseeds();
    if (CollectionUtils.isEmpty(growseeds)) {
      View inflate = LayoutInflater.from(mActivity).inflate(R.layout.member_empty_info_new, null);
      mLlContainer.addView(inflate);
      return;
    }

    for (GrowseedsBean bean : growseeds) {
      View inflate = View.inflate(mActivity, R.layout.new_home_page_ll_item, null);
      TextView tv_left = (TextView) inflate.findViewById(R.id.tv_left);
      TextView tv_mid = (TextView) inflate.findViewById(R.id.tv_mid);
      TextView tv_right = (TextView) inflate.findViewById(R.id.tv_right);

      StringUtil
          .setTextSize(bean.getGetseedcount() + " " + getString(R.string.pcs), tv_left, 23, 9);
      DebugUtils
          .setStringValue(getString(R.string.growing_days) + " " + bean.getGrowdays(), "0", tv_mid);

      if (ReapseedbuttonType.HIDE.equals(bean.getReapseedbutton())) {
        tv_right.setText(getString(R.string.wait_reap));
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setEnabled(false);
        tv_right.setTextColor(ColorUtil.getColor(R.color.c888, mActivity));

        Drawable drawable = DrableUtil
            .getDrawable(mActivity, R.drawable.home_growing_seeds_gray_line);
        ViewUtil.setBackground(tv_right, drawable);

      } else if (ReapseedbuttonType.SHOW.equals(bean.getReapseedbutton())) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setEnabled(true);
        tv_right.setText(getString(R.string.reap_seeds));

        tv_right.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));

        Drawable drawable = DrableUtil
            .getDrawable(mActivity, R.drawable.blue_shape_one);
        ViewUtil.setBackground(tv_right, drawable);
      }

      tv_right.setOnClickListener(new MyClick(growseeds.indexOf(bean), bean));

      mLlContainer.addView(inflate);

    }

  }


  public class MyClick implements View.OnClickListener {

    private int position;
    private GrowseedsBean mBean;

    public MyClick(int i, GrowseedsBean bean) {
      this.position = i;
      mBean = bean;
    }

    @Override
    public void onClick(View v) {

      alertConfimDialog(mBean);
    }
  }

  private void alertConfimDialog(final GrowseedsBean bean) {
    StytledDialog
        .showTwoBtnDialog(mActivity, getString(R.string.confim_reap), getString(R.string.reap_tips),
            getString(R.string.cancels), getString(R.string.confim), false, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                reapSeeds(bean);
              }
            });
  }

  private void reapSeeds(GrowseedsBean bean) {
    try {
      if (!NetUtil.isConnected(getActivity())) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }

      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid", bean.getOrderid());
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.REAP_SEEDS, mReapListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.reap_fail));
    }
  }

  private void setTwoList(BewHomeResultBean result) {

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity,
        LinearLayoutManager.HORIZONTAL, false);

    mBuyRecyclerView.setLayoutManager(linearLayoutManager);

    RecyclerAdapter firstAdapter = new RecyclerAdapter(result.getBuylist(), mActivity,
        this);
    mBuyRecyclerView.setAdapter(firstAdapter);

//    if (topPosition != -1) {
//      mBuyRecyclerView.scrollToPosition(topPosition);
//    }

    //--------------------------------------------------------------------------------

    LinearLayoutManager twolinearLayoutManager = new LinearLayoutManager(mActivity,
        LinearLayoutManager.HORIZONTAL, false);
    mSellRecyclerView.setLayoutManager(twolinearLayoutManager);
    RecyclerAdapter twoAdapter = new RecyclerAdapter(getSellList(result.getSelllist()),
        mActivity,
        new MyTwoOnRecyclerItemListener());
    mSellRecyclerView.setAdapter(twoAdapter);
//    if (downPosition != -1) {
//      mSellRecyclerView.scrollToPosition(downPosition);
//    }
  }

  private List<BuylistBean> getSellList(List<SelllistBean> buylist) {
    if (CollectionUtils.isEmpty(buylist)) {
      return null;
    }

    ArrayList<BuylistBean> list = new ArrayList<>();

    for (SelllistBean bean : buylist) {
      if (null == bean) {
        continue;
      }
      BuylistBean buylistBean = new BuylistBean();
      buylistBean.setOrderid(bean.getOrderid());
      buylistBean.setSeedcount(bean.getSeedcount());
      buylistBean.setSeedprice(bean.getSeedprice());
      buylistBean.setStatus(bean.getStatus());

      list.add(buylistBean);
    }

    return list;
  }

  private List<BuylistBean> getSellListOne(List<ListBean> buylist) {
    if (CollectionUtils.isEmpty(buylist)) {
      return null;
    }

    ArrayList<BuylistBean> list = new ArrayList<>();

    for (ListBean bean : buylist) {
      if (null == bean) {
        continue;
      }
      BuylistBean buylistBean = new BuylistBean();
      buylistBean.setOrderid(bean.getOrderid());
      buylistBean.setSeedcount(bean.getSeedcount());
      buylistBean.setSeedprice(bean.getSeedprice());
      buylistBean.setStatus(bean.getStatus());

      list.add(buylistBean);
    }
    return list;
  }

  private void setBasicInfo(BewHomeResultBean result) {
    //头像
    mImage = result.getImage();
    GlideManager.glideWithRound(mActivity, mImage, mIvHeandIcon, 30);

    SPUtils.putStringValue(mActivity, Config.USER_INFO, Config.HEAD_ICON_PATH, mImage);

    //会员级别
//    DebugUtils.setStringValue(getString(R.string.member_grade_two) + result.getLevel(),
//        getString(R.string.member_grade_two), mTvMemberLevel);

    //总资产
    DebugUtils.setStringValue(result.getTotalassets(), "0.00", mTvAllAsserts);

    //梦想背包
    DebugUtils.setStringValue(result.getDreambag(), "0.00", mTvDream);

    //奖金背包
    DebugUtils.setStringValue(result.getRewardbag(), "0.00", tv_money_package);

    //基金池
    mFund = result.getFund();
//    DebugUtils.setStringValue("" + mFund, "0.00", tv_found_chi);

    //头像右上角等级
    iv_level.setText(result.getLevel());

    //到计时
    timeDown(result.getEndtime());

    //是否有消息
    if ((null != result.getTips() && result.getTips())
        || RongIM.getInstance().getUnreadCount(
        new ConversationType[]{ConversationType.GROUP, ConversationType.PRIVATE,
            ConversationType.CUSTOMER_SERVICE}) > 0) {
      match_dot.setVisibility(View.VISIBLE);
    } else {
      match_dot.setVisibility(View.GONE);
    }

    SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.REAL_NAME,
        result.getTruename());
    SPUtils.putStringValue(getActivity(), Config.USER_INFO, Config.USER_ACCOUNT,
        result.getUe_account());


  }

  private void setLevelIcon(String level) {
    iv_level.setBackgroundResource(getUserLevelImageId(level));
  }

  public int getUserLevelImageId(String a) {
    if (TextUitl.isEmpty(a)) {
      return R.mipmap.grade_m0;
    }
    if ("M0".equals(a)) {
      return R.mipmap.grade_m0;
    }
    if ("M1".equals(a)) {
      return R.mipmap.grade_m1;
    }
    if ("M2".equals(a)) {
      return R.mipmap.grade_m2;
    }
    if ("M3".equals(a)) {
      return R.mipmap.grade_m3;
    }
    if ("M4".equals(a)) {
      return R.mipmap.grade_m4;
    }
    if ("M5".equals(a)) {
      return R.mipmap.grade_m5;
    }
    if ("M6".equals(a)) {
      return R.mipmap.grade_m6;
    } else {
      return R.mipmap.grade_m7;
    }
  }

  //recyclerview的item的点击事件
  @Override
  public void onItem(View view, Object object, int position) {

    int status = 0;

    if (null != object && object instanceof Integer) {
      status = (Integer) object;
      return;
    }

    if (null != object && object instanceof BuylistBean) {
      BuylistBean buylistBean = (BuylistBean) object;
      status = buylistBean.getStatus();
    }

    topPosition = position;

    Intent intent = null;
    switch (status) {
      case 0://买 带匹配
        intent = new Intent(getActivity(), BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
        break;
      case 1://买 待付款
        intent = new Intent(getActivity(), BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_money_from_home_fragment);
        break;
      case 2://买 待确认
        intent = new Intent(getActivity(), BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_comfrim_from_home_fragment);
        break;
      case 3://买 待评价
        intent = new Intent(getActivity(), BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_pingjia_from_home_fragment);
        break;
    }
    if (null != intent) {
      getActivity().startActivityForResult(intent, 1000);
    }
  }


  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
      initData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }
  }

  @Override
  public View initView() {

    View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_home_fragment, null);
    unbinder = ButterKnife.bind(this, view);

    //滑动监听
    RefreshListener listener = new RefreshListener();
    mPull.setOnPullListener(listener);
    mPull.setPullUpEnable(false);
    mPull.setPullDownEnable(false);

    mViewPager = (Banner) view.findViewById(R.id.banner);

    mViewPager.setOnBannerListener(new OnBannerListener() {
      @Override
      public void OnBannerClick(int position) {
//        showMsg("banner:" + position);
      }
    });

    iv_train.setOnBannerListener(new OnBannerListener() {
      @Override
      public void OnBannerClick(int position) {
//        showMsg("banner:" + position);
      }
    });

    //买入种子
    ViewGroup buyLayout = (ViewGroup) view.findViewById(R.id.buy_seeds);
    TextView tvBuySeeds = (TextView) buyLayout.findViewById(R.id.tv_tag);
    View ivNextPage = buyLayout.findViewById(R.id.iv_next_page);
    mTvBuyNoMatch = (TextView) buyLayout.findViewById(R.id.no_match);
    mTvBuyNoPay = (TextView) buyLayout.findViewById(R.id.no_pay);
    mTvBuyNoConfrim = (TextView) buyLayout.findViewById(R.id.no_confrim);
    mTvBuyNopingjia = (TextView) buyLayout.findViewById(R.id.no_pingjia);
    mBuyRecyclerView = (RecyclerView) buyLayout.findViewById(R.id.recyclerview);
    //tvBuySeeds.setText(getString(R.string.seed_of_buied));
    tvBuySeeds.setText(getString(R.string.buy_order));
    mTvBuyNoMatch.setOnClickListener(new MyBuyOnClickListener(R.id.no_match));
    mTvBuyNoPay.setOnClickListener(new MyBuyOnClickListener(R.id.no_pay));
    mTvBuyNoConfrim.setOnClickListener(new MyBuyOnClickListener(R.id.no_confrim));
    mTvBuyNopingjia.setOnClickListener(new MyBuyOnClickListener(R.id.no_pingjia));
    ivNextPage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
        startActivityForResult(intent, 998);
      }
    });

    //卖出种子
    ViewGroup sellLayout = (ViewGroup) view.findViewById(R.id.sell_seeds);
    TextView tvSellSeeds = (TextView) sellLayout.findViewById(R.id.tv_tag);
    View ivSellNextPage = sellLayout.findViewById(R.id.iv_next_page);
    mTvSellNoMatch = (TextView) sellLayout.findViewById(R.id.no_match);
    mTvSellNoPay = (TextView) sellLayout.findViewById(R.id.no_pay);
    mTvSellNoConfrim = (TextView) sellLayout.findViewById(R.id.no_confrim);
    mTvSellNopingjia = (TextView) sellLayout.findViewById(R.id.no_pingjia);
    mSellRecyclerView = (RecyclerView) sellLayout.findViewById(R.id.recyclerview);
    mTvSellNoMatch.setOnClickListener(new MySellOnClickListener(R.id.no_match));
    mTvSellNoPay.setOnClickListener(new MySellOnClickListener(R.id.no_pay));
    mTvSellNoConfrim.setOnClickListener(new MySellOnClickListener(R.id.no_confrim));
    mTvSellNopingjia.setOnClickListener(new MySellOnClickListener(R.id.no_pingjia));
    ivSellNextPage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
        intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT);
        startActivityForResult(intent, 997);
      }
    });

    //tvSellSeeds.setText(getString(R.string.seed_of_selled));
    tvSellSeeds.setText(getString(R.string.sell_order));
    mTvSellNoPay.setText(getString(R.string.buy_receiver_two));

    return view;
  }

  @Override
  public void initData() {
    try {
      if (!NetUtil.isConnected(MyApplication.getContext())) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams params = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, ""));
      list.add(params);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list,
          Urls.HOME_FRAMENT, mlistener, RequestNet.POST);
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
      if (mPull != null && mPull.isShown()) {
        mPull.refreshFinish(PullToRefreshLayout.FAIL);
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (null != unbinder) {
      unbinder.unbind();
    }

    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
    EventBus.getDefault().unregister(this);
  }

  @OnClick({R.id.iv_heand_icon, R.id.tv_asserts, R.id.rb_match, R.id.tv_one,
      R.id.rb_no_money, R.id.rl_finance_center, R.id.rl_sell_seeds, R.id.rl_buy_seeds,
      R.id.rl_my_team, R.id.iv_more_grow_seeds, R.id.rl_found_chi, R.id.rl_money_package,
      R.id.rl_dream, R.id.tv_all_asserts, R.id.tv_info, R.id.tv_member_level, R.id.frameLayout_one,
      R.id.tv_two, R.id.rb_comfirm, R.id.tv_three, R.id.tv_four})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_member_level://会员级别
        Intent intentLevel = new Intent(getActivity(), UserLevelActivity.class);
        startActivityForResult(intentLevel, 702);
        break;
      case R.id.tv_all_asserts://总资产
      case R.id.tv_info:
        Intent intentAssert = new Intent(getActivity(), UserDreamsActivity.class);
        intentAssert.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.ALL_SCAN);
        startActivityForResult(intentAssert, 701);
        break;
      case R.id.iv_heand_icon://个人中心
        go2UserCenter();
        break;
      case R.id.tv_asserts:
        break;
      case R.id.frameLayout_one://消息界面
        startActivityForResult(new Intent(mActivity, MessageActivity.class), 700);
        break;
      case R.id.rl_my_team://我的团队
      case R.id.rb_no_money:
      case R.id.tv_two:
        intoMyTeam();
        break;
      case R.id.rl_buy_seeds://买入种子
      case R.id.rb_comfirm://买入种子
      case R.id.tv_three://买入种子
        buySeeds();
        break;
      case R.id.rl_sell_seeds://卖出种子
      case R.id.rb_evaluate://卖出种子
      case R.id.tv_four://卖出种子
        sellSeeds();
        break;
      case R.id.rl_finance_center://财务中心
      case R.id.rb_match:
      case R.id.tv_one:
        financeCenter();
        break;
      case R.id.iv_more_grow_seeds:
        go2GrowingPage();
        break;
      case R.id.rl_dream://梦想背包
        Intent intent = new Intent(getActivity(), UserDreamsActivity.class);
        intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.DREAM_PACKAGE);
        startActivityForResult(intent, 898);
        break;
      case R.id.rl_money_package://奖金背包
        Intent intentMoney = new Intent(getActivity(), UserDreamsActivity.class);
        intentMoney.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.MONEY_PACKAGE);
        startActivityForResult(intentMoney, 899);
        break;
      case R.id.rl_found_chi://基金池
        Intent intentFound = new Intent(getActivity(), DreamFoundActivity.class);
        intentFound.putExtra(Config.USER_DREAM_PACKAGE_KEY, DebugUtils.convert(mFund, "0"));
        startActivityForResult(intentFound, 900);
        break;
    }
  }

  private void go2GrowingPage() {
    startActivityForResult(new Intent(mActivity, GrowingSeedsActivity.class), 100);
  }


  private void sellSeeds() {
    Intent intent = new Intent(getActivity(), SellSeedsActivity.class);
    startActivityForResult(intent, 992);
  }

  private void buySeeds() {
    Intent intent = new Intent(getActivity(), BuySeedsActivity.class);
    startActivityForResult(intent, 993);
  }


  private void intoMyTeam() {
//    Intent intent = new Intent(getActivity(), MyTeamActivity.class);
    Intent intent = new Intent(getActivity(), MyTeamActivityNew.class);
//    intent.putExtra("code_size", DebugUtils.convert("-1", "0"));
    startActivityForResult(intent, 994);
  }

  private void financeCenter() {
    Intent intent = new Intent(getActivity(), UserDreamsActivity.class);
    intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.ALL_SCAN);
    startActivityForResult(intent, 995);
  }

  private void go2UserCenter() {
    Intent intent = new Intent(getActivity(), UserCenterActivity.class);
    intent.putExtra("icon_url", DebugUtils.convert(mImage, ""));
    intent.putExtra("code_size", DebugUtils.convert(null, "0"));
    startActivityForResult(intent, 996);
  }

  public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
      /**
       注意：
       1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
       2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
       传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
       切记不要胡乱强转！
       */

      //Glide 加载图片简单用法
      Glide.with(context).load(path).into(imageView);

//      //Picasso 加载图片简单用法
//      Picasso.with(context).load(path).into(imageView);

      //用fresco加载图片简单用法，记得要写下面的createImageView方法
//      Uri uri = Uri.parse((String) path);
//      imageView.setImageURI(uri);
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
      //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
      SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
      return simpleDraweeView;
    }
  }


  int downPosition = -1;
  int topPosition = -1;

  //第二个横向列表
  private class MyTwoOnRecyclerItemListener implements OnRecyclerItemListener {


    @Override
    public void onItem(View view, Object object, int position) {

      if (null == object) {

        return;
      }

      BuylistBean buylistBean = (BuylistBean) object;
      int status = buylistBean.getStatus();

      downPosition = position;

      Intent intent = null;
      switch (status) {
        case 0://卖 待匹配
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT);
          break;
        case 1://卖 待付款
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT);
          break;
        case 2://卖 待确认
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT);
          break;
        case 3://卖 待评价
          intent = new Intent(getActivity(), SellSeedsProcessActivity.class);
          intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT);
          break;
      }
      if (null != intent) {
        getActivity().startActivity(intent);
      }

    }
  }


  //第1个横向列表
  private class MyBuyRecyclerItemListener implements OnRecyclerItemListener {


    @Override
    public void onItem(View view, Object object, int position) {

      if (null == object) {
        return;
      }

      BuylistBean buylistBean = (BuylistBean) object;
      int status = buylistBean.getStatus();

//      downPosition = position;

      Intent intent = null;
      switch (status) {
        case 0://买 待匹配
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
          break;
        case 1://买 待付款
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_money_from_home_fragment);
          break;
        case 2://买 待确认
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_comfrim_from_home_fragment);
          break;
        case 3://买 待评价
          intent = new Intent(getActivity(), BuySeedProcessActivity.class);
          intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_pingjia_from_home_fragment);
          break;
      }
      if (null != intent) {
        getActivity().startActivity(intent);
      }

    }
  }

  private class MyBuyOnClickListener implements OnClickListener {


    private int id;

    public MyBuyOnClickListener(int no_pay) {
      this.id = no_pay;
    }

    @Override
    public void onClick(View v) {
      String url = null;
      switch (id) {
        case R.id.no_match:
          url = Urls.buy_no_match;
          changeBuyTag(R.id.no_match);
          break;
        case R.id.no_pay:
          url = Urls.buy_no_pay;
          changeBuyTag(R.id.no_pay);
          break;
        case R.id.no_confrim:
          url = Urls.buy_no_confirm;
          changeBuyTag(R.id.no_confrim);
          break;
        case R.id.no_pingjia:
          url = Urls.buy_no_pingjia;
          changeBuyTag(R.id.no_pingjia);
          break;
      }
      if (TextUitl.isNotEmpty(url)) {
        tag = 0;
        ArrayList<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);
        RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(),
            mActivity, list, url, buySellListener, RequestNet.POST);
      } else {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
    }
  }

  private class MySellOnClickListener implements OnClickListener {


    private int id;

    public MySellOnClickListener(int id) {
      this.id = id;
    }

    @Override
    public void onClick(View v) {

      String url = null;

      try {
        switch (id) {
          case R.id.no_match:
            url = Urls.sell_no_match;
            changeSellTag(R.id.no_match);
            break;
          case R.id.no_pay:
            url = Urls.sell_no_pay;
            changeSellTag(R.id.no_pay);
            break;
          case R.id.no_confrim:
            url = Urls.sell_no_confirm;
            changeSellTag(R.id.no_confrim);
            break;
          case R.id.no_pingjia:
            url = Urls.sell_no_pingjia;
            changeSellTag(R.id.no_pingjia);
            break;
        }

        if (TextUitl.isNotEmpty(url)) {
          tag = 1;

          Log.e("MySellOnCli1111", "onClick=" + SPUtils
              .getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, ""));
          ArrayList<RequestParams> list = new ArrayList<>();
          RequestParams paramsToken = new RequestParams(Config.TOKEN,
              SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
          list.add(paramsToken);
          RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(),
              mActivity, list, url, buySellListener, RequestNet.POST);
        } else {
          showMsg(getString(R.string.GET_DATE_FAIL));
        }
      } catch (Exception e) {
        showMsg(TextUitl.isNotEmpty(e.getMessage()) ? e.getMessage() : "");
      }
    }
  }

  public void changeBuyTag(int viewId) {
//    c727274 灰色
//    c1e257c 蓝色
    switch (viewId) {
      case R.id.no_match:
        mTvBuyNoMatch.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvBuyNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_pay:
        mTvBuyNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoPay.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvBuyNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_confrim:
        mTvBuyNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoConfrim.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvBuyNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_pingjia:
        mTvBuyNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvBuyNopingjia.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        break;
      case -1:
        if (null != mActivity) {
          mTvBuyNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvBuyNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvBuyNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvBuyNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        }
        break;
    }
  }


  public void changeSellTag(int viewId) {
//    c333 灰色
//    c1e257c 蓝色
    switch (viewId) {
      case R.id.no_match:
        mTvSellNoMatch.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvSellNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_pay:
        mTvSellNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoPay.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvSellNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_confrim:
        mTvSellNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoConfrim.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        mTvSellNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        break;
      case R.id.no_pingjia:
        mTvSellNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        mTvSellNopingjia.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));
        break;
      case -1:
        if (null != mActivity) {
          mTvSellNoMatch.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvSellNoPay.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvSellNoConfrim.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
          mTvSellNopingjia.setTextColor(ColorUtil.getColor(R.color.c333, mActivity));
        }
        break;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void updte(HomeEvent event) {
    initData();
    changeBuyTag(-1);
    changeSellTag(-1);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void updteone(HomeOneEvent event) {
    initData();
    changeBuyTag(-1);
    changeSellTag(-1);
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    initData();
    changeBuyTag(-1);
    changeSellTag(-1);
  }


  public void timeDown(final String mEndtime) {
    if (TextUitl.isNotEmpty(mEndtime)) {

      Timer timer = new Timer();
      TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
          Message obtain = Message.obtain();
          obtain.what = 0;
          obtain.obj = mEndtime;
          mHandler.sendMessageDelayed(obtain, 0);
        }
      };

      timer.schedule(timerTask, 0, 1000 * 60);
    } else {
      tv_hours.setText("00");
      tv_minus.setText("00");
      tv_seconds.setText("00");
    }
  }


  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0 && TextUitl.isNotEmpty((String) msg.obj)) {
        Long aLong = DateUtil.parseDateString((String) msg.obj);
        if (null != aLong) {
          updateTimeRemaining(aLong);
        }
      }
    }
  };


  public void updateTimeRemaining(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {
      List<Integer> list = TimeUtil.getFormatDiffTimeStringOne(timeDiff);
      Integer minutes = list.get(1);
      Integer hours = list.get(2);
      Integer days = list.get(3);

      if (null != tv_hours && null != tv_minus && null != tv_seconds) {
        tv_hours.setText((days < 10 ? "0" + days : days) + "");
        tv_minus.setText((hours < 10 ? "0" + hours : hours) + "");
        tv_seconds.setText((minutes < 10 ? "0" + minutes : minutes) + "");
      }

//      StringUtil
//          .setTextSizeNewOne((minutes < 10 ? "0" + minutes : minutes) + "\nM", tv_seconds, 45, 11,
//              'M', 'M',
//              Color.parseColor("#8286CF"));
//      tv_hours.setText((hours < 10 ? "0" + hours : hours) + "");

    } else {
      if (null != tv_hours && null != tv_minus && null != tv_seconds) {
        tv_hours.setText("00");
        tv_minus.setText("00");
        tv_seconds.setText("00");
      }


//      setTextSizeNewOne(0 + "\nD", tv_hours, 45, 11, 'D', 'D',
//          Color.parseColor("#8286CF"));
//      setTextSizeNewOne(0 + "\nH", tv_minus, 45, 11, 'H', 'H',
//          Color.parseColor("#8286CF"));
//      setTextSizeNewOne(0 + "\nM", tv_seconds, 45, 11, 'M', 'M',
//          Color.parseColor("#8286CF"));
    }
  }


}
