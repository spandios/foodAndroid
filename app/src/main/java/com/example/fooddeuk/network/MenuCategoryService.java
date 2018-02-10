package com.example.fooddeuk.network;

import com.example.fooddeuk.model.restaurant.Restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryService {
    interface MenuCategoryInterFace{
        @GET("api/menuCategory")
        Call<Restaurant> getMenu(@Query("rest_id") int rest_id);
    }

    public static Call<Restaurant> getMenuCategory(int rest_id){
        MenuCategoryInterFace menuCategoryInterFace=RetrofitBase.getInstance().getRetrofit().create(MenuCategoryInterFace.class);
        Call<Restaurant> call=menuCategoryInterFace.getMenu(rest_id);
        return call;
    }





}
