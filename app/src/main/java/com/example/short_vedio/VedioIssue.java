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

    private  File imgFile;
    private  static  final int REQUEST_CODE_PIC_PHOTO = 3;
    private  static  final int REQUEST_CODE_VIDEO = 4;

    public static final String SD_HOME_DIR = Environment.getExternalStorageDirectory().getPath() + "";          //SD卡根目录
    private final String file1Location = SD_HOME_DIR + "file1.jpg";         //要上传的文件存储位置

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_issue);

        btn_selectPic = findViewById(R.id.btn_selectPic);
        btn_selectVideo = findViewById(R.id.btn_selectVideo);
        btn_uploading = findViewById(R.id.btn_issue);

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
        btn_uploading.setOnClickListener(new View.OnClickListener() {//选择视频
            @Override
            public void onClick(View v) {
                upLoadingMethod();
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
    private void upLoadingMethod() {

        //创建文件(你需要上传到服务器的文件)
        //file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
        File file = new File(file1Location);

        //创建表单map,里面存储服务器本接口所需要的数据;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //在这里添加服务器除了文件之外的其他参数
                .addFormDataPart("参数1", "值1")
                .addFormDataPart("参数2", "值2");


        //设置文件的格式;两个文件上传在这里添加
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        //添加文件(uploadfile就是你服务器中需要的文件参数)
        builder.addFormDataPart("uploadfile", file.getName(), imageBody);
        //builder.addFormDataPart("uploadfile1", file1.getName(), imageBody1);
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
                .baseUrl("你的基站地址")
                //设置委托,使用OKHttp联网,也可以设置其他的;
                .client(okHttpClient)
                //设置数据解析器,如果没有这个类需要添加依赖:
                .addConverterFactory(GsonConverterFactory.create())
                //设置支持rxJava
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //实例化请求接口,把表单传递过去;
        Call<TinyVedio> call = retrofit.create(IssueService.class).upLoading(parts);
        //开始请求
        call.enqueue(new Callback<TinyVedio>() {
            @Override
            public void onResponse(Call<TinyVedio> call, Response<TinyVedio> response) {
                //联网有响应或有返回数据
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<TinyVedio> call, Throwable t) {
                //连接失败,多数是网络不可用导致的
                System.out.println("网络不可用");
            }
        });

    }
}
