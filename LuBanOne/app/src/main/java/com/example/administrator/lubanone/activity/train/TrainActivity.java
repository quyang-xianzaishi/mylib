package com.example.administrator.lubanone.activity.train;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.training.ExamCenterFragment;
import com.example.administrator.lubanone.activity.training.MyTrainingPagerFragment;
import com.example.administrator.lubanone.activity.training.TrainCenterFragment;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.customview.UnScrollViewPager;
import com.example.administrator.lubanone.event.HomeOneEvent;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by hou on 2017/8/31.
 */

public class TrainActivity extends BaseActivity {

  private LinearLayout centerLayout, testLayout, mineLayout;
  private ImageView centerImage, testImage, mineImage;
  private TextView centerText, testText, mineText;
  private UnScrollViewPager mViewPager;

  private TrainCenterFragment trainCenterFragment;
  private ExamCenterFragment examCenterFragment;
  private MyTrainingPagerFragment myTrainingPagerFragment;

  private List<Fragment> fragments = new ArrayList<>();
  private FragmentManager fm;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  public void finish() {
    EventBus.getDefault().post(new HomeOneEvent());
    super.finish();
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train;
  }

  @Override
  public void initView() {

    centerLayout = (LinearLayout) findViewById(R.id.train_center_bottom_layout);
    testLayout = (LinearLayout) findViewById(R.id.train_test_bottom_layout);
    mineLayout = (LinearLayout) findViewById(R.id.train_mine_bottom_layout);
    centerImage = (ImageView) findViewById(R.id.train_center_bottom_image);
    testImage = (ImageView) findViewById(R.id.train_test_bottom_image);
    mineImage = (ImageView) findViewById(R.id.train_mine_bottom_image);
    mViewPager = (UnScrollViewPager) findViewById(R.id.train_viewpager);
    centerText = (TextView) findViewById(R.id.train_center_text);
    testText = (TextView) findViewById(R.id.train_test_text);
    mineText = (TextView) findViewById(R.id.train_mine_text);

    centerLayout.setOnClickListener(this);
    testLayout.setOnClickListener(this);
    mineLayout.setOnClickListener(this);

    trainCenterFragment = new TrainCenterFragment();
    examCenterFragment = new ExamCenterFragment();
    myTrainingPagerFragment = new MyTrainingPagerFragment();
    fragments.add(trainCenterFragment);
    fragments.add(examCenterFragment);
    fragments.add(myTrainingPagerFragment);

    fm = getSupportFragmentManager();
    FragmentVpAdapter adapter = new FragmentVpAdapter(fm, fragments);
    mViewPager.setAdapter(adapter);
    mViewPager.setCurrentItem(0);
    mViewPager.setOffscreenPageLimit(3);

    centerImage.setSelected(true);
    centerText.setSelected(true);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    centerImage.setSelected(false);
    testImage.setSelected(false);
    mineImage.setSelected(false);
    centerText.setSelected(false);
    testText.setSelected(false);
    mineText.setSelected(false);
    switch (v.getId()) {
      case R.id.train_center_bottom_layout:
        mViewPager.setCurrentItem(0);
        centerImage.setSelected(true);
        centerText.setSelected(true);
        break;
      case R.id.train_test_bottom_layout:
        mViewPager.setCurrentItem(1);
        testImage.setSelected(true);
        testText.setSelected(true);
        break;
      case R.id.train_mine_bottom_layout:
        mViewPager.setCurrentItem(2);
        mineImage.setSelected(true);
        mineText.setSelected(true);
        break;
    }

  }
}
