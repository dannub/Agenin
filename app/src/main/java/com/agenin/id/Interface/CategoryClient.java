package com.agenin.id.Interface;

import com.agenin.id.Model.CategoryModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryClient {
    @GET("api/categories/?isdropdown=true")
    Call<List<CategoryModel>> getCategories();

    @GET("api/categories/{slug}/topdeals/")
    Call<ResponseBody> getTopDeal(@Path("slug")String slug);
}
