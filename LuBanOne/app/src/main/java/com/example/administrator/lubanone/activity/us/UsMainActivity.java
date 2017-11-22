package com.example.administrator.lubanone.activity.us;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.customview.UnScrollViewPager;
import com.example.administrator.lubanone.event.HomeOneEvent;
import com.example.administrator.lubanone.fragment.us.FragmentCustomer;
import com.example.administrator.lubanone.fragment.us.FragmentFund;
import com.example.administrator.lubanone.fragment.us.FragmentIAC;
import com.example.administrator.lubanone.fragment.us.FragmentSpread;
import com.example.administrator.lubanone.fragment.us.FragmentVip;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by hou on 2017/6/28.
 */

public class UsMainActivity extends BaseActivity {

  private ImageView backBtn;
  private UnScrollViewPager unScrollViewPager;
  private ImageView ib_iac, ib_vip, ib_customer, ib_fund, ib_spread;
  private LinearLayout tableIac, tableVip, tableCustomer, tableFund, tableSpread;
  private TextView tv_iac, tv_vip, tv_customer, tv_fund, tv_spread;

  private List<Fragment> fragments;
  private FragmentIAC iacFragment;
  private FragmentVip vipFragment;
  private FragmentCustomer customerFragment;
  private FragmentFund fundFragment;
  private FragmentSpread spreadFragment;
  private FragmentManager fm;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_us_main;
  }

  @Override
  public void finish() {
    EventBus.getDefault().post(new HomeOneEvent());
    super.finish();
  }

  @Override
  public void initView() {

    unScrollViewPager = (UnScrollViewPager) findViewById(R.id.us_main_viewpager);
    unScrollViewPager = (UnScrollViewPager) findViewById(R.id.us_main_viewpager);
    ib_iac = (ImageView) findViewById(R.id.us_main_ib_iac);
    ib_vip = (ImageView) findViewById(R.id.us_main_ib_vip);
    ib_customer = (ImageView) findViewById(R.id.us_main_ib_customer);
    ib_fund = (ImageView) findViewById(R.id.us_main_ib_fund);
    ib_spread = (ImageView) findViewById(R.id.us_main_ib_spread);
    tableIac = (LinearLayout) findViewById(R.id.us_table_iac);
    tableVip = (LinearLayout) findViewById(R.id.us_table_vip);
    tableCustomer = (LinearLayout) findViewById(R.id.us_table_customer);
    tableFund = (LinearLayout) findViewById(R.id.us_table_fund);
    tableSpread = (LinearLayout) findViewById(R.id.us_table_spread);
    tv_iac = (TextView) findViewById(R.id.us_main_table_iac_text);
    tv_vip = (TextView) findViewById(R.id.us_main_table_vip_text);
    tv_customer = (TextView) findViewById(R.id.us_main_table_customer_text);
    tv_fund = (TextView) findViewById(R.id.us_main_table_fund_text);
    tv_spread = (TextView) findViewById(R.id.us_main_table_spread_text);

    tableIac.setOnClickListener(this);
    tableVip.setOnClickListener(this);
    tableCustomer.setOnClickListener(this);
    tableFund.setOnClickListener(this);
    tableSpread.setOnClickListener(this);

    iacFragment = new FragmentIAC();
    vipFragment = new FragmentVip();
    customerFragment = new FragmentCustomer();
    fundFragment = new FragmentFund();
    spreadFragment = new FragmentSpread();
    fragments = new ArrayList<>();
    fragments.add(iacFragment);
    fragments.add(vipFragment);
    fragments.add(customerFragment);
    fragments.add(fundFragment);
    fragments.add(spreadFragment);
    fm = getSupportFragmentManager();
    FragmentVpAdapter adapter = new FragmentVpAdapter(fm, fragments);
    unScrollViewPager.setAdapter(adapter);
    unScrollViewPager.setCurrentItem(0);
    unScrollViewPager.setOffscreenPageLimit(3);
    ib_iac.setSelected(true);
    tv_iac.setSelected(true);

    unScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        try {
          switch (position) {
            case 0:
              //关于IAC
              break;
            case 1:
              //会员风采
              break;
            case 2:
              //客户寄语
              break;
            case 3:
              //梦想基金
              break;
            case 4:
              //我要推广
              break;
            default:
              break;
          }
        } catch (DefineException e) {
          ToastUtil.showShort(e.getMessage(), getApplicationContext());
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    Intent intent = getIntent();
    if (null != intent) {
      String type = intent.getType();
      if (!TextUtils.isEmpty(type) && Config.TRAIN_CREDIT_UPLOAD_KEY.equals(type)) {
        changePage(4);
      }
    }
    if (null != intent) {
      String type = intent.getType();
      if (!TextUtils.isEmpty(type) && Config.UPLOAD_SPREAD_INFO.equals(type)) {
        changePage(4);
      }
    }
  }

  private void changePage(int position) {
    unScrollViewPager.setCurrentItem(position, false);
    switch (position) {
      case 0:
        ib_iac.setSelected(true);
        ib_vip.setSelected(false);
        ib_customer.setSelected(false);
        ib_fund.setSelected(false);
        ib_spread.setSelected(false);
        break;
      case 1:
        ib_iac.setSelected(false);
        ib_vip.setSelected(true);
        ib_customer.setSelected(false);
        ib_fund.setSelected(false);
        ib_spread.setSelected(false);
        break;
      case 2:
        ib_iac.setSelected(false);
        ib_vip.setSelected(false);
        ib_customer.setSelected(true);
        ib_fund.setSelected(false);
        ib_spread.setSelected(false);
        break;
      case 3:
        ib_iac.setSelected(false);
        ib_vip.setSelected(false);
        ib_customer.setSelected(false);
        ib_fund.setSelected(true);
        ib_spread.setSelected(false);
        break;
      case 4:
        ib_iac.setSelected(false);
        ib_vip.setSelected(false);
        ib_customer.setSelected(false);
        ib_fund.setSelected(false);
        ib_spread.setSelected(true);
        break;
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    ib_iac.setSelected(false);
    ib_vip.setSelected(false);
    ib_customer.setSelected(false);
    ib_fund.setSelected(false);
    ib_spread.setSelected(false);
    tv_iac.setSelected(false);
    tv_vip.setSelected(false);
    tv_customer.setSelected(false);
    tv_fund.setSelected(false);
    tv_spread.setSelected(false);
    switch (v.getId()) {
      case R.id.us_table_iac:
        unScrollViewPager.setCurrentItem(0, false);//关于IAC
        ib_iac.setSelected(true);
        tv_iac.setSelected(true);
        break;
      case R.id.us_table_vip:
        unScrollViewPager.setCurrentItem(1, false);//会员风采
        ib_vip.setSelected(true);
        tv_vip.setSelected(true);
        break;
      case R.id.us_table_customer:
        unScrollViewPager.setCurrentItem(2, false);//客户寄语
        ib_customer.setSelected(true);
        tv_customer.setSelected(true);
        break;
      case R.id.us_table_fund:
        unScrollViewPager.setCurrentItem(3, false);//梦想基金
        ib_fund.setSelected(true);
        tv_fund.setSelected(true);
        break;
      case R.id.us_table_spread:
        unScrollViewPager.setCurrentItem(4, false);//我要推广
        ib_spread.setSelected(true);
        tv_spread.setSelected(true);
        break;
      default:
        break;
    }
  }


}
