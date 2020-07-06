package com.agenin.id.Interface;

import com.agenin.id.Model.ProductModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductClient {
    @GET("api/products/id/{id}/")
    Call<ProductModel> getProduct(@Path("id")String _id);
    @GET("api/products/?limit=10&page=1")
    Call<ResponseBody> searchProduct(@Query("search")String search);
}
