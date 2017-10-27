package com.example.fooddeuk.http;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class RetrofitBase {
    private static RetrofitBase retrofitBase;
    private static Retrofit retrofit;

    private RetrofitBase() {
        GsonBuilder gson=new GsonBuilder().setLenient();


        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://13.124.159.166")
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build();
    }

    private static void RetrofitBaseLocal() {


        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static RetrofitBase getInstance() {
        if (retrofit == null) {
            retrofitBase = new RetrofitBase();
            return retrofitBase;
        }
        return retrofitBase;
    }

    public static RetrofitBase getLocalInstance() {
        if (retrofit == null) {
            RetrofitBaseLocal();
            return retrofitBase;
        }
        return retrofitBase;
    }

    public retrofit2.Retrofit getRetrofit() {
        return retrofit;
    }
}
