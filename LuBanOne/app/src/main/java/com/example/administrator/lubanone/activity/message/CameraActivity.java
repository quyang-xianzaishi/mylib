package com.example.administrator.lubanone.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.utils.GetThumbnailUtil;
import java.io.File;

/**
 * Created by Administrator on 2017\7\8 0008.
 */

public class CameraActivity extends Activity {

  private JCameraView jCameraView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    if (Build.VERSION.SDK_INT >= 19) {
      View decorView = getWindow().getDecorView();
      decorView.setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_FULLSCREEN
              | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    } else {
      View decorView = getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
      decorView.setSystemUiVisibility(option);
    }

    initViews();
  }

  private void initViews() {

    jCameraView = (JCameraView) findViewById(R.id.jcameraview);
    //设置视频保存路径
    jCameraView.setSaveVideoPath(
        Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
    //设置只能录像或只能拍照或两种都可以（默认两种都可以）
    jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
    //设置视频质量
    jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
    //JCameraView 监听
    jCameraView.setErrorLisenter(new ErrorLisenter() {
      @Override
      public void onError() {
        //打开 Camera 失败回调
        Log.e("CJT", "open camera error");
      }

      @Override
      public void AudioPermissionError() {
        //没有录取权限回调
        Log.e("CJT", "AudioPermissionError");
        Toast.makeText(getApplicationContext(), getString(R.string.no_record_permission),
            Toast.LENGTH_LONG).show();
      }
    });

    //录制结果监听
    jCameraView.setJCameraLisenter(new JCameraLisenter() {
      @Override
      public void captureSuccess(Bitmap bitmap) {
        //获取图片 bitmap
        Log.e("CJT", "bitmap = " + bitmap.getWidth());
        //获取缩略图
        Bitmap bitmap1 = GetThumbnailUtil.getImageThumbnail(bitmap, 60, 60);
        Log.e("CJT", "bitmap1 = " + bitmap1.getWidth());
      }

      @Override
      public void recordSuccess(String url) {
        //获取视频路径
        Log.e("CJT", "视频路径 = " + url);
//        Toast.makeText(getApplicationContext(),new File(url).exists()+"",Toast.LENGTH_SHORT).show();
        //获取缩略图
//        Bitmap bitmap1 = GetThumbnailUtil.getVideoThumbnail(url,60,60, MediaStore.Images.Thumbnails.MICRO_KIND);

        Intent intent = new Intent();
        intent.putExtra("url", url);
        setResult(RESULT_OK, intent);
        finish();
      }

      @Override
      public void quit() {
        Log.e("CJT", "finish");

        //退出按钮
        CameraActivity.this.finish();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    jCameraView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    jCameraView.onPause();
  }


}
