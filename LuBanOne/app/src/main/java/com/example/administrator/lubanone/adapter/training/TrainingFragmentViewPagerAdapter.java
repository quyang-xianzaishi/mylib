package com.example.administrator.lubanone.adapter.training;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.administrator.lubanone.activity.training.ExamCenterFragment;
import com.example.administrator.lubanone.activity.training.MyTrainingPagerFragment;
import com.example.administrator.lubanone.activity.training.TrainCenterFragment;

/**
 * Created by Administrator on 2017\6\22 0022.
 */

public class TrainingFragmentViewPagerAdapter extends FragmentPagerAdapter {

  private String[] pagerTitles = new String[]{"培训中心", "考试中心", "我的培训"};

  public TrainingFragmentViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
//        if (position == 1) {
//            return new TrainingInfomationFragment();
//        } else
    if (position == 1) {
      return new ExamCenterFragment();
    } else if (position == 2) {
      return new MyTrainingPagerFragment();
    }
    return new TrainCenterFragment();
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

}
