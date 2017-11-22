package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * 验证码删除card
 */
public class CheckCodeActivity extends BaseActivity {


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.tv_phone)
  TextView mTvPhone;
  @BindView(R.id.tv_indicator)
  TextView mTvIndicator;
  @BindView(R.id.tv_code)
  EditText mTvCode;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  private String mBankid;

  private int seconds;
  private String mPhone;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_check_code;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (null != intent) {
      mBankid = intent.getStringExtra("bankid");
      mPhone = intent.getStringExtra("phone");
      if (TextUitl.isNotEmpty(mPhone)) {
        mTvPhone.setText(mPhone);
      }
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
//    if (null != mHandler) {
//      mHandler.removeCallbacksAndMessages(null);
//    }
  }

  @OnClick({R.id.back, R.id.tv_indicator, R.id.btn_commit})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case R.id.tv_indicator:
        getSMS();
        break;
      case R.id.btn_commit:
        deleteCard();
        break;
    }
  }

  private void getSMS() {
    try {
      if (!NetUtil.isConnected(this)) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }

      seconds = Config.UPDATE_LAND_PWD_LIMIT_SECONDS;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SMS_BY_TOKNE, mListenr,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_check_code_fail));
    }
  }


  //获取短信
  RequestListener mListenr = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        getSMEByToken(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.get_check_code_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getInfo(R.string.get_check_code_fail));
    }
  };


  private void getSMEByToken(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.get_check_code_success));
      mTvIndicator.setEnabled(false);
      Drawable drawable = DrableUtil.getDrawable(this, R.drawable.get_check_code_shape_no_select);
      ViewUtil.setBackground(mTvIndicator, drawable);
      mTvIndicator.setTextColor(ColorUtil.getColor(R.color.cb6b6b6, this));
      limitSeconds();
    } else {
      showMsg(
          DebugUtils
              .convert(ResultUtil.getErrorMsg(result), getString(R.string.get_check_code_fail)));
    }
  }

  private void limitSeconds() {

    CountDownTimer one = new CountDownTimer(62 * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {

        if (seconds == 0) {
          mTvIndicator.setText(getString(R.string.reget_check_code));
          mTvIndicator.setEnabled(true);
          Drawable drawable = DrableUtil
              .getDrawable(getApplicationContext(), R.drawable.get_check_code_shape);
          ViewUtil.setBackground(mTvIndicator, drawable);
          mTvIndicator
              .setTextColor(ColorUtil.getColor(R.color.cEA5412, getApplicationContext()));
//          mHandler.removeCallbacksAndMessages(null);
          return;
        }
        if (null != mTvIndicator && seconds >= 0) {
          mTvIndicator.setText(seconds + getString(R.string.some_seconds));
        }
        seconds--;
      }

      @Override
      public void onFinish() {

      }
    };
    one.start();
  }


  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (seconds == 0) {
        mTvIndicator.setText(getString(R.string.reget_check_code));
        mTvIndicator.setEnabled(true);
        Drawable drawable = DrableUtil
            .getDrawable(getApplicationContext(), R.drawable.get_check_code_shape);
        ViewUtil.setBackground(mTvIndicator, drawable);
        mTvIndicator.setTextColor(ColorUtil.getColor(R.color.cEA5412, getApplicationContext()));
//        mHandler.removeCallbacksAndMessages(null);
        return;
      }
      if (null != mTvIndicator) {
        mTvIndicator.setText(seconds + getString(R.string.some_seconds));
      }
      seconds--;
    }
  };

  private void deleteCard() {
    try {

      if (!NetUtil.isConnected(getApplicationContext())) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }

      String trim = mTvCode.getText().toString().trim();
      if (TextUitl.isEmpty(trim)) {
        showMsg(getString(R.string.check_code_empty));
        return;
      }

      if (!StringUtil.justNumber(trim)) {
        showMsg(getString(R.string.check_code_only_number));
        return;
      }

      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams requestParamsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));

      RequestParams requestParamsBankId = new RequestParams("bankid",
          DebugUtils.convert(mBankid, ""));
      RequestParams requestParamsCode = new RequestParams("phonecode",
          DebugUtils.convert(trim, ""));

      list.add(requestParamsToken);
      list.add(requestParamsBankId);
      list.add(requestParamsCode);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.DELETE_CARD,
          deleteListListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.delete_card_fail));
      hideCommitDataDialog();
    }

  }


  @Override
  public String setTip() {
    return getString(R.string.deleting);
  }

  private RequestListener deleteListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<Object> result = GsonUtil
            .processJson(jsonObject, Object.class);
        deleteResult(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.delete_card_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.delete_card_fail));
      hideCommitDataDialog();
    }
  };

  private void deleteResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      alertDialog();
      EventBus.getDefault().post(BankCardListActivity.class);
    } else {
      showMsg(DebugUtils.convert(result.getMsg(), getString(R.string.delete_card_fail)));
    }
  }

  private void alertDialog() {

    final Dialog dialog = StytledDialog
        .showDeleteCardDialog(this, true, true, getString(R.string.delete_success));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
      }
    }, 2000);

  }
}
