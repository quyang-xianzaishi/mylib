package com.example.administrator.lubanone.activity.task;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.PhotoAdapter;
import com.example.administrator.lubanone.interfaces.LccImageCallback;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.utils.Compress4JPEG;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
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
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/6/30.
 */

public class TaskUploadPictureActivity extends BaseActivity {

  private static final String TAG = "TaskUploadPictureActivity";
  private static final int CAMERA_CODE = 125;
  private static final int IMAGE_CODE = 11;

  private TextView commitPic;
  private LinearLayout backBtn;
  private ImageView uploadImageAdd;
  private ArrayList<String> imagePaths = null;
  private ArrayList<File> imageFiles = new ArrayList<>();
  private ArrayList<File> imageCompressedFiles = new ArrayList<>();
  private RecyclerView mRecyclerView;
  private String task_if_id;//详情页面的任务id
  //选取单张
  private String imagePath;
  private File imageFile;

  private PhotoAdapter photoAdapter;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_task_upload_img;
  }

  @Override
  public void initView() {
    commitPic = (TextView) findViewById(R.id.task_upload_picture_commit);
    backBtn = (LinearLayout) findViewById(R.id.task_upload_image_back_icon);
    uploadImageAdd = (ImageView) findViewById(R.id.task_upload_image_iv);

    uploadImageAdd.setOnClickListener(this);
    backBtn.setOnClickListener(this);
    commitPic.setOnClickListener(this);
    imagePaths = new ArrayList<>();
    Intent intent = getIntent();
    task_if_id = intent.getStringExtra("task_if_id");

    mRecyclerView = (RecyclerView) findViewById(R.id.task_upload_picture_recyclerView);
    photoAdapter = new PhotoAdapter(mContext, imagePaths,true);
    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
    mRecyclerView.setAdapter(photoAdapter);

    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {
            if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
              PhotoPicker.builder()
                  .setPhotoCount(PhotoAdapter.MAX)
                  .setShowCamera(true)
                  .setPreviewEnabled(false)
                  .setSelected(imagePaths)
                  .start(TaskUploadPictureActivity.this);
            } else {
              PhotoPreview.builder()
                  .setPhotos(imagePaths)
                  .setCurrentItem(position)
                  .start(TaskUploadPictureActivity.this);
            }
          }
        }));

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.task_upload_picture_commit://提交按钮
        if (imageFile != null) {
          uploadImage();
        } else {
          HouLog.d(TAG, "上传文件imageFile为空");
        }
        break;
      case R.id.task_upload_image_back_icon://返回按钮
        finish();
//        CompressImage();
//        getMaxMemoryInfo();
        break;
      case R.id.task_upload_image_iv://图片添加按钮
        checkPermission();
        break;
    }
  }

  //上传单个文件
  private void uploadImage() {
    showUploadingDialog();
    Subscriber subscriber = new Subscriber<List<String>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        dismissUploadingDialog();
        ToastUtil.showShort("上传失败", mContext);
        HouLog.d(TAG, e.getMessage());
      }

      @Override
      public void onNext(List<String> strings) {
        dismissUploadingDialog();
        setResult(RESULT_OK);
        finish();
      }
    };
    HouLog.d(TAG, "上传图片参数:" + " 任务id" + task_if_id);

    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
    MultipartBody.Part part = MultipartBody.Part
        .createFormData("file", imageFile.getName(), requestBody);
    RequestBody token = RequestBody
        .create(MediaType.parse("text/plain"), SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestBody ifId = RequestBody.create(MediaType.parse("text/plain"), task_if_id);
    myApp.rxNetUtils.getTaskService().uploadImage(token, ifId, part)
        .map(new BaseModelFunc<List<String>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        //
        case PhotoPicker.REQUEST_CODE:
        case PhotoPreview.REQUEST_CODE:
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
          break;
        //选择图片
        case IMAGE_CODE:
          List<Uri> uris = Matisse.obtainResult(data);
          imagePath = VideoUtils.getPath(this, uris.get(0));
          HouLog.d("得到的图片", imagePath);
          if (TextUitl.isNotEmpty(imagePath) && new File(imagePath).exists()) {
            startCrop(Uri.fromFile(new File(imagePath)));
          }
          break;
        case UCrop.REQUEST_CROP:
          HouLog.d(TAG + "裁剪成功！");
          Uri croppedFileUri = UCrop.getOutput(data);
          //获取默认的下载目录
          String downloadsDirectoryPath = Environment
              .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
          String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
              croppedFileUri.getLastPathSegment());
          imageFile = new File(downloadsDirectoryPath, filename);
          HouLog.d(TAG, "裁剪后的图片: 名称:" + imageFile.getName() + " 路径:" + imageFile.getAbsolutePath());
          //保存下载的图片
          FileInputStream inStream = null;
          FileOutputStream outStream = null;
          FileChannel inChannel = null;
          FileChannel outChannel = null;
          try {
            inStream = new FileInputStream(new File(croppedFileUri.getPath()));
            outStream = new FileOutputStream(imageFile);
            inChannel = inStream.getChannel();
            outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            HouLog.d(TAG,
                "裁剪后的图片2: 名称:" + imageFile.getName() + " 路径:" + imageFile.getAbsolutePath());
            Glide.with(this).load(imageFile).into(uploadImageAdd);

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
          break;

        case UCrop.RESULT_ERROR:
          HouLog.d(TAG + "裁剪失败！");
          break;
      }
    }

  }

  /**
   * 裁剪图片
   */
  private void startCrop(Uri uri) {
    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.Options options = new UCrop.Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);

    UCrop.of(uri, destinationUri).withOptions(options).withAspectRatio(1, 1)
        .withMaxResultSize(500, 500)
        .start(this);
  }

  /**
   * 选择图片
   */
  private void choseImage() {
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(false)
        .theme(R.style.Matisse_Dracula)
        .maxSelectable(1)
        .forResult(IMAGE_CODE);
  }

  /**
   * 检查权限
   */
  private void checkPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(this, new String[]{permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
      } else {
        choseImage();
      }
    } else {
      choseImage();
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
          if (isAllGranted) {
            choseImage();
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

  //压缩图片
  public void CompressImage() {
    imageCompressedFiles.clear();
    if (imagePaths.size() > 0) {
      HouLog.d("imagePaths.size()", imagePaths.size() + "");
      for (int i = 0; i < imagePaths.size(); i++) {
        imageFiles.add(new File(imagePaths.get(i)));
      }
      HouLog.d("imageFiles.size()", imageFiles.size() + "");
      Compress4JPEG.get(this).loadsFiles(imageFiles).setCompressListener(new LccImageCallback() {
        @Override
        public void onStart() {
          HouLog.d("压缩onStart", "gogogogo");
        }

        @Override
        public void onFileSuccess(File file) {
          imageCompressedFiles.add(file);
          HouLog.d("压缩后的文件个数", imageCompressedFiles.size() + "");
        }

        @Override
        public void onError(Throwable e) {
          HouLog.d("onError", e.getLocalizedMessage());
        }
      }).launch();
    }

  }

  //获取手机为应用分配内存
  private void getMaxMemoryInfo() {
    Runtime rt = Runtime.getRuntime();
    long maxMemory = rt.maxMemory();
    Log.e("MaxMemory:", Long.toString(maxMemory / (1024 * 1024)));
    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    Log.e("MemoryClass:", Long.toString(activityManager.getMemoryClass()));
    Log.e("LargeMemoryClass:", Long.toString(activityManager.getLargeMemoryClass()));
  }

  @Override
  public void loadData() {

  }
}


