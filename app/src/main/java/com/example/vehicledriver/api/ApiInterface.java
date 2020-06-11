package com.example.vehicledriver.api;

import com.example.vehicledriver.pojo.SendDta;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("send")
    Single<Object> sendMessage(
            @Header("Authorization") String header,
            @Header("Content-Type") String ct,
            @Body SendDta sendDta);
}
