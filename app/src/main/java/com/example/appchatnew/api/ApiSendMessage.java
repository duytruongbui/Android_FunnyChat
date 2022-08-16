package com.example.appchatnew.api;

import com.example.appchatnew.model.response.ResponseBody;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiSendMessage {
    @FormUrlEncoded
    @POST("/receivemessage")
    Observable<ResponseBody> sendMessage(@Field("customer_send_info") String customer_send_info,
                                         @Field("user_receive_name") String user_receive_name,
                                         @Field("customer_send_info_text_image") String customer_send_info_text_image,
                                         @Field("customer_send_info_text_file") String customer_send_info_text_file,
                                         @Field("customer_send_info_text_gps") String customer_send_info_text_gps,
                                         @Field("customer_send_info_text_time") String customer_send_info_text_time);
}
