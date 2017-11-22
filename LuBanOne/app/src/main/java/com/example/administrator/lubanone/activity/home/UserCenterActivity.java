package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.bean.homepage.UserCenterResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.image.glide.GlideManager;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * 个人中心
 */
public class UserCenterActivity extends BaseActivity {

  private TextView mIntroduce;
  private ImageView mIcon;


  private TextView mTvLevel;
  private TextView mTvCredit;
  private TextView mTvGrade;
  private TextView mNmae;
  private String mPeixunfen;
  private String mPingjia;
  private String mIcon_url;
  private String mCodeSize;
  private Intent mIntent;


  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
    }
  };

  //个人中心
  RequestListener mRequestListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<UserCenterResultBean> result = GsonUtil
            .processJson(jsonObject, UserCenterResultBean.class);
        updataPage(result);
      } catch (Exception e) {
        show(getInfo(R.string.request_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      show(errorMsf);
    }
  };
  private View mRlVipLevel;
  private TextView mTvCuihuaji;
  private TextView mTvDreamPackage;
  private TextView mTvMoneyPackage;
  private TextView mTvActiveCode;

  private void updataPage(Result<UserCenterResultBean> result) {
    if (null == result) {
      show(getInfo(R.string.request_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      adapterData(result);
    } else {
      show(ResultUtil.getErrorMsg(result));
    }
  }

  private void adapterData(Result<UserCenterResultBean> result) {

    //会员积分
    mPeixunfen = result.getResult().getPeixunfen();
    //信用评分
    mPingjia = result.getResult().getPingjia();

    //催化剂
    String catalyst = result.getResult().getCatalyst();
    //梦想背包
    String dreampackage = result.getResult().getDreampackage();
    //奖金背包
    String moneypakage = result.getResult().getRewardpackage();
    //激活码
    String activecode = result.getResult().getActivecode();

    mNmae.setText(result.getResult().getUe_truename());
    mIntroduce.setText(result.getResult().getAutograph());
    if (!TextUitl.isEmpty(result.getResult().getLevelname())) {
      mTvLevel.setVisibility(View.VISIBLE);
      mTvLevel.setText(StringUtil.getBufferString(
          DebugUtils.convert(result.getResult().getLevelname(), "")));
    }

    mTvCredit.setText(StringUtil
        .getBufferString(DebugUtils.convert(mPeixunfen, "0") + getString(R.string.feng)));
    mTvGrade.setText(StringUtil
        .getBufferString(DebugUtils.convert(mPingjia, "0") + getString(R.string.feng)));
    mTvCuihuaji.setText(StringUtil
        .getBufferString(DebugUtils.convert(catalyst, "0") + getString(R.string.task_list_item_reward2)));

    mTvDreamPackage.setText(StringUtil
        .getBufferString(DebugUtils.convert(dreampackage, "0") + getString(R.string.ke)));

    mTvMoneyPackage.setText(StringUtil
        .getBufferString(DebugUtils.convert(moneypakage, "0") + getString(R.string.ke)));

    mTvActiveCode.setText(StringUtil
        .getBufferString(DebugUtils.convert(activecode, "0") + getString(R.string.ge)));
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_center;
  }

  @Override
  public void finish() {
    boolean b = myApp.hasActivity(MainActivity.class);
    if (!b) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
    }
    super.finish();
  }

  @Override
  public void initView() {
    mIcon = (ImageView) findViewById(R.id.icon);

    mRlVipLevel = findViewById(R.id.rl_vip_level);
    mRlVipLevel.setOnClickListener(this);

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    tvBack.setOnClickListener(this);

    ImageView tvEdit = (ImageView) findViewById(R.id.tv_input_edit);
    tvEdit.setOnClickListener(new MyOnClickListener(5));
    mIcon.setOnClickListener(new MyOnClickListener(5));

    //会员名称
    mNmae = (TextView) findViewById(R.id.tv_user_name);

    //个人说明
    mIntroduce = (TextView) findViewById(R.id.self_introduce);
    mIntroduce.setText("");

    //会员级别
    mTvLevel = (TextView) findViewById(R.id.tv_level);
    mTvLevel.setVisibility(View.GONE);
    mTvLevel.setOnClickListener(this);

    //培训积分
    View rlTwo = findViewById(R.id.rl_two);
    mTvCredit = (TextView) findViewById(R.id.tv_train_credits);
    mTvCredit.setOnClickListener(new MyOnClickListener(1));
    rlTwo.setOnClickListener(new MyOnClickListener(1));
    findViewById(R.id.iv_next_page_grade_two).setOnClickListener(new MyOnClickListener(1));

    //信用评分
    mTvGrade = (TextView) findViewById(R.id.tv_xiong_credits);
    mTvGrade.setOnClickListener(new MyOnClickListener(6));
    findViewById(R.id.iv_next_page_grade_three).setOnClickListener(new MyOnClickListener(6));
    findViewById(R.id.rl_three)
        .setOnClickListener(new MyOnClickListener(6));

    //催化剂
    findViewById(R.id.iv_next_page_grade_four).setOnClickListener(new MyOnClickListener(0));
    findViewById(R.id.rl_four).setOnClickListener(new MyOnClickListener(0));
    mTvCuihuaji = (TextView) findViewById(R.id.tv_cuihuaji);

    //梦想背包
    findViewById(R.id.iv_next_page_grade_five).setOnClickListener(new MyOnClickListener(2));
    findViewById(R.id.rl_five).setOnClickListener(new MyOnClickListener(2));
    mTvDreamPackage = (TextView) findViewById(R.id.tv_dream_package);

    //奖金背包
    findViewById(R.id.rl_six).setOnClickListener(new MyOnClickListener(3));
    findViewById(R.id.iv_next_page_grade_six).setOnClickListener(new MyOnClickListener(3));
    mTvMoneyPackage = (TextView) findViewById(R.id.tv_money_package);

    //激活码
    findViewById(R.id.iv_next_page_grade_seven).setOnClickListener(new MyOnClickListener(4));
    findViewById(R.id.rl_seven).setOnClickListener(new MyOnClickListener(4));
    mTvActiveCode = (TextView) findViewById(R.id.tv_active_code);

    mIntent = getIntent();
    if (null != mIntent) {
      mCodeSize = mIntent.getStringExtra("code_size");
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    try {
      if (null != mIntent && "register".equals(mIntent.getStringExtra("register"))) {
        if (TextUitl.isNotEmpty(mIntent.getStringExtra("register"))) {
          mIcon.setBackgroundResource(R.mipmap.head_2x);
        }
      } else {
        String path = SPUtils
            .getStringValue(this, Config.USER_INFO, Config.HEAD_ICON_PATH, null);
        if (TextUtils.isEmpty(path)) {
          mIcon.setBackgroundResource(R.mipmap.head_2x);
        } else {
          GlideManager.glideWithRound(this, path, mIcon, 50);
        }
      }

    } catch (Exception e) {
      mIcon.setBackgroundResource(R.mipmap.head_2x);
    }
  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet lRequestNet = new RequestNet(myApp, this, list, Urls.USER_CENTER, mRequestListener,
          RequestNet.POST);
    } catch (DefineException e) {
      show(e.getMessage());
    }
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.tv_back:
          finish();
          break;
        case R.id.tv_level:
        case R.id.rl_vip_level:
          Intent intent = new Intent(this, UserLevelActivity.class);
          intent.putExtra("code_size", DebugUtils.convert(mCodeSize, ""));
          startActivity(intent);
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private class MyOnClickListener implements View.OnClickListener {

    private Integer tag;

    public MyOnClickListener(int i) {
      this.tag = i;
    }

    @Override
    public void onClick(View v) {
      Intent intent = null;
      switch (tag) {
        case 0://梦想催化剂
          intent = new Intent(UserCenterActivity.this, UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.CUIHUAJI);
          break;
        case 1://培训积分
          intent = new Intent(UserCenterActivity.this, TrainCreditActivity.class);
          intent.putExtra(Config.TRAIN_CREDIT_KEY, DebugUtils.convert(mPeixunfen, ""));
          break;
        case 2://梦想背包
          intent = new Intent(UserCenterActivity.this, UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.DREAM_PACKAGE);
          break;
        case 3://奖金背包
          intent = new Intent(UserCenterActivity.this, UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.MONEY_PACKAGE);
          break;
        case 4://激活码
          intent = new Intent(UserCenterActivity.this, UserDreamsActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.ACTIVE_CODE);
          break;
        case 5://点击编辑
          intent = new Intent(UserCenterActivity.this, AccountCenterActivity.class);
          intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.BIANJI);
          intent.putExtra("icon", DebugUtils.convert(mIcon_url, null));
          break;
        case 6://信用评分
          intent = new Intent(UserCenterActivity.this, CreditGradeActivity.class);
          intent.putExtra("credit", DebugUtils.convert(mPingjia, ""));
          break;
        default:
          break;
      }
      if (null != intent) {
        startActivity(intent);
      }
    }
  }

}
