package com.example.administrator.lubanone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hou on 2017/6/22.
 */

public class FragmentVpAdapter extends FragmentPagerAdapter {

  private List<Fragment> fragments;
  private Fragment mCurrentFragment;

  public FragmentVpAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    this.fragments = fragments;
  }

  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments != null ? fragments.size() : 0;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    return super.instantiateItem(container, position);
  }

  @Override
  public void setPrimaryItem(ViewGroup container, int position, Object object) {
    mCurrentFragment = (Fragment) object;
    super.setPrimaryItem(container, position, object);
  }


  public Fragment getCurrentFragment() {
    return mCurrentFragment;
  }
}
