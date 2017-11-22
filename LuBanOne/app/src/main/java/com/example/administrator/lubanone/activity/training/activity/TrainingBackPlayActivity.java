package com.example.administrator.lubanone.activity.training.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017\6\23 0023.
 */

public class TrainingBackPlayActivity extends Activity {

    private VideoView traningPlay;
    private MediaPlayer traningMediaPlayer;
    private ImageView trainingCollect;
    private int widthvideo;
    private int heightvideo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranining_back_play);

         //获取屏幕长宽
        DisplayMetrics m = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(m);
        widthvideo = m.widthPixels;
        heightvideo = m.heightPixels;

        traningPlay = (VideoView) this.findViewById(R.id.training_back_play_video);
        trainingCollect = (ImageView) this.findViewById(R.id.training_collect);
        trainingCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        playTraining();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void playTraining() {
         String phone = "http://42.51.40.5/Uploads/video/2017-07-22/68_1500713664_1530328656.mp4";
        String ddd = "http://pic.888pic.com/00/11/41/45n888piCx46.mp4";
        final Uri uri = Uri.parse(phone);
        /*traningMediaPlayer = MediaPlayer.create(this,
                Uri.parse("http://pic.888pic.com/00/11/41/45n888piCx46.mp4"));
        // 设置SurfaceView自己不管理的缓冲区
        traningPlay.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        traningPlay.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
               *//* if (position > 0) {
                    try {
                        // 开始播放
                        play();
                        // 并直接从指定位置开始播放
                        mediaPlayer.seekTo(position);
                        position = 0;
                    } catch (Exception e) {
                    }
                }*//*
                try {
                    play(uri);
                } catch (Exception e) {
                }*/
        //设置视频控制器
        traningPlay.setMediaController(new MediaController(this));
        //播放完成回调
        traningPlay.setOnCompletionListener( new MyPlayerOnCompletionListener());
        //设置视频路径
        traningPlay.setVideoURI(uri);
        //开始播放视频
        //trainingVideoView.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( TrainingBackPlayActivity.this, "播放完成了", Toast.LENGTH_LONG).show();
        }
    }

    private void play(Uri uri) {
        try {
            traningMediaPlayer.reset();
            traningMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            //Uri uri = Uri.parse("http://pic.888pic.com/00/11/41/45n888piCx46.mp4");
            traningMediaPlayer.setDataSource(getApplicationContext(), uri);
            // 把视频画面输出到SurfaceView
            traningMediaPlayer.setDisplay(traningPlay.getHolder());
            traningMediaPlayer.prepare();
            // 播放
            traningMediaPlayer.start();
        } catch (Exception e) {
        }
    }

}
