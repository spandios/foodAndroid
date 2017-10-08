package com.example.fooddeuk.http;

import com.example.fooddeuk.model.menu.MenuCategoryItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 30..
 */

public class MenuCategoryService {
    interface MenuCategoryInterFace{
        @GET("api/menu/readMenuCategory")
        Call<MenuCategoryItem> getMenuCategory(@Query("rest_id") int rest_id);
    }

    public static Call<MenuCategoryItem> getMenuCategory(int rest_id){
        MenuCategoryInterFace menuCategoryInterFace=RetrofitBase.getInstance().getRetrofit().create(MenuCategoryInterFace.class);
        Call<MenuCategoryItem> call=menuCategoryInterFace.getMenuCategory(rest_id);
        return call;
    }





}
