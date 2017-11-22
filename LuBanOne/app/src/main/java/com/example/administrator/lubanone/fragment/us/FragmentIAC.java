package com.example.administrator.lubanone.fragment.us;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2017/6/28.
 */

public class FragmentIAC extends BaseFragment implements OnClickListener {

  private TabLayout iacTabLayout;
  private ViewPager iacViewPager;
  private LinearLayout backBtn;

  private CommonTabLayoutFragmentAdapter adapter;
  private String[] pagerTitles;
  private List<Fragment> fragments;

  private DetailsIacProjectIntroduceFragment iacProjectIntroduceFragment;
  private DetailsIacInstitutionFragment institutionFragment;
  private ListIacSocietyFragment societyFragment;
  private ListIacHonorFragment honorFragment;
  private ListIacDreamFragment dreamFragment;
  private DetailsIacQAFragment iacQAFragment;
  private FragmentManager fm;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_common2, null);

    backBtn = (LinearLayout) view.findViewById(R.id.us_common2_fragment_back);
    iacTabLayout = (TabLayout) view.findViewById(R.id.us_common2_fragment_tablayout);
    iacViewPager = (ViewPager) view.findViewById(R.id.us_common2_fragment_viewpager);
    backBtn.setOnClickListener(this);

    iacProjectIntroduceFragment = new DetailsIacProjectIntroduceFragment();
    institutionFragment = new DetailsIacInstitutionFragment();
    societyFragment = new ListIacSocietyFragment();
    honorFragment = new ListIacHonorFragment();
    dreamFragment = new ListIacDreamFragment();
    iacQAFragment = new DetailsIacQAFragment();
    fragments = new ArrayList<>();
    fm = getChildFragmentManager();
    fragments.add(iacProjectIntroduceFragment);
    fragments.add(institutionFragment);
    fragments.add(societyFragment);
    fragments.add(honorFragment);
    fragments.add(dreamFragment);
    fragments.add(iacQAFragment);
    pagerTitles = new String[]{getInfo(R.string.us_iac_introduce),
        getInfo(R.string.us_iac_institution),
        getInfo(R.string.us_iac_society), getInfo(R.string.us_iac_honor),
        getInfo(R.string.us_iac_dream), getInfo(R.string.us_iac_answer)};
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
