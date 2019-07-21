package com.example.short_vedio;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VedioPlay extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private VideoView videoView;
    private TextView textViewTime;
    private TextView textViewAuthor;

    public int videoPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.video_playvertical);

            videoView = findViewById(R.id.videoViewLandscape);
            textViewTime = findViewById(R.id.textViewTime);
            textViewAuthor = findViewById(R.id.textViewAuthor);

            videoView.setVideoPath(getVideoPath());

            MediaController mMediaController = new MediaController(this);
            videoView.setMediaController(mMediaController);
            videoView.start();
        }else {
            setContentView(R.layout.video_playhorizontal);

            videoView = findViewById(R.id.videoViewHorizontal);
            textViewTime = findViewById(R.id.textViewTime);
            textViewAuthor = findViewById(R.id.textViewAuthor);

            videoView.setVideoPath(getVideoPath());

            MediaController mMediaController = new MediaController(this);
            videoView.setMediaController(mMediaController);
            videoView.start();
        }
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        videoPosition = videoView.getCurrentPosition();

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            //横向
            setContentView(R.layout.video_playhorizontal);
            //重新实例化组件和设置监听
            videoView = findViewById(R.id.videoViewHorizontal);
            videoView.setVideoPath(getVideoPath());

            MediaController mMediaController = new MediaController(this);
            videoView.setMediaController(mMediaController);

            videoView.seekTo(videoPosition);
            videoView.start();
        }else{
            //竖向
            setContentView(R.layout.video_playvertical);
            //重新实例化组件和设置监听
            videoView = findViewById(R.id.videoViewLandscape);

            videoView.setVideoPath(getVideoPath());

            MediaController mMediaController = new MediaController(this);
            videoView.setMediaController(mMediaController);

            videoView.seekTo(videoPosition);
            videoView.start();
        }
    }

    private String getVideoPath() {
        Intent intent = getIntent();
        String vedio_url = intent.getStringExtra("vedio_url");
        textViewTime.append(intent.getStringExtra("created_At"));
        textViewAuthor.append(intent.getStringExtra("author"));
        return vedio_url;
    }

    @Override
    public void onCompletion(MediaPlayer mp) { }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.seekTo(videoPosition);
        if (videoPosition == 0) {
            mp.start();
        } else {
            mp.pause();
        }
    }
}
