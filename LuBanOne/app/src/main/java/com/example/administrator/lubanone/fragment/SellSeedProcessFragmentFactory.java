package com.example.administrator.lubanone.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quyang on 2017/6/27.
 */

public class SellSeedProcessFragmentFactory {

  private static Map<Integer, BaseFragment> mMap = new HashMap<>();


  public static BaseFragment getFragment(int position) {

    BaseFragment fragment = mMap.get(position);
    if (null != fragment) {
      return fragment;
    }
    switch (position) {
      case 0:
        fragment = new SellMatchFragment();
        break;
      case 1:
        fragment = new SellMoneyFragment();
        break;
      case 2:
        fragment = new SellComfirmFragment();
        break;
      case 3:
        fragment = new SellPinJiaFragment();
        break;
    }

    mMap.put(position, fragment);
    return fragment;
  }
}
