package com.example.administrator.lubanone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hou on 2017/6/28.
 */

public class CommonTabLayoutFragmentAdapter extends FragmentPagerAdapter {

    private String[] pagerTitles;
    private List<Fragment> fragments;

    public CommonTabLayoutFragmentAdapter(FragmentManager fm,List<Fragment> fragments, String[] pagerTitles) {
        super(fm);
        this.fragments = fragments;
        this.pagerTitles = pagerTitles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
    }
}
