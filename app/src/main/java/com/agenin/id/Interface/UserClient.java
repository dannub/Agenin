package com.agenin.id.Interface;

import com.agenin.id.Model.Api.UserAPI;
import com.agenin.id.Model.UserModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface UserClient {


    @Multipart
    @POST("api/user/register/?role=user")
    Call<UserAPI> register(@Part MultipartBody.Part bukti,
                           @PartMap Map<String,RequestBody> text);

    @GET("api/user/logout")
    Call<ResponseBody> logout();

    @Multipart
    @PATCH("api/user/info/update/{Id}?ishapus=true")
    Call<UserModel> updateHapusProfil(
            @Path("Id")String userId,
            @PartMap Map<String,RequestBody> text);
    @Multipart
    @PATCH("api/user/info/update/{Id}")
    Call<UserModel> updateProfil(
            @Path("Id")String userId,
            @PartMap Map<String,RequestBody> text);

    @Multipart
    @PATCH("api/user/info/update/{id}")
    Call<UserModel> updateProfilPhoto(
            @Path("id")String userId,
            @Part MultipartBody.Part profil,
            @PartMap Map<String,RequestBody> text);

    @PATCH("api/user/update/{Id}/token/{token}/")
    Call<ResponseBody> updateToken(
            @Path("Id")String userId,
            @Path("token")String token
            );

    @FormUrlEncoded
    @POST("api/user/login/?role=user")
    Call<UserModel> login(@Field("email") String email, @Field("password")String password);

}
