package com.example.fooddeuk.http;

import com.example.fooddeuk.model.restaurant.RestaurantItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryService {
    interface MenuCategoryInterFace{
        @GET("api/menu")
        Call<RestaurantItem.Restaurant> getMenu(@Query("rest_id") int rest_id);
    }

    public static Call<RestaurantItem.Restaurant> getMenuCategory(int rest_id){
        MenuCategoryInterFace menuCategoryInterFace=RetrofitBase.getInstance().getRetrofit().create(MenuCategoryInterFace.class);
        Call<RestaurantItem.Restaurant> call=menuCategoryInterFace.getMenu(rest_id);
        return call;
    }





}
