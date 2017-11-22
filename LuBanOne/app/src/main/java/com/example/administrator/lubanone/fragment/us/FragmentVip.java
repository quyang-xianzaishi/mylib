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

public class FragmentVip extends BaseFragment implements OnClickListener {

  private TabLayout iacTabLayout;
  private ViewPager iacViewPager;
  private LinearLayout backBtn;
  private CommonTabLayoutFragmentAdapter adapter;
  private String[] pagerTitles;
  private List<Fragment> fragments;
  private ListVipActivityFragment activityFragment;
  private ListVipStoryFragment storyFragment;
  private DetailsVipPromiseFragment promiseFragment;
  private FragmentManager fm;
  private TextView fragmentTitle;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_common, null);
    backBtn = (LinearLayout) view.findViewById(R.id.us_common_fragment_back);
    iacTabLayout = (TabLayout) view.findViewById(R.id.us_common_fragment_tablayout);
    iacViewPager = (ViewPager) view.findViewById(R.id.us_common_fragment_viewpager);
    backBtn.setOnClickListener(this);

    fragmentTitle = (TextView) view.findViewById(R.id.us_common_fragment_title);
    fragmentTitle.setText(getInfo(R.string.us_vip_title));

    activityFragment = new ListVipActivityFragment();
    storyFragment = new ListVipStoryFragment();
    promiseFragment = new DetailsVipPromiseFragment();
    fragments = new ArrayList<>();
    fm = getChildFragmentManager();
    fragments.add(activityFragment);
    fragments.add(storyFragment);
    fragments.add(promiseFragment);
    pagerTitles = new String[]{getInfo(R.string.us_vip_activity),
        getInfo(R.string.us_vip_history), getInfo(R.string.us_vip_promise)};
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
      case R.id.us_common_fragment_back:
        getActivity().finish();
        break;
      default:
        break;
    }
  }
}
