package com.example.administrator.lubanone.activity.message;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.administrator.lubanone.interfaces.LccImageCallback;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.Compress4JPEG;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\8\4 0004.
 */

public class UploadComplainEvidenceActivity extends BaseActivity {

  private TextView commitPic;
  private LinearLayout backBtn;
  private ArrayList<String> imagePaths = null;
  private ArrayList<File> imageFiles = new ArrayList<>();
  private ArrayList<File> imageCompressedFiles = new ArrayList<>();
  private RecyclerView mRecyclerView;
  private String complainMemberId;//投诉成员id
  private String reasonId;//投诉原因id

  private PhotoAdapter photoAdapter;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_upload_complain_evidence;
  }

  @Override
  public void initView() {

    commitPic = (TextView) findViewById(R.id.task_upload_picture_commit);
    backBtn = (LinearLayout) findViewById(R.id.task_upload_image_back_icon);
    backBtn.setOnClickListener(this);
    commitPic.setOnClickListener(this);
    imagePaths = new ArrayList<>();
    if (getIntent() != null && getIntent().hasExtra("complainId")) {
      complainMemberId = getIntent().getStringExtra("complainId");
    }

    if (getIntent() != null && getIntent().hasExtra("reasonId")) {
      reasonId = getIntent().getStringExtra("reasonId");
    }

    mRecyclerView = (RecyclerView) findViewById(R.id.task_upload_picture_recyclerView);
    photoAdapter = new PhotoAdapter(this, imagePaths, true);
    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
    mRecyclerView.setAdapter(photoAdapter);
    photoAdapter.setOnDeleteImageListener(new OnDeleteImageListener() {
      @Override
      public void onDeleteImage(int position) {
        imagePaths.remove(position);
        if (imagePaths.size() > 0) {
          photoAdapter.notifyDataSetChanged();
        }
      }
    });

    /*mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {
            if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
              PhotoPicker.builder()
                  .setPhotoCount(PhotoAdapter.MAX)
                  .setShowCamera(true)
                  .setPreviewEnabled(false)
                  .setSelected(imagePaths)
                  .start(UploadComplainEvidenceActivity.this);
            } else {
              PhotoPreview.builder()
                  .setPhotos(imagePaths)
                  .setCurrentItem(position)
                  .start(UploadComplainEvidenceActivity.this);
            }
          }
        }));*/

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.task_upload_picture_commit:
        if (imagePaths.size() > 0) {
          showUploadingDialog();
          uploadImageFromPath();
          //ToastUtil.showShort("开始上传投诉凭证", mContext);
        } else {
          ToastUtil.showShort("请添加图片", mContext);
        }
        break;
      case R.id.task_upload_image_back_icon:
        finish();
        break;
      default:
        break;
    }
  }

  private void uploadImageFromPath() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsUserId = new RequestParams("userid", complainMemberId);
    RequestParams paramsTypeId = new RequestParams("typeid", reasonId);
    list.add(paramsToken);
    list.add(paramsUserId);
    list.add(paramsTypeId);
    MyApplication.requestNet.uploadImageByPostFromPath(imagePaths, list,
        Urls.COMPLAIN, new RequestListener() {
          @Override
          public void onSuccess(JSONObject jsonObject) {

          }

          @Override
          public void testSuccess(String jsonObject) {
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = jsonObject;
            mHandler.sendMessage(msg);
          }

          @Override
          public void onFail(String errorMsf) {
            Message msg = mHandler.obtainMessage();
            msg.what = 0;
            msg.obj = errorMsf;
            mHandler.sendMessage(msg);
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
              Log.d("test======", "Main Thread");
            } else {
              Log.d("test======", "Not Main Thread");
            }
          }
        });
  }

  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 1://成功
          dismissUploadingDialog();
          String json = (String) msg.obj;
          HouLog.d("上传图片json", json);
          RequestBean rb = new RequestBean(json);
          if (rb.getType().equals("1")) {
            setResult(RESULT_OK);
            Toast.makeText(UploadComplainEvidenceActivity.this,
                getString(R.string.upload_complain_evidence_success), Toast.LENGTH_LONG).show();
            finish();
          } else {
            Toast.makeText(UploadComplainEvidenceActivity.this,
                getString(R.string.upload_complain_evidence_fail), Toast.LENGTH_LONG).show();
          }
          break;
        case 0://失败
          dismissUploadingDialog();
          Toast.makeText(UploadComplainEvidenceActivity.this, (String) msg.obj, Toast.LENGTH_LONG)
              .show();
          break;
      }
      super.handleMessage(msg);
    }
  };

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK &&
        (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

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

}
