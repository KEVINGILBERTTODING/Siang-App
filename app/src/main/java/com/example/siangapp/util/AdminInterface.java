package com.example.siangapp.util;

import com.example.siangapp.model.PendaftarModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdminInterface {
    @GET("admin/getAllPendaftar")
    Call<List<PendaftarModel>> getAllVerifPendaftar();
}
