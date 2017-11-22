package com.example.administrator.lubanone.adapter.training;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.lubanone.activity.training.MyCollectionFragment;
import com.example.administrator.lubanone.activity.training.MyJoinTrainingFragment;
import com.example.administrator.lubanone.activity.training.MyTrainingFragment;

/**
 * Created by Administrator on 2017\6\24 0024.
 */

public class TrainingPagerAdapter extends FragmentPagerAdapter {

    private String[] pagerTitles = new String[]{"我参加的培训", "我发起的培训","我的收藏"};

    public TrainingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new MyTrainingFragment();
        } else if (position == 2) {
            return new MyCollectionFragment();
        }
        return new MyJoinTrainingFragment();
    }


    @Override
    public int getCount() {
        return pagerTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
