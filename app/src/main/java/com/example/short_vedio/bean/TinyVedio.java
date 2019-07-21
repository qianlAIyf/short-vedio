package com.example.short_vedio.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class TinyVedio {
    @SerializedName("feeds") private List<Feed> feeds;

    @SerializedName("success") private boolean aBoolean;

    public static class Feed {
        @SerializedName("student_id") private String student_id;
        @SerializedName("user_name") private String user_name;
        @SerializedName("image_url") private String image_url;
        @SerializedName("video_url") private String vedio_url;
        @SerializedName("_id") private String _id;
        @SerializedName("image_w") private int image_w;
        @SerializedName("image_h") private int image_h;
        @SerializedName("createdAt") private Date createdAt;
        @SerializedName("updatedAt") private Date updatedAt;
        @SerializedName("__v") private int v;

        public String getStudent_id(){return student_id;}
        public String get_id(){return _id;}
        public String getUser_name(){return user_name;}
        public String getImage_url(){return image_url;}
        public String getVedio_url(){return vedio_url;}
        public int getImage_w(){return image_w;}
        public int getImage_h(){return image_h;}
        public Date getCreatedAt(){return createdAt;}
        public Date getUpdatedAt(){return updatedAt;}
    }

    public List<Feed> getFeeds(){return feeds;}
    public boolean isaBoolean(){return aBoolean;}

}
