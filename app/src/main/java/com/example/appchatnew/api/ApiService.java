package com.example.appchatnew.api;

import com.example.appchatnew.activities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    public static final String DOMAIN = "https://10.10.20.31/api/face-recognition";
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    ApiService apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);


    @Multipart
    @POST("face-recognition")
    Call<User> registerAccount(@Part(Const.KEY_USERNAME)RequestBody username,
                               @Part(Const.KEY_PASSWORD)RequestBody password,
                               @Part MultipartBody.Part avt);




}
