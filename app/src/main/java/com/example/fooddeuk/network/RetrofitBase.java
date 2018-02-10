package com.example.fooddeuk.network;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class RetrofitBase {
    private static RetrofitBase retrofitBase;
    private static Retrofit retrofit;
    HttpLoggingInterceptor logging=new HttpLoggingInterceptor();
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();

    private RetrofitBase() {
        GsonBuilder gson=new GsonBuilder().setLenient();


        retrofit = new retrofit2.Retrofit.Builder()
                //AWS
//                .baseUrl("http://13.124.159.166")
                .baseUrl("http://10.0.2.2:3000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build();
    }


    public static RetrofitBase getInstance() {
        if (retrofit == null) {
            retrofitBase = new RetrofitBase();
            return retrofitBase;
        }
        return retrofitBase;
    }


    public retrofit2.Retrofit getRetrofit() {
        return retrofit;
    }
}
