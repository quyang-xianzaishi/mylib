package com.example.administrator.lubanone.activity.train;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.RequestBean;
import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.TrainContinueUpload;
import com.example.administrator.lubanone.bean.model.TrainTestModel;
import com.example.administrator.lubanone.bean.model.TrainTestQuestions;
import com.example.administrator.lubanone.bean.model.TrainUploadModel;
import com.example.administrator.lubanone.customview.progressdialog.ProgressLoadingDialog;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.FileUtils;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.SPUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/9/2.
 */

public class UploadTrainActivity extends BaseActivity {

  private static final int VIDEO_CODE = 11;
  private static final int AUDIO_CODE = 22;
  private static final int VIDEO_RECORDING_CODE = 112;
  private static final int TEXT_UPLOAD_FINISHED = 33;
  private static final int CONTINUE_UPLOAD_CHUNK = 44;
  private static final int CAMERA_CODE = 125;

  private List<String> selects = new ArrayList<>();

  private EditText trainTheme;
  private EditText trainContent;
  private FrameLayout imageShowLayout;
  private ImageView imageShow;
  private ImageView imageDelete;
  private TextView imageType;
  private RelativeLayout audioAddLayout;
  private RelativeLayout videoAddLayout;
  private LinearLayout testAddBtn;
  private TextView summitBtn;
  private LinearLayout backBtn;

  private View test1, test2, test3, test4, test5;
  private EditText test1Name, test1A, test1B, test1C, test1D;
  private EditText test2Name, test2A, test2B, test2C, test2D;
  private EditText test3Name, test3A, test3B, test3C, test3D;
  private EditText test4Name, test4A, test4B, test4C, test4D;
  private EditText test5Name, test5A, test5B, test5C, test5D;
  private TextView test1Answer, test2Answer, test3Answer, test4Answer, test5Answer;
  private TextView test1Title, test2Title, test3Title, test4Title, test5Title;
  private RelativeLayout imageBtn1, imageBtn2, imageBtn3, imageBtn4, imageBtn5;
  //试题空白判断
  private boolean isAdd1, test1HasBlank;
  private boolean isAdd2, test2HasBlank;
  private boolean isAdd3, test3HasBlank;
  private boolean isAdd4, test4HasBlank;
  private boolean isAdd5, test5HasBlank;

  private int testPosition = 0;//记录试题显示个数

  //上传文本所需
  private String mTheme = "";//上传的主题
  private String mContent = "";//上传的内容
  private List<TrainTestQuestions> testLists = new ArrayList<>();//试题集
  private TrainTestModel testList = new TrainTestModel();//生成json字符串

  //上传媒体所需
  private String trainId = "";//上传的培训视频对应的文本内容id
  private String mediaPath = "";//音视频路径
  private String mExt;//音视频类型
  private int chunkNum = 1;//音频上传时流块总数参数

  //分片上传所需
  private int chunk = 0;//流块
  private int chunks = 100;//流块总数
  private int blockLength = 1024 * 1024;//每个块的大小
  private File mediaFile = null;//原始视频文件
  private File fileDir = new File("/mnt/sdcard/chunks");//存放分块后的视频文件
  private File fileVideoDir = new File("/mnt/sdcard/clownVideo");//存放录制的视频
  private ProgressLoadingDialog progressBarDialog;

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case TEXT_UPLOAD_FINISHED://试题上传完，上传音频或者视频
          if (mExt.equals("mp3")) {
            uploadMedia();
          } else {
            uploadChunck();
          }
          break;
        case CONTINUE_UPLOAD_CHUNK://续传
          uploadChunck();
          break;
      }
    }
  };

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_upload_train;
  }

  @Override
  public void initView() {
    trainTheme = (EditText) findViewById(R.id.train_upload_theme_edit);
    trainContent = (EditText) findViewById(R.id.train_upload_content_edit);
    imageShowLayout = (FrameLayout) findViewById(R.id.train_upload_image_show_layout);
    imageShow = (ImageView) findViewById(R.id.train_upload_image_show);
    imageDelete = (ImageView) findViewById(R.id.train_upload_image_delete);
    imageType = (TextView) findViewById(R.id.train_upload_image_type);
    audioAddLayout = (RelativeLayout) findViewById(R.id.train_upload_audio_add_layout);
    videoAddLayout = (RelativeLayout) findViewById(R.id.train_upload_video_add_layout);
    testAddBtn = (LinearLayout) findViewById(R.id.train_upload_add_test);
    summitBtn = (TextView) findViewById(R.id.train_upload_commit);
    backBtn = (LinearLayout) findViewById(R.id.train_upload_back);

    //初始化试题布局
    initTestLayout();

    backBtn.setOnClickListener(this);
    testAddBtn.setOnClickListener(this);
    audioAddLayout.setOnClickListener(this);
    videoAddLayout.setOnClickListener(this);
    imageDelete.setOnClickListener(this);
    summitBtn.setOnClickListener(this);
    checkPermission();
    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }
    if (!fileVideoDir.exists()) {
      fileVideoDir.mkdirs();
    }
    progressBarDialog = new ProgressLoadingDialog(this);
    progressBarDialog.setLabel(getString(R.string.progress_bar_loading));
    progressBarDialog.setCancelable(false);

    selects.add("a");
    selects.add("b");
    selects.add("c");
    selects.add("d");
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_upload_back:
        //返回
        finish();
        break;
      case R.id.train_upload_add_test:
        //添加试题
        addTest();
        break;
      case R.id.train_upload_audio_add_layout:
        //添加音频
        addAudio();
        break;
      case R.id.train_upload_video_add_layout:
        //添加视频
        showSelectDialog();
        break;
      case R.id.train_upload_image_delete:
        //删除展示图片
        deleteShowImage();
        break;
      case R.id.train_upload_commit:
        //提交按钮
        clickCommit();
        break;
    }
  }

  //选择银行卡
  public void selectBankList(final List<String> objects, final TextView textView) {
    OptionsPickerView a = new OptionsPickerView.Builder(this,
        new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3, View v) {
            //返回的分别是三个级别的选中位置
            textView.setText(objects.get(options1));
//            ResultBean banklistBean = mBanklist.get(options1);//tv_choosen_bank_account
//            mTvChooseBankAccount.setText(banklistBean.getBankname());
//            mBankType = Integer.parseInt(banklistBean.getBanktype());
          }
        })
        .setSubmitText(getString(R.string.complete_normal))//确定按钮文字
        .setCancelText(getString(R.string.cancels))//取消按钮文字
        .setTitleText("")//标题
        .setSubCalSize(17)//确定和取消文字大小
        .setTitleSize(20)//标题文字大小
        .setTitleColor(ColorUtil.getColor(this, R.color.c333))//标题文字颜色
        .setSubmitColor(ColorUtil.getColor(this, R.color.c333))//确定按钮文字颜色
        .setCancelColor(ColorUtil.getColor(this, R.color.c333))//取消按钮文字颜色
        .setTitleBgColor(ColorUtil.getColor(this, R.color.white))//标题背景颜色 Night mode
        .setBgColor(ColorUtil.getColor(this, R.color.white))//滚轮背景颜色 Night mode
        .setContentTextSize(18)//滚轮文字大小
        .setLinkage(false)//设置是否联动，默认true
        .setLabels("", null, null)//设置选择的三级单位
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .setCyclic(false, false, false)//循环与否
        .setSelectOptions(0, 1, 1)  //设置默认选中项
        .setOutSideCancelable(true)//点击外部dismiss default true
        .isDialog(false)//是否显示为对话框样式
        .build();
    a.setPicker(objects);
    a.show(true);

  }

  private void showSelectDialog() {
    ArrayList<String> list = new ArrayList<>();
    list.add(getInfo(R.string.upload_select_video_from_xxx));
    list.add(getInfo(R.string.upload_select_video_recording));

    StytledDialog
        .showBottomItemDialog(this, list, getInfo(R.string.upload_select_cancel), true, true,
            new MyItemDialogListener() {
              @Override
              public void onItemClick(String text, int position) {
                openPic(position);
              }
            });
  }

  private void openPic(int position) {

    if (0 == position) {//从视频库选择
      addVideo();
    }
    if (1 == position) {//录像
      // 激活系统的照相机进行录像
      Intent intent = new Intent();
      intent.setAction("android.media.action.VIDEO_CAPTURE");
      intent.addCategory("android.intent.category.DEFAULT");
      startActivityForResult(intent, VIDEO_RECORDING_CODE);
    }
  }

  /**
   * 检查权限
   */
  private void checkPermission() {
    HouLog.d("当前手机版本：" + VERSION.SDK_INT);
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(this, new String[]{permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
      }
    }
  }

  /**
   * 检查所有权限
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

  private boolean isAllGranted = true;

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case CAMERA_CODE:
        // 判断是否所有的权限都已经授予了
        if (grantResults.length > 0) {
          for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
              isAllGranted = false;
              break;
            }
          }
          if (!isAllGranted) {
            Toast.makeText(getApplicationContext(), getString(R.string.camera_deny),
                Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(), getString(R.string.camera_deny),
              Toast.LENGTH_LONG).show();
        }
        break;
      default:
        break;
    }
  }


  //获取视频片
  private File getChunkFile() {
    mediaFile = new File(mediaPath);
    if (mediaFile.length() % blockLength == 0) {
      chunks = (int) (mediaFile.length() / blockLength);
    } else {
      chunks = (int) (mediaFile.length() / blockLength) + 1;
    }
    byte[] mBlock = FileUtils.getBlock(chunk * blockLength, mediaFile, blockLength);
    File fileChunck = new File(fileDir.getAbsoluteFile() + "/" + "chunk" + chunk + ".mp4");
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    try {
      if (!fileChunck.exists()) {
        fileChunck.createNewFile();
      }
      fos = new FileOutputStream(fileChunck);
      bos = new BufferedOutputStream(fos);
      bos.write(mBlock);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bos != null) {
          bos.close();
        }
        if (fos != null) {
          fos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return fileChunck;
  }

  //分片上传视频
  private void uploadChunck() {
    File file = getChunkFile();
    dismissUploadingDialog();
    progressBarDialog.show();
    progressBarDialog.setMaxProgress(chunks);
    progressBarDialog.setProgress(chunk);
    if (file != null) {
      long len = FileUtils.getFileSize(file);
      HouLog.d("视频上传参数:" + trainId + " " + mExt + " " + chunks + " " + chunk + " " + file
          .getName() + " 文件大小:" + FileUtils.formatFileSize(len));

      OkHttpUtils.post()
          .addFile("video", "chunck.mp4", file)
          .url(Urls.ROOT_URL + "api.php/Home/train/uploadvideo")
          .addParams("token",
              SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""))
          .addParams("trainid", trainId)
          .addParams("ext", mExt)
          .addParams("chunknum", String.valueOf(chunks))
          .build()
          .execute(new myCallBack());
    } else {
      HouLog.d("chunkFile is null !!!");
    }
  }

  class myCallBack extends StringCallback {

    @Override
    public void onError(Call call, Exception e, int id) {
      progressBarDialog.dismiss();
      HouLog.d("onError " + e.toString());
      HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_fail_and_again));
      chunk = 0;
      showTipAlert();
    }

    @Override
    public void onResponse(String response, int id) {
      HouLog.d("onResponse" + response.toString());
      RequestBean rb = new RequestBean(response);
      if (rb.getType().equals("1")) {
        HouLog.d("chunk" + chunk + "上传成功");
        chunk++;
        progressBarDialog.setProgress(chunk);
        if (chunk < chunks) {
          uploadChunck();
        } else {
          //所有块上传结束
          progressBarDialog.dismiss();
          chunk = 0;
          HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_success));
          finish();
        }
      } else {
        progressBarDialog.dismiss();
        HouLog.d(rb.getMsg());
        chunk = 0;
        showTipAlert();
        HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_fail_and_again));
      }

    }
  }

  //取消视频上传
  private void cancelUpload() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<BaseModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        showTipAlert();
      }

      @Override
      public void onNext(BaseModel baseModel) {
        dismissLoadingDialog();

      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("trainid", trainId);
    myApp.rxNetUtils.getTrainService().cancleUpload(params)
        .map(new BaseModelFunc())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //上传中断提示框
  private void showTipAlert() {
    StytledDialog.showIosAlert(this, getInfo(R.string.upload_train_alert_title),
        getInfo(R.string.upload_train_alert_content), getInfo(R.string.cancel),
        getInfo(R.string.yes), "", false, false, new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            cancelUpload();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            continueUpload();
          }
        });
  }

  //视频续传
  private void continueUpload() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TrainContinueUpload>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        showTipAlert();
      }

      @Override
      public void onNext(TrainContinueUpload baseModel) {
        dismissLoadingDialog();
        chunk = Integer.parseInt(baseModel.getChunk());
        if (chunk == -1) {
          //上传成功
          progressBarDialog.dismiss();
          chunk = 0;
          HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_success));
          finish();
        } else {
          mHandler.sendEmptyMessage(CONTINUE_UPLOAD_CHUNK);
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("trainid", trainId);
    HouLog.d("续传参数：", params.toString());
    HouLog.d("trainId " + trainId);
    myApp.rxNetUtils.getTrainService().continueUpload(params)
        .map(new BaseModelFunc<TrainContinueUpload>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //上传音频
  private void uploadMedia() {
    Subscriber subscriber = new MySubscriber<BaseModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissUploadingDialog();
        HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_fail_and_again));
        HouLog.d(TAG, "onError: " + e.toString());
      }

      @Override
      public void onNext(BaseModel baseModel) {
        dismissUploadingDialog();
        HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_success));
        finish();
      }
    };
    HouLog.d("音频上传参数:" + trainId + " " + mExt + " " + mediaPath + " " + chunkNum);
    File mediaFile = new File(mediaPath);
    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), mediaFile);
    MultipartBody.Part part = MultipartBody.Part
        .createFormData("file", mediaFile.getName(), requestBody);
    RequestBody token = RequestBody
        .create(MediaType.parse("text/plain"), SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestBody trainid = RequestBody.create(MediaType.parse("text/plain"), trainId);
    RequestBody ext = RequestBody.create(MediaType.parse("text/plain"), mExt);
    RequestBody chunknum = RequestBody
        .create(MediaType.parse("text/plain"), String.valueOf(chunkNum));
    myApp.rxNetUtils.getTrainService().uploadMedia(token, trainid, ext, chunknum, part)
        .map(new BaseModelFunc())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //提交按钮
  private void clickCommit() {

    mTheme = trainTheme.getText().toString();
    mContent = trainContent.getText().toString();

    if (TextUtils.isEmpty(mTheme)) {
      HouToast
          .showLongToast(UploadTrainActivity.this, getInfo(R.string.training_commit_theme_null));
      return;
    }
    if (TextUtils.isEmpty(mContent)) {
      HouToast
          .showLongToast(UploadTrainActivity.this, getInfo(R.string.training_commit_summary_null));
      return;
    }
    if (TextUtils.isEmpty(mediaPath)) {
      HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.add_media_tip));
      return;
    }
    getTextContent();
    if (testLists.size() < 3) {
      HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.train_test_number_tip));
      return;
    }
    checkTestBlank();

    uploadTest();
  }

  //判断试题内容是否有空
  private void checkTestBlank() {
    if (isAdd1) {
      if (test1HasBlank) {
        HouToast.showLongToast(UploadTrainActivity.this,
            getInfo(R.string.train_test_has_blank1) + "1" + getInfo(
                R.string.train_test_has_blank2));
      }
      return;
    }
    if (isAdd2) {
      if (test2HasBlank) {
        HouToast.showLongToast(UploadTrainActivity.this,
            getInfo(R.string.train_test_has_blank1) + "2" + getInfo(
                R.string.train_test_has_blank2));
      }
      return;
    }
    if (isAdd3) {
      if (test3HasBlank) {
        HouToast.showLongToast(UploadTrainActivity.this,
            getInfo(R.string.train_test_has_blank1) + "3" + getInfo(
                R.string.train_test_has_blank2));
      }
      return;
    }
    if (isAdd4) {
      if (test4HasBlank) {
        HouToast.showLongToast(UploadTrainActivity.this,
            getInfo(R.string.train_test_has_blank1) + "4" + getInfo(
                R.string.train_test_has_blank2));
      }
      return;
    }
    if (isAdd5) {
      if (test5HasBlank) {
        HouToast.showLongToast(UploadTrainActivity.this,
            getInfo(R.string.train_test_has_blank1) + "5" + getInfo(
                R.string.train_test_has_blank2));
      }
      return;
    }
  }

  //获取试题内容
  private void getTextContent() {
    if (testLists == null) {
      testLists = new ArrayList<>();
    }
    if (testLists.size() > 0) {
      testLists.clear();
    }

    if (!TextUtils.isEmpty(test1Name.getText()) && !TextUtils.isEmpty(test1A.getText())
        && !TextUtils.isEmpty(test1B.getText()) && !TextUtils.isEmpty(test1C.getText())
        && !TextUtils.isEmpty(test1D.getText()) && !TextUtils.isEmpty(test1Answer.getText())) {
      testLists.add(
          new TrainTestQuestions(test1Name.getText().toString(), test1A.getText().toString(),
              test1B.getText().toString(), test1C.getText().toString(), test1D.getText().toString(),
              test1Answer.getText().toString()));
      isAdd1 = true;
      test1HasBlank = false;
    } else if (TextUtils.isEmpty(test1Name.getText()) && TextUtils.isEmpty(test1A.getText())
        && TextUtils.isEmpty(test1B.getText()) && TextUtils.isEmpty(test1C.getText())
        && TextUtils.isEmpty(test1D.getText()) && TextUtils.isEmpty(test1Answer.getText())) {
      isAdd1 = false;
    } else {
      test1HasBlank = true;
    }

    if (!TextUtils.isEmpty(test2Name.getText()) && !TextUtils.isEmpty(test2A.getText())
        && !TextUtils.isEmpty(test2B.getText()) && !TextUtils.isEmpty(test2C.getText())
        && !TextUtils.isEmpty(test2D.getText()) && !TextUtils.isEmpty(test2Answer.getText())) {
      testLists.add(
          new TrainTestQuestions(test2Name.getText().toString(), test2A.getText().toString(),
              test2B.getText().toString(), test2C.getText().toString(), test2D.getText().toString(),
              test2Answer.getText().toString()));
      isAdd2 = true;
      test2HasBlank = false;
    } else if (TextUtils.isEmpty(test2Name.getText()) && TextUtils.isEmpty(test2A.getText())
        && TextUtils.isEmpty(test2B.getText()) && TextUtils.isEmpty(test2C.getText())
        && TextUtils.isEmpty(test2D.getText()) && TextUtils.isEmpty(test2Answer.getText())) {
      isAdd2 = false;
    } else {
      test2HasBlank = true;
    }

    if (!TextUtils.isEmpty(test3Name.getText()) && !TextUtils.isEmpty(test3A.getText())
        && !TextUtils.isEmpty(test3B.getText()) && !TextUtils.isEmpty(test3C.getText())
        && !TextUtils.isEmpty(test3D.getText()) && !TextUtils.isEmpty(test3Answer.getText())) {
      testLists.add(
          new TrainTestQuestions(test3Name.getText().toString(), test3A.getText().toString(),
              test3B.getText().toString(), test3C.getText().toString(), test3D.getText().toString(),
              test3Answer.getText().toString()));
      isAdd3 = true;
      test3HasBlank = false;
    } else if (TextUtils.isEmpty(test3Name.getText()) && TextUtils.isEmpty(test3A.getText())
        && TextUtils.isEmpty(test3B.getText()) && TextUtils.isEmpty(test3C.getText())
        && TextUtils.isEmpty(test3D.getText()) && TextUtils.isEmpty(test3Answer.getText())) {
      isAdd3 = false;
    } else {
      test3HasBlank = true;
    }

    if (!TextUtils.isEmpty(test4Name.getText()) && !TextUtils.isEmpty(test4A.getText())
        && !TextUtils.isEmpty(test4B.getText()) && !TextUtils.isEmpty(test4C.getText())
        && !TextUtils.isEmpty(test4D.getText()) && !TextUtils.isEmpty(test4Answer.getText())) {
      testLists.add(
          new TrainTestQuestions(test4Name.getText().toString(), test4A.getText().toString(),
              test4B.getText().toString(), test4C.getText().toString(), test4D.getText().toString(),
              test4Answer.getText().toString()));
      isAdd4 = true;
      test4HasBlank = false;
    } else if (TextUtils.isEmpty(test4Name.getText()) && TextUtils.isEmpty(test4A.getText())
        && TextUtils.isEmpty(test4B.getText()) && TextUtils.isEmpty(test4C.getText())
        && TextUtils.isEmpty(test4D.getText()) && TextUtils.isEmpty(test4Answer.getText())) {
      isAdd4 = false;
    } else {
      test4HasBlank = true;
    }

    if (!TextUtils.isEmpty(test5Name.getText()) && !TextUtils.isEmpty(test5A.getText())
        && !TextUtils.isEmpty(test5B.getText()) && !TextUtils.isEmpty(test5C.getText())
        && !TextUtils.isEmpty(test5D.getText()) && !TextUtils.isEmpty(test5Answer.getText())) {
      testLists.add(
          new TrainTestQuestions(test5Name.getText().toString(), test5A.getText().toString(),
              test5B.getText().toString(), test5C.getText().toString(), test5D.getText().toString(),
              test5Answer.getText().toString()));
      isAdd4 = true;
      test4HasBlank = false;
    } else if (TextUtils.isEmpty(test5Name.getText()) && TextUtils.isEmpty(test5A.getText())
        && TextUtils.isEmpty(test5B.getText()) && TextUtils.isEmpty(test5C.getText())
        && TextUtils.isEmpty(test5D.getText()) && TextUtils.isEmpty(test5Answer.getText())) {
      isAdd5 = false;
    } else {
      test5HasBlank = true;
    }

    testList.setTest(testLists);

  }

  //上传试题
  private void uploadTest() {
    Subscriber subscriber = new MySubscriber<TrainUploadModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissUploadingDialog();
        HouToast.showLongToast(UploadTrainActivity.this, e.getMessage());
        HouLog.d("试题onError: " + e.toString());
      }

      @Override
      public void onNext(TrainUploadModel trainUploadModel) {
        HouLog.d("题目上传完的onNext");
        if (trainUploadModel != null) {
          HouLog.d("trainId:" + trainUploadModel.getTrainid());
          trainId = trainUploadModel.getTrainid();
          if (!TextUtils.isEmpty(trainId)) {
            mHandler.sendEmptyMessage(TEXT_UPLOAD_FINISHED);
          } else {
            //trainId:null
            HouToast
                .showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_fail_and_again));
          }
        } else {
          //result:null
          HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.upload_fail_and_again));
        }
      }
    };

    String testJsonString = JSON.toJSONString(testList);
    HouLog.d("testJsonString:", testJsonString);
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("theme", mTheme);
    params.put("notice", mContent);
    params.put("test", testJsonString);
    params.put("testnum", String.valueOf(testLists.size()));
    HouLog.d(TAG + "参数:", params.toString());
    showUploadingDialog();
    MyApplication.rxNetUtils.getTrainService()
        .uploadData(params)
        .map(new BaseModelFunc<TrainUploadModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //删除展示图片
  private void deleteShowImage() {
    mediaPath = "";
    imageShowLayout.setVisibility(View.GONE);
    videoAddLayout.setVisibility(View.VISIBLE);
    audioAddLayout.setVisibility(View.VISIBLE);
  }

  //添加试题
  private void addTest() {
    switch (testPosition) {
      case 0:
        test1.setVisibility(View.VISIBLE);
        break;
      case 1:
        test2.setVisibility(View.VISIBLE);
        break;
      case 2:
        test3.setVisibility(View.VISIBLE);
        break;
      case 3:
        test4.setVisibility(View.VISIBLE);
        break;
      case 4:
        test5.setVisibility(View.VISIBLE);
        break;
      default:
        HouToast.showLongToast(UploadTrainActivity.this, getInfo(R.string.the_most_test_number));
        break;
    }
    testPosition++;
  }

  //添加音频
  private void addAudio() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("audio/*");
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, AUDIO_CODE);
  }

  //添加视频
  private void addVideo() {
    Matisse.from(this)
        .choose(MimeType.ofVideo())
        .countable(false)
        .maxSelectable(1)
        .theme(R.style.Matisse_Dracula)
        .forResult(VIDEO_CODE);
  }

  //选择音视频后界面的展示
  private void mediaImageLayoutShow(String type) {
    switch (type) {
      case "mp3":
        imageShowLayout.setVisibility(View.VISIBLE);
        videoAddLayout.setVisibility(View.GONE);
        audioAddLayout.setVisibility(View.GONE);
        Glide.with(this).load(R.drawable.train_upload_audio_image).into(imageShow);
        imageType.setText(getInfo(R.string.upload_select_audio));
        break;
      case "mp4":
        imageShowLayout.setVisibility(View.VISIBLE);
        videoAddLayout.setVisibility(View.GONE);
        audioAddLayout.setVisibility(View.GONE);
        imageShow.setImageBitmap(VideoUtils.getVideoThumbnail(mediaPath));
        imageType.setText(getInfo(R.string.upload_select_video));
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case AUDIO_CODE:
          Uri uri = data.getData();
          mediaPath = FileUtils.getFileAbsolutePath(UploadTrainActivity.this, uri);
          mExt = "mp3";
          HouLog.d(TAG, "音频path:" + mediaPath + " 类型:" + mExt);
          break;
        case VIDEO_CODE:
          List<Uri> uris = Matisse.obtainResult(data);
          mediaPath = VideoUtils.getPath(mContext, uris.get(0));
          mExt = "mp4";
          HouLog.d(TAG, "视频path:" + mediaPath + " 类型:" + mExt);
          break;
        case VIDEO_RECORDING_CODE:
          Uri uri2 = data.getData();
          mediaPath = FileUtils.getFileAbsolutePath(UploadTrainActivity.this, uri2);
          mExt = "mp4";
          if (!TextUtils.isEmpty(mediaPath)){
            HouLog.d(TAG, "录像path:" + mediaPath + " 类型:" + mExt);
          }
          break;
        default:
          break;
      }
      mediaImageLayoutShow(mExt);
    }
  }

  //初始化试题布局
  private void initTestLayout() {
    test1 = findViewById(R.id.train_upload_test1);
    test1Name = (EditText) test1.findViewById(R.id.train_upload_test_name);
    test1A = (EditText) test1.findViewById(R.id.train_upload_test_a);
    test1B = (EditText) test1.findViewById(R.id.train_upload_test_b);
    test1C = (EditText) test1.findViewById(R.id.train_upload_test_c);
    test1D = (EditText) test1.findViewById(R.id.train_upload_test_d);
    test1Answer = (TextView) test1.findViewById(R.id.train_upload_test_answer);
    test1Title = (TextView) test1.findViewById(R.id.train_upload_test_title);
    test1Title.setText(getInfo(R.string.test1));
    imageBtn1 = (RelativeLayout) test1.findViewById(R.id.train_upload_test_select);
    imageBtn1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectBankList(selects, test1Answer);
      }
    });

    test2 = findViewById(R.id.train_upload_test2);
    test2Name = (EditText) test2.findViewById(R.id.train_upload_test_name);
    test2A = (EditText) test2.findViewById(R.id.train_upload_test_a);
    test2B = (EditText) test2.findViewById(R.id.train_upload_test_b);
    test2C = (EditText) test2.findViewById(R.id.train_upload_test_c);
    test2D = (EditText) test2.findViewById(R.id.train_upload_test_d);
    test2Answer = (TextView) test2.findViewById(R.id.train_upload_test_answer);
    test2Title = (TextView) test2.findViewById(R.id.train_upload_test_title);
    test2Title.setText(getInfo(R.string.test2));
    imageBtn2 = (RelativeLayout) test2.findViewById(R.id.train_upload_test_select);
    imageBtn2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectBankList(selects, test2Answer);
      }
    });

    test3 = findViewById(R.id.train_upload_test3);
    test3Name = (EditText) test3.findViewById(R.id.train_upload_test_name);
    test3A = (EditText) test3.findViewById(R.id.train_upload_test_a);
    test3B = (EditText) test3.findViewById(R.id.train_upload_test_b);
    test3C = (EditText) test3.findViewById(R.id.train_upload_test_c);
    test3D = (EditText) test3.findViewById(R.id.train_upload_test_d);
    test3Answer = (TextView) test3.findViewById(R.id.train_upload_test_answer);
    test3Title = (TextView) test3.findViewById(R.id.train_upload_test_title);
    test3Title.setText(getInfo(R.string.test3));
    imageBtn3 = (RelativeLayout) test3.findViewById(R.id.train_upload_test_select);
    imageBtn3.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectBankList(selects, test3Answer);
      }
    });

    test4 = findViewById(R.id.train_upload_test4);
    test4Name = (EditText) test4.findViewById(R.id.train_upload_test_name);
    test4A = (EditText) test4.findViewById(R.id.train_upload_test_a);
    test4B = (EditText) test4.findViewById(R.id.train_upload_test_b);
    test4C = (EditText) test4.findViewById(R.id.train_upload_test_c);
    test4D = (EditText) test4.findViewById(R.id.train_upload_test_d);
    test4Answer = (TextView) test4.findViewById(R.id.train_upload_test_answer);
    test4Title = (TextView) test4.findViewById(R.id.train_upload_test_title);
    test4Title.setText(getInfo(R.string.test4));
    imageBtn4 = (RelativeLayout) test4.findViewById(R.id.train_upload_test_select);
    imageBtn4.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectBankList(selects, test4Answer);
      }
    });

    test5 = findViewById(R.id.train_upload_test5);
    test5Name = (EditText) test5.findViewById(R.id.train_upload_test_name);
    test5A = (EditText) test5.findViewById(R.id.train_upload_test_a);
    test5B = (EditText) test5.findViewById(R.id.train_upload_test_b);
    test5C = (EditText) test5.findViewById(R.id.train_upload_test_c);
    test5D = (EditText) test5.findViewById(R.id.train_upload_test_d);
    test5Answer = (TextView) test5.findViewById(R.id.train_upload_test_answer);
    test5Title = (TextView) test5.findViewById(R.id.train_upload_test_title);
    test5Title.setText(getInfo(R.string.test5));
    imageBtn5 = (RelativeLayout) test5.findViewById(R.id.train_upload_test_select);
    imageBtn5.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        selectBankList(selects, test5Answer);
      }
    });
  }

  @Override
  public void loadData() {

  }

}
