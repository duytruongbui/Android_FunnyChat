package com.example.appchatnew.api;

import com.example.appchatnew.model.response.InfoUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiGetListUser {
    @GET("/getinfoall")
    Observable<List<InfoUser>> getListUser();
}
