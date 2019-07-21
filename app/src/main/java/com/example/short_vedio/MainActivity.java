package com.example.short_vedio;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.short_vedio.bean.TinyVedio;
import com.example.short_vedio.network.TinyVedioService;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button buttonIssue, buttonPhoto, buttonSubmit;

    public RecyclerView mRv;
    private TinyVedio mTinyVedios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = findViewById(R.id.rv);
        buttonIssue = findViewById(R.id.btn_issue);
        buttonPhoto = findViewById(R.id.btn_photo);


        //设置三个监听事件
        buttonIssue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VedioIssue.class));
            }
        });
        buttonPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VedioPhoto.class));
            }
        });

//RecyclerView
        mRv.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        mRv.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(){
            @NonNull @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setAdjustViewBounds(true);
                return new MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                final int itemPosition = viewHolder.getLayoutPosition();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),VedioPlay.class);
                        intent.putExtra("vedio_url",mTinyVedios.getFeeds().get(itemPosition).getVedio_url());
                        intent.putExtra("created_At",mTinyVedios.getFeeds().get(itemPosition).getCreatedAt().toString());
                        intent.putExtra("author",mTinyVedios.getFeeds().get(itemPosition).getUser_name());
                        v.getContext().startActivity(intent);
                    }
                });
                ImageView iv =(ImageView) viewHolder.itemView;
                String image_url = mTinyVedios.getFeeds().get(i).getImage_url();
                String vedio_url = mTinyVedios.getFeeds().get(i).getVedio_url();
                Glide.with(iv.getContext()).load(image_url).into(iv);
            }

            @Override
            public int getItemCount() {
                return mTinyVedios == null ? 0 : mTinyVedios.getFeeds().size();
            }
        };

        mRv.setAdapter(adapter);

        this.requestData();//获取视频列表数据
    }

    public static class MyViewHolder extends ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void requestData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TinyVedioService response = retrofit.create(TinyVedioService.class);

        Call<TinyVedio> tinyVedioCall= response.randomTinyVedio();

        tinyVedioCall.enqueue(new Callback<TinyVedio>() {
            @Override
            public void onResponse(Call call, Response response) {
                loadPics((TinyVedio) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("connect failed!" + t.toString());
            }
        });
    }

    private void loadPics(TinyVedio tinyVedios) {
        mTinyVedios = tinyVedios;
        mRv.getAdapter().notifyDataSetChanged();
    }

}
