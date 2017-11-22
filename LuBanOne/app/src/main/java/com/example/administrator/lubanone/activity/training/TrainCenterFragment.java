package com.example.administrator.lubanone.activity.training;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.util.ArrayList;

/**
 * Created by hou on 2017/8/3.
 */

public class TrainCenterFragment extends BaseFragment implements OnClickListener {

  private final static String TAG = "TrainCenterFragment";

  private LinearLayout backBtn;

  private TextView platformData;
  private TextView vipData;
  private ViewPager viewPager;
  private TabLayout mTabLayout;

  private String[] pagerTitles;
  private CommonTabLayoutFragmentAdapter tabLayoutAdapter;

  private ArrayList<Fragment> fragments;
  private PlatformDataFragment platformDataFragment;
  private VipDataFragment vipDataFragment;
  private FragmentManager fm;
  private FragmentVpAdapter adapter;
  private int mCurrentItem = 0;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_train_center, null);
    platformData = (TextView) view.findViewById(R.id.training_center_platform_data);
    vipData = (TextView) view.findViewById(R.id.training_center_vip_data);
    viewPager = (ViewPager) view.findViewById(R.id.training_center_unscrollvp);
    backBtn = (LinearLayout) view.findViewById(R.id.train_center_back_btn);
    mTabLayout = (TabLayout) view.findViewById(R.id.train_center_tab_layout);

    backBtn.setOnClickListener(this);
    platformData.setOnClickListener(this);
    vipData.setOnClickListener(this);

    fragments = new ArrayList<>();
    platformDataFragment = new PlatformDataFragment();
    vipDataFragment = new VipDataFragment();
    fragments.add(platformDataFragment);
    fragments.add(vipDataFragment);
    pagerTitles = new String[]{getInfo(R.string.platform_train_data),
        getInfo(R.string.vip_train_data)};
    fm = getChildFragmentManager();
    tabLayoutAdapter = new CommonTabLayoutFragmentAdapter(fm, fragments, pagerTitles);
    viewPager.setAdapter(tabLayoutAdapter);
    mTabLayout.setupWithViewPager(viewPager);

    adapter = new FragmentVpAdapter(fm, fragments);
    viewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        platformData.setSelected(false);
        vipData.setSelected(false);
        switch (position) {
          case 0:
            platformData.setSelected(true);
            break;
          case 1:
            vipData.setSelected(true);
            break;
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
//    viewPager.setCurrentItem(mCurrentItem, false);
    switch (mCurrentItem) {
      case 0:
        platformData.setSelected(true);
        break;
      case 1:
        vipData.setSelected(true);
        break;
      default:
        break;
    }
  }

  @Override
  public void initData() {

  }

  @Override
  public void onClick(View v) {
    platformData.setSelected(false);
    vipData.setSelected(false);
    switch (v.getId()) {
      case R.id.training_center_platform_data:
        viewPager.setCurrentItem(0, false);
        platformData.setSelected(true);
        mCurrentItem = 0;
        break;
      case R.id.training_center_vip_data:
        viewPager.setCurrentItem(1, false);
        vipData.setSelected(true);
        mCurrentItem = 1;
        break;
      case R.id.train_center_back_btn:
        getActivity().finish();
        break;
      default:
        break;
    }
  }
}
