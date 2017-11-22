package com.example.administrator.lubanone.activity.us;

import static com.example.administrator.lubanone.MyApplication.rxNetUtils;

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
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.PhotoAdapter;
import com.example.administrator.lubanone.adapter.PhotoAdapter.OnDeleteImageListener;
import com.example.administrator.lubanone.bean.RequestBean;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.TrainContinueUpload;
import com.example.administrator.lubanone.bean.model.UploadPicture;
import com.example.administrator.lubanone.bean.model.UsSpreadUploadTextModel;
import com.example.administrator.lubanone.customview.progressdialog.ProgressLoadingDialog;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.FileUtils;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
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
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.json.JSONObject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/7/3.
 */

public class UsSpreadUploadActivity extends BaseActivity {

  private static final int CAMERA_CODE = 125;
  private static final int IMAGE_CODE = 11;
  private static final int VIDEO_RECORDING_CODE = 112;
  private static final int VIDEO_CODE = 22;
  private static final int UPLOAD_TEXT_FINISH_CODE = 33;
  private static final int IMAGE_UPLOAD_SUCCESS = 44;
  private static final int IMAGE_UPLOAD_FAIL = 55;

  private EditText editTitle;//标题
  private EditText editContent;//内容简介
  private EditText editText;//正文
  private LinearLayout addLinearLayout;//添加视频/图片的布局
  private RelativeLayout imageAdd;//添加图片按钮
  private RelativeLayout videoAdd;//添加视频按钮
  private RecyclerView mRecyclerView;//图片展示布局
  private FrameLayout videoShowLayout;//视频展示布局
  private ImageView videoShowImage;//视频缩略图
  private ImageView videoShowDelete;//删除视频展示
  private TextView commit;//提交按钮
  private ArrayList<String> imagePaths;//图片路径集
  private LinearLayout backBtn;
  private RecyclerView newRecyclerView;
  private PhotoAdapter photoAdapter;

  private String if_id;
  private String videoPath = "";//视频路径
  private String mExt;//音视频类型
  private int chunk = 0;//流块
  private int chunks = 100;//流块总数
  private int blockLength = 1024 * 1024;//每个块的大小
  private File mediaFile = null;//原始视频文件
  private File fileDir = new File("/mnt/sdcard/chunks");//存放分块后的视频文件
  private ProgressLoadingDialog progressBarDialog;

  //上传参数
  private String mTitle;
  private String mType;
  private String mIntroduction;
  private String mContent;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_us_spread_upload;
  }

  @Override
  public void initView() {
    editTitle = (EditText) findViewById(R.id.us_spread_upload_title_edit);
    editContent = (EditText) findViewById(R.id.us_spread_upload_content_edit);
    editText = (EditText) findViewById(R.id.us_spread_upload_main_text_edit);
    addLinearLayout = (LinearLayout) findViewById(R.id.us_spread_upload_add_ll);
    imageAdd = (RelativeLayout) findViewById(R.id.us_spread_upload_image_add);
    videoAdd = (RelativeLayout) findViewById(R.id.us_spread_upload_video_add);
    mRecyclerView = (RecyclerView) findViewById(R.id.us_spread_upload_recycler_view);
    commit = (TextView) findViewById(R.id.us_spread_upload_content_commit);
    backBtn = (LinearLayout) findViewById(R.id.us_spread_upload_back);
    videoShowImage = (ImageView) findViewById(R.id.us_spread_upload_video_image);
    videoShowLayout = (FrameLayout) findViewById(R.id.us_spread_upload_frame_layout);
    videoShowDelete = (ImageView) findViewById(R.id.us_spread_upload_delete_video_image);
    newRecyclerView = (RecyclerView) findViewById(R.id.us_spread_upload_new_recycler_view);

    videoShowDelete.setOnClickListener(this);
    imageAdd.setOnClickListener(this);
    videoAdd.setOnClickListener(this);
    backBtn.setOnClickListener(this);
    commit.setOnClickListener(this);
    imagePaths = new ArrayList<>();

    photoAdapter = new PhotoAdapter(this, imagePaths, false);
    newRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
    newRecyclerView.setAdapter(photoAdapter);

//    mAdapter = new UsUploadImageAdapter(imagePaths, mContext);
//    mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
//    mRecyclerView.setAdapter(mAdapter);
//
//    mAdapter.setOnAddImageListener(new OnAddImageListener() {
//      @Override
//      public void onAddImage() {
//        choseImage();
//      }
//    });
    photoAdapter.setOnDeleteImageListener(new OnDeleteImageListener() {
      @Override
      public void onDeleteImage(int position) {
        imagePaths.remove(position);
        if (imagePaths.size() > 0) {
          photoAdapter.notifyDataSetChanged();
        } else {
          newRecyclerView.setVisibility(View.GONE);
          addLinearLayout.setVisibility(View.VISIBLE);
        }
      }
    });
//    mAdapter.setOnDeleteImageListener(new OnDeleteImageListener() {
//      @Override
//      public void onDeleteImage(int position) {
//        imagePaths.remove(position);
//        if (imagePaths.size() > 0) {
//          mAdapter.notifyDataSetChanged();
//        } else {
//          mRecyclerView.setVisibility(View.GONE);
//          addLinearLayout.setVisibility(View.VISIBLE);
//        }
//      }
//    });

    checkPermission();
    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }
    progressBarDialog = new ProgressLoadingDialog(this);
    progressBarDialog.setLabel(getString(R.string.progress_bar_loading));
    progressBarDialog.setCancelable(false);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      //上传
      case R.id.us_spread_upload_content_commit:
        clickCommit();
        break;
      //返回键
      case R.id.us_spread_upload_back:
        finish();
        break;
      //图片选择
      case R.id.us_spread_upload_image_add:
        choseImage();
        break;
      //视频选择
      case R.id.us_spread_upload_video_add:
        showSelectDialog();
        break;
      //视频删除
      case R.id.us_spread_upload_delete_video_image:
        imageAdd.setVisibility(View.VISIBLE);
        videoAdd.setVisibility(View.VISIBLE);
        videoShowLayout.setVisibility(View.GONE);
        videoPath = "";
        break;
      default:
        break;
    }
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
      choseVideo();
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
   * 选择图片
   */
  private void choseImage() {
//    Matisse.from(this)
//        .choose(MimeType.ofImage())
//        .countable(false)
//        .theme(R.style.Matisse_Dracula)
//        .maxSelectable(9)
//        .forResult(IMAGE_CODE);
    PhotoPicker.builder()
        .setPhotoCount(PhotoAdapter.MAX)
        .setShowCamera(true)
        .setPreviewEnabled(false)
        .start(this);
  }

  /**
   * 选择视频
   */
  private void choseVideo() {
    Matisse.from(this)
        .choose(MimeType.ofVideo())
        .countable(false)
        .theme(R.style.Matisse_Dracula)
        .maxSelectable(1)
        .forResult(VIDEO_CODE);
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

  //点击提交按钮
  private void clickCommit() {

    mTitle = editTitle.getText().toString();
    mIntroduction = editContent.getText().toString();
    mContent = editText.getText().toString();

    if (TextUtils.isEmpty(mTitle)) {
      HouToast.showLongToast(UsSpreadUploadActivity.this,
          getInfo(R.string.us_spread_upload_title_hint));
    } else if (TextUtils.isEmpty(mIntroduction)) {
      HouToast.showLongToast(UsSpreadUploadActivity.this,
          getInfo(R.string.us_spread_upload_content_hint));
    } else if (TextUtils.isEmpty(mContent)) {
      HouToast.showLongToast(UsSpreadUploadActivity.this,
          getInfo(R.string.us_spread_upload_main_text_hint));
    } else if (imagePaths.size() == 0 && TextUtils.isEmpty(videoPath)) {
      HouToast
          .showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.us_upload_add_image_video));
    } else {

      if (imagePaths.size() > 0) {
        mType = "images";
        //上传图片
        uploadTextImage();
      } else {
        mType = "video";
        //上传视频
        uploadText();
      }

      Log.d(getClass().getSimpleName(),
          "type:" + mType + "  title:" + mTitle + "  introduction:" + mIntroduction
              + "  content:"
              + mContent + "  imagepaths:" + imagePaths + "  videoPath:" + videoPath);

    }
  }

  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case IMAGE_UPLOAD_SUCCESS:
          dismissUploadingDialog();
          String json = (String) msg.obj;
          Log.d(getClass().getSimpleName() + "handler_json", json);
          RequestBean rb = new RequestBean(json);
          if (rb.getType().equals("1")) {
            //上传成功
            clearData();//清楚数据
            HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_success));
            finish();
          } else if (rb.getType().equals("0")) {
            HouLog.d("图片上传失败：" + rb.getMsg());
            HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_fail_and_again));
          } else if (rb.getType().equals("3")) {

          }
          break;
        case IMAGE_UPLOAD_FAIL:
          dismissUploadingDialog();
          HouLog.d("图片上传失败：" + msg.obj);
          HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_fail_and_again));
          break;
        case UPLOAD_TEXT_FINISH_CODE:
          uploadChunck();
          break;
      }
    }
  };

  private void clearData() {
    editTitle.setText("");
    editContent.setText("");
    editText.setText("");

    if (imagePaths.size() > 0) {
      imagePaths.clear();
      photoAdapter.notifyDataSetChanged();
      newRecyclerView.setVisibility(View.GONE);
      addLinearLayout.setVisibility(View.VISIBLE);
    }

    if (!TextUtils.isEmpty(videoPath)) {
      imageAdd.setVisibility(View.VISIBLE);
      videoShowLayout.setVisibility(View.GONE);
      videoPath = "";
    }

  }

  //上传图片
  private void uploadTextImage() {
    showUploadingDialog();
    List<RequestParams> list = new ArrayList<>();
    list.add(new RequestParams("token", SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, "")));
    list.add(new RequestParams("type", mType));
    list.add(new RequestParams("title", mTitle));
    list.add(new RequestParams("introduction", mIntroduction));
    list.add(new RequestParams("content", mContent));
    MyApplication.requestNet.uploadImageByPostFromPath(imagePaths, list,
        Urls.ROOT_URL + Urls.us_spread_upload, new RequestListener() {
          @Override
          public void onSuccess(JSONObject jsonObject) {

          }

          @Override
          public void testSuccess(String jsonObject) {
            Message msg = mHandler.obtainMessage();
            msg.what = IMAGE_UPLOAD_SUCCESS;
            msg.obj = jsonObject;
            mHandler.sendMessage(msg);
          }

          @Override
          public void onFail(String errorMsf) {
            Message msg = mHandler.obtainMessage();
            msg.what = IMAGE_UPLOAD_FAIL;
            msg.obj = errorMsf;
            mHandler.sendMessage(msg);
          }
        });
  }

  //上传文本内容
  private void uploadText() {
    showUploadingDialog();
    Subscriber subscriber = new MySubscriber<UsSpreadUploadTextModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissUploadingDialog();
        HouToast.showLongToast(UsSpreadUploadActivity.this, e.getMessage());
        HouLog.d("试题onError: " + e.toString());
      }

      @Override
      public void onNext(UsSpreadUploadTextModel usSpreadUploadTextModel) {
        HouLog.d("题目上传完的onNext");
        if (usSpreadUploadTextModel != null) {
          HouLog.d("trainId:" + usSpreadUploadTextModel.getIf_id());
          if_id = usSpreadUploadTextModel.getIf_id();
          if (!TextUtils.isEmpty(if_id)) {
            mHandler.sendEmptyMessage(UPLOAD_TEXT_FINISH_CODE);
          } else {
            //if_id:null
            HouToast.showLongToast(UsSpreadUploadActivity.this,
                getInfo(R.string.upload_fail_and_again));
          }
        } else {
          //result:null
          HouToast
              .showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_fail_and_again));
        }
      }
    };

    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("title", mTitle);
    params.put("introduction", mIntroduction);
    params.put("content", mContent);
    HouLog.d(TAG + "参数:", params.toString());
    rxNetUtils.getUploadService()
        .spreadUploadText(params)
        .map(new BaseModelFunc<UsSpreadUploadTextModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //获取上传的视频块
  private File getChunkFile() {

    mediaFile = new File(videoPath);
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
      mExt = "mp4";
      long len = FileUtils.getFileSize(file);
      HouLog.d("视频上传参数:" + if_id + " " + mExt + " " + chunks + " " + chunk + " " + file
          .getName() + " 文件大小:" + FileUtils.formatFileSize(len));

      OkHttpUtils.post()
          .addFile("video", "chunck.mp4", file)
          .url(Urls.ROOT_URL + "api.php/Home/news/uploadvideo")
          .addParams("token",
              SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""))
          .addParams("if_id", if_id)
          .addParams("ext", mExt)
          .addParams("chunknum", String.valueOf(chunks))
          .build()
          .execute(new myCallBack());
    } else {
      HouLog.d("chunkFile is null !!!");
    }
  }

  //分片上传结果回调
  class myCallBack extends StringCallback {

    @Override
    public void onError(Call call, Exception e, int id) {
      progressBarDialog.dismiss();
      HouLog.d("onError " + e.toString());
      HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_fail_and_again));
      chunk = 0;
      showTipAlert();
    }

    @Override
    public void onResponse(String response, int id) {
      HouLog.d("onResponse  " + response.toString());
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
          HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_success));
          finish();
//          HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_success));
        }
      } else {
        progressBarDialog.dismiss();
        HouLog.d(rb.getMsg());
        chunk = 0;
        showTipAlert();
        HouToast
            .showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_fail_and_again));
      }

    }
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

  //取消视频上传
  private void cancelUpload() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<CommonModel>(this) {
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
      public void onNext(CommonModel baseModel) {
        dismissLoadingDialog();

      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", if_id);
    HouLog.d("取消上传参数：" + params.toString());
    MyApplication.rxNetUtils.getUploadService().cancelUpload(params)
        .map(new BaseModelFunc<CommonModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
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
          HouToast.showLongToast(UsSpreadUploadActivity.this, getInfo(R.string.upload_success));
          finish();
        } else {
          mHandler.sendEmptyMessage(UPLOAD_TEXT_FINISH_CODE);
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", if_id);
    HouLog.d("续传参数：", params.toString());
    rxNetUtils.getUploadService().continueUpload(params)
        .map(new BaseModelFunc<TrainContinueUpload>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }


  //单片视频上传
  private void uploadTextVideo() {
    showUploadingDialog();
    Subscriber subscriber = new MySubscriber<UploadPicture>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissUploadingDialog();
        Log.d("推广上传界面error", e.toString());
        HouToast.showLongToast(UsSpreadUploadActivity.this, e.getMessage());
      }

      @Override
      public void onNext(UploadPicture uploadPicture) {
        dismissUploadingDialog();
        if (uploadPicture != null) {
          HouToast.showLongToast(UsSpreadUploadActivity.this, uploadPicture.getShow());
          Log.d("推广上传界面result", uploadPicture.getShow());
        }
      }
    };

    File videoFile = new File(videoPath);
    RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
    MultipartBody.Part part = MultipartBody.Part
        .createFormData("file", videoFile.getName(), requestBody);

    RequestBody token = RequestBody
        .create(MediaType.parse("text/plain"),
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestBody type = RequestBody.create(MediaType.parse("text/plain"), mType);
    RequestBody title = RequestBody.create(MediaType.parse("text/plain"), mTitle);
    RequestBody introduction = RequestBody.create(MediaType.parse("text/plain"), mIntroduction);
    RequestBody content = RequestBody.create(MediaType.parse("text/plain"), mContent);

    rxNetUtils.getUploadService()
        .usUploadVideo(token, type, title, introduction, content, part)
        .map(new BaseModelFunc<UploadPicture>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    List<Uri> uris;
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        //视频选择结果
        case VIDEO_CODE:
          uris = Matisse.obtainResult(data);
          videoPath = VideoUtils.getPath(this, uris.get(0));
          HouLog.d("选择的视频路径: ", videoPath);
          if (!TextUtils.isEmpty(videoPath)) {
            imageAdd.setVisibility(View.GONE);
            videoAdd.setVisibility(View.GONE);
            videoShowLayout.setVisibility(View.VISIBLE);
            videoShowImage.setImageBitmap(VideoUtils.getVideoThumbnail(videoPath));
          }
          break;
        //录像
        case VIDEO_RECORDING_CODE:
          Uri uri2 = data.getData();
          videoPath = FileUtils.getFileAbsolutePath(UsSpreadUploadActivity.this, uri2);
          if (!TextUtils.isEmpty(videoPath)) {
            HouLog.d(TAG, "录像path:" + videoPath);
            imageAdd.setVisibility(View.GONE);
            videoAdd.setVisibility(View.GONE);
            videoShowLayout.setVisibility(View.VISIBLE);
            videoShowImage.setImageBitmap(VideoUtils.getVideoThumbnail(videoPath));
          }
          break;
        //图片选择结果
        case IMAGE_CODE:
          uris = Matisse.obtainResult(data);
          ArrayList<String> paths = new ArrayList<>();
          for (int i = 0; i < uris.size(); i++) {
            paths.add(VideoUtils.getPath(this, uris.get(i)));
          }
          HouLog.d("选择的图片路径: ", paths.toString());
          if (imagePaths == null) {
            imagePaths = new ArrayList<>();
          }
          imagePaths.clear();
          imagePaths.addAll(paths);
          photoAdapter.notifyDataSetChanged();
          if (imagePaths.size() > 0) {
            addLinearLayout.setVisibility(View.GONE);
            newRecyclerView.setVisibility(View.VISIBLE);
          } else {
            addLinearLayout.setVisibility(View.VISIBLE);
            newRecyclerView.setVisibility(View.GONE);
          }

          break;
        case PhotoPreview.REQUEST_CODE:
        case PhotoPicker.REQUEST_CODE:
          List<String> photos = null;
          if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
          }
          imagePaths.clear();

          if (photos != null) {
            imagePaths.addAll(photos);
            HouLog.d("得到的图片", imagePaths.toString());
          }
          photoAdapter.notifyDataSetChanged();
          if (imagePaths.size() > 0) {
            addLinearLayout.setVisibility(View.GONE);
            newRecyclerView.setVisibility(View.VISIBLE);
          } else {
            addLinearLayout.setVisibility(View.VISIBLE);
            newRecyclerView.setVisibility(View.GONE);
          }
          break;
        default:
          break;
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    FileUtils.deleteDirWihtFile(fileDir);
  }
}
