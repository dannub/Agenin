package com.agenin.id.Interface;

import com.agenin.id.Model.Api.StarAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StarClient {

    @GET("api/user/{id}/myratings/")
    Call<StarAPI> getMyStar(@Path("id")String _id);

    @POST("api/user/{id}/myratings/create/{productID}/{star}")
    Call<ResponseBody> setMyStar(
            @Path("id")String _id,
            @Path("productID")String productID,
            @Path("star")Integer star
    );
}
