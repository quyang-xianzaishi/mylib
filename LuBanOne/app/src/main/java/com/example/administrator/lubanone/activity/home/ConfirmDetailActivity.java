package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_time;
import static com.example.administrator.lubanone.R.id.tv_upload_proof;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.example.administrator.lubanone.activity.message.CameraActivity;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.CuiType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.PConfirmlistBean;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.ProofType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.StatusType;
import com.example.administrator.lubanone.bean.homepage.UploadVideo;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.FileUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * 买入种子 待确认 details
 */
public class ConfirmDetailActivity extends BaseActivity {


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(tv_time)
  TextView mTvTime;
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
  @BindView(R.id.tv_upload_proof_new)
  TextView tv_upload_proof_new;//上传视频凭证
  @BindView(R.id.tv_time_tip)
  TextView tv_time_tip;
  private String mOrderid;
  private Boolean cuiConfim;
  private static final int VIDEO_CODE = 33;


  //崔确认
  private RequestListener mCuiComfrimListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        hideCommitDataDialog();
        if (ResultUtil.isSuccess(result)) {
          showMsg(getString(R.string.cui_comfrim_success));

          //灰色
//          mTvUploadProof
//              .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//          mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//          mTvUploadProof.setEnabled(false);
//
          mTvUploadProof.setVisibility(View.GONE);


        } else {
          showMsg(DebugUtils
              .convert(ResultUtil.getErrorMsg(result), getString(R.string.cui_comfrim_fail)));
          resetCui(false);
        }
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getString(R.string.cui_comfrim_fail));
        resetCui(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.cui_comfrim_fail));
    }
  };


  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 0) {
        Long aLong = DateUtil.parseDateString((String) msg.obj);
        if (null != aLong) {
          updateTimeRemaining(aLong);
        }
      }

      if (msg.what == 1) {
        Long aLong = DateUtil.parseDateString((String) msg.obj);
        if (null != aLong) {
          updateTimeRemainingNew(aLong);
        }
      }
    }
  };
  private int mPosition;
  private String mPrice;
  private String mSellmember;
  private static final int CAPTURE_VIDEO = 34;


  public void updateTimeRemaining(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {
      int seconds = (int) (timeDiff / 1000) % 60;
      int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
      int hours = (int) ((timeDiff / (1000 * 60 * 60)));

      if (null != mTvTime) {

        mTvTime.setText(
            (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes)
                + ":"
                + (seconds < 10 ? "0" + seconds
                : seconds));
      }
    } else {
      if (null != mTvTime) {
        mTvTime.setText(getString(R.string.zero_time));
      }
      //催确认不可点 gone  超时
      if (mTvUploadProof != null) {
//        mTvUploadProof
//            .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//        mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//        mTvUploadProof.setEnabled(false);

        mTvUploadProof.setVisibility(View.GONE);
        finish();
      }
    }
  }

  public void updateTimeRemainingNew(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {
      int seconds = (int) (timeDiff / 1000) % 60;
      int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
      int hours = (int) ((timeDiff / (1000 * 60 * 60)));

      if (null != mTvTime) {

        mTvTime.setText(
            (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes)
                + ":"
                + (seconds < 10 ? "0" + seconds
                : seconds));
      }
    } else {
      if (null != mTvTime) {
        mTvTime.setText(getString(R.string.than_time));
      }
      //催确认不可点 gone  超时
      if (mTvUploadProof != null) {
//        mTvUploadProof
//            .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//        mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//        mTvUploadProof.setEnabled(false);

        mTvUploadProof.setVisibility(View.GONE);
//        finish();
      }
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  private void alertSelectVideoDialog() {

    ArrayList<String> objects = new ArrayList<>();
    objects.add(getString(R.string.upload_video));
    objects.add(getString(R.string.select_video_resposity));
    objects.add(getString(R.string.record_video));

    StytledDialog.showBottomItemVideoDialog(this, objects, getString(R.string.cancels), true, true,
        new MyItemDialogListener() {
          @Override
          public void onItemClick(String text, int position) {
            selectVideo(position);
          }
        });

  }


  private void selectVideo(int position) {
    if (1 == position) {
      uploadVedio();
    }
    if (2 == position) {
      Intent intent = new Intent(this, CameraActivity.class);
      startActivityForResult(intent, CAPTURE_VIDEO);
    }
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_confirm_detail;
  }

  @Override
  public void initView() {

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (item != null) {
        PConfirmlistBean itemSerializable = (PConfirmlistBean) item.getSerializable("item");
        mPosition = item.getInt("position");
        mPrice = item.getString("price");
        updatePage(itemSerializable);
      }
    }

  }

  private void updatePage(PConfirmlistBean itemSerializable) {
    if (null == itemSerializable) {
      return;
    }

    mOrderid = itemSerializable.getOrderid();
    String seedcount = itemSerializable.getSeedcount();
    String price = DebugUtils.convert(mPrice, "0");
    mSellmember = itemSerializable.getSellmember();
    String paytime = itemSerializable.getPaytime();

    //订单编号
    DebugUtils.setStringValue(mOrderid, "", mTvOrderId);
    //种子数量
//    DebugUtils.setStringValue(seedcount, "", mTvSeedCount);

    StringUtil.setTextSmallSize(DebugUtils.convert(itemSerializable.getSeedcount(), "0") + " pcs",
        mTvSeedCount, 11, 9);
    //付款时间
    DebugUtils.setStringValue(paytime, "", mTvOrderTime);
    //卖方会员
    DebugUtils.setStringValue(mSellmember, "", mTvSeller);
    //实付款
    DebugUtils.setStringValue("¥" + StringUtil.getThreeString(
        DemicalUtil.multile(DebugUtils.convert(price, "0"), DebugUtils.convert(seedcount, "0"))),
        "", mTvPay);

    //是否显示催确认or上传视频凭证按钮
    selectOperation(itemSerializable);

    //催确认or上传凭证状态
    changeStatusText(itemSerializable);
  }

  private void changeStatusText(PConfirmlistBean itemSerializable) {
    if (StatusType.NO_COMPLAIN.equals(itemSerializable.getStatus())) {//没有被投诉

      //修改催确认状态
      updatePressButton(itemSerializable.getBuypressbutton());

      String endtime = itemSerializable.getEndtime();
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
        mTvTime.setText(getString(R.string.tousu_time_exception));
      }
    }

    if (StatusType.COMPLAINTED.equals(itemSerializable.getStatus())) {//被投诉

      //投诉提醒
      alertDialog();

      //修改上传视频凭证按钮状态
      updatePic(itemSerializable.getShowpic2button());

      //只要被投诉就倒计时
      //剩余处理时间
      tv_time_tip.setText(getString(R.string.shengyu_time_proof_one));

      String endtime = itemSerializable.getDealendtime();
      if (TextUitl.isNotEmpty(endtime)) {
        Timer timer = new Timer();
        final String finalEndtime = endtime;
        TimerTask timerTask = new TimerTask() {
          @Override
          public void run() {
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = finalEndtime;
            mHandler.sendMessageDelayed(obtain, 0);
          }
        };

        timer.schedule(timerTask, 0, 1000);
      } else {
        mTvTime.setText(getString(R.string.deal_time_exception));
      }
    }
  }

  private void alertDialog() {
    StytledDialog.showOneBtn(false,ColorUtil.getColor(this,R.color.c333),this, getString(R.string.complain_tis),
        getString(R.string.confirm_complait_tip), null, getString(R.string.close), null, true, true,
        new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
          }
        });

  }

  private void updatePic(String showpic2button) {
    if (ProofType.GRAY.equals(showpic2button)) {
//      tv_upload_proof_new.setText(getString(R.string.has_upload_proof_video));
//      tv_upload_proof_new.setTextColor(ColorUtil.getColor(R.color.white, this));
//      tv_upload_proof_new.setEnabled(false);
//      tv_upload_proof_new.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, this));

      tv_upload_proof_new.setVisibility(View.GONE);

    }
    if (ProofType.NORMAL.equals(showpic2button)) {
      tv_upload_proof_new.setText(getString(R.string.upload_proof_video));
      tv_upload_proof_new.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));
      tv_upload_proof_new.setEnabled(true);
      tv_upload_proof_new.setBackgroundColor(ColorUtil.getColor(R.color.white, this));
    }

    if (ProofType.DOWN_TIME.equals(showpic2button)) {
      tv_upload_proof_new.setText(getString(R.string.upload_proof_video));
      tv_upload_proof_new.setTextColor(ColorUtil.getColor(R.color.cEA5412, this));
      tv_upload_proof_new.setEnabled(true);
      tv_upload_proof_new.setBackgroundColor(ColorUtil.getColor(R.color.white, this));
    }
  }

  private void updatePressButton(String buypressbutton) {
    //是否催确认过
    if (CuiType.GRAY.equals(buypressbutton)) {//催过 灰色背景 白色文字
//      mTvUploadProof
//          .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//      mTvUploadProof.setEnabled(false);

      mTvUploadProof.setVisibility(View.GONE);

    } else if (CuiType.NORMAL.equals(buypressbutton)) {//没有催过
      mTvUploadProof
          .setBackgroundColor(ColorUtil.getColor(R.color.white, getApplicationContext()));
      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
      mTvUploadProof.setEnabled(true);
    }
  }

  private void selectOperation(PConfirmlistBean status) {
    if (StatusType.NO_COMPLAIN.equals(status.getStatus())) {//没有被投诉
      mTvUploadProof.setVisibility(View.VISIBLE);
      mTvUploadProof.setEnabled(true);
      tv_upload_proof_new.setVisibility(View.GONE);
      tv_time_tip.setText(getString(R.string.shenyu_time));
    }
    if (StatusType.COMPLAINTED.equals(status.getStatus())) {//被投诉
      mTvUploadProof.setVisibility(View.GONE);
      tv_upload_proof_new.setVisibility(View.VISIBLE);
      tv_upload_proof_new.setEnabled(true);
      tv_time_tip.setText(getString(R.string.complain_time_new1));
    }

  }


  @Override
  public void loadData() {
  }

  @Override
  public void finish() {
    Intent intent = new Intent();
    intent.putExtra("position", mPosition);
    setResult(RESULT_OK, intent);
    super.finish();
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.title, R.id.tv_seller, tv_upload_proof, R.id.tv_upload_proof_new, R.id.back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case R.id.tv_seller:
        Intent intent = new Intent(this, SeedsMemberInfoActivity.class);
        intent.putExtra("userId", DebugUtils.convert(mSellmember, ""));
        startActivity(intent);
        break;
      case R.id.tv_upload_proof://催确认
        confirm();
//        alertConfimDialog();
        break;
      case R.id.tv_upload_proof_new://上传视频凭证
//        uploadVedio();
        alertSelectVideoDialog();
        break;
    }
  }

  private void alertConfimDialog() {

  }

  private void uploadVedio() {

    Matisse.from(this)
        .choose(MimeType.ofVideo())
        .countable(false)
        .maxSelectable(1)
        .forResult(VIDEO_CODE);
  }


  //崔确认
  private void confirm() {
    try {
      if (!NetUtil.isConnected(this)) {
        throw new DefineException(getString(R.string.NET_ERROR));
      }
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          DebugUtils.convert(mOrderid, ""));
      list.add(paramsOrderId);
      list.add(paramsToken);
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.CUI_COMFRIM, mCuiComfrimListener,
          RequestNet.POST);
    } catch (DefineException e) {
      showMsg(e.getMessage());
      hideCommitDataDialog();
      resetCui(false);
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.cui_confirming);
  }


  public void resetCui(boolean toTargetPosition) {
    if (null != cuiConfim && cuiConfim) {
      cuiConfim = false;
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case VIDEO_CODE://上传视频
        if (null != data) {
          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(this, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            System.out
                .println("ComfirmFragment.onActivityResult=file大小=" + new File(videoPath).length());
            if (FileUtil.fileMSize(new File(videoPath)) > 50) {
              showMsg(getString(R.string.video_too_big_50m));
            } else {
              uploadTextVideo(videoPath);
            }

          }
        }
        break;
      case CAPTURE_VIDEO:
        if (null != data) {
          String url = data.getStringExtra("url");
          File file = new File(url);
          if (file.exists()) {
            uploadTextVideo(url);
          }
        }
        break;
    }
  }


  private void uploadTextVideo(String videoPath) {
    try {
      judgeNet();
      showUploadingDialog();
      uploadVideo = true;
      RequestNet requestNet = new RequestNet((MyApplication) this.getApplication(), this,
          new File(videoPath),
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""), mOrderid,
          Urls.upload_video, "video", System.currentTimeMillis() + "_clowm.mp4", videoListener);

    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.upload_proof_fail)));
      dismissUploadingDialog();
      resetUploadVideo(true);
    }
  }

  private Boolean uploadVideo;

  public void resetUploadVideo(boolean toTargetPositin) {
    if (null != uploadVideo && uploadVideo) {
      uploadVideo = false;
    }
  }


  private RequestListener videoListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {

      try {
        dismissUploadingDialog();
        Gson gson = new Gson();
        UploadVideo uploadVideo = gson.fromJson(jsonObject, UploadVideo.class);
        if (uploadVideo != null && "1".equals(uploadVideo.getType())) {
          showMsg(getString(R.string.upload_proof_success));

//          tv_upload_proof_new
//              .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, ConfirmDetailActivity.this));
//          tv_upload_proof_new.setText(mContext.getString(R.string.has_upload_proof_video));
//          tv_upload_proof_new.setTextColor(Color.WHITE);
//          tv_upload_proof_new.setEnabled(false);
//

          tv_upload_proof_new.setVisibility(View.GONE);

        } else {
          resetUploadVideo(true);
          showMsg(DebugUtils.convert(uploadVideo.getMsg(), getString(R.string.upload_proof_fail)));
        }

      } catch (Exception e) {
        showMsg(getString(R.string.upload_proof_fail));
        resetUploadVideo(true);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissUploadingDialog();
      resetUploadVideo(true);
      showMsg(getString(R.string.upload_proof_fail));
    }
  };

}
