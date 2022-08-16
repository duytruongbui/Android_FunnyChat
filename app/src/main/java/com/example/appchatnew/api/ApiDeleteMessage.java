package com.example.appchatnew.api;

import com.example.appchatnew.model.response.ResponseBody;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDeleteMessage {
    @FormUrlEncoded
    @POST("/delete_message")
    Observable<ResponseBody> deleteMessage(@Field("customer") String customer,
                                           @Field("username") String username,
                                           @Field("time_delete") String time_delete);
}
