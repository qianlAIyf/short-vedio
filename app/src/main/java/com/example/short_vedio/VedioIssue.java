package com.example.short_vedio;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.short_vedio.bean.TinyVedio;
import com.example.short_vedio.network.IssueService;
import com.example.short_vedio.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VedioIssue extends AppCompatActivity {

    private Button btn_selectPic;
    private Button btn_selectVideo;
    private Button btn_uploading;

    private ImageView imageView;
    private VideoView videoView;

    private String videoPath;
    private String imagePath;
    private  File imgFile;
    private  static  final int REQUEST_CODE_PIC_PHOTO = 3;
    private  static  final int REQUEST_CODE_VIDEO = 4;

    private static boolean getImagePath_State = false;  //用于判断图片文件路径获取状态
    private static boolean getVideoPath_State = false;  //用于判断视频文件路径获取状态

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_issue);

        initView();
        setListeners();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PIC_PHOTO ) {
//判断手机系统版本号
            if(Build.VERSION.SDK_INT>=19){
                //4.4及以上系统使用这个方法处理图片
                imagePath=handlerImageOnKitKat(data);
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

            if(Build.VERSION.SDK_INT>=19){
                //4.4及以上系统使用这个方法处理图片
                videoPath = handlerVideoOnKitKat(data);
            }


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
    //获取图片路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String handlerImageOnKitKat(Intent data){

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
        getImagePath_State = true;
        setPic(imagePath);
        if(getVideoPath_State && getImagePath_State){
            btn_uploading.setEnabled(true);
        }
        return imagePath;
    }
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
    //获取视频路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String handlerVideoOnKitKat(Intent data){
        String videoPath=null;
        Uri uri=data.getData();

        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Video.Media._ID+"="+id;
                videoPath=getVideoPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                videoPath=getVideoPath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的URI，则使用普通方式处理
            videoPath=getVideoPath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            videoPath=uri.getPath();
        }
        getVideoPath_State = true;
        if(getVideoPath_State && getImagePath_State){
            btn_uploading.setEnabled(true);
        }
        return videoPath;
    }
    private String getVideoPath(Uri uri, String selection) {
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
    private void upLoadingMethod() {
        btn_uploading.setText("上传中...");
        btn_uploading.setEnabled(false);
        //创建文件
        File imageFile = new File(imagePath);
        File videoFile = new File(videoPath);

        //创建表单map,里面存储服务器本接口所需要的数据;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //在这里添加服务器除了文件之外的其他参数
//                .addFormDataPart("feeds", null)
                .addFormDataPart("success", "true");

        //设置文件的格式;两个文件上传在这里添加
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        RequestBody videoDescriptionBody = RequestBody.create(MediaType.parse("multipart/form-data"),"!");
        //添加文件(file就是你服务器中需要的文件参数)
        builder.addFormDataPart("file1", imageFile.getName(), imageBody);
        builder.addFormDataPart("file2", videoFile.getName(), videoBody);
        builder.addFormDataPart("description","");
        //生成接口需要的list
        List<MultipartBody.Part> parts = builder.build().parts();
        //创建设置OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                //允许失败重试
                .retryOnConnectionFailure(true)
                .build();
        //创建retrofit实例对象
        Retrofit retrofit = new Retrofit.Builder()
                //设置基站地址(基站地址+描述网络请求的接口上面注释的Post地址,就是要上传文件到服务器的地址,
                // 这只是一种设置地址的方法,还有其他方式,不在赘述)
                .baseUrl("http://test.androidcamp.bytedance.com/")
                //设置委托,使用OKHttp联网,也可以设置其他的;
                .client(okHttpClient)
                //设置数据解析器,如果没有这个类需要添加依赖:
                .addConverterFactory(GsonConverterFactory.create())
                //设置支持rxJava
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //实例化请求接口,把表单传递过去;
        Call<TinyVedio> call = retrofit.create(IssueService.class).upLoading(parts.get(0),parts.get(1),videoDescriptionBody);
        //开始请求
        call.enqueue(new Callback<TinyVedio>() {
            @Override
            public void onResponse(Call<TinyVedio> call, Response<TinyVedio> response) {
                //联网有响应或有返回数据
                System.out.println(response.body().toString());
                System.out.println("!!!!");
                btn_uploading.setText("上传成功");
                btn_uploading.setEnabled(true);
                btn_uploading.setText("成功，继续上传");
                btn_uploading.setBackgroundColor(Color.parseColor("#00FF00"));
            }

            @Override
            public void onFailure(Call<TinyVedio> call, Throwable t) {
                //连接失败,多数是网络不可用导致的
                System.out.println("网络不可用");
                btn_uploading.setEnabled(true);
                btn_uploading.setText("失败，再次上传");
                btn_uploading.setBackgroundColor(Color.parseColor("#FF0000"));
            }
        });
    }
    public void initView(){

        btn_selectPic = findViewById(R.id.btn_selectPic);
        btn_selectVideo = findViewById(R.id.btn_selectVideo);
        btn_uploading = findViewById(R.id.upLoading);

        videoView = findViewById(R.id.video);
        imageView = findViewById(R.id.image);

        btn_uploading.setEnabled(false);
    }
    public void setListeners(){

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
        btn_uploading.setOnClickListener(new View.OnClickListener() {//选择视频
            @Override
            public void onClick(View v) {
                upLoadingMethod();
            }
        });
    }
}
