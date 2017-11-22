package com.example.administrator.lubanone.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quyang on 2017/6/27.
 */

public class BuySeedProcessFragmentFactory {

    private static Map<Integer, BaseFragment> mMap = new HashMap<>();


    public static BaseFragment getFragment(int position) {

        BaseFragment fragment = mMap.get(position);
        if (null != fragment) {
            return fragment;
        }
        switch (position) {
            case 0:
                fragment = new MatchFragment();
                break;
            case 1:
                fragment = new MoneyFragment();
                break;
            case 2:
                fragment = new ComfirmFragment();
                break;
            case 3:
                fragment = new PinJiaFragment();
                break;
        }

        mMap.put(position, fragment);
        return fragment;
    }
}
