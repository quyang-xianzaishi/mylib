package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_upload_proof;
import static com.example.qlibrary.utils.DebugUtils.convert;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.message.CustomMessageActivity;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.ComplaintType;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.GConfirmlistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.ViewsUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.interfaces.OnClickListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.WindoswUtil;
import io.rong.imlib.model.CSCustomServiceInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * 卖出种子 待确认
 */
public class SellConfirmDetailsActivity extends BaseActivity {


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
  private Boolean longPay;
  @BindView(R.id.tv_cui)
  TextView tv_cui;

  private static final int REQUEST_ALBUM_OK = 123;
  private String mOrderid;
  private int mPosition;
  @BindView(R.id.tips)
  TextView tips;
  @BindView(R.id.tv_time_tip)
  TextView tv_time_tip;


  //确认收款和投诉按钮
  private RequestListener mComfimListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        dealResult(result);
      } catch (Exception e) {
        showMsg(getString(R.string.tousu_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.tousu_fail));
    }
  };
  private String mPrice;
  private String mBuymember;

  private void dealResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      if (type == 1) {//确认收款
        showMsg(getString(R.string.receive_money_success));
        ViewsUtils.setTextView(tv_cui, getString(R.string.confrim_receive_money),
            getApplicationContext());
        finish();
      } else if (type == 2) {
        showMsg(getString(R.string.complaint_succeess));
        ViewsUtils.setTextView(mTvUploadProof, getString(R.string.has_complainted),
            getApplicationContext());
        mTvUploadProof.setVisibility(View.GONE);

        dialog();

      }
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result),
          type == 1 ? getString(R.string.receive_money_success)
              : getString(R.string.complaint_succeess)));
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
//        tv_time.setText(getString(R.string.than_time));
////        tv_time.setText("00:00:00");
//      }

//      mTvUploadProof.setEnabled(false);
//      tv_cui.setEnabled(false);
//
//      mTvUploadProof.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, this));
//      tv_cui.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, this));
//
//      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, this));
//      tv_cui.setTextColor(ColorUtil.getColor(R.color.white, this));

      finish();
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_confrim_details_new1;
  }

  @Override
  public void initView() {

    mTvUploadProof.setVisibility(View.GONE);
    tv_cui.setVisibility(View.GONE);

    String string = getString(R.string.conplaint_proof_tips_new);
    StringUtil.setPartColorAndClickable(string, tips, string.length() - 5, string.length() - 1,
        Color.parseColor("#202364"), new OnClickListener() {
          @Override
          public void onClick() {
            CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
            CSCustomServiceInfo csInfo = csBuilder.nickName(getString(R.string.rongyun_nickname))
                .build();
            Intent intent4 = new Intent();
            intent4.putExtra("customServiceInfo", csInfo);
            intent4.setClass(SellConfirmDetailsActivity.this, CustomMessageActivity.class);
            startActivity(intent4);
          }
        });

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (null != item) {
        GConfirmlistBean itemSerializable = (GConfirmlistBean) item.getSerializable("item");
        mPosition = item.getInt("position");
        mPrice = item.getString("price");
        updaatePage(itemSerializable);
      }
    }
  }

  private void updaatePage(GConfirmlistBean itemSerializable) {
    if (null == itemSerializable) {
      return;
    }
    mOrderid = itemSerializable.getOrderid();
    mBuymember = itemSerializable.getBuymember();
    DebugUtils.setStringValue(itemSerializable.getOrderid(), "", mTvOrderId);
//    DebugUtils.setStringValue(itemSerializable.getSeedcount(), "", mTvSeedCount);
    StringUtil.setTextSmallSize(DebugUtils.convert(itemSerializable.getSeedcount(), "0") + " pcs",
        mTvSeedCount, 11, 9);
    DebugUtils.setStringValue(itemSerializable.getPaytime(), "", mTvOrderTime);
    DebugUtils.setStringValue(itemSerializable.getBuymember(), "", mTvSeller);

    //隐藏未收到款，我要投诉,投诉的时候gone催确认按钮，显示我要投诉
    if (ComplaintType.GRAY.equals(itemSerializable.getComplaintbutton())) {
      tv_time_tip.setText(getString(R.string.shenyu_time_new));
      //我要投诉
      updateTouSu(itemSerializable.getComplaintbutton(), mTvUploadProof);
    } else if (ComplaintType.NORMAL.equals(itemSerializable.getComplaintbutton())) {
      tv_time_tip.setText(getString(R.string.shenyu_time));
      //确认收款
      updateCui(tv_cui);
      //我要投诉
      mTvUploadProof.setVisibility(View.VISIBLE);
      mTvUploadProof.setEnabled(true);
    }

    String multile = DemicalUtil
        .multile(convert(itemSerializable.getSeedcount(), "0"),
            convert(mPrice, "0"));

    DebugUtils.setStringValue("¥" + StringUtil.getThreeString(multile), "", mTvPay);

    String endtime = itemSerializable.getEndtime();

    //投诉 过了
    if (ComplaintType.GRAY.equals(itemSerializable.getComplaintbutton())) {
      endtime = itemSerializable.getDealendtime();
    } else {
      endtime = itemSerializable.getEndtime();
    }

    if (TextUitl.isEmpty(endtime)) {
      return;
    }

    Long aLong = DateUtil.parseDateString(endtime);
    long l = System.currentTimeMillis();

    if (aLong != null && aLong - l > 0 && aLong - l < 3600 * 1000) {
    } else {
    }

    if (aLong != null && aLong - l < 0) {
      tv_time.setText(getString(R.string.than_time));
    } else {
      if (TextUitl.isNotEmpty(endtime)) {
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
      } else {
        tv_time.setText(getString(R.string.shengyu_time_exception));
      }
    }
  }

  private void updateCui(TextView tv) {
    tv.setVisibility(View.VISIBLE);
    tv.setEnabled(true);
    tv.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));
  }

  private void updateTouSu(String complaintbutton, TextView tv) {
    if (ComplaintType.NORMAL.equals(complaintbutton)) {//显示
      tv.setVisibility(View.VISIBLE);
      tv.setEnabled(true);
      tv.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));

    } else if (ComplaintType.GRAY.equals(complaintbutton)) {//灰色
      tv.setVisibility(View.VISIBLE);
      tv.setEnabled(false);
      tv.setText(getString(R.string.has_complainted));

      ViewsUtils.grayBgWhiteTextRound4(tv, mContext);

      tv.setVisibility(View.GONE);
    }

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.back, tv_upload_proof, R.id.tv_seller, R.id.tv_cui})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case tv_upload_proof://=我要投诉
        alertDialog();
        break;
      case R.id.tv_seller:
        Intent intent = new Intent(this, BuySeedsMemberInfoActivity.class);
        intent.putExtra("userId", convert(mBuymember, ""));
        startActivity(intent);
        break;
      case R.id.tv_cui://确认收款
        alertConfimDialog();
        break;
    }
  }

  private void dialog() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = LayoutInflater.from(this).inflate(R.layout.tousu_success_layout, null);
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
      }
    }, 3500);
  }

  private void alertDialog() {

    StytledDialog
        .showAlert(this, getString(R.string.no_receiver_money),
            getString(R.string.complaint_dialog_tip),
            getString(R.string.cancels), getString(R.string.confirm),
            null, true, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                affirmReceiveMoney(2);
              }
            }, false);
  }


  private void alertConfimDialog() {
    StytledDialog
        .showAlert(this, null, getString(R.string.confirm_receive_money),
            getString(R.string.cancel), getString(R.string.confirm),
            null, true, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                affirmReceiveMoney(1);
              }
            }, true);
  }

  private int type;

  //确认收款 按钮 1   投诉2
  private void affirmReceiveMoney(int type) {
    try {

      this.type = type;
      //confirm 1为确认收款,2为投诉
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsType = new RequestParams("confirm", type + "");
      RequestParams paramsOrderId = new RequestParams("orderid", mOrderid);
      list.add(paramsToken);
      list.add(paramsType);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.SELL_COMFIRM_MONEY, mComfimListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.tousu_fail));
    }
  }


  @Override
  public void finish() {
    Intent intent = new Intent();
    intent.putExtra("position", mPosition);
    setResult(RESULT_OK, intent);
    super.finish();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);


  }

}
