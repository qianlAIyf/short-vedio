package com.example.short_vedio;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.short_vedio.utils.Utils;

import java.io.File;

public class VedioIssue extends AppCompatActivity {

    private Button btn_selectPic;
    private Button btn_selectVideo;

    private ImageView imageView;
    private VideoView videoView;

    private  File imgFile;
    private  static  final int REQUEST_CODE_PIC_PHOTO = 3;
    private  static  final int REQUEST_CODE_VIDEO = 4;

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedio_issue);

        btn_selectPic = findViewById(R.id.btn_selectPic);
        btn_selectVideo = findViewById(R.id.btn_selectVideo);

        videoView = findViewById(R.id.video);
        imageView = findViewById(R.id.image);

        btn_selectPic.setOnClickListener(new View.OnClickListener() {//选择封面图
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PIC_PHOTO);

            }
        });
        btn_selectVideo.setOnClickListener(new View.OnClickListener() {//选择视频
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent,REQUEST_CODE_VIDEO);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PIC_PHOTO ) {
//            Uri uri = data.getData();
//            Cursor cursor = getContentResolver().query(uri, null, null,
//                    null, null);
//            cursor.moveToFirst();
//            // String imgNo = cursor.getString(0); // 图片编号
//            String v_path = cursor.getString(1); // 图片文件路径
//              imgFile = new File(v_path);
//            imgFile = new File(uri.getPath().toString());
            setPic();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_CODE_VIDEO ){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPic(){
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        BitmapFactory.Options bmOption = new BitmapFactory.Options();
//        bmOption.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imgFile.getPath(),bmOption);
//        int photoW = bmOption.outWidth;
//        int photoH = bmOption.outHeight;
//
//        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
//        bmOption.inJustDecodeBounds = false;
//        bmOption.inSampleSize = scaleFactor;
//        bmOption.inPurgeable = true;
//        bmOption.inInputShareable = true;
//
//
//        Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOption);
//        bmp= Utils.rotateImage(bmp,Utils.convertUriToPath(this,Uri.fromFile(imgFile)));
//        imageView.setImageBitmap(bmp);

    }
}
