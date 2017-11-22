package com.example.administrator.lubanone.fragment.us;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2017/6/28.
 */

public class FragmentFund extends BaseFragment implements OnClickListener {

  private TabLayout iacTabLayout;
  private ViewPager iacViewPager;
  private LinearLayout backBtn;
  private CommonTabLayoutFragmentAdapter adapter;
  private String[] pagerTitles;
  private List<Fragment> fragments;
  private DetailsFundPoorFragment fundPoorFragment;
  private DetailsFundIntroduceFragment introduceFragment;
  private ListFundNewsFragment newsFragment;
  private DetailsFundDonationFragment donationFragment;
  private DetailsFundBeCustomerFragment beCustomerFragment;
  private FragmentManager fm;
  private TextView fragmentTitle;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_common2, null);
    backBtn = (LinearLayout) view.findViewById(R.id.us_common2_fragment_back);
    iacTabLayout = (TabLayout) view.findViewById(R.id.us_common2_fragment_tablayout);
    iacViewPager = (ViewPager) view.findViewById(R.id.us_common2_fragment_viewpager);
    backBtn.setOnClickListener(this);

    fragmentTitle = (TextView) view.findViewById(R.id.us_common2_fragment_title);
    fragmentTitle.setText(getInfo(R.string.us_fund_title));

    fundPoorFragment = new DetailsFundPoorFragment();
    introduceFragment = new DetailsFundIntroduceFragment();
    newsFragment = new ListFundNewsFragment();
    donationFragment = new DetailsFundDonationFragment();
    beCustomerFragment = new DetailsFundBeCustomerFragment();
    fragments = new ArrayList<>();
    fm = getChildFragmentManager();
    fragments.add(fundPoorFragment);
    fragments.add(introduceFragment);
    fragments.add(newsFragment);
    fragments.add(donationFragment);
    fragments.add(beCustomerFragment);
    pagerTitles = new String[]{getInfo(R.string.us_fund_poor), getInfo(R.string.us_fund_introduce),
        getInfo(R.string.us_fund_news), getInfo(R.string.us_fund_donation),
        getInfo(R.string.us_fund_be_custom)};
    adapter = new CommonTabLayoutFragmentAdapter(fm, fragments, pagerTitles);
    iacViewPager.setAdapter(adapter);
    //将TabLayout与ViewPager绑定在一起
    iacTabLayout.setupWithViewPager(iacViewPager);
    return view;
  }

  @Override
  public void initData() {

  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.us_common2_fragment_back:
        getActivity().finish();
        break;
      default:
        break;
    }
  }
}
