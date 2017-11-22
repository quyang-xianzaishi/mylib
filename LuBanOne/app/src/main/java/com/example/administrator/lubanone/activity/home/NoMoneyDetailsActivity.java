package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_upload_proof;

import android.Manifest.permission;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.PPaylistBean;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.ShowButtonType;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.UriUtils;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.umeng.facebook.FacebookSdk;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

/**
 * 买入种子 待付款
 */
public class NoMoneyDetailsActivity extends BaseActivity {


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
  @BindView(R.id.detele_friend)
  TextView detele_friend;

  private static final int REQUEST_ALBUM_OK = 123;
  private String mOrderid;
  private Boolean uploadPingZheng;
  private Dialog mDialog;
  private String mPrice;
  private String mSellmember;
  private String mSellmember1;


  //上传付款凭证
  private RequestListener mUploadListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        uploadResult(result);
      } catch (Exception e) {
        dismissUploadingDialog();
        showMsg(getInfo(R.string.upload_proof_fail));
        resetUpload(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissUploadingDialog();
      showMsg(getString(R.string.upload_proof_fail));
      resetUpload(false);
    }
  };

  //延长申请时间
  private RequestListener mLongListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        mDialog.dismiss();
        hideCommitDataDialog();
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {

          detele_friend.setVisibility(View.GONE);
          tv_pay_long_time.setVisibility(View.GONE);

          showOkDialog(getString(R.string.commit_shengqing));
        } else {
          showMsg(
              DebugUtils
                  .convert(ResultUtil.getErrorMsg(result),
                      getString(R.string.commit_shengqing_fail)));
          resetLong(false);
        }
      } catch (Exception e) {
        hideCommitDataDialog();
        resetLong(false);
        showMsg(getString(R.string.long_pay_time_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      mDialog.dismiss();
      resetLong(false);
      showMsg(getString(R.string.commit_shengqing_fail));
    }
  };

  private void showOkDialog(String text) {
    final Dialog dialog = StytledDialog.showDuiHaoDialog(this, true, true, text);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
      }
    }, 2000);
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
//  //上传付款凭证
//  private void uploadPic(File saveFile) {
//    if (TextUtils.isEmpty(mOrderid)) {
//      showMsg(getString(R.string.order_id_empty));
//      return;
//    }
//    try {
//      showUploadingDialog();
//      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
//          saveFile,
//          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""), mOrderid,
//          Urls.UPLOAD_PAY_PIC, "image",
//          "pay_proof.jpg", mUploadListener);
//    } catch (Exception e) {
//      dismissUploadingDialog();
//      resetUpload(false);
//    }
//
//  }

  public void updateTimeRemaining(long endtime) {

    long timeMillis = System.currentTimeMillis();
    long timeDiff = endtime - timeMillis;

    if (timeDiff > 0) {

      //在1小时内才可现实延长打款按钮
      if (timeDiff < 3600 * 1000 && timeDiff > 0) {
        //小于一小时才可以延长打款，大于或等于都gone
        if (null != detele_friend && tv_pay_long_time != null) {
          detele_friend.setEnabled(true);
          tv_pay_long_time.setEnabled(true);
//          detele_friend.setVisibility(View.VISIBLE);
//          tv_pay_long_time.setVisibility(View.VISIBLE);
        }
      } else {
        if (null != detele_friend) {
          detele_friend.setVisibility(View.GONE);
        }
        if (null != tv_pay_long_time) {
          tv_pay_long_time.setVisibility(View.GONE);
        }
      }

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
      //超时finish
      finish();

//      if (null != tv_time) {
//        //超时提示
//        tv_time.setText(getString(R.string.than_time));
//      }
//
//      //超时后不能延长打款
//      detele_friend.setEnabled(false);
//      tv_pay_long_time.setEnabled(false);
//
//      //超时后不能上超付款凭证，灰色
//      mTvUploadProof
//          .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//      mTvUploadProof.setEnabled(false);
    }
  }


  @Override
  protected void beforeSetContentView() {
    FacebookSdk.sdkInitialize(getApplicationContext());
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_confrim_details;
  }

  @Override
  public void initView() {

    mTvUploadProof.setText(getString(R.string.uuload_pay_proof));

    tv_pay_long_time.setOnClickListener(this);
    tv_pay_long_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    tv_pay_long_time.getPaint().setAntiAlias(true);//抗锯齿

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (null != item) {
        PPaylistBean itemSerializable = (PPaylistBean) item.getSerializable("item");
        mPrice = item.getString("price");
        mSellmember = itemSerializable.getSellmember();
        updaatePage(itemSerializable);
      }
    }
  }

  private void updaatePage(PPaylistBean itemSerializable) {
    if (null == itemSerializable) {
      return;
    }
    mOrderid = itemSerializable.getOrderid();
    DebugUtils.setStringValue(itemSerializable.getOrderid(), "", mTvOrderId);
//    DebugUtils.setStringValue(itemSerializable.getSeedcount(), "", mTvSeedCount);
    DebugUtils.setStringValue(itemSerializable.getMatchtime(), "", mTvOrderTime);
    DebugUtils.setStringValue(itemSerializable.getSellmember(), "", mTvSeller);

    StringUtil.setTextSmallSize(DebugUtils.convert(itemSerializable.getSeedcount(), "0") + " pcs",
        mTvSeedCount, 11, 9);

    //申请延长打款时间按钮
    updateLongerTimer(itemSerializable);

    String multile = DemicalUtil
        .multile(DebugUtils.convert(itemSerializable.getSeedcount(), "0"),
            DebugUtils.convert(mPrice, "0"));

    DebugUtils.setStringValue("¥" + StringUtil.getThreeString(multile), "", mTvPay);

    String endtime = itemSerializable.getEndtime();

    Long aLong = DateUtil.parseDateString(endtime);
    long l = System.currentTimeMillis();

    //剩余时间小于1小时的时候
    if (aLong != null && aLong - l < 3600 * 1000 && aLong - l > 0) {
      //小于一小时才可以延长打款，大于或等于都gone
//      detele_friend.setVisibility(View.VISIBLE);
//      tv_pay_long_time.setVisibility(View.VISIBLE);

      //小于1小时弹窗提示
      alertDialog();
    } else if (aLong != null && aLong - l > 3600 * 1000) {
      //大于1小时都不显示延长打款
//      detele_friend.setVisibility(View.GONE);
//      tv_pay_long_time.setVisibility(View.GONE);
    } else if (aLong != null && aLong - l <= 0) {
      //超时
      finish();
    }

    if (TextUitl.isNotEmpty(endtime)) {

      if (aLong != null && aLong - l > 0) {
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
      } else if (aLong != null && aLong - l <= 0) {
        //剩余时间小于等于0,超时了
        tv_time.setText(getString(R.string.than_time));
      }

    } else {
      //剩余时间小于等于0,超时了
      tv_time.setText(getString(R.string.shengyu_time_exception));
    }
  }

  private void updateLongerTimer(PPaylistBean itemSerializable) {

    int showButton = itemSerializable.getShowButton();
    if (ShowButtonType.HIDE_LONG_TIME.equals(showButton)) {//影藏
      detele_friend.setVisibility(View.GONE);
      tv_pay_long_time.setVisibility(View.GONE);
    }
    if (ShowButtonType.SHOW_LONG_TIME.equals(showButton)) {//正常显示 long_pay_time
      detele_friend.setVisibility(View.VISIBLE);
      detele_friend.setEnabled(true);
      detele_friend.setText(getString(R.string.special_reason));

      tv_pay_long_time.setVisibility(View.VISIBLE);
      tv_pay_long_time.setText(getString(R.string.long_pay_time));
      tv_pay_long_time.setTextColor(Color.parseColor("#20277D"));

    }

    if (ShowButtonType.GRAY_LONG_TIME.equals(showButton)) {//灰色 卖家没响应

      tv_pay_long_time.setText(getString(R.string.seller_no_respose_one));
      tv_pay_long_time.setTextColor(Color.parseColor("#20277D"));

      detele_friend.setVisibility(View.GONE);
      tv_pay_long_time.setVisibility(View.GONE);
    }

    if (ShowButtonType.GRAY_AGREE_LONG_TIME.equals(showButton)) {//灰色 卖家已同意

      tv_pay_long_time.setText(getString(R.string.seller_agree));
      tv_pay_long_time.setTextColor(Color.parseColor("#20277D"));

      detele_friend.setVisibility(View.GONE);
      tv_pay_long_time.setVisibility(View.GONE);
    }


  }

  private void alertDialog() {

    StytledDialog
        .showOneBtn(false, ColorUtil.getColor(this, R.color.c333), this,
            getString(R.string.tixing_pay), getString(R.string.buy_seeds_list_tips),
            null, getString(R.string.close), null, false, true, new MyDialogListener() {
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
          .showOneBtn(false, ColorUtil.getColor(this, R.color.c333), this,
              getString(R.string.is_longer_new), getString(R.string.new_tips),
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


  //延长打款
  private void longerPayTime() {

    longPay = true;

    try {

      //PPaylistBean
      if (!NetUtil.isConnected(this)) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          DebugUtils.convert(mOrderid, ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          list, Urls.LONG_PAY_TIME, mLongListener,
          RequestNet.POST);
    } catch (Exception e) {
      hideCommitDataDialog();
      showMsg(getString(R.string.shen_qing_fail));
      resetLong(false);
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

  /**
   * 检查所有需要的权限
   */
  private boolean checkPermissionAllGranted(String[] permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission)
          != PackageManager.PERMISSION_GRANTED) {
        // 只要有一个权限没有被授予, 则直接返回 false
        return false;
      }
    }
    return true;
  }

  private static final int CAMERA_CODE = 125;

  private void choosePic() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(this, new String[]{permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
      } else {
        showSelectDialog();
      }
    } else {
      showSelectDialog();
    }
  }


  private void showSelectDialog() {
    ArrayList<String> list = new ArrayList<>();
    list.add(getString(R.string.camera));
    list.add(getString(R.string.select_pic));

    StytledDialog.showBottomItemDialog(this, list, getString(R.string.cancel), true, true,
        new MyItemDialogListener() {
          @Override
          public void onItemClick(String text, int position) {
            openPic(position);
          }
        });
  }


  private void openPic(int position) {

    if (0 == position) {
      cameraPic();
    }
    if (1 == position) {
      xiangce();
    }
  }


  private void xiangce() {
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(false)
        .maxSelectable(1)
        .forResult(11);
  }

  private void cameraPic() {
    takePic();
  }


  private String pathTakePhoto;              //拍照路径
  private Uri imageUri;                            //拍照Uri
  private final int TAKE_PHOTO = 5;//拍照标记


  private void takePic() {
    File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image2.jpg");
    pathTakePhoto = outputImage.toString();
    try {
      if (outputImage.exists()) {
        outputImage.delete();
      }
      outputImage.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
    imageUri = UriUtils.getUri(outputImage, this);
    Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(intentPhoto, TAKE_PHOTO);
  }

  @OnClick({R.id.back, tv_upload_proof, R.id.tv_seller})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case tv_upload_proof://上传付款凭证
        if (mTvUploadProof.isEnabled()) {
//          uploadPic();
          choosePic();
        } else {
          showMsg(getString(R.string.upload_tips));
        }
        break;
      case R.id.tv_seller:
        Intent intent = new Intent(this, SeedsMemberInfoActivity.class);
        intent.putExtra("userId", DebugUtils.convert(mSellmember, ""));
        startActivity(intent);
        break;
    }
  }

  private void uploadPic() {
    uploadPingZheng = true;
    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(albumIntent, REQUEST_ALBUM_OK);
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_ALBUM_OK:
        try {
          Uri dataData = data.getData();
          startCrop(dataData);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case TAKE_PHOTO:
        String path = imageUri
            .getPath();//content://com.example.administrator.lubanone.my.package.name

        File file = new File(Environment.getExternalStorageDirectory(),
            "suishoupai_image2.jpg");//suishoupai_image2

        if (file.exists()) {
          System.out.println("MoneyFragment.onActivityResultddd 1");
        }

        uploadPics(file);
        break;
      case 11:
        if (null != data) {
          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(this, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            System.out.println("MoneyFragment.onActivityResultddd 2");
            uploadPics(new File(videoPath));
          }
        }
        break;
    }

    if (resultCode == RESULT_OK) {
      //裁切成功
      if (requestCode == 100) {
        Uri croppedFileUri = UCrop.getOutput(data);
        //获取默认的下载目录
        String downloadsDirectoryPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
            croppedFileUri.getLastPathSegment());
        mSaveFile = new File(downloadsDirectoryPath, filename);
        if (mSaveFile.exists()) {
          showMsg("ewtt");
        }

        //保存下载的图片
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
          inStream = new FileInputStream(new File(croppedFileUri.getPath()));
          outStream = new FileOutputStream(mSaveFile);
          inChannel = inStream.getChannel();
          outChannel = outStream.getChannel();
          inChannel.transferTo(0, inChannel.size(), outChannel);
          Bitmap bitmap = BitmapFactory.decodeFile(mSaveFile.getPath());

          int i = bitmap.getRowBytes() * bitmap.getHeight();
          System.out.println("quyang=" + i / 1024);

          //上传图片
          uploadPic(mSaveFile);

        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            outChannel.close();
            outStream.close();
            inChannel.close();
            inStream.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    //裁切失败
    if (resultCode == UCrop.RESULT_ERROR) {
      showMsg(getString(R.string.crop_pic_fail));
    }
  }


  //上传付款凭证
  private void uploadPics(File saveFile) {
    if (TextUtils.isEmpty(mOrderid)) {
      showMsg(getString(R.string.order_id_empty));
      return;
    }
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          saveFile,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""),
          mOrderid,
          Urls.UPLOAD_PAY_PIC, "image",
          "pay_proof.jpg", mUploadListener);
    } catch (Exception e) {
      dismissUploadingDialog();
      resetUpload(false);
    }

  }


  private void startCrop(Uri dataData) {
    boolean equals = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    if (!equals) {
      showMsg(getString(R.string.sd_card_tips));
      return;
    }
    Uri destinationUri = Uri
        .fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.Options options = new UCrop.Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);

    UCrop.of(dataData, destinationUri).withOptions(options).withAspectRatio(9, 16)
        .withMaxResultSize(500, 500)
        .start(this, 100);
  }


  private void uploadPic(File saveFile) {
    if (TextUtils.isEmpty(mOrderid)) {
      showMsg(getString(R.string.order_id_empty));
      return;
    }
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this,
          saveFile,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""),
          mOrderid,
          Urls.UPLOAD_PAY_PIC, "image",
          "pay_proof.jpg", mUploadListener);
    } catch (Exception e) {
      dismissUploadingDialog();
      resetUpload(false);
    }

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
//      mTvUploadProof
//          .setBackgroundColor(ColorUtil.getColor(R.color.cBBB, getApplicationContext()));
//      mTvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//      mTvUploadProof.setEnabled(false);
      finish();
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.upload_proof_fail)));
      resetUpload(false);
    }
  }
}
