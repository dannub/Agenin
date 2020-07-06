package com.agenin.id.Interface;

import com.agenin.id.Model.YoutubeVideoModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface YoutubeClient {
    @GET("api/video/")
    Call<List<YoutubeVideoModel>> getYoutube();
}
