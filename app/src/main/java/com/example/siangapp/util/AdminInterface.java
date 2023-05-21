package com.example.siangapp.util;

import com.example.siangapp.model.DivisiModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AdminInterface {
    @GET("admin/getAllPendaftar")
    Call<List<PendaftarModel>> getAllVerifPendaftar();

    @GET("admin/getDetailUser")
    Call<PendaftarModel> getDetailUser(
            @Query("user_id") String userId
    );

    @FormUrlEncoded
    @POST("admin/verification")
    Call<ResponseModel> verifPendaftar(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("admin/reject")
    Call<ResponseModel> rejectPendaftar(
            @Field("user_id") String userId,
            @Field("alasan") String alasan
    );

    @GET("admin/getalldivisi")
    Call<List<DivisiModel>> getAllDivisi();

    @GET("admin/getPesertaByDivisi")
    Call<List<PendaftarModel>> getPesertaByDivisi(
            @Query("divisi") String divisi
    );

    @GET("admin/getPesertaAcc")
    Call<List<PendaftarModel>> getPesertaAcc(
    );





}
