package com.example.short_vedio.network;

import com.example.short_vedio.bean.TinyVedio;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IssueService {
    @Multipart
    @POST("mini_douyin/invoke/video")
    Call<TinyVedio> upLoading(@Part List<MultipartBody.Part> partList);
}
