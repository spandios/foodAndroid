package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.item.CurrentLocationListItem;
import com.example.heojuyeong.foodandroid.item.MenuCategoryItem;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryService {


    interface MenuCategoryInterFace{
        @GET("api/menu/readMenuCategory")
        Call<MenuCategoryItem> getMenuCategory(@Query("rest_id") int rest_id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<MenuCategoryItem> getCall(int rest_id){
        MenuCategoryInterFace menuCategoryInterFace=MenuCategoryInterFace.retrofit.create(MenuCategoryInterFace.class);
        Call<MenuCategoryItem> call=menuCategoryInterFace.getMenuCategory(rest_id);
        return call;
    }





}
