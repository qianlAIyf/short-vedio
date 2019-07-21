package com.example.short_vedio;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
        setContentView(R.layout.video_issue);

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
            //判断手机系统版本号
            if(Build.VERSION.SDK_INT>=19){
                //4.4及以上系统使用这个方法处理图片
                handlerImageOnKitKat(data);
            }
//            else{
////                //4.4以下系统使用这个方法处理图片
////                handlerImageBeforeKitKat(data);
////            }
        }
        else if(requestCode == REQUEST_CODE_VIDEO ){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPic(String imagePath){
        imgFile = new File(imagePath);
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOption = new BitmapFactory.Options();
        bmOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getPath(),bmOption);
        int photoW = bmOption.outWidth;
        int photoH = bmOption.outHeight;

        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
        bmOption.inJustDecodeBounds = false;
        bmOption.inSampleSize = scaleFactor;
        bmOption.inPurgeable = true;
        bmOption.inInputShareable = true;


        Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOption);
        bmp= Utils.rotateImage(bmp,Utils.convertUriToPath(this,Uri.fromFile(imgFile)));

        imageView.setImageBitmap(bmp);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handlerImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();

        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的URI，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath=uri.getPath();
        }
        System.out.println(imagePath);
        setPic(imagePath);
        //startPhotoZoom(uri);
    }
//    private void handlerImageBeforeKitKat(Intent data){
//        Uri cropUri=data.getData();
//        setPic(cropUri);
//        //startPhotoZoom(cropUri);
//    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
