package com.example.administrator.lubanone.activity.home;


import static com.example.administrator.lubanone.R.id.et_seed_count;
import static com.example.administrator.lubanone.R.id.tv_should_pay;
import static com.example.qlibrary.utils.StringUtil.setTextSizeNewOne;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.bean.homepage.BuySeedsResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.TimeUtil;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * 买入种子
 */
public class BuySeedsActivity extends BaseActivity implements OnFocusChangeListener {


  private RequestListener mBuySeedsListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        dismissLoadingDialog();
        Result<BuySeedsResultBean> result = GsonUtil
            .processJson(jsonObject, BuySeedsResultBean.class);
        getBuySeedsInfo(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.GET_DATE_FAIL));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissLoadingDialog();
      showMsg(getInfo(R.string.GET_DATE_FAIL));
    }
  };

  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitInfo(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.buy_seeds_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.buy_seeds_fail));
    }
  };

  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0 && TextUitl.isNotEmpty((String) msg.obj)) {
        Long aLong = DateUtil.parseDateString((String) msg.obj);
        if (null != aLong) {
          updateTimeRemaining(aLong);
        }
      }
    }
  };


  @BindView(R.id.iv_back_click)
  ImageView mIvBackClick;
  @BindView(et_seed_count)
  EditText mEtSeedCount;
  @BindView(tv_should_pay)
  TextView mTvShouldPay;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  @BindView(R.id.activity_buy_seeds)
  LinearLayout mActivityBuySeeds;
  @BindView(R.id.et_safe_code)
  EditText mEtSafeCode;//安全码
  private String mPrice;//种子价格
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.tv_cuihuaji_count)
  TextView tv_cuihuaji_count;
  @BindView(R.id.tv_sow_count)
  TextView tv_sow_count;
  private String mEndtime;
  @BindView(R.id.tv_hours)
  TextView tv_hours;
  @BindView(R.id.tv_minus)
  TextView tv_minus;
  @BindView(R.id.tv_seconds)
  TextView tv_seconds;
  @BindView(R.id.iv)
  ImageView iv;
  private String mSowtimes;
  private String mCatalyst;
  private String mLeastseeds;

  @Override
  protected void beforeSetContentView() {

  }


  @Override
  protected int getContentViewId() {
    return R.layout.activity_buy_seeds;
  }

  @Override
  public void initView() {
    //et_seed_count
    mEtSeedCount.addTextChangedListener(new MyTextWatcher());
  }


  @Override
  public void loadData() {
    try {
      judgeNet();
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams requestParamsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(requestParamsToken);
      showLoadingDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.BUY_SEEDS, mBuySeedsListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.GET_DATE_FAIL)));
      dismissLoadingDialog();
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  private void getBuySeedsInfo(Result<BuySeedsResultBean> result) {
    if (null == result || result.getResult() == null) {
      showMsg(
          getString(R.string.GET_DATE_FAIL));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePae(result);
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    }
  }

  private void updatePae(final Result<BuySeedsResultBean> result) {
    if (result.getResult() == null) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      return;
    }
    tv_sow_count.setText(result.getResult().getSowtimes());
    tv_cuihuaji_count.setText(result.getResult().getCatalyst());

//    alertDialog(result.getResult().getSowtimes(), result.getResult().getCatalyst());
//    alertDialog(result.getResult().getSowtimes(), result.getResult().getCatalyst());
    mSowtimes = result.getResult().getSowtimes();
    mCatalyst = result.getResult().getCatalyst();
    mLeastseeds = result.getResult().getLeastseeds();

    String s =
        getString(R.string.can_buy_seeds) + DebugUtils.convert(mLeastseeds, "0") + getString(
            R.string.can_buy_seeds_later);
    mEtSeedCount.setHint(s);

    mEndtime = result.getResult().getEndtime();

    if (TextUitl.isNotEmpty(mEndtime)) {

      Timer timer = new Timer();
      TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
          Message obtain = Message.obtain();
          obtain.what = 0;
          obtain.obj = mEndtime;
          mHandler.sendMessageDelayed(obtain, 0);
        }
      };

      timer.schedule(timerTask, 0, 1000 * 60);
    } else {
      setTextSizeNewOne(0 + "0\nH", tv_hours, 45, 11, 'H', 'H',
          Color.parseColor("#8286CF"));
      setTextSizeNewOne(0 + "0\nM", tv_minus, 45, 11, 'M', 'M',
          Color.parseColor("#8286CF"));
      setTextSizeNewOne(0 + "0\nS", tv_seconds, 45, 11, 'S', 'S',
          Color.parseColor("#8286CF"));
    }

    mPrice = result.getResult().getPrice();

    mTvShouldPay.setText(DebugUtils.convert(StringUtil.getThreeString(mPrice), ""));
  }

  private void alertDialog(String sowtimes, String catalyst) {

    String msg =
        getString(R.string.buy_seeds_tips_fre) + DebugUtils.convert(sowtimes, "0") + getString(
            R.string.buy_seeds_tips_mid) + "一" + getString(
            R.string.buy_seeds_tips_mid_one) + DebugUtils.convert(catalyst, "0") + getString(
            R.string.buy_seeds_tips_back);

    StytledDialog.showBuySeedDialog(this, getString(R.string.shenyu_time_tips), msg,
        getString(R.string.get_cuihuaji_method), getString(R.string.i_know), false, true,
        new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
            Intent intent = new Intent(BuySeedsActivity.this, UserDreamsActivity.class);
            intent.putExtra(Config.USER_DREAM_PACKAGE_KEY, Config.CUIHUAJI);
            startActivity(intent);
          }
        });
  }

  public void updateTimeRemaining(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {
      List<Integer> list = TimeUtil.getFormatDiffTimeStringOne(timeDiff);
      Integer minutes = list.get(1);
      Integer hours = list.get(2);
      Integer days = list.get(3);

      if (null != tv_hours && null != tv_minus && null != tv_seconds) {
        StringUtil
            .setTextSizeNewOne((days < 10 ? "0" + days : days) + "\nD", tv_hours, 45, 11, 'D', 'D',
                Color.parseColor("#8286CF"));
        StringUtil
            .setTextSizeNewOne((hours < 10 ? "0" + hours : hours) + "\nH", tv_minus, 45, 11,
                'H', 'H',
                Color.parseColor("#8286CF"));
        StringUtil
            .setTextSizeNewOne((minutes < 10 ? "0" + minutes : minutes) + "\nM", tv_seconds, 45, 11,
                'M', 'M',
                Color.parseColor("#8286CF"));
      }


    } else {
      if (null != tv_hours && null != tv_minus && null != tv_seconds) {
        setTextSizeNewOne(0 + "\nD", tv_hours, 45, 11, 'D', 'D',
            Color.parseColor("#8286CF"));
        setTextSizeNewOne(0 + "\nH", tv_minus, 45, 11, 'H', 'H',
            Color.parseColor("#8286CF"));
        setTextSizeNewOne(0 + "\nM", tv_seconds, 45, 11, 'M', 'M',
            Color.parseColor("#8286CF"));
      }
    }
  }


  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back_click, R.id.btn_commit,
      R.id.tv_back, R.id.iv})
  public void onViewClicked(View view) {
    try {

      switch (view.getId()) {
        case R.id.tv_back:
        case R.id.iv_back_click:
          finish();
          break;
        case R.id.btn_commit:
          commit();
          break;
        case R.id.iv:
          alertDialog(mSowtimes, mCatalyst);
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
  }

  private void dialogNew() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = LayoutInflater.from(this).inflate(R.layout.tousu_success_layout, null);
    TextView title = (TextView) view.findViewById(R.id.tv);
    TextView content = (TextView) view.findViewById(R.id.content);
    title.setText(getString(R.string.commits_success));
    content.setText(getString(R.string.buy_tips));

    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
        finish();
      }
    }, 3500);
  }

  private void commit() {
    judgeNet();
    final String count = mEtSeedCount.getText().toString().trim();
    final String code = mEtSafeCode.getText().toString().trim();
    final String shouldPay = mTvShouldPay.getText().toString().trim();

    isEmptyWithException(count, getInfo(R.string.seeds_count_empty));
    isFalseWithException(DemicalUtil.less(count, DebugUtils.convert(mLeastseeds, "0")),
        getString(R.string.seed_count_less_least));

    isEmptyWithException(code, getInfo(R.string.pwd_empty));
    isEmptyWithException(shouldPay, getInfo(R.string.shoule_pay_money_empty));

    StytledDialog
        .showIosAlert(false, ColorUtil.getColor(this, R.color.c333), this,
            getInfo(R.string.is_commit_one),
            getInfo(R.string.commit_seeds_count) + count + getInfo(R.string.buy_seeds_commit_info)
                + shouldPay + getInfo(R.string.buy_seeds_commit_info_two), getInfo(R.string.cancel),
            getInfo(R.string.commits), "",
            false, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                realCommit(count, shouldPay, code);
              }
            });
  }

  private void realCommit(String count, String shouldPay, String code) {
    try {
      List<RequestParams> lists = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsCount = new RequestParams("buycount", count);
      RequestParams paramsPwd = new RequestParams("paypwd", code);
      lists.add(paramsToken);
      lists.add(paramsPwd);
      lists.add(paramsCount);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, lists, Urls.BUY_SEEDS_COMMIT,
          mCommitListener, RequestNet.POST);
    } catch (Exception e) {
      hideCommitDataDialog();
      showMsg(getString(R.string.buy_seeds_fail));
    }
  }


  private void commitInfo(final Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
//      dialog();
      dialogNew();
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.buy_seeds_fail)));
    }
  }

  private void dialog() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    tv.setText(getInfo(R.string.commit_success_tips));
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        startActivity(new Intent(BuySeedsActivity.this, MainActivity.class));
      }
    }, 3500);

  }

  //种子数量et焦点监听
  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (!hasFocus && !TextUtils.isEmpty(mEtSeedCount.getText().toString().trim())) {
      if (TextUtils.isEmpty(mPrice)) {
        showMsg(getString(R.string.seed_price_empty));
        return;
      }
      BigDecimal decimal = new BigDecimal(mEtSeedCount.getText().toString().trim());
      BigDecimal price = new BigDecimal(mPrice);
      String allPrice = decimal.multiply(price).toString();
      mTvShouldPay.setText(allPrice);
    }
  }

  //监听et内容变化
  private class MyTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

      if (!TextUtils.isDigitsOnly(s)) {
        showMsg(getString(R.string.just_nuber));
        return;
      }

      if (TextUtils.isEmpty(mPrice)) {
        showMsg(getString(R.string.seed_price_empty));
        return;
      }

      if (TextUtils.isEmpty(s)) {
        return;
      }

      BigDecimal decimal = new BigDecimal(s.toString());
      BigDecimal price = new BigDecimal(mPrice);
      String allPrice = decimal.multiply(price).toString();

      if (count != before) {
        substring(allPrice);
      }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
  }

  private void substring(String allPrice) {
    String sss = "";
    String string = allPrice.replace(",", "");
    int b = string.length() / 3;
    if (string.length() >= 3) {
      int yushu = string.length() % 3;
      if (yushu == 0) {
        b = string.length() / 3 - 1;
        yushu = 3;
      }
      for (int i = 0; i < b; i++) {
        sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
        string = string.substring(3, string.length());
      }
      sss = sss + string;
      mTvShouldPay.setText(sss);
    }
  }
}
