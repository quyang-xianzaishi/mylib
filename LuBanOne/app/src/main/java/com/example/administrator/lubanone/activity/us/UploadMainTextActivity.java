package com.example.administrator.lubanone.activity.us;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore.Video.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.task.TaskUploadGvAdapter;
import com.example.administrator.lubanone.utils.FileUtils;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.ToastUtil;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/**
 * Created by hou on 2017/7/4.
 */

public class UploadMainTextActivity extends BaseActivity {

  private static final int IMAGE_CODE = 111;
  private static final int CAMERA_CODE = 125;
  private EditText mainTextEt;
  private GridView mGridView;
  private LinearLayout addPicture;
  private TextView complete;
  private ArrayList<String> imagePaths = new ArrayList<>();//图片路径
  private String videoPath = "";//视频路径
  private String videoName = "";//视频名字
  private String mContent = "";//正文
  private LinearLayout backBtn;
  private LinearLayout focusableLL;
  private TextView showVideo;
  private ImageView videoPicture;

  private static final int REQUEST_PREVIEW_CODE = 22;
  private TaskUploadGvAdapter mAdapter;
  //调用系统相册-选择视频
  private static final int VIDEO_CODE = 33;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_us_upload_main_text;
  }

  @Override
  public void initView() {
    videoPicture = (ImageView) findViewById(R.id.us_spread_upload_main_text_video_picture);
    showVideo = (TextView) findViewById(R.id.us_spread_upload_main_text_show_video);
    focusableLL = (LinearLayout) findViewById(R.id.us_spread_upload_main_text_focusable);
    mainTextEt = (EditText) findViewById(R.id.us_spread_upload_main_text_edit);
    mGridView = (GridView) findViewById(R.id.us_spread_upload_main_text_gv);
    addPicture = (LinearLayout) findViewById(R.id.us_spread_upload_main_text_add_picture);
    complete = (TextView) findViewById(R.id.us_spread_upload_main_text_complete);
    backBtn = (LinearLayout) findViewById(R.id.us_spread_upload_main_text_back);
    backBtn.setOnClickListener(this);
    addPicture.setOnClickListener(this);
    complete.setOnClickListener(this);

    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoPreviewIntent intent = new PhotoPreviewIntent(mContext);
        intent.setCurrentItem(position);
        intent.setPhotoPaths(imagePaths);
        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    focusableLL.setFocusable(true);
    focusableLL.setFocusableInTouchMode(true);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    mContent = mainTextEt.getText().toString();
    switch (v.getId()) {
      case R.id.us_spread_upload_main_text_add_picture:
        checkPermission();
        break;
      case R.id.us_spread_upload_main_text_complete:
        if (TextUtils.isEmpty(mContent)) {
          ToastUtil.showShort(getInfo(R.string.us_upload_edit_text), mContext);
        } else if (imagePaths.size() == 0 && TextUtils.isEmpty(videoPath)) {
          ToastUtil.showShort(getInfo(R.string.us_upload_add_image_video), mContext);
        } else {
          Intent intent1 = new Intent();
          intent1.putStringArrayListExtra("imagePaths", imagePaths);
          intent1.putExtra("videoPath", videoPath);
          intent1.putExtra("editMain", mContent);
          setResult(RESULT_OK, intent1);
          finish();
        }
        break;
      case R.id.us_spread_upload_main_text_back:
        finish();
        break;
      default:
        break;
    }
  }

  /**
   * 检查权限
   */
  private void checkPermission() {
    if (VERSION.SDK_INT > VERSION_CODES.M) {
      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(this, new String[]{permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
      } else {
        choosePic();
      }
    } else {
      choosePic();
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

  private void choosePic() {
    ArrayList<String> list = new ArrayList<>();
    list.add(getInfo(R.string.upload_select_video));
    list.add(getInfo(R.string.upload_select_image));

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
      if (imagePaths.size() > 0) {
        ToastUtil.showShort(getInfo(R.string.us_upload_tip), mContext);
      } else {
        Intent i = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, VIDEO_CODE);
      }
    }
    if (1 == position) {
      Matisse.from(this)
          .choose(MimeType.ofImage())
          .theme(R.style.Matisse_Dracula)
          .maxSelectable(9)
          .countable(false)
          .forResult(IMAGE_CODE);
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
            choosePic();
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        // 预览
        case REQUEST_PREVIEW_CODE:
          loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
          break;
        //video
        case VIDEO_CODE:
          mGridView.setVisibility(View.GONE);
          Uri uriVideo = data.getData();
          videoPath = VideoUtils.getPath(mContext, uriVideo);
          String[] temp = videoPath.split("/");
          showVideo.setText(temp[temp.length - 1]);
          videoPicture.setImageBitmap(VideoUtils.getVideoThumbnail(videoPath));
          try {
            HouLog.d("videoPath:" + videoPath + "  videoSize:" + FileUtils
                .formatFileSize(FileUtils.getFileSize(new File(videoPath))));
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        //image
        case IMAGE_CODE:
          List<Uri> uris = Matisse.obtainResult(data);
          ArrayList<String> paths = new ArrayList<>();
          for (int i = 0; i < uris.size(); i++) {
            paths.add(VideoUtils.getPath(this, uris.get(i)));
          }
          HouLog.d("选择的图片路径: ", paths.toString());
          loadAdpater(paths);
          break;
        default:
          break;
      }
    }
  }

  private void loadAdpater(ArrayList<String> paths) {
    if (imagePaths == null) {
      imagePaths = new ArrayList<>();
    }
    imagePaths.clear();
    imagePaths.addAll(paths);
    try {
      JSONArray obj = new JSONArray(imagePaths);
      Log.e("--", obj.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (mAdapter == null) {
      mAdapter = new TaskUploadGvAdapter(mContext, imagePaths);
      mGridView.setAdapter(mAdapter);
    } else {
      mAdapter.notifyDataSetChanged();
    }
  }

}
