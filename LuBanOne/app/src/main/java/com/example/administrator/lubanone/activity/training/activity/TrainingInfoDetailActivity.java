package com.example.administrator.lubanone.activity.training.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017\6\23 0023.
 */

public class TrainingInfoDetailActivity extends Activity {

    private TextView trainingVideoAuthor;
    private VideoView trainingVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_info_detail);

        trainingVideoAuthor = (TextView) this.findViewById(R.id.training_info_detail_author);
        trainingVideoView = (VideoView) this.findViewById(R.id.training_info_detail_video);

        Uri uri = Uri.parse("http://pic.888pic.com/00/11/41/45n888piCx46.mp4");
        //设置视频控制器
        trainingVideoView.setMediaController(new MediaController(this));
        //播放完成回调
        trainingVideoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
        //设置视频路径
        trainingVideoView.setVideoURI(uri);
        //开始播放视频
        //trainingVideoView.start();

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( TrainingInfoDetailActivity.this, "播放完成了", Toast.LENGTH_LONG).show();
        }
    }
}
