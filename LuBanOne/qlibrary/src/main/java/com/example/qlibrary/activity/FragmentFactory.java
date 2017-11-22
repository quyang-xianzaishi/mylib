package com.example.qlibrary.activity;

import android.util.SparseArray;

/**
 * Created by Administrator on 2017/6/18.
 */

public class FragmentFactory {

    private static final int MAIN_PAGER = 0;
    private static final int FENLEI_PAGER = 1;
    private static final int SHOPPING_PAGER = 2;
    private static final int MINE_PAGER = 3;

    private static SparseArray<BaseFragment> mMap = new SparseArray<>();

    public static BaseFragment getFragment(int position) {
        if (position < 0) {
            return null;
        }

        BaseFragment baseFragment = mMap.get(position);
        if (null != baseFragment) {
            return baseFragment;
        }

        BaseFragment fragment = null;

        switch (position) {
            case MAIN_PAGER:
//                fragment = new MainFragment();
                break;
            case FENLEI_PAGER:
//                fragment = new FenLeiFragment();
                break;
            case SHOPPING_PAGER:
//                fragment = new ShoppingFragment();
                break;
            case MINE_PAGER:
//                fragment = new MineFragment();
                break;
        }

        mMap.put(position, fragment);
        return fragment;

    }
}
