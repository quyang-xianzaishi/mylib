package com.example.administrator.lubanone.activity.training.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.videopalyer.OnPlayerBackListener;
import com.example.administrator.lubanone.videopalyer.OnShowThumbnailListener;
import com.example.administrator.lubanone.videopalyer.PlayStateParams;
import com.example.administrator.lubanone.videopalyer.PlayerView;

public class PlayerActivity extends Activity {

  private PlayerView player;
  private Context mContext;
  private View rootView;
  private String url = "http://42.51.40.5/Uploads/video/2017-07-11/hehe.mp4";
  private String theme;
  private String videoUrl;
  private String imageUrl;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = this;
    rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
    setContentView(rootView);

    theme = getIntent().getStringExtra("theme");
    videoUrl = getIntent().getStringExtra("videoUrl");
    imageUrl = getIntent().getStringExtra("imageUrl");
    HouLog.d("视频地址:" + videoUrl + " 图片地址:" + imageUrl + " 主题:" + theme);
    player = new PlayerView(this, rootView)
        .setTitle(theme)
        .setScaleType(PlayStateParams.fitparent)
        .forbidTouch(false)
        .hideMenu(true)
        .showThumbnail(new OnShowThumbnailListener() {
          @Override
          public void onShowThumbnail(ImageView ivThumbnail) {
            Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.color.cl_default)
                .error(R.color.cl_error)
                .into(ivThumbnail);
          }
        })
        .setPlaySource(videoUrl)
        .setPlayerBackListener(new OnPlayerBackListener() {
          @Override
          public void onPlayerBack() {
            //这里可以简单播放器点击返回键
            finish();
          }
        })
        .startPlay();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (player != null) {
      player.onPause();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (player != null) {
      player.onResume();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (player != null) {
      player.onDestroy();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (player != null) {
      player.onConfigurationChanged(newConfig);
    }
  }

  @Override
  public void onBackPressed() {
    if (player != null && player.onBackPressed()) {
      return;
    }
    super.onBackPressed();
  }

}
