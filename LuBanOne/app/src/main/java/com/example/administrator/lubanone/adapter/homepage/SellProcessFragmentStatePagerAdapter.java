package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.fragment.SellSeedProcessFragmentFactory;

/**
 * Created by quyang on 2017/6/30.
 */

public class SellProcessFragmentStatePagerAdapter extends FragmentPagerAdapter {


  private String[] array;

  public SellProcessFragmentStatePagerAdapter(Context context,
      FragmentManager supportFragmentManager) {
    super(supportFragmentManager);
    this.array = context.getResources().getStringArray(R.array.buy_sell);
  }

  @Override
  public Fragment getItem(int position) {
    return SellSeedProcessFragmentFactory.getFragment(position);
  }

  @Override
  public int getCount() {
    return array.length;
  }
}
