package com.agenin.id.Interface;

import com.agenin.id.Model.Api.WishlistAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WishlistClient {
    @POST("api/user/{id}/mywishlists/create/{productID}")
    Call<ResponseBody> setMyWishlist(
            @Path("id")String _id,
            @Path("productID")String productID
    );
    @DELETE("api/user/{id}/mywishlists/delete/{wishlist}")
    Call<ResponseBody> deleteMyWishlist(
            @Path("id")String _id,
            @Path("wishlist")String wishlist
    );
    @GET("api/user/{id}/mywishlists/")
    Call<WishlistAPI> getMyWishlist(@Path("id")String _id);
}
