package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.iarcuschin.simpleratingbar.SimpleRatingBar.OnRatingBarChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class PingLunActivity extends BaseActivity {

  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitResult(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.ping_jia_fail_one));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.ping_jia_fail_one));
    }
  };


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.frameLayout)
  FrameLayout mFrameLayout;
  @BindView(R.id.rating_bar)
  SimpleRatingBar mRatingBar;
  @BindView(R.id.activity_ping_lun)
  LinearLayout mActivityPingLun;
  @BindView(R.id.et_ping_lun)
  EditText mEtPingLun;
  @BindView(R.id.btn)
  Button mBtn;
  @BindView(R.id.tv_back)
  TextView tvBack;
  @BindView(R.id.tv_good)
  TextView tv_good;
  @BindView(R.id.tv_tips)
  TextView tv_tips;


  private String rating;
  private String mOrderId;
  private String mType;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_ping_lun;
  }

  @Override
  public void initView() {

    mEtPingLun.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          mEtPingLun.setHint("");
          mEtPingLun.setGravity(Gravity.START);
        }
      }
    });

    mRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
      @Override
      public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
        PingLunActivity.this.rating = rating + "";
        tv_good.setText(getPingJia(PingLunActivity.this.rating));
      }
    });

    Intent intent = getIntent();
    if (null != intent && null != intent.getBundleExtra("bundle")) {
      Bundle bundle = intent.getBundleExtra("bundle");
      mOrderId = bundle.getString("orderId");
      mType = bundle.getString("type");

      System.out.println("PingLunActivity.commit,orderid 1="+mOrderId);
      System.out.println("PingLunActivity.commit,type 1="+mType);

      if ("buy".equals(mType)) {
        tv_tips.setText(getString(R.string.give_stars_buy));
      }
    }
  }

  @Override
  public void loadData() {
  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.rating_bar, R.id.btn, R.id.tv_back})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.btn:
          commit();
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private String getPingJia(String rating) {
    if ("1.0".equals(rating)) {
      return getString(R.string.very_cha);
    }
    if ("2.0".equals(rating)) {
      return getString(R.string.cha);
    }
    if ("3.0".equals(rating)) {
      return getString(R.string.normal_cha);
    }
    if ("4.0".equals(rating)) {
      return getString(R.string.good);
    } else {
      return getString(R.string.very_good);
    }
  }

  private void commit() {
    judgeNet();

    if (TextUtils.isEmpty(rating)) {
      throw new DefineException(getInfo(R.string.rating_or_pinglung_empty));
    }

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsOrderId = new RequestParams("orderid", mOrderId);
    RequestParams paramsEvaluate = new RequestParams("evaluate", rating.charAt(0) + "");
    RequestParams paramsEvaluatetext = new RequestParams("evaluatetext",
        mEtPingLun.getText().toString().trim());
    list.add(paramsToken);
    list.add(paramsOrderId);
    list.add(paramsEvaluate);
    list.add(paramsEvaluatetext);

    System.out.println("PingLunActivity.commit,token=" + SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    System.out.println("PingLunActivity.commit,orderid="+mOrderId);
    System.out.println("PingLunActivity.commit,evaluate="+rating.charAt(0) + "");
    System.out.println("PingLunActivity.commit,evaluatetext="+mEtPingLun.getText().toString().trim());
    showCommitDataDialog();
    String url = getUrl();
    RequestNet requestNet = new RequestNet(myApp, this, list,
        url, mListener, RequestNet.POST);
  }

  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  public String getUrl() {
    if ("buy".equals(mType)) {
      return Urls.BUY_PINGJIA;
    }
    if ("sell".equals(mType)) {
      return Urls.SELL_PINGJIA_INTO;
    }
    return "";
  }


  private void commitResult(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showSuccessDialog();
    } else {
      showMsg(
          DebugUtils
              .convert(ResultUtil.getErrorMsg(result), getString(R.string.ping_jia_fail) + "ddd"));
    }
  }


  private void showSuccessDialog() {

    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    tv.setText(getInfo(R.string.pingjia_success));
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        finish();
      }
    }, 2000);

  }
}
