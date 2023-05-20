package com.example.siangapp.util;

import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface PesertaInterface {
    @GET("peserta/getKegiatanByUserId")
    Call<List<KegiatanModel>> getKegiatanByUserId(
            @Query("user_id")  String userId
    );

    @Multipart
    @POST("peserta/insertKegiatan")
    Call<ResponseModel> insertKegiatan(
            @PartMap Map<String, RequestBody> textData
            );

    @Multipart
    @POST("peserta/updateKegiatan")
    Call<ResponseModel> updateKegiatan(
            @PartMap Map<String, RequestBody> textData
            );

    @FormUrlEncoded
    @POST("peserta/deleteKegiatan")
    Call<ResponseModel> deleteKegiatan(
            @Field("id") String id
    );





}
