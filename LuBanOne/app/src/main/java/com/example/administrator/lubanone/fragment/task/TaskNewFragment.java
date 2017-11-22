package com.example.administrator.lubanone.fragment.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.task.TaskCollectActivity;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2017/8/24.
 */

public class TaskNewFragment extends BaseFragment {

  private String[] pagerTitles;
  private ViewPager viewPager;
  private TabLayout tabLayout;
  private ImageView collectBtn;
  private CommonTabLayoutFragmentAdapter adapter;
  private UnfinishedTaskFragment unfinishedTaskFragment;
  private ReviewingTaskFragment reviewingTaskFragment;
  private FinishedTaskFragment finishedTaskFragment;
  private List<Fragment> fragments = new ArrayList<>();
  private FragmentManager fragmentManager;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_task_new, null);
    viewPager = (ViewPager) view.findViewById(R.id.task_fragment_viewPager);
    tabLayout = (TabLayout) view.findViewById(R.id.task_fragment_tablayout);
    collectBtn = (ImageView) view.findViewById(R.id.task_fragment_collect_btn);

    collectBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), TaskCollectActivity.class);
        startActivity(intent);
      }
    });

    pagerTitles = new String[]{getInfo(R.string.task_new_task),
        getInfo(R.string.task_reviewing), getInfo(R.string.task_finished)};
    unfinishedTaskFragment = new UnfinishedTaskFragment();
    reviewingTaskFragment = new ReviewingTaskFragment();
    finishedTaskFragment = new FinishedTaskFragment();
    fragments.add(unfinishedTaskFragment);
    fragments.add(reviewingTaskFragment);
    fragments.add(finishedTaskFragment);
    fragmentManager = getActivity().getSupportFragmentManager();
    adapter = new CommonTabLayoutFragmentAdapter(fragmentManager, fragments, pagerTitles);
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
//    setIndicator(mActivity, tabLayout, 45, 45);
    return view;
  }

  public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
    Class<?> tabLayout = tabs.getClass();
    Field tabStrip = null;
    try {
      tabStrip = tabLayout.getDeclaredField("mTabStrip");
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }

    tabStrip.setAccessible(true);
    LinearLayout ll_tab = null;
    try {
      ll_tab = (LinearLayout) tabStrip.get(tabs);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    int left = (int) (getDisplayMetrics(context).density * leftDip);
    int right = (int) (getDisplayMetrics(context).density * rightDip);

    for (int i = 0; i < ll_tab.getChildCount(); i++) {
      View child = ll_tab.getChildAt(i);
      child.setPadding(0, 0, 0, 0);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
          LinearLayout.LayoutParams.MATCH_PARENT, 1);
      params.leftMargin = left;
      params.rightMargin = right;
      child.setLayoutParams(params);
      child.invalidate();
    }
  }

  public static DisplayMetrics getDisplayMetrics(Context context) {
    DisplayMetrics metric = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
    return metric;
  }

  public static float getPXfromDP(float value, Context context) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
        context.getResources().getDisplayMetrics());
  }


  @Override
  public void initData() {

  }


}
