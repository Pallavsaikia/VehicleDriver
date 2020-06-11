package com.example.vehicledriver.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public ApiClient(){}
    private String baseURL = "https://fcm.googleapis.com/fcm/";
    private Retrofit retrofit= null;

    private HttpLoggingInterceptor logging =new  HttpLoggingInterceptor();

    private OkHttpClient getHttpLogClient(Context context){

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).build();

    }

    public Retrofit getApi(Context context){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpLogClient(context))
                    .build();
        }
        return retrofit;
    }
}
