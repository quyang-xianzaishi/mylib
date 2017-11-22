package com.example.administrator.lubanone.fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quyang on 2017/6/27.
 */

public class FragmentFactory {

    private static Map<Integer, BaseFragment> mMap = new HashMap<>();


    public static BaseFragment getFragment(int position) {

        BaseFragment fragment = mMap.get(position);
        if (null != fragment) {
            return fragment;
        }
        switch (position) {
            case 0:
                fragment = new AllScaleFragment();
                break;
            case 1:
                fragment = new DreamPackageFragment();
                break;
            case 2:
                fragment = new MoneyPackageFragment();
                break;
            case 3:
                fragment = new ActiveCodeFragment();
                break;
            case 4:
                fragment = new CuiHuaJiFragment();
                break;
        }

        mMap.put(position, fragment);
        return fragment;
    }
}
