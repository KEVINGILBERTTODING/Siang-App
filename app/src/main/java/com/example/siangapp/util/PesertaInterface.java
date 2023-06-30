package com.example.siangapp.util;

import com.example.siangapp.model.HistoryProjectModel;
import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.model.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
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

    @GET("peserta/getTugasByUserId")
    Call<List<ProjectModel>> getProjectByUserId(
            @Query("user_id")  String userId
    );

    @GET("peserta/getHistoryProjectByUserId")
    Call<List<HistoryProjectModel>> getHistoryProjectByUserId(
            @Query("user_id")  String userId
    );

    @Multipart
    @POST("peserta/insertHistoryProject")
    Call<ResponseModel> insertHistoryProject(
            @PartMap Map<String, RequestBody> textData
            );

    @FormUrlEncoded
    @POST("peserta/deleteHistory")
    Call<ResponseModel> deleteHistory(
            @Field("id") String id
    );

    @Multipart
    @POST("peserta/updateHistoryProject")
    Call<ResponseModel> updateHistory(
            @PartMap Map<String, RequestBody> textData
    );

    @GET("peserta/getPesertaById")
    Call<PendaftarModel> getProfile(
            @Query("user_id") String userd
    );
    @GET("peserta/getPesertaById2")
    Call<PendaftarModel> getProfile2(
            @Query("user_id") String userd
    );



    @Multipart
    @POST("peserta/updatePhotoProfile")
    Call<ResponseModel> updatePhotoProfile(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part filePart
    );

    @FormUrlEncoded
    @POST("peserta/updateProfile")
    Call<ResponseModel> updateProfile(
            @Field("id") String id,
               @Field("nama_peserta") String nama_peserta,
                @Field("alamat") String alamat,
                @Field("asal_sekolah") String asal_sekolah,
                @Field("jurusan") String jurusan,
                @Field("nim") String nim,
                @Field("alamat_sekolah") String alamat_sekolah,
                @Field("no_telp") String no_telp
    );

    @GET("peserta/validasiKuotaMagang")
    Call<ResponseModel> cekKuotaMagang();









}
