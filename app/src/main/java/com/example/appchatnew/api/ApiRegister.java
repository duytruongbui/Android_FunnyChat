package com.example.appchatnew.api;

import com.example.appchatnew.model.response.ResponseBody;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRegister {
    @FormUrlEncoded
    @POST("/registeruser")
    Observable<ResponseBody> registerUser(@FieldMap HashMap<String,String> register);
}
