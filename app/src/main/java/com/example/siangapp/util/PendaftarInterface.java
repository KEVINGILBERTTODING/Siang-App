package com.example.siangapp.util;

import com.example.siangapp.model.DivisiModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface PendaftarInterface {

    @GET("peserta/getPesertaById")
    Call<PendaftarModel> getUserById(
            @Query("user_id") String userId
    );

    @GET("peserta/getAllDivisi")
    Call<List<DivisiModel>> getAllDivisi();

    @Multipart
    @POST("peserta/insertFormulirPendaftaran")
    Call<ResponseModel> insertFormulirPendaftaran(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part fileProposal
            );
}
