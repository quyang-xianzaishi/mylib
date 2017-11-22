package com.example.administrator.lubanone.activity.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.train.TrainActivity;
import com.example.administrator.lubanone.activity.us.UsMainActivity;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.customview.UnScrollViewPager;
import com.example.administrator.lubanone.fragment.HomeNewFragment;
import com.example.administrator.lubanone.fragment.TrainingFragment;
import com.example.administrator.lubanone.fragment.task.TaskNewFragment;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener,
    TrainingFragment.OnFragmentInteractionListener {

  private Context context;
  private ArrayList<Fragment> fragments;
  private UnScrollViewPager unScrollViewPager;
  private ImageView ib_home, ib_task, ib_training, ib_msg;
  //  private ImageButton ib_home, ib_task, ib_training, ib_msg, ib_us;
  private HomeNewFragment homeFragment;
  private TaskNewFragment taskFragment;
  private TrainingFragment trainingFragment;
  private FragmentManager fm;
  private FragmentTransaction ft;
  private int currentPosition = 0;//记录用户在主页面哪个table

  private long mLastTime = 0;

  public static boolean isForeground = false;
  public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
  public static final String KEY_TITLE = "title";
  public static final String KEY_MESSAGE = "message";
  public static final String KEY_EXTRAS = "extras";
  private TextView mTvMainPage;
  private TextView mTvTaskPage;
  private TextView mTvTainPage;
  private TextView mTvWePage;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override
  public void initView() {
    context = getApplicationContext();

    View rlMainPage = findViewById(R.id.rl_main_page);
    View rlTask = findViewById(R.id.rl_task);
    View rlTrain = findViewById(R.id.rl_train);
    View rlAboutWe = findViewById(R.id.rl_about_we);
    rlMainPage.setOnClickListener(this);
    rlTask.setOnClickListener(this);
    rlTrain.setOnClickListener(this);
    rlAboutWe.setOnClickListener(this);

    //首页text
    mTvMainPage = (TextView) findViewById(R.id.tv_main_page);
    mTvTaskPage = (TextView) findViewById(R.id.tv_task_page);
    mTvTainPage = (TextView) findViewById(R.id.tv_tain_page);
    mTvWePage = (TextView) findViewById(R.id.tv_we_page);

    unScrollViewPager = (UnScrollViewPager) findViewById(R.id.main_viewpager);
    ib_home = (ImageView) findViewById(R.id.mainActivity_ib_home);
    ib_task = (ImageView) findViewById(R.id.mainActivity_ib_task);
    ib_training = (ImageView) findViewById(R.id.mainActivity_ib_training);
    ib_msg = (ImageView) findViewById(R.id.mainActivity_ib_msg);
//    ib_us = (ImageButton) findViewById(R.id.mainActivity_ib_us);
    ib_home.setOnClickListener(this);
    ib_training.setOnClickListener(this);
    ib_task.setOnClickListener(this);
    ib_msg.setOnClickListener(this);
//    ib_us.setOnClickListener(this);

//    ib_home.setSelected(true);
    changeIcon(0);

//    homeFragment = new HomeFragment();
    homeFragment = new HomeNewFragment();
    taskFragment = new TaskNewFragment();
    trainingFragment = new TrainingFragment();
    fragments = new ArrayList<>();
    fragments.add(homeFragment);
    fragments.add(taskFragment);
    fragments.add(trainingFragment);
    fm = getSupportFragmentManager();
    ft = fm.beginTransaction();
    FragmentVpAdapter adapter = new FragmentVpAdapter(fm, fragments);
    unScrollViewPager.setAdapter(adapter);
    unScrollViewPager.setCurrentItem(0);
    unScrollViewPager.setOffscreenPageLimit(3);

    unScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        try {
          switch (position) {
            case 0:

              break;
            case 1:

              break;
            case 2:

              break;
            default:
              break;
          }
        } catch (DefineException e) {
          ToastUtil.showShort(e.getMessage(), getApplicationContext());
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @Override
  public void loadData() {
    if (homeFragment != null && homeFragment.isAdded()) {
//      homeFragment.initData();
//      homeFragment.getInitList();
    }
  }

  @Override
  public void onClick(View v) {
//    ib_home.setSelected(false);
//    ib_task.setSelected(false);
//    ib_training.setSelected(false);
    switch (v.getId()) {
      case R.id.mainActivity_ib_home:
      case R.id.rl_main_page:
        unScrollViewPager.setCurrentItem(0, false);//首页
        homeFragment.initData();
//        ib_home.setSelected(true);
//        ib_msg.setSelected(false);
        changeIcon(0);
        currentPosition = 0;
        break;
      case R.id.mainActivity_ib_task:
      case R.id.rl_task:
        unScrollViewPager.setCurrentItem(1, false);//任务
//        ib_task.setSelected(true);
//        ib_msg.setSelected(false);
        changeIcon(1);
        currentPosition = 1;
        break;
      case R.id.mainActivity_ib_training:
      case R.id.rl_train:
//        unScrollViewPager.setCurrentItem(2, false);//培训
//        ib_training.setSelected(true);
//        ib_msg.setSelected(false);
//        changeIcon(2);
//        currentPosition = 2;
        startActivity(new Intent(MainActivity.this, TrainActivity.class));
        break;
      case R.id.mainActivity_ib_msg:
      case R.id.rl_about_we:
        Intent intent = new Intent(MainActivity.this, UsMainActivity.class);
        startActivity(intent);
        break;
//        //消息
//        Intent intent1 = new Intent();
//        intent1.setClass(MainActivity.this, MessageActivity.class);
//        this.startActivity(intent1);
//        changeIcon(3);
//        //ib_msg.setSelected(true);
//        //currentPosition = 2;
//        break;
//      case R.id.mainActivity_ib_us:
//        Intent intent = new Intent(MainActivity.this, UsMainActivity.class);
//        startActivity(intent);
//        break;
      default:
        break;
    }
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }


  private void changeIcon(int a) {
    switch (a) {
      case 0://首页
        ib_home.setBackgroundResource(R.mipmap.tabbar_home_icon_select);
        ib_task.setBackgroundResource(R.mipmap.tabbar_task_icon2x);
        ib_training.setBackgroundResource(R.mipmap.icon_pxs_93);
        ib_msg.setBackgroundResource(R.mipmap.tabbar_about2x);

        mTvMainPage.setTextColor(Color.parseColor("#262E7E"));
        mTvTaskPage.setTextColor(Color.parseColor("#888888"));
        mTvTainPage.setTextColor(Color.parseColor("#888888"));
        mTvWePage.setTextColor(Color.parseColor("#888888"));

        break;
      case 1://任务
        ib_home.setBackgroundResource(R.mipmap.tabbar_home_icon2x);
        ib_task.setBackgroundResource(R.mipmap.tabbar_task_icon_select);
        ib_training.setBackgroundResource(R.mipmap.icon_pxs_93);
        ib_msg.setBackgroundResource(R.mipmap.tabbar_about2x);

        mTvMainPage.setTextColor(Color.parseColor("#888888"));
        mTvTaskPage.setTextColor(Color.parseColor("#262E7E"));
        mTvTainPage.setTextColor(Color.parseColor("#888888"));
        mTvWePage.setTextColor(Color.parseColor("#888888"));
        break;
      case 2://培训
        ib_home.setBackgroundResource(R.mipmap.tabbar_home_icon2x);
        ib_task.setBackgroundResource(R.mipmap.tabbar_task_icon2x);
        ib_training.setBackgroundResource(R.mipmap.tabbar_train_icon_select);
        ib_msg.setBackgroundResource(R.mipmap.tabbar_about2x);

        mTvMainPage.setTextColor(Color.parseColor("#888888"));
        mTvTaskPage.setTextColor(Color.parseColor("#888888"));
        mTvTainPage.setTextColor(Color.parseColor("#262E7E"));
        mTvWePage.setTextColor(Color.parseColor("#888888"));
        break;
      case 3://关于我们
        ib_home.setBackgroundResource(R.mipmap.tabbar_home_icon2x);
        ib_task.setBackgroundResource(R.mipmap.tabbar_task_icon2x);
        ib_training.setBackgroundResource(R.mipmap.icon_pxs_93);
        ib_msg.setBackgroundResource(R.mipmap.tabbar_about_icon_select);

        mTvMainPage.setTextColor(Color.parseColor("#888888"));
        mTvTaskPage.setTextColor(Color.parseColor("#888888"));
        mTvTainPage.setTextColor(Color.parseColor("#888888"));
        mTvWePage.setTextColor(Color.parseColor("#262E7E"));
        break;
    }

  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (null != intent) {
      String type = intent.getType();
      if (!TextUtils.isEmpty(type) && Config.MAIN_PAIN__KEY.equals(type)) {
        if (null != unScrollViewPager && ib_task != null) {
          currentPosition = 1;
        }
      }

      //考试
      if (!TextUtils.isEmpty(type) && Config.TRAIN_CREDIT_EXAM_KEY.equals(type)) {
        if (null != unScrollViewPager && ib_task != null) {
          currentPosition = 2;
          trainingFragment.setCurrentPage(2);
        }
      }

      //培训
      if (!TextUtils.isEmpty(type) && Config.TRAIN_CREDIT_TRAIN_KEY.equals(type)) {
        if (null != unScrollViewPager && ib_task != null) {
          currentPosition = 2;
          trainingFragment.setCurrentPage(3);
        }
      }

      //去培训
      String train_system = intent.getStringExtra("train_system");
      if (!TextUtils.isEmpty(train_system)) {
        if (null != unScrollViewPager && ib_task != null) {
          currentPosition = 2;

          //培训
          if (!TextUtils.isEmpty(type) && Config.TRAIN_CREDIT_TRAIN_KEY.equals(type)) {
            if (null != unScrollViewPager && ib_task != null) {
              currentPosition = 2;
              trainingFragment.setCurrentPage(3);
            }
          }
        }
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    unScrollViewPager.setCurrentItem(currentPosition, false);//首页
    switch (currentPosition) {
      case 0:
//        ib_home.setSelected(true);
//        ib_task.setSelected(false);
//        ib_training.setSelected(false);
        changeIcon(0);
        break;
      case 1:
        changeIcon(1);
//        ib_task.setSelected(true);
//        ib_home.setSelected(false);
//        ib_training.setSelected(false);
        break;
      case 2:
        changeIcon(2);
//        ib_task.setSelected(false);
//        ib_home.setSelected(false);
//        ib_training.setSelected(true);
        break;
      default:
        break;
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK)
        && (event.getAction() == KeyEvent.ACTION_DOWN)) {
      if (System.currentTimeMillis() - mLastTime > 2000) {
        System.out.println(Toast.LENGTH_SHORT);
        Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_LONG).show();
        mLastTime = System.currentTimeMillis();
      } else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

}
