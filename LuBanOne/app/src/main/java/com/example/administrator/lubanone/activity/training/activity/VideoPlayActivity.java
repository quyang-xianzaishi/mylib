package com.example.administrator.lubanone.activity.training.activity;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.utils.HouLog;
import java.io.IOException;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by hou on 2017/8/10.
 */

public class VideoPlayActivity extends BaseActivity {
  /**
   * 同步进度
   */
  private static final int MESSAGE_SHOW_PROGRESS = 1;
  /**
   * 播放总时长
   */
  private long duration;
  /**
   * 当前播放位置
   */
  private int currentPosition;
  /**
   * 是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
   */
  private boolean isDragging = false;

  private LinearLayout controlLinearLayout;
  private SurfaceView mSurfaceView;
  private ImageView startBtn;
  private TextView currentTime;
  private TextView endTime;
  private SeekBar mSeekBar;
  private LinearLayout finishLayout;
  private TextView finishText;
  private ImageView restartPlay;
  private LinearLayout loadingLayout;

  private IjkMediaPlayer ijkMediaPlayer;
  private SurfaceHolder mSurfaceHolder;
  private boolean f = true;
  private String mediaPath = "http://42.51.40.5/Uploads/video/2017-07-11/hehe.mp4";

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case MESSAGE_SHOW_PROGRESS:
          if (!isDragging){
            HouLog.d("----------",ijkMediaPlayer.isPlaying()+"");
            HouLog.d("00000000");
            if (ijkMediaPlayer.isPlaying()){
              HouLog.d("1111111111");
              loadingLayout.setVisibility(View.GONE);
              syncProgress();
              msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
              sendMessageDelayed(msg,500);
            }else{
              loadingLayout.setVisibility(View.VISIBLE);
              HouLog.d("222222222");
            }
          }
          break;
      }
    }
  };
  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_video_play;
  }

  @Override
  public void initView() {
    controlLinearLayout = (LinearLayout) findViewById(R.id.video_play_line1);
    mSurfaceView = (SurfaceView) findViewById(R.id.video_play_surfaceView);
    startBtn = (ImageView) findViewById(R.id.app_video_play);
    mSeekBar = (SeekBar) findViewById(R.id.app_video_seekBar);
    currentTime = (TextView) findViewById(R.id.app_video_currentTime);
    endTime = (TextView) findViewById(R.id.app_video_endTime);
    finishLayout = (LinearLayout) findViewById(R.id.video_play_replay);
    finishText = (TextView) findViewById(R.id.video_play_status_text);
    restartPlay = (ImageView) findViewById(R.id.video_play_replay_icon);
    loadingLayout = (LinearLayout) findViewById(R.id.video_play_loading);

    mSeekBar.setMax(1000);
    ijkMediaPlayer = new IjkMediaPlayer();
    mSurfaceHolder = mSurfaceView.getHolder();

    startPlay();
    addListener();
  }

  //开始播放
  private void startPlay(){
    try {
      ijkMediaPlayer.setDataSource(mediaPath);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  //设置事件监听
  private void addListener(){

    startBtn.setOnClickListener(this);
    restartPlay.setOnClickListener(this);

    //设置进度条时间监听
    mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
          //不是用户拖动的，自动播放的情况
          return;
        } else {
          long duration = getDuration();
          int position = (int) ((duration * progress * 1.0) / 1000);
          String time = generateTime(position);
          currentTime.setText(time);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = getDuration();
        ijkMediaPlayer.seekTo((long) ((duration * seekBar.getProgress() * 1.0) / 1000));
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        isDragging = false;
        mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
      }
    });

    //当触摸sufaceView时显示或消失底部按钮
    mSurfaceView.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == (MotionEvent.ACTION_DOWN)) {
          if (f == true) {
            f = false;
            controlLinearLayout.setVisibility(View.VISIBLE);
          } else {
            f = true;
            controlLinearLayout.setVisibility(View.GONE);
          }
        }
        return true;
      }
    });

    //当前加载进度的监听
    ijkMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
      @Override
      public void onBufferingUpdate(IMediaPlayer mp, int percent) {

      }
    });

    //mediaPlayer完成
    ijkMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
      @Override
      public void onCompletion(IMediaPlayer mp) {
        mp.seekTo(0);
        finishLayout.setVisibility(View.VISIBLE);
        finishText.setText("重新播放");
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        startBtn.setImageResource(R.drawable.simple_player_arrow_white_24dp);
      }
    });

    //mediaPlayer准备工作
    ijkMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
      @Override
      public void onPrepared(IMediaPlayer mp) {
        mp.start();
        mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
      }
    });

    mSurfaceHolder.addCallback(new Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        //连接ijkPlayer 和surfaceHOLDER
        ijkMediaPlayer.setDisplay(holder);
        //开启异步准备
        ijkMediaPlayer.prepareAsync();
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {

      }
    });
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.app_video_play:
        if (ijkMediaPlayer.isPlaying()) {
          ijkMediaPlayer.pause();
          mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
          startBtn.setImageResource(R.drawable.simple_player_arrow_white_24dp);
        } else {
          ijkMediaPlayer.start();
          startBtn.setImageResource(R.drawable.simple_player_icon_media_pause);
          mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        }
        break;
      case R.id.video_play_replay_icon:
        finishLayout.setVisibility(View.GONE);
        ijkMediaPlayer.start();
        startBtn.setImageResource(R.drawable.simple_player_icon_media_pause);
        mHandler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        break;
    }
  }

  /**
   * 同步进度
   */
  private void syncProgress() {
    long position = ijkMediaPlayer.getCurrentPosition();
    long duration = ijkMediaPlayer.getDuration();
    if (mSeekBar != null) {
      if (duration > 0) {
        long pos = 1000L * position / duration;
        mSeekBar.setProgress((int) pos);
      }
    }
    currentTime.setText(generateTime(position));
    endTime.setText(generateTime(duration));
  }

  /**
   * 时长格式显示
   */
  private String generateTime(long time) {
    int totalSeconds = (int) (time / 1000);
    int seconds = totalSeconds % 60;
    int minutes = (totalSeconds / 60) % 60;
    int hours = totalSeconds / 3600;
    return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
        : String.format("%02d:%02d", minutes, seconds);
  }

  /**
   * 获取当前播放位置
   */
  private int getCurrentPosition() {
    currentPosition = (int) ijkMediaPlayer.getCurrentPosition();
    return currentPosition;
  }

  /**
   * 获取视频总时长
   */
  private long getDuration() {
    duration = ijkMediaPlayer.getDuration();
    return duration;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (ijkMediaPlayer!=null){
      ijkMediaPlayer.start();
    }
    HouLog.d("onResume");
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (ijkMediaPlayer !=null){
      ijkMediaPlayer.pause();
    }
    HouLog.d("onPause");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (ijkMediaPlayer!=null){
      ijkMediaPlayer.release();
    }
    HouLog.d("onDestroy");
  }
}
