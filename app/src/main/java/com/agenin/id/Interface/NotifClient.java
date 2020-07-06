package com.agenin.id.Interface;

import com.agenin.id.Model.Api.NotifAPI;
import com.agenin.id.Model.Api.WishlistAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotifClient {
    @GET("api/user/{id}/mynotifications/")
    Call<NotifAPI> getMyNotification(@Path("id")String _id);

    @GET("api/user/{id}/mynotifications/count")
    Call<Integer> getCountMyNotification(@Path("id")String _id);

    @DELETE("api/user/{id}/mynotifications/deleteAllPesan")
    Call<ResponseBody> deleteAllNotif(@Path("id")String _id);
}
