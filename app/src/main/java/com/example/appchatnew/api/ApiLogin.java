package com.example.appchatnew.api;

import com.example.appchatnew.model.response.InfoUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiLogin {
    @FormUrlEncoded
    @POST("/validateinfo")
    Observable<List<InfoUser>> sendData(@Field("username") String username, @Field("password") String password);
}
