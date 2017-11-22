package com.example.administrator.lubanone.activity.home;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.AliPay;
import com.example.administrator.lubanone.event.ZhiFuBaoAccountActivityEvent;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.UriUtils;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCrop.Options;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class AliActivity extends BaseActivity {

  @BindView(R.id.tv)
  TextView mTv;
  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.iv_waring)
  ImageView mIvWaring;
  @BindView(R.id.tv_tips)
  TextView mTvTips;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  private String mPay;
  @BindView(R.id.add)
  RelativeLayout add;
  @BindView(R.id.tv_name)
  TextView tv_name;
  @BindView(R.id.ll_container)
  LinearLayout ll_container;

  private static final int CAMERA_CODE = 125;
  private final int TAKE_PHOTO = 5;//拍照标记

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_ali;
  }

  @Override
  public void initView() {
    add.setOnClickListener(this);
    mBtnCommit.setOnClickListener(this);

    Intent intent = getIntent();
    if (null != intent) {
      String stringExtra = intent.getStringExtra("account");
      tv_name.setText(getString(R.string.zhifubao_account) + stringExtra);
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_commit:
        if (ll_container.getChildCount() > 2) {
          showMsg(getString(R.string.only_upload_one_erweima));
          return;
        }
        saveAndFinish();
        break;
      case R.id.add:
        choosePic();
        break;
    }

  }


  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }

  private void choosePic() {

    if (VERSION.SDK_INT >= VERSION_CODES.M) {

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
    if (ll_container.getChildCount() > 1) {
      showMsg(getString(R.string.erweima_count));
      return;
    }

    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(false)
        .maxSelectable(1)
        .forResult(11);
  }

  private void cameraPic() {

    if (ll_container.getChildCount() > 1) {
      showMsg(getString(R.string.erweima_count));
      return;
    }
    takePic();
  }

  private String pathTakePhoto;              //拍照路径
  private Uri imageUri;                            //拍照Uri
  private File mSaveFile;

  private void takePic() {
    File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image.jpg");
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case 11:
        if (null != data) {

          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(this, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            startCrop(Uri.fromFile(new File(videoPath)));
          }
        }
        break;
      case 2:
        break;
      case TAKE_PHOTO:
        String path = imageUri
            .getPath();

        Uri srcUri = null;
        if (VERSION.SDK_INT >= 24) {
          File file = new File(Environment.getExternalStorageDirectory(),
              "suishoupai_image.jpg");
          srcUri = Uri.fromFile(file);
        } else {
          srcUri = Uri.fromFile(new File(path));
        }

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));

        Options options = new Options();
        options.setMaxScaleMultiplier(5);
        options.setCompressionQuality(70);
        options.setCompressionFormat(CompressFormat.JPEG);

        if (null != srcUri) {
          UCrop.of(imageUri, destinationUri).withOptions(options).withAspectRatio(1, 1)
              .withMaxResultSize(500, 500)
              .start(this);
        }
        break;
//      case REQUEST_ALBUM_OK:
//        try {
//          Uri dataData = data.getData();
//          startCrop(dataData);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//        break;

      default:
        break;

    }

    if (resultCode == RESULT_OK) {
      //裁切成功
      if (requestCode == UCrop.REQUEST_CROP && null != data) {
        Uri croppedFileUri = UCrop.getOutput(data);
        //获取默认的下载目录
        String downloadsDirectoryPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
            croppedFileUri.getLastPathSegment());
        mSaveFile = new File(downloadsDirectoryPath, filename);

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

          View inflate = getLayoutInflater().inflate(R.layout.zhifubao_item, null);
          ImageView iv = (ImageView) inflate.findViewById(R.id.item_us_spread_upload_image);
          ImageView delete = (ImageView) inflate.findViewById(R.id.item_us_spread_delete_image);

          iv.setImageBitmap(bitmap);

          delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if (ll_container.getChildCount() > 0) {
                ll_container.removeViewAt(0);
              }
            }
          });

          ll_container.addView(inflate, 0);

          //上传icon
//          saveAndFinish();
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
      Toast.makeText(this, getString(R.string.crop_fail), Toast.LENGTH_SHORT).show();
    }
  }


  private void saveAndFinish() {
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet(myApp, this, mSaveFile,
          SPUtils
              .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""),
          Urls.UPLOAD_ERWEIMA, "image", SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
              "") + ".jpg", mUploadListener);
    } catch (Exception e) {
      showMsg(getString(R.string.upload_erweima_error));
      dismissLoadingDialog();
    }

  }

  private RequestListener mUploadListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<AliPay> result = GsonUtil
            .processJson(jsonObject, AliPay.class);
        uploadResult(result);
      } catch (Exception e) {
        dismissUploadingDialog();
        show(getInfo(R.string.upload_erweima_error));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissUploadingDialog();
      show(getInfo(R.string.upload_erweima_error));
    }
  };

  private void uploadResult(Result<AliPay> result) {
    dismissUploadingDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.upload_success));
      ZhiFuBaoAccountActivityEvent event = new ZhiFuBaoAccountActivityEvent();
      event.setAlipay(result.getResult().getQrcode());
      EventBus.getDefault().post(event);
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.upload_fail)));
    }
  }


  private void startCrop(Uri dataData) {
    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    Options options = new Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);

    UCrop.of(dataData, destinationUri).withOptions(options).withAspectRatio(1, 1)
        .withMaxResultSize(500, 500)
        .start(this);
  }


}
