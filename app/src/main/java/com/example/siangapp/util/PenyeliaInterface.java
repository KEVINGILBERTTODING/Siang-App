package com.example.siangapp.util;

import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
