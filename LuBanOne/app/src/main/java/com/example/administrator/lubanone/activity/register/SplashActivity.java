package com.example.administrator.lubanone.activity.register;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.land.LandActivity;
import com.example.administrator.lubanone.adapter.SplashViewPagerAdapter;
import com.example.administrator.lubanone.bean.landregister.ViewPagerResultBean;
import com.example.administrator.lubanone.bean.landregister.ViewPagerResultBean.FlashlistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.utils.DotUtil;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
    View.OnTouchListener {

  private ViewPager mViewPager;
  private Button mRegister;
  private Button mLandBtn;
  private LinearLayout mContainer;
  private long mLastTime = 0;
  private List<String> mArrayList;
  private LinearLayout mBottom;
  private RelativeLayout mRlLast;

  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<ViewPagerResultBean> result = GsonUtil
            .processJson(jsonObject, ViewPagerResultBean.class);
        updateViewPager(result);
      } catch (Exception e) {
        show(getInfo(R.string.GET_DATE_FAIL));
        if (mViewPager != null) {
          mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
          if (null != mBottom) {
            mBottom.setVisibility(View.GONE);
          }
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      show(getString(R.string.GET_DATE_FAIL));
      if (mViewPager != null) {
        mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      }
      if (null != mBottom) {
        mBottom.setVisibility(View.VISIBLE);
      }
    }
  };

  private void updateViewPager(Result<ViewPagerResultBean> result) {
    if (result == null || result.getResult() == null) {
      show(getInfo(R.string.GET_DATE_FAIL));
      mBottom.setVisibility(View.GONE);
      mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      initViewPager(result.getResult());
      return;
    }
    show(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    mBottom.setVisibility(View.GONE);
    mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
  }

  private void initViewPager(ViewPagerResultBean result) {
    if (null == result || CollectionUtils.isEmpty(result.getFlashlist())) {
      show(getString(R.string.GET_DATE_FAIL));
      mBottom.setVisibility(View.GONE);
      mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      return;
    }

    mArrayList = new ArrayList<>();
    List<FlashlistBean> flashlist = result.getFlashlist();
    if (CollectionUtils.isEmpty(flashlist)) {
      show(getString(R.string.GET_DATE_FAIL));
      mBottom.setVisibility(View.GONE);
      mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      return;
    }
    for (FlashlistBean pic : flashlist) {
      if (TextUtils.isEmpty(pic.getFlash())) {
        continue;
      }
      mArrayList.add(pic.getFlash());
    }

    if (CollectionUtils.isEmpty(mArrayList)) {
      show(getString(R.string.GET_DATE_FAIL));
      mBottom.setVisibility(View.GONE);
      mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      return;
    }

    mViewPager.setAdapter(new SplashViewPagerAdapter(mArrayList, this));

//    //初始化dot
//    initDot();
//
//    //默认第一个dot
//    mContainer.getChildAt(0).setBackgroundResource(R.drawable.circle_white_all_shape);

    // 发送延时消息
//    handler.removeMessages(0);
//    handler.sendEmptyMessageDelayed(0, 2000);
  }


  // 创建Handler消息机制,让viewpager动起来
  Handler handler = new Handler() {
    private int next;

    public void handleMessage(android.os.Message msg) {
      int currentItem = mViewPager.getCurrentItem();
      next = currentItem + 1;
      if (next > mViewPager.getAdapter().getCount() - 1) {
        next = 0;
      }
      mViewPager.setCurrentItem(next, true);

      // 重新发送延时消息,使viewpager不停的动起来
      handler.sendEmptyMessageDelayed(0, 2000);

    }
  };


  @Override
  protected void beforeSetContentView() {
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_splash;
  }


  @Override
  public void initView() {

    //是否登录过
//    boolean booleanValue = SPUtils
//        .getBooleanValue(getApplicationContext(), Config.USER_INFO, Config.IS_LANDED, false);
    String userName = SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.USER_NAME, "");
    if (!TextUtils.isEmpty(userName)) {
      startNewActivityThenFinish(this, MainActivity.class);
      return;
    }

    mBottom = (LinearLayout) findViewById(R.id.bottom);

    mViewPager = (ViewPager) findViewById(R.id.view_pager);
    mRegister = (Button) findViewById(R.id.register_btn);
    mLandBtn = (Button) findViewById(R.id.land_btn);

    mRegister.setOnClickListener(this);
    mLandBtn.setOnClickListener(this);
    mViewPager.addOnPageChangeListener(this);

    mViewPager.setCurrentItem(0);
    mContainer = (LinearLayout) findViewById(R.id.container);

    mViewPager.setOnTouchListener(this);

    mBottom.setVisibility(View.GONE);

    mRlLast = (RelativeLayout) findViewById(R.id.rl_last);
    mRlLast.setVisibility(View.GONE);
    View ivBtn = findViewById(R.id.iv_btn);
    ivBtn.setOnClickListener(this);

  }

  @Override
  public void loadData() {
    if (!NetUtil.isConnected(getApplicationContext())) {
      ToastUtil.showShort(getInfo(R.string.NET_ERROR), getApplicationContext());
      if (null != mViewPager) {
        mViewPager.setBackgroundResource(R.mipmap.view_pager_fail);
      }
      if (null != mBottom) {
        mBottom.setVisibility(View.GONE);
      }
      return;
    }

    RequestNet requestNet = new RequestNet(myApp, this, null, Urls.SPLASH_VIEW_PAGER,
        mListener,
        RequestNet.POST);

  }

  private void initDot() {
    for (int i = 0; i < mArrayList.size(); i++) {
      TextView textView = DotUtil.getOval(this, 40, R.drawable.circle_white_shape);
      mContainer.addView(textView);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView
          .getLayoutParams();
      if (i == mArrayList.size() - 1) {
        layoutParams.setMargins(0, 0, 0, 0);
      } else {
        layoutParams.setMargins(0, 0, DpUtil.dp2px(this, 10), 0);
      }
      textView.setLayoutParams(layoutParams);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.register_btn:
        //startNewActivityThenFinish(SplashActivity.this, RegisterActivity.class);
        startNewActivityThenFinish(SplashActivity.this, RegisterFourActivity.class);
        break;
      case R.id.land_btn:
        startNewActivityThenFinish(SplashActivity.this, LandActivity.class);
        break;
      case R.id.iv_btn:
        startNewActivityThenFinish(this, LandActivity.class);
        break;
    }
  }


  @Override
  public void onPageSelected(int position) {
    if (position == mArrayList.size() - 1) {
      mBottom.setVisibility(View.GONE);
      mRlLast.setVisibility(View.VISIBLE);
    } else {
      mBottom.setVisibility(View.GONE);
      mRlLast.setVisibility(View.GONE);
    }
  }

  private void updateIndicator(int position) {
    for (int i = 0; i < mContainer.getChildCount(); i++) {
      View child = mContainer.getChildAt(i);
      if (i == position) {
        child.setBackgroundResource(R.drawable.circle_white_all_shape);
      } else {
        child.setBackgroundResource(R.drawable.circle_white_shape);
      }
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
      if (System.currentTimeMillis() - mLastTime > 2000) {
        ToastUtil.showShort(getInfo(R.string.BACK_TIP), getApplicationContext());
        mLastTime = System.currentTimeMillis();
      } else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }


  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
//        handler.removeMessages(0);
        break;
      case MotionEvent.ACTION_UP:
//        handler.sendEmptyMessageDelayed(0, 1000);
        break;
    }
    return false;
  }

}
