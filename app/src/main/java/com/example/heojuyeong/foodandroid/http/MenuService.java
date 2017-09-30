package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.model.menu.MenuItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 31..
 */

public class MenuService {

    interface MenuInterFace{
        @GET("api/menu/readMenu")
        Call<ArrayList<MenuItem>> getMenu(@Query("menu_category_id") int menu_category_id);
    }

    public static Call<ArrayList<MenuItem>> getMenu(int menu_category_id){
        MenuService.MenuInterFace menuInterFace= RetrofitBase.getInstance().getRetrofit().create(MenuService.MenuInterFace.class);
        Call<ArrayList<MenuItem>> call=menuInterFace.getMenu(menu_category_id);
        return call;
    }





}
