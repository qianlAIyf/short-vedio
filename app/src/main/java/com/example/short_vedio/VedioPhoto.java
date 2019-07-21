package com.example.short_vedio;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.VideoProfile;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.short_vedio.utils.Utils;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

import retrofit2.http.Url;

public class VedioPhoto extends AppCompatActivity {

    private ImageView imageView = null;
    private VideoView videoView = null;
    private Button takePhoto;
    private Button takeVideo;
    private Button upLoading;

    private File imgFile;

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
        upLoading = findViewById(R.id.upLoading);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        takePhoto.setOnClickListener(new View.OnClickListener() {//拍照

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imgFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, imgFile.getPath());
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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
        upLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView == null||videoView == null){
                    Toast.makeText(VedioPhoto.this, "请拍摄封面图片和视频", Toast.LENGTH_SHORT).show();
                }
                else if(imageView != null&&videoView != null){

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
        else if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }
    private void setPic(){

        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOption = new BitmapFactory.Options();
        bmOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOption);
        int photoW = bmOption.outWidth;
        int photoH = bmOption.outHeight;

        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
        bmOption.inJustDecodeBounds = false;
        bmOption.inSampleSize = scaleFactor;
        bmOption.inPurgeable = true;
        bmOption.inInputShareable = true;


        Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOption);
        bmp=Utils.rotateImage(bmp,Utils.convertUriToPath(this,Uri.fromFile(imgFile)));
        imageView.setImageBitmap(bmp);

    }

}
