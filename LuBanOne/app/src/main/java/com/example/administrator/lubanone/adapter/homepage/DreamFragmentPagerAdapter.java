package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.UserDreamsActivity;
import com.example.administrator.lubanone.fragment.FragmentFactory;

/**
 * Created by quyang on 2017/6/27.
 */

public class DreamFragmentPagerAdapter extends FragmentPagerAdapter {

    private UserDreamsActivity mActivity;

    public DreamFragmentPagerAdapter(Activity activity, FragmentManager manager) {
        super(manager);
        this.mActivity = (UserDreamsActivity) activity;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        String[] array = mActivity.getResources().getStringArray(R.array.dreams);
        return array.length;
    }


}
