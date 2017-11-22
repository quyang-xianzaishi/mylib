package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.MyFragmentStatePagerAdapter;
import com.example.administrator.lubanone.bean.homepage.BuySellBean;
import com.example.administrator.lubanone.event.HomeEvent;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.fragment.BuySeedProcessFragmentFactory;
import com.example.qlibrary.image.glide.GlideManager;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.view.TraingleIndicator;
import com.umeng.facebook.FacebookSdk;
import org.greenrobot.eventbus.EventBus;

/**
 * 买入流程
 */
public class BuySeedProcessActivity extends BaseActivity {


  @BindView(R.id.iv_back_click)
  ImageView mIvBackClick;
  @BindView(R.id.iv_delete_click)
  ImageView mIvDeleteClick;
  @BindView(R.id.user_name_click)
  TextView mUserNameClick;
  @BindView(R.id.iv_finance_center_click)
  ImageView mIvFinanceCenterClick;
  @BindView(R.id.tv_finance_center_click)
  TextView mTvFinanceCenterClick;
  @BindView(R.id.rl_finance)
  RelativeLayout mRlFinance;
  @BindView(R.id.iv_my_team_click)
  ImageView mIvMyTeamClick;
  @BindView(R.id.tv_my_team_click)
  TextView mTvMyTeamClick;
  @BindView(R.id.icon_click)
  ImageView mIconClick;
  @BindView(R.id.rb_match)
  ImageView mRbMatch;
  @BindView(R.id.match_dot)
  TextView mMatchDot;
  @BindView(R.id.rb_no_money)
  ImageView mRbNoMoney;
  @BindView(R.id.no_money_dot)
  TextView mNoMoneyDot;
  @BindView(R.id.rb_comfirm)
  ImageView mRbComfirm;
  @BindView(R.id.confirm_dot)
  TextView mConfirmDot;
  @BindView(R.id.rb_evaluate)
  ImageView mRbEvaluate;
  @BindView(R.id.evaluate_dot)
  TextView mEvaluateDot;
  @BindView(R.id.rg)
  TraingleIndicator mRg;
  @BindView(R.id.activity_buy_seed_process)
  LinearLayout mActivityBuySeedProcess;

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  @BindView(R.id.tv_back)
  TextView mTvBack;
  private String mActiveCode;
  @BindView(R.id.tv_one)
  TextView tvOne;
  @BindView(R.id.tv_two)
  TextView tvTwo;
  @BindView(R.id.tv_three)
  TextView tvThree;
  @BindView(R.id.tv_four)
  TextView tvFour;

  @Override
  protected void beforeSetContentView() {
    FacebookSdk.sdkInitialize(getApplicationContext());
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_buy_seed_process_new;
  }

  @Override
  public void initView() {

    mRg.bindIndicator(mViewPager);
    mRg.setOnItemClick();

    String path = SPUtils
        .getStringValue(this, Config.USER_INFO, Config.HEAD_ICON_PATH, null);
    if (TextUtils.isEmpty(path)) {
      //icon_click
      mIconClick.setBackgroundResource(R.mipmap.head_2x);
    } else {
      GlideManager.glideWithRound(this, path, mIconClick, 33);
    }

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME, ""))
        .append("\n").append(SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_ACCOUNT,
            ""));

    mUserNameClick.setText(stringBuilder);

    mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mRg.scroll(position, positionOffset);
      }

      @Override
      public void onPageSelected(int position) {

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    mViewPager.setAdapter(
        new MyFragmentStatePagerAdapter(getApplicationContext(), getSupportFragmentManager()));
    mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    Intent intent = getIntent();
    if (null != intent) {
      int buy = intent.getIntExtra(Config.BUY_PROCESS_KEY, -1);
      switchFragmentByType(buy);

      Bundle bundle = null;
      if (intent.hasExtra("bundle")) {
        bundle = intent.getBundleExtra("bundle");
      }
      BuySellBean counts = null;
      if (bundle != null && bundle.containsKey("counts")) {
        counts = (BuySellBean) bundle.getSerializable("counts");
      }
      if (bundle != null && bundle.containsKey("active_code")) {
        mActiveCode = bundle.getString("active_code");
      }
      updateRedDot(counts);
    }
  }

  private void updateRedDot(BuySellBean counts) {
    if (null == counts) {
      mMatchDot.setVisibility(View.GONE);
      mNoMoneyDot.setVisibility(View.GONE);
      mConfirmDot.setVisibility(View.GONE);
      mEvaluateDot.setVisibility(View.GONE);
      return;
    }
    if (!TextUtils.isEmpty(counts.getBuyMatch()) && !"0".equals(counts.getBuyMatch())) {
      mMatchDot.setText(counts.getBuyMatch());
    } else {
      mMatchDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getBuyNoMoney()) && !"0".equals(counts.getBuyNoMoney())) {
      mNoMoneyDot.setText(counts.getBuyNoMoney());
    } else {
      mNoMoneyDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getBuyConfirm()) && !"0".equals(counts.getBuyConfirm())) {
      mConfirmDot.setText(counts.getBuyConfirm());
    } else {
      mConfirmDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getBuyPingJia()) && !"0".equals(counts.getBuyPingJia())) {
      mEvaluateDot.setText(counts.getBuyPingJia());
    } else {
      mEvaluateDot.setVisibility(View.GONE);
    }

  }

  private void switchFragmentByType(int buy) {

    switch (buy) {
      case Config.buy_process_match_from_home_fragment:
        updateIcon(0);
        break;
      case Config.buy_process_money_from_home_fragment:
        updateIcon(1);
        break;
      case Config.buy_process_comfrim_from_home_fragment:
        updateIcon(2);
        break;
      case Config.buy_process_pingjia_from_home_fragment:
        updateIcon(3);
        break;
      default:
        break;
    }
  }

  @Override
  public void loadData() {
//    if (Config.isOnline) {
//      judgeNet();
//    }
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.iv_back_click, R.id.iv_delete_click, R.id.user_name_click,
      R.id.iv_finance_center_click, R.id.tv_finance_center_click, R.id.iv_my_team_click,
      R.id.tv_my_team_click, R.id.icon_click, R.id.rb_match, R.id.rb_no_money, R.id.rb_comfirm,
      R.id.rb_evaluate, R.id.tv_back, R.id.tv_one, R.id.tv_two, R.id.tv_three, R.id.tv_four})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back_click:
      case R.id.tv_back:
        finish();
        break;
      case R.id.user_name_click:
      case R.id.icon_click:
        startNewActivityThenFinish(this, UserCenterActivity.class);
      case R.id.iv_finance_center_click:
      case R.id.tv_finance_center_click:
        startNewActivityThenFinish(this, UserDreamsActivity.class);
        break;
      case R.id.iv_my_team_click:
      case R.id.tv_my_team_click:
        Intent intent = new Intent(this, MyTeamActivity.class);
        intent.putExtra("code_size", DebugUtils.convert(mActiveCode, ""));
        startActivity(intent);
        finish();
        break;
      case R.id.rb_match:
      case R.id.tv_one:
        updateIcon(0);
        break;
      case R.id.rb_no_money:
      case R.id.tv_two:
        updateIcon(1);
        break;
      case R.id.rb_comfirm:
      case R.id.tv_three:
        updateIcon(2);
        break;
      case R.id.rb_evaluate:
      case R.id.tv_four:
        updateIcon(3);
        break;
      default:
        break;
    }
  }

  public void updateIcon(int posotion) {

    mViewPager.setCurrentItem(posotion, false);

    switch (posotion) {
      case 0:
        mRbMatch.setBackgroundResource(R.mipmap.tabbar_wait_select);
        tvOne.setTextColor(getResColor(R.color.cEA5412));

        mRbNoMoney.setBackgroundResource(R.mipmap.tabbar_pay2x);
        tvTwo.setTextColor(Color.parseColor("#888888"));

        mRbComfirm.setBackgroundResource(R.mipmap.tabbar_confirm2x);
        tvThree.setTextColor(Color.parseColor("#888888"));

        mRbEvaluate.setBackgroundResource(R.mipmap.tabber_evaluate2x);
        tvFour.setTextColor(Color.parseColor("#888888"));

        break;
      case 1:
        mRbMatch.setBackgroundResource(R.mipmap.tabbar_wait2x);
        tvOne.setTextColor(Color.parseColor("#888888"));

        mRbNoMoney.setBackgroundResource(R.mipmap.tabbar_pay_select);
        tvTwo.setTextColor(getResColor(R.color.cEA5412));

        mRbComfirm.setBackgroundResource(R.mipmap.tabbar_confirm2x);
        tvThree.setTextColor(Color.parseColor("#888888"));

        mRbEvaluate.setBackgroundResource(R.mipmap.tabber_evaluate2x);
        tvFour.setTextColor(Color.parseColor("#888888"));
        break;
      case 2:

        mRbMatch.setBackgroundResource(R.mipmap.tabbar_wait2x);
        tvOne.setTextColor(Color.parseColor("#888888"));

        mRbNoMoney.setBackgroundResource(R.mipmap.tabbar_pay2x);
        tvTwo.setTextColor(Color.parseColor("#888888"));

        mRbComfirm.setBackgroundResource(R.mipmap.tabbar_confirm_select);
        tvThree.setTextColor(getResColor(R.color.cEA5412));

        mRbEvaluate.setBackgroundResource(R.mipmap.tabber_evaluate2x);
        tvFour.setTextColor(Color.parseColor("#888888"));
        break;
      case 3:
        mRbMatch.setBackgroundResource(R.mipmap.tabbar_wait2x);
        tvOne.setTextColor(Color.parseColor("#888888"));

        mRbNoMoney.setBackgroundResource(R.mipmap.tabbar_pay2x);
        tvTwo.setTextColor(Color.parseColor("#888888"));

        mRbComfirm.setBackgroundResource(R.mipmap.tabbar_confirm2x);
        tvThree.setTextColor(Color.parseColor("#888888"));

        mRbEvaluate.setBackgroundResource(R.mipmap.tabber_evaluate_select);
        tvFour.setTextColor(getResColor(R.color.cEA5412));
        break;
      default:
        break;
    }

  }


  //更新上面的四个按钮
  public void onitem(String size, String p_paylist, String p_confirmlist, String p_dpingjialist) {
    if (TextUtils.isEmpty(size) || "0".equals(size)) {
      mMatchDot.setVisibility(View.GONE);
    } else {
      mMatchDot.setVisibility(View.VISIBLE);
      mMatchDot.setText(String.valueOf(size));
    }
    if (TextUtils.isEmpty(p_paylist) || "0".equals(p_paylist)) {
      mNoMoneyDot.setVisibility(View.GONE);
    } else {
      mNoMoneyDot.setVisibility(View.VISIBLE);
      mNoMoneyDot.setText(String.valueOf(p_paylist.trim()));
    }

    if (TextUtils.isEmpty(p_confirmlist) || "0".equals(p_confirmlist)) {
      mConfirmDot.setVisibility(View.GONE);
    } else {
      mConfirmDot.setVisibility(View.VISIBLE);
      mConfirmDot.setText(String.valueOf(p_confirmlist.trim()));
    }

    if (TextUtils.isEmpty(p_dpingjialist) || "0".equals(p_dpingjialist)) {
      mEvaluateDot.setVisibility(View.GONE);
    } else {
      mEvaluateDot.setVisibility(View.VISIBLE);
      mEvaluateDot.setText(String.valueOf(p_dpingjialist.trim()));
    }
  }


  private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//      if (isFirst && position == 0) {
//      System.out.println("onPageScrolled =" + position);
//        BuySeedProcessFragmentFactory.getFragment(0).initData();
//        isFirst = !isFirst;
//      }

    }

    @Override
    public void onPageSelected(int position) {
      System.out.println("onPageScrolled b=" + position);
      updateIcon(position);
//      BuySeedProcessFragmentFactory.getFragment(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  }


  @Override
  public void finish() {
    super.finish();
    for (int a = 0; a < 4; a++) {
      BaseFragment fragment = BuySeedProcessFragmentFactory.getFragment(a);
      fragment = null;
    }
    MyApplication.getInstance().removeActivity(this);
    EventBus.getDefault().post(new HomeEvent());
  }
}
