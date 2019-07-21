package com.example.short_vedio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.short_vedio.utils.Utils;

import java.io.File;

public class VedioPhoto extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private Button takePhoto;
    private Button takeVideo;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedio_photo);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        takePhoto = findViewById(R.id.takePhoto);
        takeVideo = findViewById(R.id.takeVideo);

        takePhoto.setOnClickListener(new View.OnClickListener() {//拍照
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imgFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        });

        takeVideo.setOnClickListener(new View.OnClickListener() {//录像
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);//图片显示

        }
        else if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

}
