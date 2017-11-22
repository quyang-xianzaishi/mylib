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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.SellProcessFragmentStatePagerAdapter;
import com.example.administrator.lubanone.bean.homepage.BuySellBean;
import com.example.administrator.lubanone.event.HomeOneEvent;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.fragment.SellSeedProcessFragmentFactory;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.view.TraingleIndicator;
import org.greenrobot.eventbus.EventBus;

/**
 * 卖出种子过程
 */
public class SellSeedsProcessActivity extends BaseActivity {


  @BindView(R.id.iv_back_click)
  ImageView mIvBackClick;
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

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_buy_seed_process;
  }

  @Override
  public void initView() {

    tvTwo.setText(getString(R.string.no_receive_money));

    mRg.bindIndicator(mViewPager);
    mRg.setOnItemClick();

    String path = SPUtils
        .getStringValue(this, Config.USER_INFO, Config.HEAD_ICON_PATH, null);

    TextView title = (TextView) findViewById(R.id.textView5);
    title.setText(getString(R.string.sell));

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
        new SellProcessFragmentStatePagerAdapter(getApplicationContext(),
            getSupportFragmentManager()));
    mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    Intent intent = getIntent();
    if (null != intent) {
      int sellType = intent.getIntExtra(Config.SELL_PROCESS_KEY, -1);
      getDateByType(sellType);

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
    if (!TextUtils.isEmpty(counts.getSellMatch()) && !"0".equals(counts.getSellMatch())) {
      mMatchDot.setText(counts.getSellMatch());
    } else {
      mMatchDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getSellNoMoney()) && !"0".equals(counts.getSellNoMoney())) {
      mNoMoneyDot.setText(counts.getSellNoMoney());
    } else {
      mNoMoneyDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getSellConfirm()) && !"0".equals(counts.getSellConfirm())) {
      mConfirmDot.setText(counts.getSellConfirm());
    } else {
      mConfirmDot.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(counts.getSellPingJia()) && !"0".equals(counts.getSellPingJia())) {
      mEvaluateDot.setText(counts.getSellPingJia());
    } else {
      mEvaluateDot.setVisibility(View.GONE);
    }

  }

  private void getDateByType(int sellType) {

    switch (sellType) {
      case Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT:
        mViewPager.setCurrentItem(0);
        updateIcon(0);
        break;
      case Config.SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT:
        mViewPager.setCurrentItem(1);
        updateIcon(1);
        break;
      case Config.SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT:
        mViewPager.setCurrentItem(2);
        updateIcon(2);
        break;
      case Config.SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT:
        mViewPager.setCurrentItem(3);
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


  @OnClick({R.id.iv_back_click, R.id.rb_match, R.id.rb_no_money, R.id.rb_comfirm,
      R.id.rb_evaluate, R.id.tv_one, R.id.tv_two, R.id.tv_three, R.id.tv_four,})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back_click:
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

  //更新卖出的4个按钮
  public void onitem(String jsbzlist, String g_paylist, String s, String g_dpingjialist) {
    if (TextUtils.isEmpty(jsbzlist) || "0".equals(jsbzlist)) {
      mMatchDot.setVisibility(View.GONE);
    } else {
      mMatchDot.setVisibility(View.VISIBLE);
      mMatchDot.setText(String.valueOf(jsbzlist));
    }
    if (TextUtils.isEmpty(g_paylist) || "0".equals(g_paylist)) {
      mNoMoneyDot.setVisibility(View.GONE);
    } else {
      mNoMoneyDot.setVisibility(View.VISIBLE);
      mNoMoneyDot.setText(String.valueOf(g_paylist.trim()));
    }

    if (TextUtils.isEmpty(s) || "0".equals(s)) {
      mConfirmDot.setVisibility(View.GONE);
    } else {
      mConfirmDot.setVisibility(View.VISIBLE);
      mConfirmDot.setText(String.valueOf(s.trim()));
    }

    if (TextUtils.isEmpty(g_dpingjialist) || "0".equals(g_dpingjialist)) {
      mEvaluateDot.setVisibility(View.GONE);
    } else {
      mEvaluateDot.setVisibility(View.VISIBLE);
      mEvaluateDot.setText(String.valueOf(g_dpingjialist.trim()));
    }

  }

  private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      updateIcon(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  }

  @Override
  public void finish() {
    super.finish();
    for (int a = 0; a < 4; a++) {
      BaseFragment fragment = SellSeedProcessFragmentFactory.getFragment(a);
      fragment = null;
    }
    MyApplication.getInstance().removeActivity(this);
    EventBus.getDefault().post(new HomeOneEvent());
  }
}
