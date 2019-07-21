package com.example.short_vedio.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class TinyVedio {
    @SerializedName("feeds") private List<T> feeds;

    @SerializedName("success") private boolean aBoolean;

    public static class T {
        @SerializedName("student_id") private String student_id;
        @SerializedName("user_name") private String user_name;
        @SerializedName("image_url") private String image_url;
        @SerializedName("vedio_url") private String vedio_url;
        @SerializedName("_id") private String _id;
        @SerializedName("image_w") private int image_w;
        @SerializedName("image_h") private int image_h;
        @SerializedName("createdAt") private Date createdAt;
        @SerializedName("updatedAt") private Date updatedAt;
        @SerializedName("__v") private int v;

        public String getStudent_id(){return new T().student_id;}
        public String get_id(){return new T()._id;}
        public String getImage_url(){return new T().image_url;}
        public String getVedio_url(){return new T().vedio_url;}
        public int getImage_w(){return new T().image_w;}
        public int getImage_h(){return new T().image_h;}
        public Date getCreatedAt(){return new T().createdAt;}
        public Date getUpdatedAt(){return new T().updatedAt;}
    }

    public List<T> getFeeds(){return feeds;}
    public boolean isaBoolean(){return aBoolean;}

}
