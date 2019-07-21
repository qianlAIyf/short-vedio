package com.example.short_vedio.network;

import com.example.short_vedio.bean.TinyVedio;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TinyVedioService {
    @GET("mini_douyin/invoke/video")
    Call<TinyVedio> randomTinyVedio();
}
