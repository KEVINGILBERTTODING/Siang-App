package com.example.siangapp.util;

import com.example.siangapp.model.ResponseModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface AuthInterface {

    @Multipart
    @POST("authapi/register")
    Call<ResponseModel>register(
            @PartMap Map<String, RequestBody>textData
            );


    @FormUrlEncoded
    @POST("authapi/login")
    Call<ResponseModel>login(
            @Field("email") String email,
            @Field("password") String password
    );
}
