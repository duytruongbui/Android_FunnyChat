package com.example.appchatnew.api;

import com.example.appchatnew.model.response.ChatResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiGetListChat {
    @FormUrlEncoded
    @POST("/joinroomdb")
    Observable<List<ChatResponse>> getListChat(@Field("customer_id_username") String customer_id_username,
                                               @Field("user_send_id") String user_send_id);
}
