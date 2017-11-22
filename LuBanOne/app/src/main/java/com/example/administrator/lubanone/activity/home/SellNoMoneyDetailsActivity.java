package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_upload_proof;
import static com.example.qlibrary.utils.DebugUtils.convert;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.ComplainType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.CuiType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.GPaylistBean;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.LongPayType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.StatusType;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.ViewsUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * 卖出种子 待付款
 */
public class SellNoMoneyDetailsActivity extends BaseActivity {


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.tv_order_id)
  TextView mTvOrderId;
  @BindView(R.id.tv_seed_count)
  TextView mTvSeedCount;
  @BindView(R.id.tv_order_time)
  TextView mTvOrderTime;
  @BindView(R.id.tv_seller)
  TextView mTvSeller;
  @BindView(R.id.tv_pay)
  TextView mTvPay;
  @BindView(tv_upload_proof)
  TextView mTvUploadProof;
  @BindView(R.id.tv_time)
  TextView tv_time;
  @BindView(R.id.tv_pay_long_time)
  TextView tv_pay_long_time;
  private Boolean longPay;
  private File mSaveFile;
  @BindView(R.id.tv_cui)
  TextView tv_cui;
  @BindView(R.id.detele_friend)
  TextView detele_friend;

  private static final int REQUEST_ALBUM_OK = 123;
  private String mOrderid;
  private Boolean uploadPingZheng;
  private Boolean longPayMoney;
  private Dialog mDialog;

  //投诉
  private RequestListener mComplaintListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        System.out.println("SellMoneyFragment.testSuccess=tosu=" + jsonObject);
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        comliantResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.tousu_fail));
        resetTousu(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.tousu_fail));
      resetTousu(false);
    }
  };
  private int mPosition;
  private String mPrice;
  private String mBuymember;


  private void comliantResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.tousu_success));
//      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, this));
//      mTvUploadProof.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, this));
//      mTvUploadProof.setText(getString(R.string.has_tousu));
//      mTvUploadProof.setEnabled(false);

      finish();
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.tousu_fail)));
      resetTousu(false);
    }
  }


  //催一催
  private RequestListener mCuiYiCuiListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        cuiResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.cui_fail));
        resetCuiYiCui(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.cui_fail));
      resetCuiYiCui(false);
    }
  };


  private void cuiResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.cui_success));

//      tv_cui.setTextColor(ColorUtil.getColor(R.color.white, this));
//      tv_cui.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, this));
//      tv_cui.setText(getString(R.string.has_cui));
//      tv_cui.setEnabled(false);

      tv_cui.setVisibility(View.GONE);
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.cui_fail)));
      resetCuiYiCui(false);
    }
  }


  public void resetLongPayMoney(boolean toTargetPosition) {
    if (null != longPayMoney && longPayMoney) {
      longPayMoney = false;
    }
  }


  //延长打款时间
  private RequestListener mLongPayTimeListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        longerResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.long_pay_time_fail_one));
        resetLongPayMoney(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.long_pay_time_fail_one));
      resetLongPayMoney(false);
    }
  };


  private void longerResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.long_pay_time_success));
      tv_pay_long_time.setEnabled(false);
      tv_pay_long_time.setText(getString(R.string.has_agree_long_time));
    } else {
      showMsg(convert(ResultUtil.getErrorMsg(result), getString(R.string.long_pay_time_fail_one)));
      resetLongPayMoney(false);
    }
  }


  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0) {
        Long aLong = DateUtil.parseDateString((String) msg.obj);
        if (null != aLong) {
          updateTimeRemaining(aLong);
        }
      }
    }
  };


  public void updateTimeRemaining(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {
      int seconds = (int) (timeDiff / 1000) % 60;
      int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
      int hours = (int) ((timeDiff / (1000 * 60 * 60)));

      if (null != tv_time) {
        tv_time.setText(
            (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes)
                + ":"
                + (seconds < 10 ? "0" + seconds
                : seconds));
      }
    } else {
//      if (null != tv_time) {
//        tv_time.setText("00:00:00");
//      }
//      //剩余时间为0时候不能催
//      if (null != tv_cui) {
//        tv_cui
//            .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//        tv_cui.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//        tv_cui.setEnabled(false);
//      }

      finish();
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_confrim_details_new;
  }

  @Override
  public void initView() {

    tv_pay_long_time.setOnClickListener(this);
    tv_pay_long_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    tv_pay_long_time.getPaint().setAntiAlias(true);//抗锯齿
    tv_pay_long_time.setText(getString(R.string.agreed_long_new));

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (null != item) {
        GPaylistBean itemSerializable = (GPaylistBean) item.getSerializable("item");
        mPosition = item.getInt("position");
        mPrice = item.getString("price");
        updaatePage(itemSerializable);
      }
    }
  }

  private void updaatePage(GPaylistBean itemSerializable) {
    if (null == itemSerializable) {
      return;
    }
    mOrderid = itemSerializable.getOrderid();
    mBuymember = itemSerializable.getBuymember();
    DebugUtils.setStringValue(itemSerializable.getOrderid(), "", mTvOrderId);
//    DebugUtils.setStringValue(itemSerializable.getSeedcount(), "", mTvSeedCount);
    StringUtil.setTextSmallSize(DebugUtils.convert(itemSerializable.getSeedcount(),"0")+" pcs",mTvSeedCount, 11, 9);
    DebugUtils.setStringValue(itemSerializable.getMatchtime(), "", mTvOrderTime);
    DebugUtils.setStringValue(itemSerializable.getBuymember(), "", mTvSeller);

    //我要投诉
    mTvUploadProof.setVisibility(View.GONE);
    //催一催
    tv_cui.setVisibility(View.GONE);

    //延长打款
    detele_friend.setVisibility(View.GONE);
    tv_pay_long_time.setVisibility(View.GONE);

    String multile = DemicalUtil
        .multile(convert(itemSerializable.getSeedcount(), "0"),
            convert(mPrice, "0"));

    DebugUtils.setStringValue("¥" + StringUtil.getThreeString(multile), "", mTvPay);

    //超时了
    if (StatusType.COMPLAINTED.equals(itemSerializable.getStatus())) {
      //超时
      tv_time.setText(getString(R.string.than_time));
      //我要投诉
      updateTouSu(itemSerializable.getStatus(), itemSerializable.getComplaintbutton(),
          mTvUploadProof);
    } else {
      //催一催
      updateCui(itemSerializable.getSellpressbutton(), tv_cui);

      //延长
      updateLong(itemSerializable.getProlongbutton(), tv_pay_long_time, detele_friend);

      String endtime = itemSerializable.getEndtime();
      if (TextUitl.isEmpty(endtime)) {
        tv_time.setText(getString(R.string.shengyu_time_exception));
        return;
      }

      Long aLong = DateUtil.parseDateString(endtime);
      long l = System.currentTimeMillis();

      if (aLong != null && aLong - l < 0) {
        //剩余时间
        tv_time.setText(getString(R.string.zero_time));
      } else {
        Timer timer = new Timer();
        final String finalEndtime = endtime;
        TimerTask timerTask = new TimerTask() {
          @Override
          public void run() {
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = finalEndtime;
            mHandler.sendMessageDelayed(obtain, 0);
          }
        };
        timer.schedule(timerTask, 0, 1000);
      }
    }
  }

  private void updateLong(String prolongbutton, TextView tv_pay, TextView friend) {
    if (LongPayType.GONE.equals(prolongbutton)) {
      tv_pay.setVisibility(View.GONE);
      friend.setVisibility(View.GONE);
    } else if (LongPayType.DISPLAY_CAN_CLICK.equals(prolongbutton)) {
      tv_pay.setVisibility(View.VISIBLE);
      friend.setVisibility(View.VISIBLE);
      tv_pay.setEnabled(true);
    }
  }

  private void updateCui(String sellpressbutton, TextView tv) {

    if (CuiType.GONE.equals(sellpressbutton)) {//gone
      tv.setVisibility(View.GONE);
    } else if (CuiType.DISPLAY_CAN_CLICK.equals(sellpressbutton)) {//显示可点击
      tv.setVisibility(View.VISIBLE);
      tv.setEnabled(true);
      tv.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));
    } else if (CuiType.DISPLAY_NO_CLICK.equals(sellpressbutton)) {//显示不可点击
      tv.setVisibility(View.VISIBLE);
      tv.setEnabled(false);
      //灰色背景 白色文字
      ViewsUtils.grayBgWhiteTextRound4(tv, mContext);
    }

  }

  private void updateTouSu(String status, String complaintbutton, TextView tv) {

    if (ComplainType.GONE.equals(complaintbutton)) {//隐藏
      tv.setVisibility(View.GONE);
    } else if (ComplainType.DISPLAY.equals(complaintbutton)) {//显示
      tv.setVisibility(View.VISIBLE);
      tv.setEnabled(true);
      tv.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));

    }
//    else if (ComplainType.GRAY.equals(complaintbutton)) {//灰色
//      tv.setVisibility(View.VISIBLE);
//      tv.setEnabled(false);
//      ViewsUtils.grayBgWhiteTextRound4(tv, mContext);
//    }

  }

  private void alertDialog() {

    StytledDialog
        .showOneBtn(false, ColorUtil.getColor(this, R.color.c333),this, getString(R.string.tixing_pay), getString(R.string.buy_seeds_list_tips),
            getString(R.string.close), null, null, false, true, new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onThird(DialogInterface dialog) {
                super.onThird(dialog);
                dialog.dismiss();
              }
            });


  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.tv_pay_long_time) {

      mDialog = StytledDialog
          .showOneBtn(false, ColorUtil.getColor(this, R.color.c333),this, getString(R.string.agreed_long_new), getString(R.string.new_tips),
              getString(R.string.cancels), getString(R.string.confirm), null, false, true,
              new MyDialogListener() {
                @Override
                public void onFirst(DialogInterface dialog) {
                  dialog.dismiss();
                }

                @Override
                public void onSecond(DialogInterface dialog) {
                  longerPayTime();
                }

                @Override
                public void onThird(DialogInterface dialog) {
                  super.onThird(dialog);
                  dialog.dismiss();
                }
              });

    }

  }


  //延长打款时间
  private void longerPayTime() {

    try {
      longPayMoney = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          convert(mOrderid, ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.AGREE_LONGER_PAY_TIME, mLongPayTimeListener,
          RequestNet.POST);
    } catch (Exception e) {
      resetLongPayMoney(false);
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.longger_timing);
  }


  public void resetLong(boolean toTargetPositinon) {
    if (longPay != null && longPay) {
      longPay = false;
    }
  }


  @OnClick({R.id.back, tv_upload_proof, R.id.tv_seller, R.id.tv_cui})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case tv_upload_proof://=我要投诉
        if (mTvUploadProof.isEnabled()) {
          alertTouSuDialog();
        } else {
          showMsg(getString(R.string.upload_tips));
        }
        break;
      case R.id.tv_seller:
        Intent intent = new Intent(this, BuySeedsMemberInfoActivity.class);
        intent.putExtra("userId", convert(mBuymember, ""));
        startActivity(intent);
        break;
      case R.id.tv_cui://催一催
        cui();
        break;
    }
  }

  private void alertTouSuDialog() {

    StytledDialog.showIosAlert(this, getString(R.string.complain_title_text),
        getString(R.string.complaint_dialog_tip), getString(R.string.commits),
        getString(R.string.confirm), null, true, true, new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
            uploadPic();
          }
        });

  }


  @Override
  public void finish() {
    Intent intent = new Intent();
    intent.putExtra("position", mPosition);
    setResult(RESULT_OK, intent);
    super.finish();
  }

  private Boolean cuiYiCui;


  public void resetCuiYiCui(boolean toTargetPosition) {
    if (null != cuiYiCui && cuiYiCui) {
      cuiYiCui = false;
    }

  }

  private void cui() {
    try {
      cuiYiCui = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderid = new RequestParams("orderid", mOrderid);
      list.add(paramsToken);
      list.add(paramsOrderid);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.SELL_CUI, mCuiYiCuiListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.cui_fail));
      resetCuiYiCui(false);
    }
  }


  private Boolean tousu;

  public void resetTousu(boolean toTargetPosition) {
    if (tousu != null && tousu) {
      tousu = false;
    }
  }

  private void uploadPic() {
    try {
      tousu = true;

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          convert(mOrderid, ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.COMPLAINT, mComplaintListener,
          RequestNet.POST);
    } catch (Exception e) {
      resetTousu(false);
      showMsg(getString(R.string.tousu_fail));
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);


  }

  public void resetUpload(boolean toTargetPosion) {
    if (null != uploadPingZheng && uploadPingZheng) {
      uploadPingZheng = false;
    }
  }


  private void uploadResult(Result<Object> result) {
    dismissUploadingDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.upload_proof_success));
      mTvUploadProof
          .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
      mTvUploadProof.setEnabled(false);
    } else {
      showMsg(
          convert(ResultUtil.getErrorMsg(result), getString(R.string.upload_proof_fail)));
      resetUpload(false);
    }
  }
}
