package com.example.siangapp.util;

import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.model.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface PenyeliaInterface {
    @GET("penyelia/getAllPendaftarByDivisi")
    Call<List<PendaftarModel>> getAllPendaftarByDivisi(
            @Query("divisi") String divisi
    );

    @GET("penyelia/getAllKegiatanByUserId")
    Call<List<KegiatanModel>> getKegiatanByUserId(
            @Query("user_id") String userId
    );

    @GET("penyelia/getTugasByDivisi")
    Call<List<ProjectModel>> getTugasByDivisi(
            @Query("divisi") String divisi
    );

    @GET("penyelia/getPendaftarProject")
    Call<List<PendaftarModel>> getPendaftarProject(
            @Query("divisi") String divisi
    );

    @Multipart
    @POST("penyelia/insertTugas")
    Call<ResponseModel> insertTugas(
            @PartMap Map<String, RequestBody> textData
            );

    @Multipart
    @POST("penyelia/deleteProject")
    Call<ResponseModel> deleteProject(
            @PartMap Map<String, RequestBody> textData
    );

    @Multipart
    @POST("penyelia/updateProject")
    Call<ResponseModel> updateProject(
            @PartMap Map<String, RequestBody> textData
    );

}
