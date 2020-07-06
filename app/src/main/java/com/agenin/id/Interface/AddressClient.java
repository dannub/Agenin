package com.agenin.id.Interface;

import com.agenin.id.Model.AddressModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AddressClient {

    @GET("api/user/{id}/myaddresses/")
    Call<List<AddressModel>> getMyAddress(@Path("id")String _id);

    @PATCH("api/user/{id}/myaddresses/selected/{addressID}")
    Call<List<AddressModel>> updateSelectedAddress(
            @Path("id")String _id,
            @Path("addressID")String addressID
    );

    @DELETE("api/user/{id}/myaddresses/delete/{addressID}")
    Call<ResponseBody> deleteSelectedAddress(
            @Path("id")String _id,
            @Path("addressID")String addressID
    );

    @FormUrlEncoded
    @POST("api/user/{id}/myaddresses/create")
    Call<AddressModel> addNewAddress(
            @Path("id")String _id,
            @Field("nama") String nama,
            @Field("no_telepon") String no_telepon,
            @Field("no_alternatif") String no_alternatif,
            @Field("negara") String negara,
            @Field("provinsi") String provinsi,
            @Field("kabupaten") String kabupaten,
            @Field("kecamatan") String kecamatan,
            @Field("kodepos") String kodepos,
            @Field("detail") String detail

    );

    @FormUrlEncoded
    @PATCH("api/user/{id}/myaddresses/update/{addressid}")
    Call<AddressModel> updateAddress(
            @Path("id")String _id,
            @Path("addressid")String addressid,
            @Field("nama") String nama,
            @Field("no_telepon") String no_telepon,
            @Field("no_alternatif") String no_alternatif,
            @Field("negara") String negara,
            @Field("provinsi") String provinsi,
            @Field("kabupaten") String kabupaten,
            @Field("kecamatan") String kecamatan,
            @Field("kodepos") String kodepos,
            @Field("detail") String detail

    );
}
