package com.agenin.id.Interface;

import com.agenin.id.Model.Api.CartInputAPI;
import com.agenin.id.Model.Api.MyOrderAPI;
import com.agenin.id.Model.Api.NotaItemAPI;
import com.agenin.id.Model.Api.OrderDetailsAPI;
import com.agenin.id.Model.Api.OrderUserAPI;
import com.agenin.id.Model.Api.StarAPI;
import com.agenin.id.Model.UserModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotaClient {
    @Multipart
    @POST("api/user/{id}/mynota/create")
    Call<MyOrderAPI> setNota(
            @Path("id") String _id,
            @Part MultipartBody.Part bukti,
            @PartMap Map<String, RequestBody> nota);

    @Multipart
    @POST("api/user/{id}/mynota/create")
    Call<MyOrderAPI> setDelivNota(
            @Path("id") String _id,
            @Query("deliv")Boolean deliv,
            @Part MultipartBody.Part bukti,
            @PartMap Map<String, RequestBody> nota);

    @GET("api/user/{id}/mynota/user/")
    Call<OrderUserAPI> getAllMyOrder(@Path("id")String _id);

    @GET("api/user/{id}/mynota/{notaid}/product/{productid}")
    Call<OrderDetailsAPI> getMyOrderDetails(
            @Path("id")String _id,
            @Path("notaid")String nota_id,
            @Path("productid")String product_id);

    @DELETE("api/user/{id}/mynota/delete/user/{notaid}")
    Call<ResponseBody> deleteNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id);

    @PATCH("api/user/{id}/mynota/canceled/{notaid}")
    Call<ResponseBody> canceledNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id);

    @PATCH("api/user/{id}/mynota/update/{notaid}")
    Call<ResponseBody> delivedNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id);
}
