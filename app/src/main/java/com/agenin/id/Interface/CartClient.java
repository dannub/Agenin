package com.agenin.id.Interface;

import com.agenin.id.Model.Api.CartInputAPI;
import com.agenin.id.Model.CartInputModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartClient {

    @POST("api/user/{id}/mycarts/create/{productID}")
    Call<Integer> setMyCart(
            @Path("id")String _id,
            @Path("productID")String productID
    );

    @DELETE("api/user/{id}/mycarts/delete/{cartID}")
    Call<ResponseBody> deleteMyCart(
            @Path("id")String _id
            ,@Path("cartID")String cartID
    );

    @GET("api/user/{id}/mycarts/")
    Call<ResponseBody> getMyCart(
            @Path("id")String _id
    );

    @PATCH("api/user/{id}/mycarts/update")
    Call<ResponseBody> updateMyCart(
            @Path("id")String _id,
            @Body CartInputAPI carts
    );
}
