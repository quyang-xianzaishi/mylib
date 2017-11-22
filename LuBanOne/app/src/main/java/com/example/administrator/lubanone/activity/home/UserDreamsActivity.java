package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.rl_two;
import static com.example.administrator.lubanone.R.id.tv_dream_package;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.DreamFragmentPagerAdapter;
import com.example.administrator.lubanone.customview.UnScrollViewPager;

/**
 * 总览，梦想背包，奖金背包，激活码，催化剂
 */
public class UserDreamsActivity extends BaseActivity {


  @BindView(R.id.view_pager)
  UnScrollViewPager mViewPager;
  @BindView(R.id.tv_all)
  TextView mTvAll;
  @BindView(tv_dream_package)
  TextView mTvDreamPackage;
  @BindView(R.id.tv_money_package)
  TextView mTvMoneyPackage;
  @BindView(R.id.tv_active_code)
  TextView mTvActiveCode;
  @BindView(R.id.tv_cuihuaji)
  TextView mTvCuihuaji;
  @BindView(R.id.activity_user_dreams)
  LinearLayout mActivityUserDreams;
  @BindView(R.id.iv_all)
  ImageView mIvAll;
  @BindView(R.id.rl_main_page_one)
  RelativeLayout mRlMainPageOne;
  @BindView(R.id.rl_main_page)
  RelativeLayout mRlMainPage;
  @BindView(R.id.iv_dream_package)
  ImageView mIvDreamPackage;
  @BindView(R.id.rl_two_one)
  RelativeLayout mRlTwoOne;
  @BindView(rl_two)
  RelativeLayout mRlTwo;
  @BindView(R.id.iv_money_package)
  ImageView mIvMoneyPackage;
  @BindView(R.id.rl_three_one)
  RelativeLayout mRlThreeOne;
  @BindView(R.id.rl_three)
  RelativeLayout mRlThree;
  @BindView(R.id.iv_active_code)
  ImageView mIvActiveCode;
  @BindView(R.id.rl_four_one)
  RelativeLayout mRlFourOne;
  @BindView(R.id.rl_four)
  RelativeLayout mRlFour;
  @BindView(R.id.iv_cuihuaji)
  ImageView mIvCuihuaji;
  @BindView(R.id.rl_five_one)
  RelativeLayout mRlFiveOne;
  @BindView(R.id.rl_five)
  RelativeLayout mRlFive;
  private Intent mIntent;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_dreams;
  }

  @Override
  public void initView() {

    mViewPager.setAdapter(new DreamFragmentPagerAdapter(this, getSupportFragmentManager()));
    updateBottomIconAndText(0);

    mIntent = getIntent();
    if (null != mIntent) {
      int value = mIntent.getIntExtra(Config.USER_DREAM_PACKAGE_KEY, -1);
      getDataByType(value);
    }
  }

  private void getDataByType(int value) {
    switch (value) {
      case Config.ALL_SCAN:
        updateBottomIconAndText(0);
        break;
      case Config.DREAM_PACKAGE:
        updateBottomIconAndText(1);
        break;
      case Config.MONEY_PACKAGE:
        updateBottomIconAndText(2);
        break;
      case Config.ACTIVE_CODE:
        updateBottomIconAndText(3);
        break;
      case Config.CUIHUAJI:
        updateBottomIconAndText(4);
        break;
      default:
        break;
    }
  }

  private void updateBottomIconAndText(int position) {

    mViewPager.setCurrentItem(position, false);

    if (0 == position) {

      mTvAll.setTextColor(Color.parseColor("#262E7E"));
      mTvDreamPackage.setTextColor(Color.parseColor("#888888"));
      mTvMoneyPackage.setTextColor(Color.parseColor("#888888"));
      mTvActiveCode.setTextColor(Color.parseColor("#888888"));
      mTvCuihuaji.setTextColor(Color.parseColor("#888888"));

      mIvAll.setBackgroundResource(R.mipmap.tabbar_all_select);
      mIvDreamPackage.setBackgroundResource(R.mipmap.tabbar_dream2x);
      mIvMoneyPackage.setBackgroundResource(R.mipmap.tabbar_jiang2x);
      mIvActiveCode.setBackgroundResource(R.mipmap.tabbar_activation2x);
      mIvCuihuaji.setBackgroundResource(R.mipmap.tabbar_cui2x);

    }

    if (1 == position) {

      mTvAll.setTextColor(Color.parseColor("#888888"));
      mTvDreamPackage.setTextColor(Color.parseColor("#262E7E"));
      mTvMoneyPackage.setTextColor(Color.parseColor("#888888"));
      mTvActiveCode.setTextColor(Color.parseColor("#888888"));
      mTvCuihuaji.setTextColor(Color.parseColor("#888888"));

      mIvAll.setBackgroundResource(R.mipmap.tabbar_all2x);
      mIvDreamPackage.setBackgroundResource(R.mipmap.tabbar_dream_select);
      mIvMoneyPackage.setBackgroundResource(R.mipmap.tabbar_jiang2x);
      mIvActiveCode.setBackgroundResource(R.mipmap.tabbar_activation2x);
      mIvCuihuaji.setBackgroundResource(R.mipmap.tabbar_cui2x);

    }
    if (2 == position) {

      mTvAll.setTextColor(Color.parseColor("#888888"));
      mTvDreamPackage.setTextColor(Color.parseColor("#888888"));
      mTvMoneyPackage.setTextColor(Color.parseColor("#262E7E"));
      mTvActiveCode.setTextColor(Color.parseColor("#888888"));
      mTvCuihuaji.setTextColor(Color.parseColor("#888888"));

      mIvAll.setBackgroundResource(R.mipmap.tabbar_all2x);
      mIvDreamPackage.setBackgroundResource(R.mipmap.tabbar_dream2x);
      mIvMoneyPackage.setBackgroundResource(R.mipmap.tabbar_jiang_select);
      mIvActiveCode.setBackgroundResource(R.mipmap.tabbar_activation2x);
      mIvCuihuaji.setBackgroundResource(R.mipmap.tabbar_cui2x);

    }

    if (3 == position) {

      mTvAll.setTextColor(Color.parseColor("#888888"));
      mTvDreamPackage.setTextColor(Color.parseColor("#888888"));
      mTvMoneyPackage.setTextColor(Color.parseColor("#888888"));
      mTvActiveCode.setTextColor(Color.parseColor("#262E7E"));
      mTvCuihuaji.setTextColor(Color.parseColor("#888888"));

      mIvAll.setBackgroundResource(R.mipmap.tabbar_all2x);
      mIvDreamPackage.setBackgroundResource(R.mipmap.tabbar_dream2x);
      mIvMoneyPackage.setBackgroundResource(R.mipmap.tabbar_jiang2x);
      mIvActiveCode.setBackgroundResource(R.mipmap.tabbar_activation_select);
      mIvCuihuaji.setBackgroundResource(R.mipmap.tabbar_cui2x);

    }

    if (4 == position) {

      mTvAll.setTextColor(Color.parseColor("#888888"));
      mTvDreamPackage.setTextColor(Color.parseColor("#888888"));
      mTvMoneyPackage.setTextColor(Color.parseColor("#888888"));
      mTvActiveCode.setTextColor(Color.parseColor("#888888"));
      mTvCuihuaji.setTextColor(Color.parseColor("#262E7E"));

      mIvAll.setBackgroundResource(R.mipmap.tabbar_all2x);
      mIvDreamPackage.setBackgroundResource(R.mipmap.tabbar_dream2x);
      mIvMoneyPackage.setBackgroundResource(R.mipmap.tabbar_jiang2x);
      mIvActiveCode.setBackgroundResource(R.mipmap.tabbar_activation2x);
      mIvCuihuaji.setBackgroundResource(R.mipmap.tabbar_cui_select);

    }
  }


  @Override
  public void loadData() {
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.view_pager, R.id.tv_all, R.id.iv_all, tv_dream_package, R.id.tv_money_package,
      R.id.tv_active_code, R.id.tv_cuihuaji, R.id.rl_main_page_one, R.id.rl_main_page, rl_two,
      R.id.rl_two_one, R.id.iv_dream_package, R.id.rl_three, R.id.rl_three_one,
      R.id.iv_money_package, R.id.rl_four, R.id.rl_four_one, R.id.iv_active_code, R.id.rl_five,
      R.id.rl_five_one, R.id.iv_cuihuaji})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.view_pager:
        break;
      case R.id.tv_all:
      case R.id.rl_main_page:
      case R.id.rl_main_page_one:
      case R.id.iv_all:
        updateBottomIconAndText(0);
        break;
      case R.id.tv_dream_package:
      case R.id.rl_two:
      case R.id.rl_two_one:
      case R.id.iv_dream_package:
        updateBottomIconAndText(1);
        break;
      case R.id.tv_money_package:
      case R.id.rl_three:
      case R.id.rl_three_one:
      case R.id.iv_money_package:
        updateBottomIconAndText(2);
        break;
      case R.id.tv_active_code:
      case R.id.rl_four:
      case R.id.rl_four_one:
      case R.id.iv_active_code:
        updateBottomIconAndText(3);
        break;
      case R.id.tv_cuihuaji:
      case R.id.rl_five:
      case R.id.rl_five_one:
      case R.id.iv_cuihuaji:
        updateBottomIconAndText(4);
        break;
      default:
        break;
    }
  }

  @Override
  public void finish() {
    super.finish();
    //销毁fragment
//    for (int a = 0; a < 5; a++) {
//      BaseFragment fragment = FragmentFactory.getFragment(0);
//      if (null != fragment) {
//        fragment = null;
//      }
//    }
  }


  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    if (null != intent) {
      int intExtra = intent.getIntExtra(Config.USER_DREAM_PACKAGE_KEY, -1);
      if (intExtra == Config.CUIHUAJI) {//催化剂
        updateBottomIconAndText(4);
      }

      if (intExtra == Config.DREAM_PACKAGE) {//梦想背包
        updateBottomIconAndText(1);
      }

      if (intExtra == Config.MONEY_PACKAGE) {//奖金背包
        updateBottomIconAndText(2);
      }

      if (intExtra == Config.ACTIVE_CODE) {//激活码
        updateBottomIconAndText(3);
      }
    }

  }

}
