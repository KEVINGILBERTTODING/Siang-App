package com.example.siangapp.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataApi {
    public static final String IP_ADDRESS ="";// isi ip address kalian
    public static final String Base_URL ="http://"+IP_ADDRESS+"/ci_siang/api/";
    public static Retrofit retrofit = null;
    public static  Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }
        return retrofit;

    }

}
