package com.example.administrator.lubanone.adapter.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\25 0025.
 */

public class MyPagerFragmentAdapter extends FragmentPagerAdapter {
  private List<Fragment> fragments;
  private FragmentManager fm;
  private boolean[] flags;//标识,重新设置fragment时全设为true

  public MyPagerFragmentAdapter(FragmentManager fm,
      List<Fragment> fragments) {
    super(fm);
    this.fm = fm;
    this.fragments = fragments;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    super.destroyItem(container, position, object);
  }

  @Override
  public Fragment getItem(int arg0) {
    return fragments.get(arg0);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }
  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    if (flags != null && flags[position]) {
      /**得到缓存的fragment, 拿到tag并替换成新的fragment*/
      Fragment fragment = (Fragment) super.instantiateItem(container, position);
      String fragmentTag = fragment.getTag();
      FragmentTransaction ft = fm.beginTransaction();
      ft.remove(fragment);
      fragment = fragments.get(position);
      ft.add(container.getId(), fragment, fragmentTag);
      ft.attach(fragment);
      ft.commit();
      /**替换完成后设为false*/
      flags[position] = false;
      if (fragment != null){
        return fragment;
      }else {
        return super.instantiateItem(container, position);
      }
    } else {
      return super.instantiateItem(container, position);
    }
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  public void setFragments(List<Fragment> fragments) {
    if (this.fragments != null) {
      flags = new boolean[fragments.size()];
      for (int i = 0; i < fragments.size(); i++) {
        flags[i] = true;
      }
    }
    this.fragments = fragments;
    notifyDataSetChanged();
  }


}
