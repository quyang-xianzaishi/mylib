package com.example.administrator.lubanone.activity.training.activity;

import static com.example.administrator.lubanone.R.id.train_upload_rl;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
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
import com.example.administrator.lubanone.utils.FileUtils;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
 * Created by hou on 2017/8/5.
 */

public class TrainUploadActivity extends BaseActivity {

  private static final String TAG = "TrainUploadActivity";
  private static final int VIDEO_CODE = 11;
  private static final int AUDIO_CODE = 22;
  private static final int CAMERA_CODE = 125;

  private RelativeLayout titleBar;
  private EditText trainTheme;
  private EditText trainSummary;
  private TextView trainSelect;
  private TextView trainCommit;
  private LinearLayout backBtn;
  private TextView mediaName;
  private EditText test1Name, test1A, test1B, test1C, test1D;
  private EditText test2Name, test2A, test2B, test2C, test2D;
  private EditText test3Name, test3A, test3B, test3C, test3D;
  private EditText test4Name, test4A, test4B, test4C, test4D;
  private EditText test5Name, test5A, test5B, test5C, test5D;
  private TextView test1Title, test2Title, test3Title, test4Title, test5Title;
  private TextView test1Answer, test2Answer, test3Answer, test4Answer, test5Answer;

  private TrainTestModel testList = new TrainTestModel();
  private List<TrainTestQuestions> testlists = new ArrayList<>();

  private boolean hasBlankTest = false;
  private String testId = "";

  private String trainId = "1";
  private String mediaPath = "";//音视频路径
  private String mExt;//音视频类型
  private int chunkNum = 1;//音频上传时流块总数参数
  private int chunk = 0;//流块
  private int chunks = 100;//流块总数
  private int blockLength = 1024 * 1024;//每个块的大小
  private File mediaFile = null;//原始视频文件
  private File fileDir = new File("/mnt/sdcard/chunks");//存放分块后的视频文件
  private ProgressLoadingDialog progressBarDialog;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.acitivity_train_upload;
  }

  @Override
  public void initView() {

    backBtn = (LinearLayout) findViewById(R.id.train_upload_back_icon);
    titleBar = (RelativeLayout) findViewById(train_upload_rl);
    trainTheme = (EditText) findViewById(R.id.train_upload_theme);
    trainSummary = (EditText) findViewById(R.id.train_upload_summary);
    trainSelect = (TextView) findViewById(R.id.train_upload_select);
    trainCommit = (TextView) findViewById(R.id.train_upload_commit);
    mediaName = (TextView) findViewById(R.id.train_upload_media_name);

    backBtn.setOnClickListener(this);
    trainSelect.setOnClickListener(this);
    trainCommit.setOnClickListener(this);

    ViewGroup test1 = (ViewGroup) findViewById(R.id.train_test1);
    test1Name = (EditText) test1.findViewById(R.id.train_upload_test_name);
    test1A = (EditText) test1.findViewById(R.id.train_upload_test_a);
    test1B = (EditText) test1.findViewById(R.id.train_upload_test_b);
    test1C = (EditText) test1.findViewById(R.id.train_upload_test_c);
    test1D = (EditText) test1.findViewById(R.id.train_upload_test_d);
    test1Answer = (TextView) test1.findViewById(R.id.train_upload_test_answer);
    test1Title = (TextView) test1.findViewById(R.id.train_upload_test_title);
    test1Title.setText(getInfo(R.string.test1));

    ViewGroup test2 = (ViewGroup) findViewById(R.id.train_test2);
    test2Name = (EditText) test2.findViewById(R.id.train_upload_test_name);
    test2A = (EditText) test2.findViewById(R.id.train_upload_test_a);
    test2B = (EditText) test2.findViewById(R.id.train_upload_test_b);
    test2C = (EditText) test2.findViewById(R.id.train_upload_test_c);
    test2D = (EditText) test2.findViewById(R.id.train_upload_test_d);
    test2Answer = (TextView) test2.findViewById(R.id.train_upload_test_answer);
    test2Title = (TextView) test2.findViewById(R.id.train_upload_test_title);
    test2Title.setText(getInfo(R.string.test2));

    ViewGroup test3 = (ViewGroup) findViewById(R.id.train_test3);
    test3Name = (EditText) test3.findViewById(R.id.train_upload_test_name);
    test3A = (EditText) test3.findViewById(R.id.train_upload_test_a);
    test3B = (EditText) test3.findViewById(R.id.train_upload_test_b);
    test3C = (EditText) test3.findViewById(R.id.train_upload_test_c);
    test3D = (EditText) test3.findViewById(R.id.train_upload_test_d);
    test3Answer = (TextView) test3.findViewById(R.id.train_upload_test_answer);
    test3Title = (TextView) test3.findViewById(R.id.train_upload_test_title);
    test3Title.setText(getInfo(R.string.test3));

    ViewGroup test4 = (ViewGroup) findViewById(R.id.train_test4);
    test4Name = (EditText) test4.findViewById(R.id.train_upload_test_name);
    test4A = (EditText) test4.findViewById(R.id.train_upload_test_a);
    test4B = (EditText) test4.findViewById(R.id.train_upload_test_b);
    test4C = (EditText) test4.findViewById(R.id.train_upload_test_c);
    test4D = (EditText) test4.findViewById(R.id.train_upload_test_d);
    test4Answer = (TextView) test4.findViewById(R.id.train_upload_test_answer);
    test4Title = (TextView) test4.findViewById(R.id.train_upload_test_title);
    test4Title.setText(getInfo(R.string.test4));

    ViewGroup test5 = (ViewGroup) findViewById(R.id.train_test5);
    test5Name = (EditText) test5.findViewById(R.id.train_upload_test_name);
    test5A = (EditText) test5.findViewById(R.id.train_upload_test_a);
    test5B = (EditText) test5.findViewById(R.id.train_upload_test_b);
    test5C = (EditText) test5.findViewById(R.id.train_upload_test_c);
    test5D = (EditText) test5.findViewById(R.id.train_upload_test_d);
    test5Answer = (TextView) test5.findViewById(R.id.train_upload_test_answer);
    test5Title = (TextView) test5.findViewById(R.id.train_upload_test_title);
    test5Title.setText(getInfo(R.string.test5));

    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }
    progressBarDialog = new ProgressLoadingDialog(this);
    progressBarDialog.setLabel(getString(R.string.progress_bar_loading));
    progressBarDialog.setCancelable(false);
  }

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

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 0://试题上传完，上传音频或者视频
          if (mExt.equals("mp3")) {
            uploadMedia();
          } else {
            uploadChunck();
          }
          break;
        case 1://续传
          uploadChunck();
          break;
      }
    }
  };

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
      HouLog.d("onError " + e.getMessage());
      ToastUtil.showShort("上传失败，请重试", mContext);
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
          setResult(RESULT_OK);
          finish();
        }
      } else {
        progressBarDialog.dismiss();
        HouLog.d(rb.getMsg());
        chunk = 0;
        showTipAlert();
        ToastUtil.showShort("上传失败，请重试", mContext);
      }

    }
  }

  //取消视频上传
  private void cancelUpload() {
    showLoadingDialog();
    Subscriber subscriber = new Subscriber<BaseModel>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
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

  //视频续传
  private void continueUpload() {
    showLoadingDialog();
    Subscriber subscriber = new Subscriber<TrainContinueUpload>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
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
          setResult(RESULT_OK);
          finish();
        } else {
          mHandler.sendEmptyMessage(1);
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
    Subscriber subscriber = new Subscriber<BaseModel>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        dismissUploadingDialog();
        ToastUtil.showShort("上传失败", mContext);
        HouLog.d(TAG, "onError: " + e.getMessage());
      }

      @Override
      public void onNext(BaseModel baseModel) {
        dismissUploadingDialog();
        ToastUtil.showShort("上传成功", mContext);
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

  //上传试题
  private void uploadTest() {
    Subscriber subscriber = new Subscriber<TrainUploadModel>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        dismissUploadingDialog();
        ToastUtil.showShort(e.getMessage(), mContext);
        HouLog.d("试题onError: " + e.getMessage());
      }

      @Override
      public void onNext(TrainUploadModel trainUploadModel) {
        HouLog.d("题目上传完的onNext");
        if (trainUploadModel != null) {
          HouLog.d("trainId:" + trainUploadModel.getTrainid());
          trainId = trainUploadModel.getTrainid();
          if (!TextUtils.isEmpty(trainId)) {
            mHandler.sendEmptyMessage(0);
          } else {
            //trainId:null
            ToastUtil.showShort("上传失败请重试", mContext);
          }
        } else {
          //result:null
          ToastUtil.showShort("上传失败请重试", mContext);
        }
      }
    };
    if (TextUtils.isEmpty(trainTheme.getText())) {
      ToastUtil.showShort("培训主题不能为空", mContext);
      return;
    }
    if (TextUtils.isEmpty(trainSummary.getText())) {
      ToastUtil.showShort("培训简介不能为空", mContext);
      return;
    }
    if (TextUtils.isEmpty(mediaPath)) {
      ToastUtil.showShort("请选择上传文件", mContext);
      return;
    }
    getTests();
    if (hasBlankTest) {
      ToastUtil.showShort("问题" + testId + "中有空白", mContext);
      return;
    }
    if (testlists.size() < 3) {
      ToastUtil.showShort("问题不能小于3题", mContext);
      return;
    }
    String testJsonString = JSON.toJSONString(testList);
    HouLog.d("testJsonString:", testJsonString);
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("theme", trainTheme.getText().toString());
    params.put("notice", trainSummary.getText().toString());
    params.put("test", testJsonString);
    params.put("testnum", String.valueOf(testlists.size()));
    HouLog.d(TAG + "参数:", params.toString());
    showUploadingDialog();
    MyApplication.rxNetUtils.getTrainService()
        .uploadData(params)
        .map(new BaseModelFunc<TrainUploadModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  private void getTests() {
    if (testlists != null && testlists.size() > 0) {
      testlists.clear();
    }

    dealWithTest("1", test1Name.getText().toString(), test1A.getText().toString(),
        test1B.getText().toString(), test1C.getText().toString(), test1D.getText().toString(),
        test1Answer.getText().toString());

    dealWithTest("2", test2Name.getText().toString(), test2A.getText().toString(),
        test2B.getText().toString(), test2C.getText().toString(), test2D.getText().toString(),
        test2Answer.getText().toString());

    dealWithTest("3", test3Name.getText().toString(), test3A.getText().toString(),
        test3B.getText().toString(), test3C.getText().toString(), test3D.getText().toString(),
        test3Answer.getText().toString());

    dealWithTest("4", test4Name.getText().toString(), test4A.getText().toString(),
        test4B.getText().toString(), test4C.getText().toString(), test4D.getText().toString(),
        test4Answer.getText().toString());

    dealWithTest("5", test5Name.getText().toString(), test5A.getText().toString(),
        test5B.getText().toString(), test5C.getText().toString(), test5D.getText().toString(),
        test5Answer.getText().toString());

//    if (!TextUtils.isEmpty(test1Name.getText()) && !TextUtils.isEmpty(test1A.getText())
//        && !TextUtils.isEmpty(test1B.getText()) && !TextUtils.isEmpty(test1C.getText())
//        && !TextUtils.isEmpty(test1D.getText()) && !TextUtils.isEmpty(test1Answer.getText())) {
//      testlists.add(
//          new TrainTestQuestions(test1Name.getText().toString(), test1A.getText().toString(),
//              test1B.getText().toString(), test1C.getText().toString(), test1D.getText().toString(),
//              test1Answer.getText().toString()));
//
//    } else if (TextUtils.isEmpty(test1Name.getText()) && TextUtils.isEmpty(test1A.getText())
//        && TextUtils.isEmpty(test1B.getText()) && TextUtils.isEmpty(test1C.getText())
//        && TextUtils.isEmpty(test1D.getText()) && TextUtils.isEmpty(test1Answer.getText())) {
//
//    } else {
//      ToastUtil.showShort("问题1中有空白", mContext);
//    }
//
//    if (!TextUtils.isEmpty(test2Name.getText()) && !TextUtils.isEmpty(test2A.getText())
//        && !TextUtils.isEmpty(test2B.getText()) && !TextUtils.isEmpty(test2C.getText())
//        && !TextUtils.isEmpty(test2D.getText()) && !TextUtils.isEmpty(test2Answer.getText())) {
//      testlists.add(
//          new TrainTestQuestions(test2Name.getText().toString(), test2A.getText().toString(),
//              test2B.getText().toString(), test2C.getText().toString(), test2D.getText().toString(),
//              test2Answer.getText().toString()));
//    } else if (TextUtils.isEmpty(test2Name.getText()) && TextUtils.isEmpty(test2A.getText())
//        && TextUtils.isEmpty(test2B.getText()) && TextUtils.isEmpty(test2C.getText())
//        && TextUtils.isEmpty(test2D.getText()) && TextUtils.isEmpty(test2Answer.getText())) {
//
//    } else {
//      ToastUtil.showShort("问题2中有空白", mContext);
//    }
//
//    if (!TextUtils.isEmpty(test3Name.getText()) && !TextUtils.isEmpty(test3A.getText())
//        && !TextUtils.isEmpty(test3B.getText()) && !TextUtils.isEmpty(test3C.getText())
//        && !TextUtils.isEmpty(test3D.getText()) && !TextUtils.isEmpty(test3Answer.getText())) {
//      testlists.add(
//          new TrainTestQuestions(test3Name.getText().toString(), test3A.getText().toString(),
//              test3B.getText().toString(), test3C.getText().toString(), test3D.getText().toString(),
//              test3Answer.getText().toString()));
//    } else {
//      ToastUtil.showShort("问题3中有空白", mContext);
//    }
//
//    if (!TextUtils.isEmpty(test4Name.getText()) && !TextUtils.isEmpty(test4A.getText())
//        && !TextUtils.isEmpty(test4B.getText()) && !TextUtils.isEmpty(test4C.getText())
//        && !TextUtils.isEmpty(test4D.getText()) && !TextUtils.isEmpty(test4Answer.getText())) {
//      testlists.add(
//          new TrainTestQuestions(test4Name.getText().toString(), test4A.getText().toString(),
//              test4B.getText().toString(), test4C.getText().toString(), test4D.getText().toString(),
//              test4Answer.getText().toString()));
//    } else {
//      ToastUtil.showShort("问题4中有空白", mContext);
//    }
//
//    if (!TextUtils.isEmpty(test5Name.getText()) && !TextUtils.isEmpty(test5A.getText())
//        && !TextUtils.isEmpty(test5B.getText()) && !TextUtils.isEmpty(test5C.getText())
//        && !TextUtils.isEmpty(test5D.getText()) && !TextUtils.isEmpty(test5Answer.getText())) {
//      testlists.add(
//          new TrainTestQuestions(test5Name.getText().toString(), test5A.getText().toString(),
//              test5B.getText().toString(), test5C.getText().toString(), test5D.getText().toString(),
//              test5Answer.getText().toString()));
//    } else {
//      ToastUtil.showShort("问题5中有空白", mContext);
//    }

    testList.setTest(testlists);
  }

  private void dealWithTest(String testId, String question, String a, String b, String c, String d,
      String answer) {
    if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(a)
        && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(c)
        && !TextUtils.isEmpty(d) && !TextUtils.isEmpty(answer)) {
      HouLog.d(testId + " 1");
      testlists.add(
          new TrainTestQuestions(question, a, b, c, d, answer));
      hasBlankTest = false;
    } else if (TextUtils.isEmpty(question) && TextUtils.isEmpty(a)
        && TextUtils.isEmpty(b) && TextUtils.isEmpty(c)
        && TextUtils.isEmpty(d) && TextUtils.isEmpty(answer)) {
      HouLog.d(testId + " 2");
    } else {
      HouLog.d(testId + " 3");
      ToastUtil.showShort("问题" + testId + "中有空白", mContext);
      hasBlankTest = true;
      this.testId = testId;
    }
  }

  private String getDownLoadPercent() {
    String baifenbi = "0";// 接受百分比的值
    if (chunk >= chunks) {
      return "100";
    }
    double baiy = chunk * 1.0;
    double baiz = chunks * 1.0;
    // 防止分母为0出现NoN
    if (baiz > 0) {
      double fen = (baiy / baiz) * 100;
      //NumberFormat nf = NumberFormat.getPercentInstance();
      //nf.setMinimumFractionDigits(2); //保留到小数点后几位
      // 百分比格式，后面不足2位的用0补齐
      //baifenbi = nf.format(fen);
      //注释掉的也是一种方法
      DecimalFormat df1 = new DecimalFormat("0");//0.00
      baifenbi = df1.format(fen);
    }
    return baifenbi;
  }

  @Override
  protected void onResume() {
    super.onResume();
    titleBar.setFocusable(true);
    titleBar.setFocusableInTouchMode(true);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_upload_commit:
        uploadTest();
        break;
      case R.id.train_upload_back_icon:
        finish();
        break;
      case R.id.train_upload_select:
        choosePic();
//        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//        albumIntent.setType("video/*;audio/*");
//        startActivityForResult(albumIntent, 1);
        break;
      default:
        break;
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
          if (isAllGranted) {
            showSelectDialog();
          } else {
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

  private void showSelectDialog() {
    ArrayList<String> list = new ArrayList<>();
    list.add(getInfo(R.string.upload_select_video));
    list.add(getInfo(R.string.upload_select_audio));

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

    if (0 == position) {
      Matisse.from(this)
          .choose(MimeType.ofVideo())
          .countable(false)
          .maxSelectable(1)
          .theme(R.style.Matisse_Dracula)
          .forResult(VIDEO_CODE);
    }
    if (1 == position) {
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("audio/*");
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      startActivityForResult(intent, AUDIO_CODE);
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case AUDIO_CODE:
          Uri uri = data.getData();
          mediaPath = FileUtils.getFileAbsolutePath(TrainUploadActivity.this, uri);
          mExt = "mp3";
          HouLog.d(TAG, "path:" + mediaPath + " 类型:" + mExt);
          break;
        case VIDEO_CODE:
          List<Uri> uris = Matisse.obtainResult(data);
          mediaPath = VideoUtils.getPath(mContext, uris.get(0));

          mExt = "mp4";
          HouLog.d(TAG, "path:" + mediaPath + " 类型:" + mExt);
          break;
        default:
          break;
      }
      String[] temp = mediaPath.split("/");
      mediaName.setText(temp[temp.length - 1]);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    FileUtils.deleteDirWihtFile(fileDir);
  }
}
