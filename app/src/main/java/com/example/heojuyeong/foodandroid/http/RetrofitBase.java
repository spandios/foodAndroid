package com.example.heojuyeong.foodandroid.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by heojuyeong on 2017. 9. 22..
 */

public class RetrofitBase {
    private static RetrofitBase retrofitBase;
    private static Retrofit retrofit;

    private RetrofitBase(){
        retrofit=new retrofit2.Retrofit.Builder()
                .baseUrl("http://13.124.97.184")
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

    retrofit2.Retrofit getRetrofit() {
        return retrofit;
    }
}
