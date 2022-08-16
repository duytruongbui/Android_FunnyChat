package com.example.appchatnew.api;

import com.example.appchatnew.model.response.ResponseBody;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiJoinRoom {
    @FormUrlEncoded
    @POST("/joinroom")
    Observable<ResponseBody> create(@Field("customer_id_username") String customer, @Field("user_send_id") String user_send);
}
